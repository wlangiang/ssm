package com.kaishengit.controller;

import com.kaishengit.exception.NotFoundException;
import com.kaishengit.pojo.Role;
import com.kaishengit.pojo.User;
import com.kaishengit.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * Created by Administrator on 2017/1/12.
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;


    @RequestMapping(method = RequestMethod.GET)
    public String list(Model model){
        List<User> userList = userService.findAllUser();
        model.addAttribute("userList",userList);
        return "user/list";
    }
    @RequestMapping(value="/new",method = RequestMethod.GET)
    public String add(Model model){
        List<Role> roleList = userService.findAllRole();
        model.addAttribute("roleList",roleList);
        return "user/new";
    }
    @RequestMapping(value = "/new",method = RequestMethod.POST)
    public String add(User user,Integer[] roleIds,RedirectAttributes redirectAttributes){
        userService.saveUser(user,roleIds);
        redirectAttributes.addFlashAttribute("message","添加成功");
        return "redirect:/user";
    }
    @RequestMapping(value = "/{id:\\d+}/edit",method = RequestMethod.GET)
    public String edit(@PathVariable Integer id,Model model){

        User user = userService.findUserById(id);
        if(user==null){
            throw new NotFoundException();
        }else{
            List<Role> roleList = userService.findAllRole();
            model.addAttribute("roleList",roleList);
            model.addAttribute("user",user);
            return "user/edit";
        }
    }
    @RequestMapping(value="/{id:\\d+}/edit",method=RequestMethod.POST)
    public String edit(User user,Integer[] roleIds,RedirectAttributes redirectAttributes){

        userService.editUser(user,roleIds);
        redirectAttributes.addFlashAttribute("message","修改成功");
        return "redirect:/user";
    }
    @RequestMapping(value = "/{id}/del",method = RequestMethod.GET)
    public String del(@PathVariable Integer id,RedirectAttributes redirectAttributes){
        
        userService.del(id);
        redirectAttributes.addFlashAttribute("message","删除成功");
        return "redirect:/user";
    }


}
