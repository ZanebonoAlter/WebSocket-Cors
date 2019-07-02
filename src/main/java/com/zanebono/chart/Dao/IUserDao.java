package com.zanebono.chart.Dao;

import com.zanebono.chart.Model.User;

import java.util.List;

public interface IUserDao {
    public String selectUser(String username,String password);
    public List<User> selectAllUser();
    public  List<User> selectOnlineUser();
    public void changeOnline(String username);
    public void changeOffline(String username);
    public User selectByName(String username);
}
