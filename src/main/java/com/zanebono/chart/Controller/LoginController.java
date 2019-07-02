package com.zanebono.chart.Controller;

import com.google.gson.Gson;
import com.zanebono.chart.Model.User;
import com.zanebono.chart.Service.LoginService;
import com.zanebono.chart.Service.RSAService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

@Api(value = "/Login",description = "用户登录接口")
@RestController
@RequestMapping("/Login")
public class LoginController {
    @Autowired
    private LoginService loginService;
    @Autowired
    private RSAService rsaService;
    @ApiOperation(value = "自动登陆接口",notes = "判断cookie 和 session 是否自动登陆")
    @RequestMapping(value = "/Has_Login",produces = "text/html;charset=UTF-8",method = RequestMethod.POST)
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
    @ApiOperation(value = "用户登录接口(CORS)",notes = "用户登录判断,跨域方法为CORS")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name="username",value = "用户名",required = true,paramType = "query",dataType = "String"),
            @ApiImplicitParam(name="password",value = "密码",required = true,paramType = "query",dataType = "String")
    })
    @RequestMapping(value = "/Judge", produces = "text/html;charset=UTF-8",method = RequestMethod.POST)
    public String Judge_Login(HttpServletRequest request, String username, String password, HttpServletResponse response) {
        username=username.replace(" ","+");
        password=password.replace(" ","+");
        byte[] usernameBytes = username.getBytes();
        byte[] passwordBytes = password.getBytes();
        String username_new = rsaService.decrypt(Base64.decodeBase64(usernameBytes));
        String password_new = rsaService.decrypt(Base64.decodeBase64(passwordBytes));
        String result = this.loginService.SelectLogin(username_new, password_new);
        if (result.equals("ok")){
            Cookie cookie = new Cookie("username",username);
            cookie.setMaxAge(60*60);
            response.addCookie(cookie);
            System.out.println("添加cookie成功");
            request.getSession().setAttribute("username", username_new);
            if(username_new.equals("zanebono")){
                result="ok admin";
            }
        }
        return result;
    }
    @ApiOperation(value = "用户登录接口(Jsonp)",notes = "用户登录判断，跨域方法为Jsonp")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name="callback",value = "Jsonp验证",required = true,paramType = "query",dataType = "String"),
            @ApiImplicitParam(name="username",value = "用户名",required = true,paramType = "query",dataType = "String"),
            @ApiImplicitParam(name="password",value = "密码",required = true,paramType = "query",dataType = "String")
    })
    @RequestMapping(value = "/Jsonp", produces = "text/html;charset=UTF-8",method= RequestMethod.GET)
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
    @ApiOperation(value = "JWT认证接口",notes = "通过过滤器对该方法进行JWT操作，成功颁发令牌")
    @RequestMapping(value = "/Signup", produces = "text/json;charset=UTF-8",method = RequestMethod.POST)
    public String Judge_Login_JWT(@RequestBody User user) {
        String result = this.loginService.SelectLogin(user.getUsername(), user.getPassword());
        return result;
    }
    @ApiOperation(value = "获取用户接口",notes = "获取所有用户，跨域方法为Jsonp")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name="callback",value = "Jsonp验证",required = true,paramType = "query",dataType = "String")
    })
    @RequestMapping(value = "/GetAll", produces = "text/html;charset=UTF-8",method = RequestMethod.GET)
    public String Get_All(@RequestParam("callback") String callback, HttpServletRequest request) {
        Gson gson = new Gson();
        List<User> result = this.loginService.SelectAllUser();
        return callback + "(" + gson.toJson(result) + ")";
    }
    @ApiOperation(value = "获取在线用户接口",notes = "获取所有在线用户，跨域方法为Cors，在有新的或者断开WebSocket连接时前端调用")
    @RequestMapping(value = "/GetOnline", produces = "text/html;charset=UTF-8",method = RequestMethod.POST)
    public String Get_All() {
        Gson gson = new Gson();
        List<User> result = this.loginService.SelectOnlineUser();
        for (User user:result
             ) {
            System.out.println(user.getUsername());
        }
        return gson.toJson(result);
    }
    @ApiIgnore
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
