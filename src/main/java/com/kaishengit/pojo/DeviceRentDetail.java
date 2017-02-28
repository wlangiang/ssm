package com.kaishengit.pojo;

import lombok.Data;

/**
 * Created by Administrator on 2017/2/18.
 */
@Data
public class DeviceRentDetail {
    private Integer id;
    private String deviceName;
    private String deviceUnit;
    private Float devicePrice;
    private Integer num;
    private Float totalPrice;
    private Integer rentId;
}
