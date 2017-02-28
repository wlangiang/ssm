package com.kaishengit.mapper;

import com.kaishengit.pojo.Role;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Administrator on 2017/1/13.
 */
public interface RoleMapper {

    List<Role> findAll();

    Role findById(Integer id);

    void saveNewUserRole(@Param("userid") Integer userid,
                         @Param("roleid") Integer roleid);

    void delRoleByUserId(Integer id);

    List<Role> findByUserId(Integer id);

}
