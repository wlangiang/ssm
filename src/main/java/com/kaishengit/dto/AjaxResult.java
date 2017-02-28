package com.kaishengit.dto;

import lombok.Data;

/**
 * Created by Administrator on 2017/2/17.
 */
@Data
public class AjaxResult {

    public static final String SUCCESS="success";
    public static final String ERROR = "error";

    private String status;
    private String message;
    private Object data;
    public AjaxResult(){}

    public AjaxResult(String status,String message){
        this.status=status;
        this.message=message;
    }
    public AjaxResult(Object data){
        this.status=SUCCESS;
        this.data=data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
