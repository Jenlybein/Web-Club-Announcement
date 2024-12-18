package com.announce.controller;

import com.announce.common.Result;
import com.announce.pojo.NewsType;
import com.announce.pojo.vo.AnnounceDetailVo;
import com.announce.pojo.vo.AnnounceQueryVo;
import com.announce.service.NewsAnnounceService;
import com.announce.service.NewsTypeService;
import com.announce.service.impl.NewsAnnounceServiceImpl;
import com.announce.service.impl.NewsTypeServiceImpl;
import com.announce.util.WebUtil;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/portal/*")
public class PortalController extends BaseController{
    private final NewsTypeService typeService = new NewsTypeServiceImpl();
    private final NewsAnnounceService announceService = new NewsAnnounceServiceImpl();

    /**
     * 获取公告详细内容
     */
    protected void showAnnounceDetail(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        int hid = Integer.parseInt(req.getParameter("hid"));
        AnnounceDetailVo detailVo = announceService.getAnnounceDetail(hid);
        Map<String, Object> data = new HashMap<>();
        data.put("announce", detailVo);
        WebUtil.writeJson(resp,Result.sucess(data));
    }

    /**
     * 搜寻相应的页面
     */
    protected void findNewsPage(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        AnnounceQueryVo announceQueryVo = WebUtil.readJson(req, AnnounceQueryVo.class);
        Map pageInfo = announceService.findPage(announceQueryVo);
        Map data = new HashMap();
        data.put("pageInfo", pageInfo);
        WebUtil.writeJson(resp, Result.sucess(data));
    }

    /**
     * 查询所有公告类型的业务接口实现, 装入Result响应给客户端
     */
    protected void getAllTypes(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        List<NewsType> types = typeService.getAllTypes();

        WebUtil.writeJson(resp, Result.sucess(types));
    }
}
