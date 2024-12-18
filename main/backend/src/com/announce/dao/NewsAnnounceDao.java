package com.announce.dao;

import com.announce.pojo.NewsAnnounce;
import com.announce.pojo.vo.AnnounceDetailVo;
import com.announce.pojo.vo.AnnouncePageVo;
import com.announce.pojo.vo.AnnounceQueryVo;

import java.util.List;

public interface NewsAnnounceDao {
    /**
     * 获取相应的文章列表
     */
    List<AnnouncePageVo> findPageList(AnnounceQueryVo announceQueryVo) throws Exception;

    /**
     * 获取获得的文章总数
     */
    int findPageCount(AnnounceQueryVo announceQueryVo) throws Exception;

    /**
     * 增加文章浏览量
     */
    int increasePageView(int hid) throws Exception;

    /**
     * 获取文章详细内容
     */
    AnnounceDetailVo getAnnounceDetail(int hid) throws Exception;

    int addNewsAnnounce(NewsAnnounce announce) throws Exception;

    NewsAnnounce findByHid(Integer hid) throws Exception;

    int updateByHid(NewsAnnounce newsAnnounce) throws Exception;

    int removeByHid(int hid) throws Exception;
}
