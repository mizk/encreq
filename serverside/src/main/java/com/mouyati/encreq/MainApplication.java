package com.mouyati.encreq;

import com.mouyati.encreq.service.RSAService;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.security.Security;

@SpringBootApplication
public class MainApplication {

    public static void main(String[] args) {
        Security.addProvider(new BouncyCastleProvider());
        SpringApplication application=new SpringApplication(MainApplication.class);
        application.addListeners(new ContextUtils());
        application.run(args);
        RSAService rsaService=ContextUtils.getBean(RSAService.class);
        rsaService.generateKey(1024);
    }

}
