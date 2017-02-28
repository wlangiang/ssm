package com.kaishengit.pojo;

import lombok.Data;

import java.security.Timestamp;

/**
 * Created by Administrator on 2017/2/18.
 */
@Data
public class DeviceRent {
    private Integer id;
    private String companyName;
    private String linkMan;
    private String tel;
    private String cardNum;
    private String address;
    private String fax;
    private String rentDate;
    private String backDate;
    private Integer totalDay;
    private Float totalPrice;
    private Float preCost;
    private Float lastCost;
    private Timestamp createTime;
    private String createUser;
    private String serialNumber;
    private String state;


}
