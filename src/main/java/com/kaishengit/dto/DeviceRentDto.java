package com.kaishengit.dto;

import java.util.List;

/**
 * Created by Administrator on 2017/2/17.
 */
public class DeviceRentDto {


    /**
     * deviceArray : [{"id":"4","name":"电动车","unit":"辆","price":"110","num":"1","total":110}]
     * fileArray : [{"sourceFileName":"2075631-ba418638f5bf445e.jpg","newFileName":"5ddb8b31-7006-4a9f-8f3d-7a2e1d0c892d.jpg"}]
     * companyName : 张三的公司
     * tel : 16868686868
     * linkMan : 张三
     * cardNum : 1111-1111
     * address : 河南省焦作市
     * fax : 988989-214
     * rentDate : 2017-02-18
     * backDate : 2017-02-24
     * totalDate : 6
     */

    private String companyName;
    private String tel;
    private String linkMan;
    private String cardNum;
    private String address;
    private String fax;
    private String rentDate;
    private String backDate;
    private Integer totalDate;
    private List<DeviceArrayBean> deviceArray;
    private List<FileArrayBean> fileArray;

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getLinkMan() {
        return linkMan;
    }

    public void setLinkMan(String linkMan) {
        this.linkMan = linkMan;
    }

    public String getCardNum() {
        return cardNum;
    }

    public void setCardNum(String cardNum) {
        this.cardNum = cardNum;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getRentDate() {
        return rentDate;
    }

    public void setRentDate(String rentDate) {
        this.rentDate = rentDate;
    }

    public String getBackDate() {
        return backDate;
    }

    public void setBackDate(String backDate) {
        this.backDate = backDate;
    }

    public Integer getTotalDate() {
        return totalDate;
    }

    public void setTotalDate(Integer totalDate) {
        this.totalDate = totalDate;
    }

    public List<DeviceArrayBean> getDeviceArray() {
        return deviceArray;
    }

    public void setDeviceArray(List<DeviceArrayBean> deviceArray) {
        this.deviceArray = deviceArray;
    }

    public List<FileArrayBean> getFileArray() {
        return fileArray;
    }

    public void setFileArray(List<FileArrayBean> fileArray) {
        this.fileArray = fileArray;
    }

    public static class DeviceArrayBean {
        /**
         * id : 4
         * name : 电动车
         * unit : 辆
         * price : 110
         * num : 1
         * total : 110
         */

        private Integer id;
        private String name;
        private String unit;
        private Float price;
        private Integer num;
        private Float total;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public Float getPrice() {
            return price;
        }

        public void setPrice(Float price) {
            this.price = price;
        }

        public Integer getNum() {
            return num;
        }

        public void setNum(Integer num) {
            this.num = num;
        }

        public Float getTotal() {
            return total;
        }

        public void setTotal(Float total) {
            this.total = total;
        }
    }

    public static class FileArrayBean {
        /**
         * sourceFileName : 2075631-ba418638f5bf445e.jpg
         * newFileName : 5ddb8b31-7006-4a9f-8f3d-7a2e1d0c892d.jpg
         */

        private String sourceFileName;
        private String newFileName;

        public String getSourceFileName() {
            return sourceFileName;
        }

        public void setSourceFileName(String sourceFileName) {
            this.sourceFileName = sourceFileName;
        }

        public String getNewFileName() {
            return newFileName;
        }

        public void setNewFileName(String newFileName) {
            this.newFileName = newFileName;
        }
    }
}
