package com.zanebono.chart.Controller;

import com.google.gson.Gson;
import com.zanebono.chart.Model.User;
import com.zanebono.chart.Service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/Login")
public class LoginController {
    @Autowired
    private LoginService loginService;
    @RequestMapping(value = "/Has_Login",produces = "text/html;charset=UTF-8")
    public String Has_Login(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        if(request.getSession().getAttribute("username")==null){
        if(cookies!=null){
            for (Cookie cookie:cookies){
                if (cookie.getName().equals("username")){
                    request.getSession().setAttribute("username",cookie.getValue());
                    return "go_ahead";
                }
            }
        }}
        return "go_back";
    }
    @RequestMapping(value = "/Judge", produces = "text/html;charset=UTF-8")
    public String Judge_Login(HttpServletRequest request, String username, String password, HttpServletResponse response) {
        String result = this.loginService.SelectLogin(username, password);
        if (result.equals("ok")){
            Cookie cookie = new Cookie("username",username);
            cookie.setMaxAge(60*60);
            response.addCookie(cookie);
            System.out.println("添加cookie成功");
            request.getSession().setAttribute("username", username);
        }
        return result;
    }

    @RequestMapping(value = "/Jsonp", produces = "text/html;charset=UTF-8")
    public String Judge_Login_Jsonp(@RequestParam("callback") String callback,HttpServletRequest request, String username, String password,HttpServletResponse response) {
        Gson gson = new Gson();
        String result = this.loginService.SelectLogin(username, password);
        if (result.equals("ok")){
            Cookie cookie = new Cookie("username",username);
            Cookie cookie1 = new Cookie("test","test");
            cookie.setMaxAge(3600);
            response.addCookie(cookie);
            response.addCookie(cookie1);
            System.out.println("添加cookie成功");
            request.getSession().setAttribute("username", username);
        }
        return callback + "(" + gson.toJson(result) + ")";
    }
    @RequestMapping(value = "/GetAll", produces = "text/html;charset=UTF-8")
    public String Get_All(@RequestParam("callback") String callback, HttpServletRequest request) {
        Gson gson = new Gson();
        List<User> result = this.loginService.SelectAllUser();
        return callback + "(" + gson.toJson(result) + ")";
    }

    @RequestMapping(value = "/Hello", produces = "text/html;charset=UTF-8")
    public String Hello(HttpServletRequest request){
        System.out.println("hello");
        Cookie[] cookies = request.getCookies();
        String username="";
        if(cookies!=null)
        for(Cookie cookie : cookies){
            if(cookie.getName().equals("username"))
                username=cookie.getValue();
        }
        System.out.println(username);
        return "hello";
    }
}
