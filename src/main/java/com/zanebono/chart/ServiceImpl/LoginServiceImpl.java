package com.zanebono.chart.ServiceImpl;

import com.zanebono.chart.Dao.IUserDao;
import com.zanebono.chart.Mapper.UserMapper;
import com.zanebono.chart.Model.User;
import com.zanebono.chart.Service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoginServiceImpl implements LoginService {
    @Autowired
    private IUserDao iUserDao;
    @Autowired
    private UserMapper userMapper;
    @Override
    public String SelectLogin(String username,String password) {
        //return this.iUserDao.selectUser(username,password);
        User user = userMapper.selectUser(username,password);
        if(user==null){
            return "not ok";
        }
        return "ok";
    }

    @Override
    public List<User> SelectAllUser() {
        return this.userMapper.selectAllUser();
    }

    @Override
    public List<User> SelectOnlineUser() {
        return this.userMapper.selectOnlineUser();
    }

    @Override
    public void ChangeOnline(String username) {
        this.userMapper.changeOnline(username);
    }

    @Override
    public void ChangeOffline(String username) {
        this.userMapper.changeOffline(username);
    }

    @Override
    public User selectByName(String username) {
        return this.userMapper.selectByName(username);
    }
}
