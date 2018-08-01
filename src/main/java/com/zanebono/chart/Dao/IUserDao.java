package com.zanebono.chart.Dao;

import com.zanebono.chart.Model.User;

import java.util.List;

public interface IUserDao {
    public String selectUser(String username,String password);
    public List<User> selectAllUser();
}
