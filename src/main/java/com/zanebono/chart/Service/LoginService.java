package com.zanebono.chart.Service;

import com.zanebono.chart.Model.User;

import java.util.List;

public interface LoginService {
    public String SelectLogin(String username,String password);
    public List<User> SelectAllUser();
}
