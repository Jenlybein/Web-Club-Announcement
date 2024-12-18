package com.announce.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.lang.reflect.Method;

public class BaseController extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 设置响应的MIME类型为JSON，字符编码为UTF-8，以解决乱码问题
        resp.setContentType("application/json;charset=UTF-8");

        // 获取请求的URI
        String requestURI = req.getRequestURI();
        // 将URI按照“/”分割
        String[] split = requestURI.split("/");
        // 获取最后一个部分作为方法名
        String methodName = split[split.length - 1];

        // 获取当前类的Class对象
        Class<?> clazz = this.getClass();
        try {
            // 通过反射获取指定的方法
            Method method = clazz.getDeclaredMethod(methodName, HttpServletRequest.class, HttpServletResponse.class);
            // 设置方法可以访问
            method.setAccessible(true);
            // 通过反射调用方法
            method.invoke(this, req, resp);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }
}
