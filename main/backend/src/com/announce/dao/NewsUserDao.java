package com.announce.dao;

import com.announce.pojo.NewsUser;

public interface NewsUserDao {
    NewsUser findByUsername(String username) throws Exception;

    NewsUser findByUid(Integer userID) throws Exception;

    Integer insertUser(NewsUser user) throws Exception;
}
