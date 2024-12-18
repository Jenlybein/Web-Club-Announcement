package com.announce.dao;

import com.announce.pojo.NewsType;

import java.util.List;

public interface NewsTypeDao {
    /**
     * 查询所有公告类型
     * @return 以List<NewsType>集合形式返回
     */
    List<NewsType> getAllTypes() throws Exception;
}
