package com.zanebono.chart.ServiceImpl;

import com.zanebono.chart.Dao.IRecordDao;
import com.zanebono.chart.Model.Chart_Record;
import com.zanebono.chart.Service.ReceiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReceiveServiceImpl implements ReceiveService {
    @Autowired
    private IRecordDao iRecordDao;
    @Override
    public List<Chart_Record> receiveRecord() {
        return this.iRecordDao.loadAll();
    }
}
