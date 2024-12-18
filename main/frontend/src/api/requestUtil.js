import axios from "axios";
import pinia from '@/stores/index';
import NProgress from "nprogress";

import { ElMessage } from 'element-plus';
import { useUserInfoStore } from '@/stores/userInfo';
import { getToken } from '@/stores/tokenUtils';

import "nprogress/nprogress.css";

// 配置新建一个 axios 实例
const service = axios.create({
  baseURL: "http://localhost:8080/", // 设置基本URL，所有请求都会基于此URL
  timeout: 50000, // 设置请求超时时间为50秒
});

/**
 * 添加请求拦截器
 * @param config - 请求配置对象
 * @returns config - 修改后的请求配置对象
 */
service.interceptors.request.use(
  (config) => {
    NProgress.start(); // 开启进度条
    // 获取用户信息存储：非组件中使用 Pinia 需要显式地传入 Pinia 实例。
    //const userInfoStore = useUserInfoStore(pinia);
    const token = getToken();

    if (token) {
      // 通过将token添加到请求头中，携带给后台。
      (config.headers)['token'] = token;
    }
    return config;
  }
);

/**
 * 添加响应拦截器
 * @param response - 响应对象
 * @returns 响应数据中的data属性数据或拒绝请求
 */
service.interceptors.response.use(
  (response) => {
    NProgress.done(); // 关闭进度条
    // 判断响应状态码
    if (response.data.code !== 200) {
      if (response.data.code == 501) return Promise.reject(ElMessage.error("用户名有误"));
      else if (response.data.code == 503) return Promise.reject(ElMessage.error("密码有误"));
      else if (response.data.code == 504) {
        
        return Promise.reject(ElMessage.error("登录已过期"));
      }
      else if (response.data.code == 505) return Promise.reject(ElMessage.error("用户名占用"));
    } else {
      return response.data.data; // 返回成功响应数据中的data属性数据
    }
  },
  (error) => {
    NProgress.done(); // 关闭进度条
    return Promise.reject(error.message);
  }
);

export default service;
