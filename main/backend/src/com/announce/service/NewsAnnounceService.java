package com.announce.service;

import com.announce.pojo.NewsAnnounce;
import com.announce.pojo.vo.AnnounceDetailVo;
import com.announce.pojo.vo.AnnounceQueryVo;

import java.util.Map;

public interface NewsAnnounceService {
    /**
     * 根据查询信息查找公告并需要响应的数据
     * pageInfo:{
     *         pageData:[
     *             {
     *                 hid:
     *                 title:
     *                 type:
     *                 pageViews:
     *                 pastHours:
     *                 publisher:
     *             },
     *             {...}
     *         ],
     *         pageNum:
     *         pageSize:
     *         totalPage:
     *         totalSize:
     * }
     */
    Map findPage(AnnounceQueryVo announceQueryVo) throws Exception;

    /**
     * 获取公告详细内容
     */
    AnnounceDetailVo getAnnounceDetail(int hid) throws Exception;

    /**
     * 增加新公告
     */
    int addNewsAnnounce(NewsAnnounce announce) throws Exception;

    /**
     * 根据hid寻找公告
     */
    NewsAnnounce findByHid(Integer hid) throws Exception;

    int updateByHid(NewsAnnounce newsAnnounce) throws Exception;

    int removeByHid(int hid) throws Exception;
}
