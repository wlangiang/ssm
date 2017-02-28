package com.kaishengit.service;

import com.kaishengit.dto.DeviceRentDto;
import com.kaishengit.pojo.Device;
import com.kaishengit.pojo.DeviceRent;
import com.kaishengit.pojo.DeviceRentDetail;
import com.kaishengit.pojo.DeviceRentDoc;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipOutputStream;

/**
 * Created by Administrator on 2017/2/16.
 */
public interface DeviceService {
    List<Device> findAllDevice();

    List<Device> findDeviceBySearchParam(Map<String, Object> searchParam);

    Long count();

    Long countBySearchParam(Map<String, Object> searchParam);

    void saveDevice(Device device);

    void delDevice(Integer id);

    Device findDeviceById(Integer id);

    String saveRent(DeviceRentDto deviceRentDto);

    DeviceRent findDeviceRentBySerialNum(String serialNumber);

    List<DeviceRentDetail> findDeviceRentDetailListByRentId(Integer id);

    List<DeviceRentDoc> findDeviceRentDocListByRentId(Integer id);

    InputStream downLoadFile(Integer id) throws IOException;

    DeviceRentDoc findDeviceRentDocListById(Integer id);

    void downloadZipFile(DeviceRent deviceRent, ZipOutputStream zipOutputStream) throws IOException;

    DeviceRent findDeviceRentById(Integer id);

    List<DeviceRent> findDeviceRentByParam(Map<String, Object> queryParam);

    Long countOfDeviceRent();

    void changeRentState(Integer id);
}
