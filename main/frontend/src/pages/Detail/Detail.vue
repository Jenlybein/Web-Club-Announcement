<template>
    <div class="seeDetails">
        <div>
            <h4>{{ detailList.title }}</h4>
        </div>
        <div style="margin-right: 250px">
            <span>{{ detailList.typeName }}</span>
            <span>{{ detailList.pageViews }}浏览</span>
            <span>{{ detailList.pastHours }}小时前</span>
        </div>

        <div style="width: 500px; margin: 20px 0px 0px 70px">
            <p>
                {{ detailList.article }}
            </p>
        </div>
    </div>
</template>

<script>
import { defineComponent } from 'vue'
export default defineComponent({
    name: 'Detail'
})
</script>

<script setup>
import { getAnnounceDetail } from "@/api/requestApi"
import { ref, onMounted } from "vue"
import { useRoute } from 'vue-router'
const route = useRoute() // 路由信息对象

const detailList = ref({}) //详情数据
//获取详情初始化数据
const getDetailList = async () => {
    let result = await getAnnounceDetail(route.query.hid)
    detailList.value = result.announce
}

onMounted(() => {
    getDetailList()
})
</script>

<style src="./Detail.css" scoped></style>