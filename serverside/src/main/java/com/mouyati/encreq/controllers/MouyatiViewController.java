package com.mouyati.encreq.controllers;

import com.mouyati.encreq.entity.Request;
import com.mouyati.encreq.entity.ResultSet;
import com.mouyati.encreq.service.AesService;
import com.mouyati.encreq.service.RSAService;
import org.apache.tomcat.util.codec.binary.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;

@RestController
public class MouyatiViewController {
    private RSAService rsa;
    private AesService aes;
    private static final Logger logger = LoggerFactory.getLogger(MouyatiViewController.class);

    @Autowired
    public MouyatiViewController(RSAService rsaService, AesService aesService) {
        this.rsa = rsaService;
        this.aes = aesService;
    }

    @RequestMapping(value = "/keys", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResultSet generatePublicKey() {
        String pk = rsa.publicKeyContent();
        ResultSet r = new ResultSet();
        r.setErrorCode(0);
        r.setErrorMsg("");
        r.setData(pk);
        return r;
    }

    @RequestMapping(value = "/upload", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResultSet wnc(@RequestBody Request request) {
        if (request == null) {
            return ResultSet.valueOf("请求内容不能为空", -1);
        }
        String token = request.getToken();
        String body = request.getBody();
        String iv = request.getIv();
        if (token == null || body == null || iv == null) {
            return ResultSet.valueOf("缺少参数", -2);
        }
        try {
            Key privateKey = rsa.privateKey();
            byte[] bytes=token.getBytes(StandardCharsets.UTF_8);
            byte[] keys = rsa.decrypt(Base64.getDecoder().decode(bytes), privateKey);
            bytes=iv.getBytes(StandardCharsets.UTF_8);
            byte[] ivs = rsa.decrypt(Base64.getDecoder().decode(bytes), privateKey);
            bytes= body.getBytes(StandardCharsets.UTF_8);
            byte[] content = aes.decrypt(Base64.getDecoder().decode(bytes), keys, ivs);
            String words = StringUtils.newStringUtf8(content);
            logger.info(words);
            return ResultSet.valueOf("成功", 0);
        } catch (Exception exception) {
            return ResultSet.valueOf("非法请求", -3);
        }

    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResultSet exceptionHandler(Throwable t) {
        ResultSet r = new ResultSet();
        r.setErrorCode(500);
        r.setErrorMsg("服务器内部错误");
        r.setData(null);
        return r;
    }
}
