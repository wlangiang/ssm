package com.kaishengit.mapper;

import com.kaishengit.pojo.DeviceRentDetail;

import java.util.List;

/**
 * Created by Administrator on 2017/2/18.
 */
public interface DeviceRentDetailMapper {

    void batchSave(List<DeviceRentDetail> detailList);

    List<DeviceRentDetail> findDeviceRentDetailByRentId(Integer rentId);
}
