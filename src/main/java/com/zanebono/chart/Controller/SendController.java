package com.zanebono.chart.Controller;

import com.google.gson.Gson;
import com.zanebono.chart.Service.SendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/Send")
public class SendController {
    @Autowired
    private SendService sendService;
    @RequestMapping(value="/Insert",produces="text/html;charset=UTF-8")
    public String Insert_Record(@RequestParam("callback")String callback,HttpServletRequest request, String decription){
        Gson gson = new Gson();
        //返回插入成功 插入失败
        return callback+"("+gson.toJson(this.sendService.insertRecord((String) request.getSession().getAttribute("username"),decription))+")";
    }
}
