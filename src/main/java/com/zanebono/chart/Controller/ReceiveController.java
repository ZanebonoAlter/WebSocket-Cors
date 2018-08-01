package com.zanebono.chart.Controller;

import com.google.gson.Gson;
import com.zanebono.chart.Model.Chart_Record;
import com.zanebono.chart.Service.ReceiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;

@RestController
@RequestMapping("/Receive")
public class ReceiveController {
    @Autowired
    private ReceiveService receiveService;
    @RequestMapping(value="/Load_All",produces="text/html;charset=UTF-8")
    public String Load_All(@RequestParam("callback")String callback){
        Gson gson = new Gson();
        List<Chart_Record> rs = this.receiveService.receiveRecord();
        return callback+"("+gson.toJson(rs)+")";
    }
}
