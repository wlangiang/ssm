package com.kaishengit.service;

import com.kaishengit.pojo.Disk;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by Administrator on 2017/2/21.
 */
public interface DiskService {

    List<Disk> findDiskByPath(Integer path);

    void saveFile(Integer fid, MultipartFile file);

    void saveFolder(Disk disk);

    Disk findDiskById(Integer id);

    InputStream downLoadFile(Integer id) throws FileNotFoundException;

    void delById(Integer id);
}
