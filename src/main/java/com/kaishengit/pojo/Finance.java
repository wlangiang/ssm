package com.kaishengit.pojo;

import lombok.Data;

/**
 * Created by Administrator on 2017/2/23.
 */
@Data
public class Finance {

    public static final String TYPE_INCOME="收入";
    public static final String TYPE_SPENGDING="支出";
    public static final String STATE_SUCCESS="已到账";
    public static final String STATE_ERROR="未到账";
    public static final String MODULE_RENT_ADVICE="设备租赁";
    public static final String MODULE_LABOR_DISPATCH="劳务派遣";
    public static final String REMARK_PRE="预付款";
    public static final String REMARK_LAST="尾款";



    private Integer id;
    private String serialNumber;
    private String type;
    private Float money;
    private String state;
    private String module;
    private String createDate;
    private String createUser;
    private String confirmUser;
    private String confirmDate;
    private String remark;
    private String moduleSerialNumber;
}
