package com.zanebono.chart.Mapper;

import com.zanebono.chart.Model.Chart_Record;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface RecordMapper {
    @Select("select * from chart_record")
     List<Chart_Record> loadAll();
    @Insert("insert into chart_record(username,decription,time) values(#{username},#{decription},#{time})")
     int insert(Chart_Record cr);
}
