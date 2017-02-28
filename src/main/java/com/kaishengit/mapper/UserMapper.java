package com.kaishengit.mapper;

import com.kaishengit.pojo.User;

import java.util.List;

/**
 * Created by Administrator on 2017/1/12.
 */
public interface UserMapper {

    List<User> findAll();

    void save(User user);

    User findById(Integer id);

    void edit(User user);

    void del(Integer id);

    void update(User user);

    User findByUsername(String username);
}
