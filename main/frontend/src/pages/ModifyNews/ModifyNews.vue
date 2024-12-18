<template>
    <el-card class="box-card AddNewsContainer">
        <el-form :rules="newsRules" :model="formData" ref="formRef" size="default">
            <el-form-item label="文章标题" prop="title">
                <el-input v-model="formData.title" placeholder="请输入标题"></el-input>
            </el-form-item>
            <el-form-item style="margin: 50px 0;" label="文章内容" prop="article">
                <el-input v-model="formData.article" type="textarea" rows="8"></el-input>
            </el-form-item>
            <el-form-item label="文章类别" prop="type">
                <el-select v-model="formData.type" placeholder="请选择文章类别">
                    <el-option v-for="item in allTypeList" :label="item.tname" :value="item.tid.toString()">
                    </el-option>
                </el-select>
            </el-form-item>
        </el-form>
        <el-form-item>
            <el-button @click="handlerCancel">取消</el-button>
            <el-button type="primary" @click="handlerSave">保存</el-button>
        </el-form-item>
    </el-card>
</template>

<script setup>
import { isUserOverdue, getAnnounceByHid, saveOrAddNews, issueNews } from '@/api/requestApi';
import { ref, onMounted } from "vue"
import { useRoute } from 'vue-router'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useUserInfoStore } from '@/stores/userInfo';

const router = useRouter()
const route = useRoute()

// 获取分类
let allTypeList = ref([]);

const formRef = ref()
// 校验规则
const validateType = (rule, value, callback) => {
    if (value.length) {
        callback()
    } else {
        callback(new Error('文章类别是必须的'))
    }
}
// 校验规则
const validateArticle = (rule, value, callback) => {
    if (value.length) {
        callback()
    } else {
        callback(new Error('文章内容是必须的'))
    }
}
// 校验规则
const validateTitle = (rule, value, callback) => {
    if (value.length) {
        callback()
    } else {
        callback(new Error('文章标题是必须的'))
    }
}
// 校验规则
const newsRules = {
    title: [{ required: true, trigger: 'blur', validator: validateTitle }],
    article: [{ required: true, trigger: 'blur', validator: validateArticle }],
    type: [{ required: true, validator: validateType }],
}

// 点击修改按钮后，获取数据回显
const formData = ref({
    hid: null,
    title: "",   // 文章标题
    article: "", // 文章内容
    type: "",     // 文章类别
})

const clickModifyEcho = async () => {
    if (!route.query.hid) return
    let result = await getAnnounceByHid(route.query.hid)

    formData.value.title = result.title
    formData.value.article = result.article

    formData.value.type = allTypeList.value[result.type - 1].tname;
}

//点击取消的回调
const handlerCancel = () => {
    router.back()
}

//点击保存的回调
const handlerSave = async () => {
    await formRef.value?.validate()

    //发送请求判断用户是否token过期
    await isUserOverdue()

    const Obj = { ...formData.value }

    //整理请求参数
    Obj.hid = route.query.hid
    Obj.type = allTypeList.value[parseInt(Obj.type) - 1].tid;

    //发送请求
    if (route.query.hid) {
        await saveOrAddNews(Obj)
        ElMessage.success("修改成功")
    }
    else {
        await issueNews(formData.value)
        ElMessage.success("添加成功")
    }
    router.push({ name: "Announcements" });
}

onMounted(async () => {
    allTypeList.value = (await useUserInfoStore().getList()).slice();
    clickModifyEcho()
})
</script>

<style scoped>
.AddNewsContainer {
    width: 600px;
    margin: 150px auto;
}
</style>