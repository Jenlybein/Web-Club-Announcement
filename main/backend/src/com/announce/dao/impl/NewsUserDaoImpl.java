package com.announce.dao.impl;

import com.announce.dao.BaseDao;
import com.announce.dao.NewsUserDao;
import com.announce.pojo.NewsUser;

public class NewsUserDaoImpl extends BaseDao implements NewsUserDao {
    @Override
    public NewsUser findByUsername(String username) throws Exception {
        String sql = """
                select uid, username, user_pwd userPwd, nick_name nickname
                from news_user
                where username = ?
                """;
        return executeQueryBean(NewsUser.class, sql, username);
    }

    @Override
    public NewsUser findByUid(Integer userID) throws Exception {
        String sql = """
                select uid, username, user_pwd userPwd, nick_name nickname
                from news_user
                where uid = ?
                """;
        return executeQueryBean(NewsUser.class, sql, userID);
    }

    @Override
    public Integer insertUser(NewsUser user) throws Exception {
        String sql = """
                insert into news_user values(DEFAULT , ?, ?, ?)
                """;
        return executeUpdate(sql,
                user.getUsername(),
                user.getUserPwd(),
                user.getNickname()
                );
    }
}
