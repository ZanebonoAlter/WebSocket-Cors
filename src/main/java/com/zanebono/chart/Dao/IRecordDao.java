package com.zanebono.chart.Dao;

import com.zanebono.chart.Model.Chart_Record;

import java.util.List;

public interface IRecordDao {
    public List<Chart_Record> loadAll();
    public int insert(Chart_Record cr);
}
