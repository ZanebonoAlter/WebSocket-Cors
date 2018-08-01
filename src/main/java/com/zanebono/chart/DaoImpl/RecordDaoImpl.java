package com.zanebono.chart.DaoImpl;

import com.zanebono.chart.Dao.IRecordDao;
import com.zanebono.chart.Model.Chart_Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RecordDaoImpl implements IRecordDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Override
    public List<Chart_Record> loadAll() {
        List<Chart_Record> result= this.jdbcTemplate.query("select * from chart_record",new BeanPropertyRowMapper(Chart_Record.class));
        return result;
    }

    @Override
    public int insert(Chart_Record cr) {
        int result = this.jdbcTemplate.update("insert into chart_record(username,decription,time) values(?,?,?)",cr.getUsername(),cr.getDecription(),cr.getTime());
        return result;
    }
}
