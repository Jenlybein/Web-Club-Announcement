<template>
    <div class="announce">
        <!-- 公告列表 -->
        <div>
            <ListItem v-for="item in pageData" :key="item.hid" :item :getPageList="getPageList"></ListItem>
        </div>
        <!-- 分页器 -->
        <div style="margin-top: 20px">
            <el-pagination v-model:current-page="findNewsPageInfo.pageNum" v-model:page-size="findNewsPageInfo.pageSize"
                @size-change="getPageList" @current-change="getPageList" :page-sizes="[5, 7, 10]" background
                layout="prev, pager, next , ->, sizes, total" :total="totalSize" />
        </div>
    </div>
</template>

<script setup>

import { ref, onMounted, watch, onUpdated } from "vue";
import { getfindNewsPageInfo } from "@/api/requestApi"
import ListItem from "./components/ListItem.vue";
import { useRoute } from "vue-router";
import { useUserInfoStore } from "@/stores/userInfo";

// 获取分类
let allTypeList = null;

// 公告搜索请求数据
const findNewsPageInfo = ref(
    {
        keyWords: "",   // 搜索标题关键字
        type: 0,        // 新闻类型
        pageNum: 1,     // 页码数
        pageSize: 5,    // 页大小
    }
)

// 初始化列表数据
const pageData = ref([{
    hid: null,
    pageViews: null,
    pastHours: null,
    publisher: null,
    title: "",
    type: null
}])

// 初始化请求分页列表数据
const totalSize = ref(0) //分页总数量
const getPageList = async () => {
    let result = await getfindNewsPageInfo(findNewsPageInfo.value)

    pageData.value = result.pageInfo.pageData
    pageData.value.forEach(item => { item.type = allTypeList[item.type - 1].tname; })

    findNewsPageInfo.value.pageNum = result.pageInfo.pageNum
    findNewsPageInfo.value.pageSize = result.pageInfo.pageSize

    totalSize.value = result.pageInfo.totalSize
}

// 监视初始化参数type的变化,当type发生改变的时候重新发送请求获取列表数据
watch(() => findNewsPageInfo.value, () => {
    getPageList()
}, {
    deep: true,
})

// 组件挂载的生命周期钩子
onMounted(async () => {
    allTypeList = (await useUserInfoStore().getList()).slice();
    getPageList();
    findNewsPageInfo.value.type = useRoute()?.query?.tid
    findNewsPageInfo.value.keyWords = useRoute()?.query?.keyword
})

onUpdated(() => {
    findNewsPageInfo.value.type = useRoute().query.tid
    findNewsPageInfo.value.keyWords = useRoute().query.keyword
})

</script>

<style src="./Announcements.css" scoped></style>