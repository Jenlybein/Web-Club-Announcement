package com.announce.service.impl;

import com.announce.dao.BaseDao;
import com.announce.dao.NewsAnnounceDao;
import com.announce.dao.impl.NewsAnnounceDaoImpl;
import com.announce.pojo.NewsAnnounce;
import com.announce.pojo.vo.AnnounceDetailVo;
import com.announce.pojo.vo.AnnouncePageVo;
import com.announce.pojo.vo.AnnounceQueryVo;
import com.announce.service.NewsAnnounceService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewsAnnounceServiceImpl extends BaseDao implements NewsAnnounceService {
    NewsAnnounceDao announceDao = new NewsAnnounceDaoImpl();
    @Override
    public Map findPage(AnnounceQueryVo announceQueryVo) throws Exception {
        int pageNum = announceQueryVo.getPageNum();
        int pageSize = announceQueryVo.getPageSize();
        List<AnnouncePageVo> pageData = announceDao.findPageList(announceQueryVo);
        int totalSize = announceDao.findPageCount(announceQueryVo);
        int totalPage = totalSize % pageSize == 0 ?  totalSize/pageSize  : totalSize/pageSize+1;
        Map pageInfo = new HashMap();
        pageInfo.put("pageData", pageData);
        pageInfo.put("pageNum", pageNum);
        pageInfo.put("pageSize", pageSize);
        pageInfo.put("totalSize", totalSize);
        pageInfo.put("totalPage", totalPage);

        return pageInfo;
    }

    @Override
    public AnnounceDetailVo getAnnounceDetail(int hid) throws Exception {
        // 修改浏览量
        if(announceDao.increasePageView(hid)>0){
            System.out.println("访问量修改成功");
        }else{
            System.out.println("访问量修改失败");
        }

        // 查询内容
        return announceDao.getAnnounceDetail(hid);
    }

    @Override
    public int addNewsAnnounce(NewsAnnounce announce) throws Exception {
        return announceDao.addNewsAnnounce(announce);
    }

    @Override
    public NewsAnnounce findByHid(Integer hid) throws Exception {
        return announceDao.findByHid(hid);
    }

    @Override
    public int updateByHid(NewsAnnounce newsAnnounce) throws Exception {
        return announceDao.updateByHid(newsAnnounce);
    }

    @Override
    public int removeByHid(int hid) throws Exception {
        return announceDao.removeByHid(hid);
    }
}
