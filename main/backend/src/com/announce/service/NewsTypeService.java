package com.announce.service;

import com.announce.pojo.NewsType;

import java.util.List;

public interface NewsTypeService {

    /**
     * 查询所有公告类型
     * @return 以List<NewsType>集合形式返回
     */
    List<NewsType> getAllTypes() throws Exception;
}
