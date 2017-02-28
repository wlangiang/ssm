package com.kaishengit.controller;

import com.google.common.collect.Maps;
import com.kaishengit.dto.AjaxResult;
import com.kaishengit.dto.DataTablesResult;
import com.kaishengit.dto.DeviceRentDto;
import com.kaishengit.exception.NotFoundException;
import com.kaishengit.exception.ServiceException;
import com.kaishengit.pojo.Device;
import com.kaishengit.pojo.DeviceRent;
import com.kaishengit.pojo.DeviceRentDetail;
import com.kaishengit.pojo.DeviceRentDoc;
import com.kaishengit.service.DeviceService;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipOutputStream;

/**
 * Created by Administrator on 2017/2/17.
 */
@Controller
@RequestMapping("/device/rent")
public class DeviceRentController {
    @Autowired
    private DeviceService deviceService;
    @GetMapping
    public String list(){

        return "device/rent/list";
    }
    @GetMapping("/load")
    @ResponseBody
    public DataTablesResult load(HttpServletRequest request){
        String draw = request.getParameter("draw");
        String start = request.getParameter("start");
        String length = request.getParameter("length");

        Map<String,Object> queryParam = Maps.newHashMap();
        queryParam.put("start",start);
        queryParam.put("length",length);

        List<DeviceRent> rentList = deviceService.findDeviceRentByParam(queryParam);
        Long count = deviceService.countOfDeviceRent();
        return new DataTablesResult(draw,count,count,rentList);

    }

    @GetMapping("/new")
    public String newRent(Model model){
        List<Device> deviceList = deviceService.findAllDevice();
        model.addAttribute("deviceList",deviceList);
        return"/device/rent/new";
    }
    @PostMapping("/new")
    @ResponseBody
    public AjaxResult saveRent(@RequestBody DeviceRentDto deviceRentDto){
        try {
            String serialNumber = deviceService.saveRent(deviceRentDto);
            AjaxResult result = new AjaxResult();
            result.setStatus(AjaxResult.SUCCESS);
            result.setData(serialNumber);

            return result;
        } catch (ServiceException e) {
            return new AjaxResult(AjaxResult.ERROR,e.getMessage());
        }
    }
    @GetMapping("/device.json")
    @ResponseBody
    public AjaxResult deviceJson(Integer id){
        Device device = deviceService.findDeviceById(id);
        if(device==null){
            return new AjaxResult(AjaxResult.ERROR,"该设备不存在");
        }else{
            return new AjaxResult(device);
        }
    }
    @GetMapping("/{serialNumber:\\d+}")
    public String showDeviceRent(@PathVariable String serialNumber,Model model){
        DeviceRent deviceRent = deviceService.findDeviceRentBySerialNum(serialNumber);
        if(deviceRent==null){
            throw new NotFoundException();
        }else{
            List<DeviceRentDetail> rentDetailList = deviceService.findDeviceRentDetailListByRentId(deviceRent.getId());
            List<DeviceRentDoc> rentDocList = deviceService.findDeviceRentDocListByRentId(deviceRent.getId());

            model.addAttribute("deviceRent",deviceRent);
            model.addAttribute("rentDetailList",rentDetailList);
            model.addAttribute("rentDocList",rentDocList);
            return "/device/rent/show";
        }
    }
    @GetMapping("/download")
    @ResponseBody
    public ResponseEntity<InputStreamResource> downLoadFile(Integer id) throws IOException {
        InputStream inputStream = deviceService.downLoadFile(id);
        if(inputStream==null){
            throw new NotFoundException();
        }else{
            DeviceRentDoc rentDoc = deviceService.findDeviceRentDocListById(id);
            String fileName = rentDoc.getSourceName();
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            httpHeaders.setContentDispositionFormData("attachment",fileName, Charset.forName("UTF-8"));

            return new ResponseEntity<>(new InputStreamResource(inputStream),httpHeaders, HttpStatus.OK);
        }
    }


   /* @GetMapping("/download")
    public void uploadRentDoc(Integer id, HttpServletResponse response) throws IOException {
        InputStream inputStream = deviceService.downLoadFile(id);
        if(inputStream==null){
            throw new NotFoundException();
        }else{
            DeviceRentDoc rentDoc = deviceService.findDeviceRentDocListById(id);
            //将文件下载标记为二进制
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            //更改文件下载的名称
            String fileName = rentDoc.getSourceName();
            fileName = new String (fileName.getBytes("UTF-8"),"ISO8859-1");
            response.setHeader("Content-Disposition","attachment;filename='"+fileName+"'");

            OutputStream outputStream=response.getOutputStream();
            IOUtils.copy(inputStream,outputStream);
            outputStream.flush();
            outputStream.close();
            inputStream.close();
        }
    }*/

    @GetMapping("/download/zip")
   public void downloadZipFile(Integer id,HttpServletResponse response) throws IOException {
        DeviceRent deviceRent = deviceService.findDeviceRentById(id);
        if(deviceRent==null){
            throw new NotFoundException();
        }else{
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            String fileName = deviceRent.getCompanyName()+".zip";

            fileName = new String(fileName.getBytes("UTF-8"),"ISO8859-1");
            response.setHeader("Content-Disposition","attachment;filename=\""+fileName+"\"");

            OutputStream outputStream = response.getOutputStream();
            ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream);
            deviceService.downloadZipFile(deviceRent,zipOutputStream);

        }
    }

    @PostMapping("/state/change")
    @ResponseBody
    public AjaxResult stateChange(Integer id){
       deviceService.changeRentState(id);
       return new AjaxResult(AjaxResult.SUCCESS);
    }
}
