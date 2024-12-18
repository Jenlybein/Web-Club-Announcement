package com.announce.dao.impl;

import com.announce.dao.BaseDao;
import com.announce.dao.NewsTypeDao;
import com.announce.pojo.NewsType;

import java.util.List;

public class NewsTypeDaoImpl extends BaseDao implements NewsTypeDao {

    @Override
    public List<NewsType> getAllTypes() throws Exception {
        String sql = "select tid, tname from news_type";

        return executeQuery(NewsType.class, sql);
    }
}
