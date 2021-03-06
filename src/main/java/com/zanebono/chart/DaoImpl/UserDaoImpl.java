package com.zanebono.chart.DaoImpl;

import com.zanebono.chart.Dao.IUserDao;
import com.zanebono.chart.Model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserDaoImpl implements IUserDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Override
    public String selectUser(String username, String password) {
       List<User> users = jdbcTemplate.query("select * from user where username = ? and password=?",new Object[]{username,password},new BeanPropertyRowMapper(User.class));
       if(users.size()>0){
           return "ok";
       }else{
           return "not ok";
       }
    }

    @Override
    public List<User> selectAllUser() {
        List<User> users = jdbcTemplate.query("select * from user",new BeanPropertyRowMapper(User.class));
        return users;
    }

    @Override
    public List<User> selectOnlineUser() {
        List<User> users = jdbcTemplate.query("select * from user where status=?",new Object[]{"在线"},new BeanPropertyRowMapper(User.class));
        return users;
    }

    @Override
    public void changeOnline(String username) {
        this.jdbcTemplate.update("update user set status = '在线' where username = ?",username);
    }
    @Override
    public void changeOffline(String username) {
        this.jdbcTemplate.update("update user set status = '离线' where username = ?",username);
    }

    @Override
    public User selectByName(String username) {
        List<User> users = jdbcTemplate.query("select * from user where username=?",new Object[]{"username"},new BeanPropertyRowMapper(User.class));
        if(users.size()>0)
            return users.get(0);
        return null;
    }
}
