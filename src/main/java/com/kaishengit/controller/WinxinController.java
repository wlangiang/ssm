package com.kaishengit.controller;

import com.kaishengit.service.WeixinService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Administrator on 2017/2/25.
 */
@Controller
@RequestMapping("/wx")
public class WinxinController {
    @Autowired
    private WeixinService winxinService;

    private Logger logger = LoggerFactory.getLogger(WinxinController.class);
    @GetMapping("/init")
    @ResponseBody
    public String init(String msg_signature,String timestamp,String nonce,String echostr){

        logger.info("{}--{}--{}--{}",msg_signature,timestamp,nonce,echostr);
        return winxinService.init(msg_signature,timestamp,nonce,echostr);
    }
}
