package com.kaishengit.mapper;

import com.kaishengit.pojo.DeviceRentDoc;

import java.util.List;

/**
 * Created by Administrator on 2017/2/18.
 */
public interface DeviceRentDocMapper {

    void batchSave(List<DeviceRentDoc> rentDocList);

    List<DeviceRentDoc> findByRentId(Integer rentId);

    DeviceRentDoc findById(Integer id);
}
