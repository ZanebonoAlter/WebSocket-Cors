package com.zanebono.chart.Controller;

import com.google.gson.Gson;
import com.zanebono.chart.Service.RSAService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import org.apache.commons.codec.binary.Base64;

@RestController
@RequestMapping("/RSA")
public class RSAController {
    @Autowired
    private RSAService rsaService;
    @ApiOperation(value = "公钥获取接口",notes = "获取服务器本地的公钥")
    @RequestMapping(value = "/getPublicKey",method = RequestMethod.POST)
    public String getPublicKey(){
        Gson gson = new Gson();
        return Base64.encodeBase64String(rsaService.getPublicKey().getEncoded());
    }
}
