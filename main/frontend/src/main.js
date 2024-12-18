import { createApp } from 'vue'
import App from './App.vue'

import { createPinia } from 'pinia'
import router from './router'

import ElementPlus from 'element-plus'
import zhCn from 'element-plus/es/locale/lang/zh-cn'
import 'element-plus/dist/index.css'

import mitt from 'mitt'

const app = createApp(App)

app.config.globalProperties.Bus = mitt()
app.use(ElementPlus, {
    locale: zhCn,
})
app.use(createPinia())
app.use(router)

app.mount('#app')

