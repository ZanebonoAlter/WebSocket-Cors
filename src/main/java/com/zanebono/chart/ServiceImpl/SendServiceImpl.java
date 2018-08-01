package com.zanebono.chart.ServiceImpl;

import com.zanebono.chart.Dao.IRecordDao;
import com.zanebono.chart.Model.Chart_Record;
import com.zanebono.chart.Service.SendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;

@Service
public class SendServiceImpl implements SendService {
    @Autowired
    private IRecordDao iRecordDao;
    @Override
    public String insertRecord(String username,String decription) {
        Chart_Record cr = new Chart_Record();
        cr.setUsername(username);
        cr.setDecription(decription);
        Calendar calendar = Calendar.getInstance();
        cr.setTime(calendar.getTime());
        if(username.equals("")||username.equals(null)){
            return "用户登录已过期";
        }
        if(this.iRecordDao.insert(cr)>0){
            return "插入成功";
        }
        return "插入失败";
    }
}
