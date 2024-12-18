import request from "@/api/requestUtil";
import axios from "axios";

// portal/findAllTypes
// 获取分类列表
export const getfindAllTypes = () => {
  return request.get("portal/getAllTypes");
};

// 分页带条件查询所有公告
// @param {Object} info - 分页和查询条件
export const getfindNewsPageInfo = (info) => {
  return request.post("portal/findNewsPage", info);
};

// 查看公告详情
// @param {Number} id - 公告 ID
export const getAnnounceDetail = (id) => {
  return request({
    method: "post",
    url: "portal/showAnnounceDetail",
    headers: {
      "Content-Type": "application/x-www-form-urlencoded;charset=utf-8",
    },
    data: `hid=${id}`,
  });
};

// 删除公告
export const removeByHid = (id) => {
  return request({
    method: "post",
    url: "announce/remove",
    headers: {
      "Content-Type": "application/x-www-form-urlencoded;charset=utf-8",
    },
    data: `hid=${id}`,
  });
};

// 用户登录
// @param {Object} info - 登录信息
export const getLogin = (info) => {
  return request.post("user/login", info);
};

// 获取用户信息
export const getUserInfo = () => {
  return request.get("user/getUserInfo");
};

// 注册校验的接口  user/checkUserName
// @param {String} username - 用户名
export const registerValidateApi = async (username) => {
  try {
    const response = await axios({
      method: "post",
      url: "http://localhost:8080/user/checkRegisterName",
      headers: {
        "Content-Type": "application/x-www-form-urlencoded;charset=utf-8",
      },
      data: `username=${username}`,
      timeout: 50000, // 设置请求超时时间为50秒
    });

    return response.data.code === 505;
  } catch (error) {
    console.error("Error in registerValidateApi:", error);
    return false;
  }
};

// 注册的接口
// @param {Object} userInfo - 用户信息
export const registerApi = (userInfo) => {
  return request.post("user/register", userInfo);
};

// 判断用户登录过期的接口
export const isUserOverdue = () => {
  return request.get("user/checkLogin");
};

// 修改公告回显的接口
// @param {Number} id - 公告 ID
export const getAnnounceByHid = (id) => {
  return request({
    method: "post",
    url: "announce/findAnnounceByHid",
    headers: {
      "Content-Type": "application/x-www-form-urlencoded;charset=utf-8",
    },
    data: `hid=${id}`,
  });
};

// 点击保存修改的回调
// @param {Object} news - 新闻信息
export const saveOrAddNews = (news) => {
  return request.post("announce/update", news);
};

// 发布新闻
// @param {Object} news - 新闻信息
export const issueNews = (news) => {
  return request.post("announce/publish", news);
};
