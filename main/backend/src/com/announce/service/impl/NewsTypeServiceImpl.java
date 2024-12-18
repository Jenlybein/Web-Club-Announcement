package com.announce.service.impl;

import com.announce.dao.BaseDao;
import com.announce.dao.NewsTypeDao;
import com.announce.dao.impl.NewsTypeDaoImpl;
import com.announce.pojo.NewsType;
import com.announce.service.NewsTypeService;

import java.util.List;

public class NewsTypeServiceImpl extends BaseDao implements NewsTypeService {
    private final NewsTypeDao newsTypeDao = new NewsTypeDaoImpl();

    @Override
    public List<NewsType> getAllTypes() throws Exception {
        return newsTypeDao.getAllTypes();
    }
}
