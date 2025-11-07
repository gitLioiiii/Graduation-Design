<template>
  <article class="base-article">

    <ElCard style="max-width: 400px">
    <ElForm
      ref="form"
      :model="model"
      @submit.prevent="login"
      :rules="rules"
      label-width="90"
      label-position="left"
    >
      <h2>海洋乐园预约系统</h2>
      <p>欢迎登录，请输入您的信息</p>

      <ElFormItem label="登录身份">
        <ElRadioGroup v-model="model.role">
          <ElRadio value="user">普通用户</ElRadio>
          <ElRadio value="admin">管理员</ElRadio>
        </ElRadioGroup>
      </ElFormItem>

      <ElFormItem prop="username" label="用户名">
        <ElInput v-model="model.username" type="text" placeholder="请输入用户名">
          <template v-slot:suffix>
            <img src="@/assets/icons/user.png" alt="用户图标" style="width: 16px; height: 16px;" />
          </template>
        </ElInput>
      </ElFormItem>
      <ElFormItem prop="password" label="密码">
        <ElInput v-model="model.password" type="password" placeholder="请输入密码">
          <template v-slot:suffix>
            <img src="@/assets/icons/password.png" alt="密码图标" style="width: 16px; height: 16px;" />
          </template>
        </ElInput>
      </ElFormItem>
      <ElFormItem>
        <div class="btn-actions">
          <span class="register-text">还没有账户？</span>
          <RouterLink :to="{name: 'register'}" class="register-link">注册</RouterLink>
          <ElButton type="primary" native-type="submit" class="login-btn">登录</ElButton>
        </div>
      </ElFormItem>
    </ElForm>
    </ElCard>
  </article>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { ElCard, ElForm, ElFormItem, ElInput, ElButton, ElMessage, ElRadioGroup, ElRadio } from 'element-plus'
import { useRouter, RouterLink } from 'vue-router'
import request from '@/utils/request'
import { useUserStore } from '@/stores/user'
const userStore = useUserStore()

const form = ref(null)
const router = useRouter()
const model = reactive({
  role: 'user', // 默认为普通用户
  username: '',
  password: '',
})

const rules= reactive({
  username: [
    { required:true, message: '请输入用户名。', trigger: 'blur'},
    { min: 2, max:8, message: '名称仅限2~8个字符', trigger: 'blur' },
  ],
  password: [
    { required:true, message:'请输入密码。', trigger: 'blur'},
    { min: 2, max:32, message: '名称仅限2~32个字符', trigger: 'blur' },
  ],
})

const login = () => {
    form.value.validate().then((result) => {
        if (result === true) {
            // 根据角色选择不同的接口
            const loginUrl = model.role === 'admin' ? '/admin/login' : '/login'

            request
                .post(loginUrl, {
                    username: model.username,
                    password: model.password
                })
                .then((response) => {
                    if (response.data.status === true) {
                        // 将角色信息添加到用户数据中
                        const userData = {
                            ...response.data.payload,
                            user: {
                                ...response.data.payload.user,
                                role: model.role
                            }
                        }

                        userStore.login(userData)
                        userStore.cache('user', userData)

                        ElMessage.success('登录成功。')

                        // 根据角色跳转到不同页面
                        if (model.role === 'admin') {
                            router.replace({ name: 'ticket_index' })
                        } else {
                            router.replace({ name: 'index' })
                        }
                    } else {
                        ElMessage.error('登录失败。')
                    }
                })
                .catch(() => {
                    ElMessage.error('登录失败。')
                })
        }
    })
}
</script>

<style lang="scss" scoped>

.base-article {
  height: 100vh;
  background: url('@/assets/img/海洋主题.jpg') center center no-repeat;
  background-size: cover;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0rem 0rem 0rem 0rem;
}

.el-card {
  background: rgba(255, 255, 255, 0.7);
  box-shadow: 0 4px 24px rgba(0, 0, 0, 0.18);
}

.sapn {
  color: #666;
}

.RouterLink {
  color: #409eff;
  // 移除下划线样式
  text-decoration: none;
}

.btn-actions {
  display: flex;
  justify-content: space-around;
  width: 100%;
  padding-right: 1rem;
  gap: 1rem;
  align-items: center;
}

.login-btn {
  padding: 0 28px;
  min-height: 36px;
}

.register-text {
  color: #666;
}

.register-link {
  color: #409eff;
  text-decoration: none;
}

.register-link:hover {
  text-decoration: underline;
}
</style>
