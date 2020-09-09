package com.course.controller;

import com.course.model.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Objects;


@Log4j
@RestController
@Api(value = "v1",tags={"this is my first demo"})
@RequestMapping("v1")
public class UserManager {

    @Autowired
    public SqlSessionTemplate template;

    @ApiOperation(value="登录接口",httpMethod = "POST")
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public Boolean login(HttpServletResponse response, @RequestBody User user){
        int i = template.selectOne("login",user);
        Cookie cookie = new Cookie("login","true");
        response.addCookie(cookie);
        log.info("查询到的结果是："+i);
        if(i==1){
            log.info("登录的用户是："+user.getUserName());
            return true;
        }
        return false;
    }

    @ApiOperation(value = "添加用户接口",httpMethod = "POST")
    @RequestMapping(value = "/addUser",method = RequestMethod.POST)
    public boolean addUser(HttpServletRequest request,@RequestBody User user){
        Boolean x = verIfyCookies(request);
        int result = 0;
        if(x==true){
            result =  template.insert("addUser",user);
        }
        if(result>0){
            log.info("添加的用户数量是："+result);
            return true;
        }
        return  false;

    }

    private Boolean verIfyCookies(HttpServletRequest request){

        Cookie[] cookies = request.getCookies();
        if(Objects.isNull(cookies)){
            log.info("cookies为空");
            return true;
        }
        for(Cookie cookie:cookies){
            if(cookie.getName().equals("login")&&cookie.getValue().equals("true")){
                log.info("cookies验证通过");
                return true;
            }
        }
        return false;
    }

    @ApiOperation(value="获取用户列表信息接口",httpMethod = "POST")
    @RequestMapping(value = "/getUserInfo",method = RequestMethod.POST)
    public List<User> getUserInfo(HttpServletRequest request,@RequestBody User user){
        Boolean x = verIfyCookies(request);
        if(x==true){
            List<User> users = template.selectList("getUserInfo",user);
            log.info("getUerInfo获取到的用户数量是："+users.size());
            return users;
        }else {
            return null;
        }

    }

    @ApiOperation(value = "更新/删除用户接口",httpMethod = "POST")
    @RequestMapping(value = "/updateUser",method = RequestMethod.POST)
    public int updateUser(HttpServletRequest request,@RequestBody User user){
        Boolean x = verIfyCookies(request);
        int i = 0;
        if(x==true){
            i = template.update("updateUserInfo",user);
        }
        if(i>0){
            log.info("更新数据的条目数为："+i);
        }else {
            log.info("更新数据失败请检查原因");
        }
        return i;
    }
    @ApiOperation(value = "获取用户列表接口",httpMethod = "POST")
    @RequestMapping(value = "/getUserList" ,method = RequestMethod.POST)
    public List<User> getUserList(HttpServletRequest request, @RequestBody User user){
        Boolean x = verIfyCookies(request);
        if(x==true){
            List<User> users = template.selectList("getUserList",user);
            log.info("getUserList获取到的用户数量是："+users.size());
            return  users;
        }
        return null;
    }
}
