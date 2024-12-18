package com.announce.controller;

import com.announce.common.Result;
import com.announce.common.ResultCodeEnum;
import com.announce.pojo.NewsUser;
import com.announce.service.NewsUserService;
import com.announce.service.impl.NewsUserServiceImpl;
import com.announce.util.JwtUtil;
import com.announce.util.MD5Util;
import com.announce.util.WebUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/user/*")
public class NewsUserController extends BaseController {
    private final NewsUserService  userService = new NewsUserServiceImpl();

    /**
     * 前端校验是否失去登录状态
     */
    protected void checkLogin(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String token = req.getHeader("token");
        Result result = Result.build(null, ResultCodeEnum.NOTLOGIN);
        if(token != null) {
            if(!JwtUtil.isExpiration(token)){
                result = Result.sucess(null);
            }
        }
        WebUtil.writeJson(resp, result);
    }

    /**
     * 注册业务接口
     */
    protected void register(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        NewsUser user = WebUtil.readJson(req, NewsUser.class);
        NewsUser userCheck = userService.findByUsername(user.getUsername());
        Result<Object> result;
        if(userCheck == null) {
            Integer rows = userService.registerUser(user);
            result = rows!=0 ? Result.sucess(null) : Result.build(null, ResultCodeEnum.USERNAME_ERROR);

        }else{
            result = Result.build(null, ResultCodeEnum.USERNAME_USED);
        }
        WebUtil.writeJson(resp, result);
    }

    /**
     * 检查用户名是否已存在
     */
    protected void checkRegisterName(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String username = req.getParameter("username");
        NewsUser user = userService.findByUsername(username);
        Result<Object> result = (user == null) ? Result.sucess(null) : Result.build(null, ResultCodeEnum.USERNAME_USED);
        WebUtil.writeJson(resp,result);
    }

    /**
     * 根据token获得用户信息
     */
    protected void getUserInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String token = request.getHeader("token");

        Result result = Result.build(null, ResultCodeEnum.NOTLOGIN);
        if(token != null && !token.isEmpty()) {
            if(!JwtUtil.isExpiration(token)){
                Integer userID = JwtUtil.getUserId(token).intValue();
                NewsUser newsUser = userService.findByUid(userID);
                if(newsUser != null) {
                   Map<String, NewsUser> data = new HashMap<>();
                   newsUser.setUserPwd("");
                   data.put("loginUser",newsUser);

                   result = Result.sucess(data);
                }
            }
        }
        WebUtil.writeJson(response, result);
    }

    /**
     * 处理登录表单提交的业务接口实现
     */
    protected void login(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        NewsUser paramUser = WebUtil.readJson(req, NewsUser.class);
        NewsUser loginUser = userService.findByUsername(paramUser.getUsername());
        Result result = null;
        if(loginUser == null){
            result = Result.build(null, ResultCodeEnum.USERNAME_ERROR);
        }else{
            if(MD5Util.encrypt(paramUser.getUserPwd()).equalsIgnoreCase(loginUser.getUserPwd())){
                Integer uid = loginUser.getUid();
                String token = JwtUtil.createToken(uid.longValue());
                Map<String, String> data = new HashMap<>();
                data.put("token", token);
                result = Result.build(data, ResultCodeEnum.SUCCESS);
            }else{
                result = Result.build(null, ResultCodeEnum.PASSWORD_ERROR);
            }
        }
        WebUtil.writeJson(resp,result);
    }

}
