package com.kaishengit.service;

import com.kaishengit.pojo.Role;
import com.kaishengit.pojo.User;

import java.util.List;

/**
 * Created by Administrator on 2017/1/12.
 */
public interface UserService {

    List<User> findAllUser();

    User findUserById(Integer id);

    void editUser(User user, Integer[] roleIds);

    void del(Integer id);

    List<Role> findAllRole();

    void saveUser(User user, Integer[] roleIds);
}
