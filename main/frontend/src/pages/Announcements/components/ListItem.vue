<template>
    <!-- 每一项头条列表 -->
    <div class="list-item">
        <div>
            <span class="text">{{ item.title }}</span>
        </div>
        <div class="detail">
            <span>{{ item.type }}</span>
            <span>{{ item.pageViews }}浏览</span>
            <span v-if="item.pastHours < 24">{{ item.pastHours }}小时前</span>
            <span v-else-if="item.pastHours < 720">{{ Math.floor(item.pastHours / 24) }}天前</span>
            <span v-else-if="item.pastHours < 720*12">{{ Math.floor(item.pastHours / 720) }}月前</span>
            <span v-else>{{ Math.floor(item.pastHours / 720 / 12) }}年前</span>
        </div>
        <div>
            <el-button @click="toDetail(item.hid)" size="small"
                style="background: #198754; margin-left: 15px; color: #bbd3dc">查看全文</el-button>
            <el-popconfirm v-if="item.publisher == type" @confirm="handlerDelete(item.hid)"
                :title="`您确定要删除${item.title}吗?`">
                <template #reference>
                    <el-button size="small" style="background: #dc3545; color: #bbd3dc">删除</el-button>
                </template>
            </el-popconfirm>

            <el-button @click="Modify(item.hid)" v-if="item.publisher == type" size="small"
                style="background: #212529; color: #bbd3dc">修改</el-button>
        </div>
    </div>
</template>

<script setup>
import pinia from "@/stores";
import { useRouter } from "vue-router";
import { useUserInfoStore } from "@/stores/userInfo";
import { removeByHid } from "@/api/requestApi";
import { ElMessage } from 'element-plus'

const router = useRouter()
const userInfoStore = useUserInfoStore(pinia)

const props = defineProps({
    item: {
        type: Object,
        required: true
    },
    getPageList:{
        type: Function,
        required: true
    }
});

// 获取用户ID
const type = userInfoStore.uid

// 点击查看全文的回调
const toDetail = (hid) => {
    router.push({ name: "Detail", query: { hid } });
}

// 点击删除的回调
const handlerDelete = async (id) => {
    await removeByHid(id);
    ElMessage.success('删除成功!');
    props.getPageList(); //重新获取列表请求
}

//点击修改的回调
const Modify = (hid) => {
    router.push({ name: "ModifyNews", query: { hid } });
}
</script>

<style src="./ListItem.css" scoped></style>