import pinia from "@/stores";
import { getToken, removeToken } from "@/stores/tokenUtils";
import { useUserInfoStore } from "@/stores/userInfo";
import { createRouter, createWebHistory } from "vue-router";


const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: "/",
      name: "home",
      redirect: "/announcements",
    },
    {
      // 头条
      path: "/announcements",
      component: () => import("@/pages/Announcements/Announcements.vue"),
      name: "Announcements",
    },
    {
      //头条详情
      path: "/detail",
      component: () => import("@/pages/Detail/Detail.vue"),
      name: "Detail",
    },
    {
      // 登录注册
      path: "/loginregister",
      component: () => import("@/pages/LoginRegister/LoginRegister.vue"),
      name: "LoginRegister",
    },
    {
      //发布新闻的页面
      path: "/modifynews",
      component: () => import("../pages/ModifyNews/ModifyNews.vue"),
      name: "ModifyNews",
    },
  ],
});

//全局前置守卫
const userInfoStore = useUserInfoStore(pinia)
router.beforeEach(async (to, from, next) => {
  const token = getToken();
  const userInfo = !!userInfoStore.nickName;
  if (token) {
    if (to.path == "/loginregister") {
      next({ path: "/" });
    } else {
      if (userInfo) {
        next();
      } else {
        try {
          await userInfoStore.getInfo();
          next();
        } catch (error) {
          console.log(error);
          removeToken();
        }
      }
    }
  } else {
    next();
  }
});

export default router;
