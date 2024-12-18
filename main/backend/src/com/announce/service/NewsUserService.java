package com.announce.service;

import com.announce.pojo.NewsUser;

public interface NewsUserService {
    /**
     * 根据用户登录的账号寻找用户
     * @param username 输入的账号
     * @return 完整的NewsUser对象，无则返回null
     */
    NewsUser findByUsername(String username) throws Exception;

    /**
     * 根据token的ID寻找用户
     * @param userID token中的ID
     * @return 完整的NewsUser对象，无则返回null
     */
    NewsUser findByUid(Integer userID) throws Exception;

    /**
     * 注册业务
     * @param user NewsUser的完整信息
     * @return 数据库受影响的行数
     */
    Integer registerUser(NewsUser user) throws Exception;
}
