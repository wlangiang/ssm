package com.kaishengit.service.impl;

import com.google.common.collect.Lists;
import com.kaishengit.exception.NotFoundException;
import com.kaishengit.exception.ServiceException;
import com.kaishengit.mapper.DiskMapper;
import com.kaishengit.pojo.Disk;
import com.kaishengit.service.DiskService;
import com.kaishengit.shiro.ShiroUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.List;
import java.util.UUID;

/**
 * Created by Administrator on 2017/2/21.
 */
@Service
public class DiskServiceImpl implements DiskService {
    @Autowired
    private DiskMapper diskMapper;
    @Value("${upload.path}")
    private String savePath;

    @Override
    public List<Disk> findDiskByPath(Integer path) {

        return diskMapper.findByPath(path);
    }

    @Override
    @Transactional
    public void saveFile(Integer fid, MultipartFile file) {
        String sourceName = file.getOriginalFilename();
        String newName = UUID.randomUUID().toString();
        Long size = file.getSize();
        if(sourceName.lastIndexOf(".")!=-1){
            newName = newName+sourceName.substring(sourceName.lastIndexOf("."));
        }
        try {
            File saveFile = new File(new File(savePath),newName);
            FileOutputStream outputStream = new FileOutputStream(saveFile);
            InputStream inputStream = file.getInputStream();
            IOUtils.copy(inputStream,outputStream);
            outputStream.flush();
            outputStream.close();
            inputStream.close();
        } catch (IOException e) {
            throw new ServiceException("保存到磁盘失败",e);
        }
        Disk disk = new Disk();
        disk.setSourceName(sourceName);
        disk.setName(newName);
        disk.setFid(fid);
        disk.setCreateUser(ShiroUtil.getCurrentUserName());
       disk.setSize(FileUtils.byteCountToDisplaySize(size));
       disk.setType(Disk.FILE_TYPE);

       diskMapper.saveDisk(disk);



    }

    @Override
    public void saveFolder(Disk disk) {
       disk.setCreateUser(ShiroUtil.getCurrentUserName());
       disk.setType(Disk.DIRECTORY_TYPE);
       diskMapper.saveDisk(disk);
    }

    @Override
    public Disk findDiskById(Integer id) {
        return diskMapper.findById(id);
    }

    @Override
    public InputStream downLoadFile(Integer id) throws FileNotFoundException {
        Disk disk = diskMapper.findById(id);
        if(disk==null||Disk.DIRECTORY_TYPE.equals(disk.getType())){
            return null;
        }else{
                File file = new File(new File(savePath),disk.getName());
                return new FileInputStream(file);
        }
    }

    @Override
    @Transactional
    public void delById(Integer id) {
        Disk disk = findDiskById(id);
        if(disk != null) {
            if (Disk.FILE_TYPE.equals(disk.getType())) {
                File file = new File(savePath, disk.getName());
                file.delete();
                diskMapper.delDisk(id);
            } else {
                List<Disk> diskList = diskMapper.findAll();
                List<Integer> delIdList = Lists.newArrayList();
                findDelId(diskList, delIdList, id);
                delIdList.add(id);

                diskMapper.batchDel(delIdList);
            }
        }



    }

    private void findDelId(List<Disk> diskList, List<Integer> delIdList, Integer id) {

        for(Disk disk:diskList){
            if(disk.getFid()==id){

                delIdList.add(disk.getId());
                if(disk.getType().equals(Disk.DIRECTORY_TYPE)){
                    findDelId(diskList,delIdList,disk.getId());

                }else{
                    File file = new File(savePath,disk.getName());
                    file.delete();
                }
            }
        }
    }

}
