package com.kaishengit.service;

import java.io.InputStream;

/**
 * Created by Administrator on 2017/2/18.
 */
public interface FileService {

    String uploadFile(String originalFilename, String contentType, InputStream inputStream);

}
