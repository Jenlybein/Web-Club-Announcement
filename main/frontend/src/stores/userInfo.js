import { defineStore } from "pinia";
import {
  getToken,
  removeToken,
  setToken,
  setName,
  getName,
  removeName,
  setUid,
  getUid,
  removeUid,
} from "@/stores/tokenUtils";
import { getLogin, getUserInfo, getfindAllTypes } from "@/api/requestApi";

/**
 * 用户信息
 * @methods setUserInfos 设置用户信息
 */
export const useUserInfoStore = defineStore("userInfo", {
  // 定义状态
  state() {
    return {
      token: getToken(), // 从本地存储中获取 token
      nickName: getName(), // 用户昵称
      uid: getUid(), // 用户 ID
      allTypeList: '', //全部分类
    };
  },

  // 定义方法
  actions: {
    /**
     * 用户登录
     * @param loginForm - 登录表单数据
     */
    async login(loginForm) {
      // 发送登录请求
      const result = await getLogin(loginForm);

      // 请求成功后, 取出 token 并保存到 Pinia 和本地存储中
      this.token = result.token;
      setToken(this.token);
      this.getInfo();
    },

    /**
     * 获取用户信息
     */
    async getInfo() {
      // 发送请求获取用户信息
      const result = await getUserInfo();

      // 更新存储中的用户信息
      this.nickName = result.loginUser.nickname;
      this.uid = result.loginUser.uid;
      setName(this.nickName);
      setUid(this.uid);
    },

    /**
     * 初始化用户信息
     */
    initUserInfo() {
      // 移除本地存储中的 token
      removeToken();
      removeName();
      removeUid();
      // 重置用户信息
      this.token = "";
      this.nickName = "";
      this.uid = "";
      console.log("已执行初始化用户信息");
    },

    async getList() {
      if(!this.allTypeList){
        this.allTypeList = await getfindAllTypes();
      }
      return this.allTypeList;
    },
  },
});
