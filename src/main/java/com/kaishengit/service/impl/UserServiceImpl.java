package com.kaishengit.service.impl;

import com.kaishengit.mapper.RoleMapper;
import com.kaishengit.mapper.UserMapper;
import com.kaishengit.pojo.Role;
import com.kaishengit.pojo.User;
import com.kaishengit.service.UserService;
import com.kaishengit.service.WeixinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 2017/1/12.
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private WeixinService weixinService;

    @Override
    @Transactional
    public List<User> findAllUser() {
        return userMapper.findAll();
    }

    @Override
    public User findUserById(Integer id) {

        return userMapper.findById(id);
    }

    @Override
    @Transactional
    public void editUser(User user, Integer[] roleIds) {
        //删除原有角色
        roleMapper.delRoleByUserId(user.getId());
        //添加新角色
       saveUserRole(user,roleIds);
       //更新角色
        /*if(StringUtils.isNotEmpty(user.getPassword())){
            user.setPassword(user.getPassword());
        }*/
        userMapper.update(user);
    }

    private void saveUserRole(User user, Integer[] roleIds) {
        if(roleIds!=null){
            for(Integer roleId:roleIds){
                Role role = roleMapper.findById(roleId);
                if(role!=null){
                    roleMapper.saveNewUserRole(user.getId(),roleId);
                }
            }
        }

    }

    @Override
    @Transactional
    public void del(Integer id) {
        //删除用户的角色
        roleMapper.delRoleByUserId(id);
        //删除用户
        userMapper.del(id);
    }

    @Override
    public List<Role> findAllRole() {
        return roleMapper.findAll();
    }

    @Override
    @Transactional
    public void saveUser(User user, Integer[] roleIds) {
        //1.保存用户
        userMapper.save(user);
        //2.保存用户和角色的关系
        if(roleIds!=null){
            for(Integer id:roleIds){
                Role role = roleMapper.findById(id);
                if(role!=null){
                    roleMapper.saveNewUserRole(user.getId(),id);
                }
            }
        }
        //3.保存到微信
        com.kaishengit.dto.winxin.User wxUser = new com.kaishengit.dto.winxin.User();
        wxUser.setUserid(user.getId().toString());
        wxUser.setName(user.getUsername());
        wxUser.setMobile(user.getMobile());
        wxUser.setDepartment(Arrays.asList(roleIds));
        weixinService.save(wxUser);

    }
}
