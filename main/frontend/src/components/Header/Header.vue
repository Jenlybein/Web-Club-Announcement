<template>
    <div class="header">
        <div class="left" style="margin-left: 0px;">
            <!-- 标题 -->
            <RouterLink to="/" class="titleandicon">
                <div class="icon">⚪</div>
                <div class="title">
                    <div class="chinese">Web俱乐部公告 - 网页端</div>
                    <div class="english">Web Club Announcement - Web</div>
                </div>
            </RouterLink>
        </div>

        <div class="right" style="display:flex; margin-left: 10px;">
            <!-- 公告类型 -->
            <div class="types1">
                <!-- 响应式布局1：横向排列 -->
                <button v-for="(item) in allTypeList" :key="item.tid" @click="RouterPush(item.tid)"
                    style=" border: 0; outline: none; background-color: transparent;">
                    <span :class="{ type: true, hightlight: item.isHighlight }">
                        {{ item.tname }}
                    </span>
                </button>
            </div>
            <select class="types2" @change="RouterPush($event.target.value)">
                <!-- 响应式布局2：选项 -->
                <option v-for="(item) in allTypeList" :key="item.tid" :value="item.tid"
                    :class="{ type: true, hightlight: item.isHighlight }">
                    {{ item.tname }}
                </option>
            </select>


            <!-- 搜索栏 -->
            <div class="search" style="margin-right: 5px;">
                <el-button type="primary" @click="SearchShow">搜索</el-button>
                <span class="search-input" ref="searchInput" @blur="SearchBlur">
                    <input class="search-text" type="text" placeholder="请输入搜索内容" @keydown.enter="handleSearch">
                    <button class="search-button" type="primary" @click="handleSearch">🔍</button>
                </span>
            </div>

            <!-- 登录和注册 -->
            <div v-if="nickName" style="display: flex; justify-content: center; align-items: center;">
                <el-dropdown>
                    <el-button type="primary">
                        您好:{{ nickName }}<el-icon class="el-icon--right"><arrow-down /></el-icon>
                    </el-button>
                    <template #dropdown>
                        <el-dropdown-menu>
                            <el-dropdown-item @click="handlerNews">发布新闻</el-dropdown-item>
                            <el-dropdown-item>个人中心</el-dropdown-item>
                            <el-dropdown-item>浏览记录</el-dropdown-item>
                            <el-dropdown-item @click="Logout">退出登录</el-dropdown-item>
                        </el-dropdown-menu>
                    </template>
                </el-dropdown>
            </div>
            <div v-else class="containerButton">
                <el-button size="default" style="background: #212529; color: #ffffff"
                    @click="toLoginRegesiter">登录</el-button>
            </div>
        </div>
    </div>
</template>

<script setup>
import { ref, onMounted, onUpdated } from 'vue'
import { useUserInfoStore } from "@/stores/userInfo"
import { isUserOverdue } from "@/api/requestApi"
import { useRouter } from 'vue-router';
import { removeToken } from '@/stores/tokenUtils';
import { ArrowDown } from '@element-plus/icons-vue'

const userInfoStore = useUserInfoStore()
const router = useRouter()
const nickName = ref("")

// 获取分类
const allTypeList = ref([]);

const getTypeList = async () => {
    allTypeList.value = (await userInfoStore.getList()).slice();

    allTypeList.value.unshift({
        tid: 0,
        tname: "全部"
    })

    // 添加高亮标识
    allTypeList.value.forEach((item) => {
        item.isHighlight = false
    })
}

// 公告类型跳转
const RouterPush = (tid) => {
    router.push({ name: 'Announcements', query: { tid } });
    allTypeList.value.forEach((item) => {
        item.isHighlight = false
    })
    allTypeList.value[tid].isHighlight = true
}

// 搜索框
const searchInput = ref(null);
let firstClick = true;
const SearchShow = () => {
    document.addEventListener('click', SearchBlur);
    searchInput.value.style.display = "flex";
    firstClick = true;
}
const SearchReset = () => {
    searchInput.value.style.display = 'none';
    document.removeEventListener('click', SearchBlur);
    searchInput.value.children[0].value = '';
}
const SearchBlur = (event) => {
    if (firstClick) {
        firstClick = false;
    }
    else if (searchInput.value.style.display != "none" && !searchInput.value.contains(event.target)) {
        SearchReset();
    }
}
const handleSearch = () => {
    const keyword = searchInput.value.children[0].value;
    router.push({ name: 'Announcements', query: { keyword } });
    SearchReset();
}

// 登录与注册
const toLoginRegesiter = () => {
    router.push({ name: "LoginRegister" });
}
// 点击退出登录的回调
const Logout = () => {
    removeToken()
    userInfoStore.initUserInfo()
    nickName.value = ""
    router.go({ name: "Announcements" });
}

//点击发布新闻的回调
const handlerNews = async () => {
    //发送请求判断用户是否token过期
    await isUserOverdue();
    router.push({ name: "ModifyNews" });
}

onMounted(async () => {
    await getTypeList();
    nickName.value = userInfoStore.nickName;
})

onUpdated(async () => {
    nickName.value = userInfoStore.nickName;
})

</script>

<style scoped src="./Header.css"></style>