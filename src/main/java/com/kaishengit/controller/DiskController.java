package com.kaishengit.controller;

import com.kaishengit.dto.AjaxResult;
import com.kaishengit.exception.ServiceException;
import com.kaishengit.pojo.Disk;
import com.kaishengit.service.DiskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;

/**
 * Created by Administrator on 2017/2/21.
 */
@Controller
@RequestMapping("/disk")
public class DiskController {
    @Autowired
    private DiskService diskService;
    @GetMapping
    public String list(@RequestParam(required = false,defaultValue="0") Integer path,
                       Model model){
        List<Disk> diskList = diskService.findDiskByPath(path);
        model.addAttribute("diskList",diskList);
        model.addAttribute("fid",path);
        return "disk/list";
    }
    @PostMapping("/upload")
    @ResponseBody
    public AjaxResult saveFile(Integer fid, MultipartFile file){

        try {
            diskService.saveFile(fid,file);
            return new AjaxResult(AjaxResult.SUCCESS);
        } catch (ServiceException e) {
            return new AjaxResult(AjaxResult.ERROR,e.getMessage());
        }
    }
    @PostMapping("/folder/new")
    @ResponseBody
    public AjaxResult saveFolder(Disk disk){
        diskService.saveFolder(disk);
        return new AjaxResult(AjaxResult.SUCCESS);
    }
    @GetMapping("/download")
    @ResponseBody
    public ResponseEntity<InputStreamResource> downloadFile(Integer id) throws FileNotFoundException {
        Disk disk = diskService.findDiskById(id);
        InputStream inputStream = diskService.downLoadFile(id);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment",disk.getSourceName(), Charset.forName("UTF-8"));

        return new ResponseEntity<InputStreamResource>(new InputStreamResource(inputStream),headers, HttpStatus.OK);

    }

    @GetMapping("/del/{id:\\d+}")
    @ResponseBody
    public AjaxResult del(@PathVariable Integer id){
        diskService.delById(id);

        return new AjaxResult(AjaxResult.SUCCESS);
    }


}
