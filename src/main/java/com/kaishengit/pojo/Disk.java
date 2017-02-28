package com.kaishengit.pojo;

import lombok.Data;

import java.sql.Timestamp;


/**
 * Created by Administrator on 2017/2/21.
 */
@Data
public class Disk {

    public static final String FILE_TYPE="file";
    public static final String DIRECTORY_TYPE="directory";

    private Integer id;
    private String sourceName;
    private String name;
    private Integer fid;
    private String size;
    private Timestamp createTime;
    private String createUser;
    private String type;


}
