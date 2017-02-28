package com.kaishengit.service.impl;

import com.google.common.collect.Lists;
import com.kaishengit.dto.DeviceRentDto;
import com.kaishengit.dto.winxin.SendMessage;
import com.kaishengit.exception.ServiceException;
import com.kaishengit.mapper.*;
import com.kaishengit.pojo.*;
import com.kaishengit.service.DeviceService;
import com.kaishengit.service.WeixinService;
import com.kaishengit.shiro.ShiroUtil;
import com.kaishengit.util.SerialNumberUtil;
import org.apache.commons.io.IOUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by Administrator on 2017/2/16.
 */
@Service
public class DeviceServiceImpl implements DeviceService{
    @Autowired
    private DeviceMapper deviceMapper;
    @Autowired
    private DeviceRentMapper deviceRentMapper;
    @Autowired
    private DeviceRentDetailMapper deviceRentDetailMapper;
    @Autowired
    private DeviceRentDocMapper deviceRentDocMapper;
    @Autowired
    private FinanceMapper financeMapper;
    @Autowired
    private WeixinService weixinService;
    @Value("${upload.path}")
    private String fileSavePath;

    @Override
    public List<Device> findAllDevice() {

        return deviceMapper.findAll();
    }

    @Override
    public List<Device> findDeviceBySearchParam(Map<String, Object> searchParam) {

        return deviceMapper.findBySearchParam(searchParam);
    }

    @Override
    public Long count() {
        return deviceMapper.count();
    }

    @Override
    public Long countBySearchParam(Map<String, Object> searchParam) {

        return deviceMapper.countBySearchParam(searchParam);
    }

    @Override
    public void saveDevice(Device device) {
        device.setCurrentNum(device.getTotalNum());
        deviceMapper.saveDevice(device);
    }

    @Override
    public void delDevice(Integer id) {
        deviceMapper.delDevice(id);
    }

    @Override
    public Device findDeviceById(Integer id) {
        return deviceMapper.findDeviceById(id);
    }

    @Override
    @Transactional
    public String saveRent(DeviceRentDto deviceRentDto) {
        //1.保存合同
        DeviceRent rent = new DeviceRent();
        rent.setCompanyName(deviceRentDto.getCompanyName());
        rent.setLinkMan(deviceRentDto.getLinkMan());
        rent.setCardNum(deviceRentDto.getCardNum());
        rent.setTel(deviceRentDto.getTel());
        rent.setAddress(deviceRentDto.getAddress());
        rent.setFax(deviceRentDto.getFax());
        rent.setRentDate(deviceRentDto.getRentDate());
        rent.setBackDate(deviceRentDto.getBackDate());
        rent.setTotalDay(deviceRentDto.getTotalDate());
        rent.setTotalPrice(0F);
        rent.setPreCost(0F);
        rent.setLastCost(0F);
        rent.setCreateUser(ShiroUtil.getCurrentUserName());
        rent.setSerialNumber(SerialNumberUtil.getSerialNumber());

        deviceRentMapper.save(rent);
        //2.保存合同详情

        List<DeviceRentDto.DeviceArrayBean> deviceArray = deviceRentDto.getDeviceArray();
        List<DeviceRentDetail> detailList = Lists.newArrayList();

            float total = 0F;
            for(DeviceRentDto.DeviceArrayBean bean : deviceArray){
                //查询当前库存设备是否足够
                Device device = deviceMapper.findDeviceById(bean.getId());
                if(device.getCurrentNum() < bean.getNum()){
                    throw new ServiceException(device.getName()+"库存不足");
                }else{
                    device.setCurrentNum(device.getCurrentNum()-bean.getNum());
                    deviceMapper.updateCurrent(device);
                }

                DeviceRentDetail rentDetail = new DeviceRentDetail();
                rentDetail.setDeviceName(bean.getName());
                rentDetail.setDeviceUnit(bean.getUnit());
                rentDetail.setDevicePrice(bean.getPrice());
                rentDetail.setNum(bean.getNum());
                rentDetail.setTotalPrice(bean.getTotal());
                rentDetail.setRentId(rent.getId());

                detailList.add(rentDetail);
                total +=bean.getTotal();
            }
            if(!detailList.isEmpty()){
                deviceRentDetailMapper.batchSave(detailList);
            }

        //3.计算合同总价及预付款、尾款
        total = total*rent.getTotalDay();
            float preCost = total*0.3F;
            float lastCost = total-preCost;

            deviceRentMapper.updateCost(total,preCost,lastCost,rent.getId());

        //4.保存文件
        List<DeviceRentDto.FileArrayBean> fileArray = deviceRentDto.getFileArray();
        List<DeviceRentDoc> rentDocList = Lists.newArrayList();

        for(DeviceRentDto.FileArrayBean doc : fileArray){
            DeviceRentDoc rentDoc = new DeviceRentDoc();

            rentDoc.setNewName(doc.getNewFileName());
            rentDoc.setSourceName(doc.getSourceFileName());
            rentDoc.setRentId(rent.getId());

            rentDocList.add(rentDoc);
        }
        if(!rentDocList.isEmpty()){
            deviceRentDocMapper.batchSave(rentDocList);
        }

        //5.写入财务流水
        rent = findDeviceRentById(rent.getId());
        Finance finance = new Finance();
        finance.setSerialNumber(SerialNumberUtil.getSerialNumber());
        finance.setType(Finance.TYPE_INCOME);
        finance.setMoney(preCost);
        finance.setState(Finance.STATE_ERROR);
        finance.setModule(Finance.MODULE_RENT_ADVICE);
        finance.setCreateDate(DateTime.now().toString("YYYY-MM-dd"));
        //finance.setCreateDate(rent.getCreateTime().toString());
        finance.setCreateUser(ShiroUtil.getCurrentUserName());
        finance.setModuleSerialNumber(rent.getSerialNumber());
        finance.setRemark(Finance.REMARK_PRE);
        financeMapper.save(finance);

        //6.给财务发送消息
        SendMessage message  = new SendMessage();
        message.setToparty("3");
        SendMessage.TextBean textBean = new SendMessage.TextBean();
        textBean.setContent("设备租赁模块添加一笔财务流水[预付款]，请确认");
        message.setText(textBean);
        weixinService.sendMessage(message);



        return rent.getSerialNumber();
    }

    @Override
    public DeviceRent findDeviceRentBySerialNum(String serialNumber) {

        return deviceRentMapper.findDeviceRentBySerialNum(serialNumber);

    }

    @Override
    public List<DeviceRentDetail> findDeviceRentDetailListByRentId(Integer rentId) {
        return deviceRentDetailMapper.findDeviceRentDetailByRentId(rentId);
    }

    @Override
    public List<DeviceRentDoc> findDeviceRentDocListByRentId(Integer id) {
        return deviceRentDocMapper.findByRentId(id);
    }

    @Override
    public InputStream downLoadFile(Integer id) throws IOException {
        DeviceRentDoc rentDoc = deviceRentDocMapper.findById(id);
        if(rentDoc==null){
            return null;
        }else{
            File file = new File(new File(fileSavePath),rentDoc.getNewName());
            if(file.exists()){
                return new FileInputStream(file);
            }else{
                return null;
            }
        }
    }

    @Override
    public DeviceRentDoc findDeviceRentDocListById(Integer id) {
        return deviceRentDocMapper.findById(id);
    }

    @Override
    public void downloadZipFile(DeviceRent deviceRent, ZipOutputStream zipOutputStream) throws IOException {
        List<DeviceRentDoc> docList = findDeviceRentDocListByRentId(deviceRent.getId());
        for(DeviceRentDoc doc : docList){
            ZipEntry entry = new ZipEntry(doc.getSourceName());
            zipOutputStream.putNextEntry(entry);

            InputStream inputStream = downLoadFile(doc.getId());
            IOUtils.copy(inputStream,zipOutputStream);
            inputStream.close();
        }
        zipOutputStream.closeEntry();
        zipOutputStream.flush();
        zipOutputStream.close();
    }

    @Override
    public DeviceRent findDeviceRentById(Integer id) {
        return deviceRentMapper.findById(id);
    }

    @Override
    public List<DeviceRent> findDeviceRentByParam(Map<String, Object> queryParam) {
        return deviceRentMapper.findByParam(queryParam);
    }

    @Override
    public Long countOfDeviceRent() {
        return deviceRentMapper.count();
    }

    @Override
    @Transactional
    public void changeRentState(Integer id) {
        //1. 将合同修改为已完成
        DeviceRent rent =findDeviceRentById(id);
        rent.setState("已完成");
        deviceRentMapper.updateState(rent);
        //2. 向财务模块添加尾款记录
        Finance finance = new Finance();
        finance.setSerialNumber(SerialNumberUtil.getSerialNumber());
        finance.setType(Finance.TYPE_INCOME);
        finance.setMoney(rent.getLastCost());
        finance.setState(Finance.STATE_ERROR);
        finance.setModule(Finance.MODULE_RENT_ADVICE);
        finance.setCreateDate(DateTime.now().toString("yyyy-MM-dd"));
        finance.setCreateUser(ShiroUtil.getCurrentUserName());
        finance.setModuleSerialNumber(rent.getSerialNumber());
        finance.setRemark(Finance.REMARK_LAST);

        financeMapper.save(finance);

        //3.给财务发送消息
        SendMessage message  = new SendMessage();
        message.setToparty("3");
        SendMessage.TextBean textBean = new SendMessage.TextBean();
        textBean.setContent("设备租赁模块添加一笔财务流水[尾款]，请确认");
        message.setText(textBean);
        weixinService.sendMessage(message);

    }
}
