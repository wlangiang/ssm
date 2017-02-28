package com.kaishengit.mapper;

import com.kaishengit.pojo.Disk;

import java.util.List;

/**
 * Created by Administrator on 2017/2/21.
 */
public interface DiskMapper {

    List<Disk> findByPath(Integer path);

    void saveDisk(Disk disk);

    Disk findById(Integer id);

    void delDisk(Integer id);

    List<Disk> findAll();

    void batchDel(List<Integer> delIdList);
}
