package com.kaishengit.service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.gson.Gson;
import com.kaishengit.dto.winxin.SendMessage;
import com.kaishengit.dto.winxin.User;
import com.kaishengit.exception.ServiceException;
import com.qq.weixin.mp.aes.AesException;
import com.qq.weixin.mp.aes.WXBizMsgCrypt;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2017/2/25.
 */
@Service
public class WeixinService {

    @Value("${wx.token}")
    private String token;
    @Value("${wx.aeskey}")
    private String aesKey;
    @Value("${wx.corpid}")
    private String corPid;
    @Value("${wx.secret}")
    private String secRet;

    private static Logger logger = LoggerFactory.getLogger(WeixinService.class);

    private static final String CREATE_USER_URL="https://qyapi.weixin.qq.com/cgi-bin/user/create?access_token={0}";
    private static final String ACCESS_TOKEN="https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid={0}&corpsecret={1}";
    private static final String SEND_MESSAGE="https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token={0}";

    private LoadingCache<String,String> cache = CacheBuilder.newBuilder()
            .maximumSize(10)
            .expireAfterWrite(7200, TimeUnit.SECONDS)
            .build(new CacheLoader<String, String>() {
                @Override
                public String load(String s) throws Exception {
                    String url = MessageFormat.format(ACCESS_TOKEN,corPid,secRet);
                    OkHttpClient httpClient = new OkHttpClient();
                    Request request = new Request.Builder().url(url).build();
                    Response response = httpClient.newCall(request).execute();
                    String result = response.body().string();
                    response.close();
                    Map<String,Object> map = new Gson().fromJson(result,HashMap.class);
                    if(map.containsKey("errcode")){
                        logger.error("获取微信AccessToken异常{}",map.get("errcode"));
                        throw new ServiceException("获取微信AccessToken异常:"+map.get("errcode"));
                    }else{
                        return map.get("access_token").toString();
                    }

                }
            });



    public String init(String msg_signature, String timestamp, String nonce, String echostr) {
        try {
            WXBizMsgCrypt wxBizMsgCrypt = new WXBizMsgCrypt(token,aesKey,corPid);
            return wxBizMsgCrypt.VerifyURL(msg_signature, timestamp, nonce, echostr);
        } catch (AesException e) {
           throw new ServiceException("微信初始化异常",e);
        }
    }

    public String getAccessToken(){
        try {
            return cache.get("");
        } catch (ExecutionException e) {
            throw new ServiceException("获取AccessToken异常",e);
        }
    }

    public void save(User user) {

        String url = MessageFormat.format(CREATE_USER_URL,getAccessToken());

        String json = new Gson().toJson(user);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"),json);
        OkHttpClient httpClient = new OkHttpClient();
        Request request = new Request.Builder().post(requestBody).url(url).build();

        try {
            Response response = httpClient.newCall(request).execute();
            String resultJson = response.body().string();


            Map<String,Object> result = new Gson().fromJson(resultJson,HashMap.class);
            Object errorCode = result.get("errcode");
            if(!"0.0".equals(errorCode.toString())){
                logger.error("创建微信用户异常：{}",resultJson);
                throw new ServiceException("创建微信用户异常:"+resultJson);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送消息
     * @param message
     */
    public void sendMessage(SendMessage message){

        String url = MessageFormat.format(SEND_MESSAGE,getAccessToken());
        String json = new Gson().toJson(message);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset = UTF-8"),json);

        Request request = new Request.Builder().post(requestBody).url(url).build();
        try {
            Response response = new OkHttpClient().newCall(request).execute();
            String result = response.body().string();

            Map<String,Object> map = new Gson().fromJson(result,HashMap.class);
            String errCode = map.get("errcode").toString();
            if(!"0.0".equals(errCode)){
                logger.error("微信发送消息失败{}",result);
                throw new ServiceException("微信发送消息失败"+result);
            }
        } catch (IOException e) {
            throw new ServiceException("微信发送消息异常",e);
        }

    }
}
