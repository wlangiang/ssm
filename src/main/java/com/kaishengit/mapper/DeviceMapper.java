package com.kaishengit.mapper;

import com.kaishengit.pojo.Device;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/2/16.
 */
public interface DeviceMapper {

    List<Device> findAll();

    List<Device> findBySearchParam(Map<String, Object> searchParam);

    Long count();

    Long countBySearchParam(Map<String, Object> searchParam);

    void saveDevice(Device device);

    void delDevice(Integer id);

    Device findDeviceById(Integer id);

    void updateCurrent(Device device);
}
