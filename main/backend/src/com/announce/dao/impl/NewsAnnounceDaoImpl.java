package com.announce.dao.impl;

import com.announce.dao.BaseDao;
import com.announce.dao.NewsAnnounceDao;
import com.announce.pojo.NewsAnnounce;
import com.announce.pojo.vo.AnnounceDetailVo;
import com.announce.pojo.vo.AnnouncePageVo;
import com.announce.pojo.vo.AnnounceQueryVo;

import java.util.ArrayList;
import java.util.List;

public class NewsAnnounceDaoImpl extends BaseDao implements NewsAnnounceDao {

    @Override
    public List<AnnouncePageVo> findPageList(AnnounceQueryVo announceQueryVo) throws Exception {
        List<Object> params = new ArrayList<>();
        String sql = """
                select hid, title, type, publisher,
                    page_views pageViews,
                    TIMESTAMPDIFF(HOUR, update_time, now()) pastHours
                from news_announce
                where is_deleted = 0
                """;

        Integer type = announceQueryVo.getType();
        if(type!=null && type!=0){
            sql += " and type = ?";
            params.add(type);
        }

        if(announceQueryVo.getKeyWords()!=null&& !announceQueryVo.getKeyWords().isEmpty()){
            sql += " and title like ?";
            params.add("%"+announceQueryVo.getKeyWords()+"%");
        }

        sql = sql.concat(" order by pastHours ASC, pageViews DESC");
        sql = sql.concat(" limit ?,?");
        params.add((announceQueryVo.getPageNum()-1)*announceQueryVo.getPageSize());
        params.add(announceQueryVo.getPageSize());

        return executeQuery(AnnouncePageVo.class, sql, params.toArray());
    }

    @Override
    public int findPageCount(AnnounceQueryVo announceQueryVo) throws Exception {
        List<Object> params = new ArrayList<>();
        String sql = """
                select count(1)
                from news_announce
                where is_deleted = 0
                """;

        Integer type = announceQueryVo.getType();
        if(type!=null && type!=0){
            sql += " and type = ?";
            params.add(type);
        }

        String keywords = announceQueryVo.getKeyWords();
        if(keywords!=null && !keywords.isEmpty()){
            sql += " and title like ?";
            params.add("%"+keywords+"%");
        }

        Long count = executeQuerySingle(Long.class, sql, params.toArray());
        return count.intValue();
    }

    @Override
    public int increasePageView(int hid) throws Exception {
        String sql = "update news_announce set page_views = page_views + 1 where hid = ?";
        return executeUpdate(sql, hid);
    }

    @Override
    public AnnounceDetailVo getAnnounceDetail(int hid) throws Exception {
        String sql = """
                select hid, title, article, type, publisher,
                    page_views pageViews,
                    tname typeName,
                    TIMESTAMPDIFF(HOUR, update_time, now()) pastHours,
                    nick_name author
                from news_announce h
                    left join news_type t on h.type = t.tid
                    left join news_user u on h.publisher = u.uid
                where h.hid = ?
                """;
        return executeQueryBean(AnnounceDetailVo.class,sql,hid);
    }

    @Override
    public int addNewsAnnounce(NewsAnnounce announce) throws Exception {
        String sql = """
                insert into news_announce values(DEFAULT, ?, ?, ?, ?, 0, now(), now(), 0)
                """;
        return executeUpdate(sql,
                announce.getTitle(),
                announce.getArticle(),
                announce.getType(),
                announce.getPublisher()
        );
    }

    @Override
    public NewsAnnounce findByHid(Integer hid) throws Exception {
        String sql = """
                select hid, title, article, type, publisher,
                    page_views pageViews,
                    create_time createTime,
                    update_time updateTime,
                    is_deleted isDeleted
                from news_announce
                where hid = ?
                """;
        return executeQueryBean(NewsAnnounce.class,sql,hid);
    }

    @Override
    public int updateByHid(NewsAnnounce newsAnnounce) throws Exception {
        String sql = """
                update news_announce
                set title = ?, article = ?, type = ?, update_time = now()
                where hid = ?
                """;
        return executeUpdate(sql,
                newsAnnounce.getTitle(),
                newsAnnounce.getArticle(),
                newsAnnounce.getType(),
                newsAnnounce.getHid()
        );
    }

    @Override
    public int removeByHid(int hid) throws Exception {
        String sql = """
                update news_announce
                set is_deleted = 1
                where hid = ?
                """;
        return executeUpdate(sql,hid);
    }
}
