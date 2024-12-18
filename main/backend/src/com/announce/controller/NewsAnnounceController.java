package com.announce.controller;

import com.announce.common.Result;
import com.announce.pojo.NewsAnnounce;
import com.announce.service.NewsAnnounceService;
import com.announce.service.impl.NewsAnnounceServiceImpl;
import com.announce.util.JwtUtil;
import com.announce.util.WebUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/announce/*")
public class NewsAnnounceController extends BaseController{
    private final NewsAnnounceService announceService = new NewsAnnounceServiceImpl();

    /**
     * 删除公告
     */
    protected void remove(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        int hid = Integer.parseInt(req.getParameter("hid"));
        announceService.removeByHid(hid);

        WebUtil.writeJson(resp, Result.sucess(null));
    }

    /**
     * 更新公告
     */
    protected void update(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        NewsAnnounce newsAnnounce = WebUtil.readJson(req, NewsAnnounce.class);
        announceService.updateByHid(newsAnnounce);

        WebUtil.writeJson(resp, Result.sucess(null));
    }

    /**
     * 修改头条回显业务接口
     */
    protected void findAnnounceByHid(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        Integer hid = Integer.parseInt(req.getParameter("hid"));
        NewsAnnounce announce = announceService.findByHid(hid);
        WebUtil.writeJson(resp, Result.sucess(announce));
    }

    /**
     * 发布公告
     */
    protected void publish(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String token = req.getHeader("token");
        Integer userId = JwtUtil.getUserId(token).intValue();

        NewsAnnounce announce = WebUtil.readJson(req, NewsAnnounce.class);
        announce.setPublisher(userId);

        announceService.addNewsAnnounce(announce);

        WebUtil.writeJson(resp, Result.sucess(null));
    }
}

