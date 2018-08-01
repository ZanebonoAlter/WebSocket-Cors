package com.zanebono.chart.ServiceImpl;

import com.zanebono.chart.Dao.IUserDao;
import com.zanebono.chart.Model.User;
import com.zanebono.chart.Service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoginServiceImpl implements LoginService {
    @Autowired
    private IUserDao iUserDao;
    @Override
    public String SelectLogin(String username,String password) {
        return this.iUserDao.selectUser(username,password);
    }

    @Override
    public List<User> SelectAllUser() {
        return this.iUserDao.selectAllUser();
    }
}
