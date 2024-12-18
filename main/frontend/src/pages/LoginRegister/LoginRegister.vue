<template>
    <div class="loginRegister">
        <div class="container">
            <!-- 登录注册面板 -->
            <div ref="panel" class="panel leftPanel">
                <!-- 登录表单 -->
                <el-form :model="loginForm" ref="formRef" class="form" :rules="loginRules" label-width="auto"
                    size="large" key="login">
                    <h2 class="formTitle">登录账号</h2>
                    <span class="formDesp">请输入账号和密码</span>

                    <el-form-item label="用户名" prop="username" style="margin-top: 5px;">
                        <el-input v-model="loginForm.username" ref="username" name="username" autocomplete="off"
                            placeholder="账号" style="width: 300px;">
                        </el-input>
                    </el-form-item>

                    <el-form-item label="密码" prop="userPwd" style="margin-top: 25px;">
                        <el-input type="password" v-model="loginForm.userPwd" autocomplete="off" placeholder="密码"
                            style="width: 300px;">
                        </el-input>
                    </el-form-item>

                    <el-form-item>
                        <el-button type="success" @click.native.prevent="login" class="button">
                            登录
                        </el-button>
                    </el-form-item>
                </el-form>

                <!-- 注册表单 -->
                <el-form :model="registerForm" ref="formRef" label-width="80px" class="form displayNone"
                    :rules="registerRules" key="register">
                    <h2 class="formTitle">注册账号</h2>
                    <span class="formDesp">请输入注册所需信息</span>

                    <el-form-item label="昵称" prop="nickName" style="margin-top: 5px;">
                        <el-input v-model="registerForm.nickName" autocomplete="off" ref="nickName" name="nickName"
                            placeholder="请输入昵称" style="width: 300px;"></el-input>
                    </el-form-item>

                    <el-form-item label="账号" prop="username" style="margin-top: 25px;">
                        <el-input v-model="registerForm.username" autocomplete="off" ref="username" name="username"
                            placeholder="请输入账号" style="width: 300px;"></el-input>
                    </el-form-item>

                    <el-form-item label="密码" prop="userPwd" style="margin-top: 25px;">
                        <el-input type="password" v-model="registerForm.userPwd" ref="userPwd" name="userPwd"
                            autocomplete="off" placeholder="请输入密码" style="width: 300px;"></el-input>
                    </el-form-item>

                    <el-form-item label="确认密码" prop="confirmPassword" style="margin-top: 25px;">
                        <el-input type="password" v-model="registerForm.confirmPassword" autocomplete="off"
                            ref="confirmPassword" name="confirmPassword" placeholder="请确认密码"
                            style="width: 300px;"></el-input>
                    </el-form-item>

                    <el-form-item>
                        <el-button type="primary" @click="register" class="button">注册</el-button>
                        <el-button type="danger" @click="resetForm" class="button">重置</el-button>
                    </el-form-item>
                </el-form>
            </div>

            <!-- 切换按钮 -->
            <div ref="switcher" class="switcher rightSwitch">
                <!-- 切换到登录 -->
                <div class="switchContainer displayNone">
                    <h3 class="formTitle">已有账号</h3>
                    <span class="formDesp">无需注册，立刻登录</span>
                    <button class="switch_button button" @click="switchClick">前往登录</button>
                </div>
                <!-- 切换到注册 -->
                <div class="switchContainer">
                    <h3 class="formTitle">还未注册</h3>
                    <span class="formDesp">请先注册账号再进行登录</span>
                    <button class="switch_button button" @click="switchClick">前往注册</button>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup>
import { ref } from 'vue';
import { useUserInfoStore } from '@/stores/userInfo';
import { useRouter } from 'vue-router'
import { registerValidateApi, registerApi } from '@/api/requestApi';
import { ElMessage } from 'element-plus';

const userInfoStore = useUserInfoStore()
const router = useRouter();
const formRef = ref();
const loading = ref(false);

//// 界面动效
const panel = ref();
const switcher = ref();
const switchClick = () => {
    panel.value.classList.toggle('leftPanel');
    panel.value.classList.toggle('rightPanel');
    panel.value.children[0].classList.toggle('displayNone');
    panel.value.children[1].classList.toggle('displayNone');

    switcher.value.classList.toggle('leftSwitch');
    switcher.value.classList.toggle('rightSwitch');
    switcher.value.children[0].classList.toggle('displayNone');
    switcher.value.children[1].classList.toggle('displayNone');
}

//// 校验规则
const validateUsername = async (rule, value, callback) => {
    if (value.length < 4) {
        callback(new Error('用户名长度不能小于4位'))
    } else {
        callback()
    }
}
const validatePassword = (rule, value, callback) => {
    if (value.length < 6) {
        callback(new Error('密码长度不能小于6位'))
    } else {
        callback()
    }
}
const validateRegisName = async (rule, value, callback) => {
    if (value.length < 4) {
        callback(new Error('用户名长度不能小于4位'))
    } else if (await registerValidateApi(registerForm.value.username)) {
        callback(new Error('用户名已被占用'))
    } else {
        callback()
    }
}
const validateNickName = (rule, value, callback) => {
    if (value.length < 2 || value.length > 6) {
        callback(new Error('姓名必须在2-6位'))
    }
    else {
        callback()
    }
}
const validateConfirmPassword = (rule, value, callback) => {
    if (value.length < 6) {
        callback(new Error('密码长度不能小于6位'))
    } else if (registerForm.value.userPwd != value) {
        callback(new Error('两次输入的密码不一样'))
    } else {
        callback()
    }
}

//// 登录部分
/**
 * 账号密码参数
 */
const loginForm = ref({
    username: "",
    userPwd: "",
})

/**
 * 登录校验
 */
const loginRules = {
    username: [{ required: true, validator: validateUsername }],
    userPwd: [{ required: true, trigger: 'blur', validator: validatePassword }]
}

/**
 * 点击登录的回调
 */
const login = async () => {
    await formRef.value?.validate()
    loading.value = true
    try {
        await userInfoStore.login(loginForm.value)
        router.push({ name: "Announcements" });
    } finally {
        loading.value = false
    }
}

//// 注册部分
/**
 * 初始化注册参数
 */
const registerForm = ref({
    username: "",
    userPwd: "",
    confirmPassword: "",
    nickName: ''
})

/**
 * 注册校验
 */
const registerRules = {
    nickName: [{ required: true, trigger: 'blur', validator: validateNickName }],
    username: [{ required: true, validator: validateRegisName }],
    userPwd: [{ required: true, trigger: 'blur', validator: validatePassword }],
    confirmPassword: [{ required: true, trigger: 'blur', validator: validateConfirmPassword }]
}

/**
 * 点击注册的回调
 */
const register = async () => {
    await formRef.value?.validate()
    if (registerForm.value.userPwd == registerForm.value.confirmPassword) {
        //  整理参数
        const obj = {
            username: "",
            userPwd: "",
            nickname: ''
        }
        obj.username = registerForm.value.username
        obj.userPwd = registerForm.value.userPwd
        obj.nickname = registerForm.value.nickName
        //  调用注册接口
        await registerApi(obj)
        resetForm()
        ElMessage.success("注册成功，请登录！")
        router.go(0);
    } else {
        return ElMessage.error("注册出现错误！")
    }
}

/**
 * 点击重置的回调
 */
const resetForm = () => {
    formRef.value?.resetFields()
}

</script>


<style src="./LoginRegister.css" scoped></style>