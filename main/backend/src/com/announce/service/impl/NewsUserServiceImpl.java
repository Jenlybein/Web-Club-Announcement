package com.announce.service.impl;

import com.announce.dao.BaseDao;
import com.announce.dao.NewsUserDao;
import com.announce.dao.impl.NewsUserDaoImpl;
import com.announce.pojo.NewsUser;
import com.announce.service.NewsUserService;
import com.announce.util.MD5Util;

public class NewsUserServiceImpl extends BaseDao implements NewsUserService {
    private final NewsUserDao newsUserDao = new NewsUserDaoImpl();

    @Override
    public NewsUser findByUsername(String username) throws Exception {
        return newsUserDao.findByUsername(username);
    }

    @Override
    public NewsUser findByUid(Integer userID) throws Exception {
        return newsUserDao.findByUid(userID);
    }

    @Override
    public Integer registerUser(NewsUser user) throws Exception {
        user.setUserPwd(MD5Util.encrypt(user.getUserPwd()));

        return newsUserDao.insertUser(user);
    }
}
