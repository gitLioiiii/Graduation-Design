<template>
  <article class="base-article">
    <ElCard style="max-width: 400px">
    <ElForm
      ref="form"
      :model="model"
      @submit.prevent="save"
      :rules="rules"
      label-width="90"
      label-position="left"
    >
      <h2>海洋乐园预约系统</h2>
      <p>欢迎注册，请输入您的信息</p>

      <ElFormItem label="注册身份">
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
      <ElFormItem prop="name" label="姓名">
        <ElInput v-model="model.name" type="text" placeholder="请输入姓名">
          <template v-slot:suffix>
            <img src="@/assets/icons/name.png" alt="用户姓名" style="width: 16px; height: 16px;" />
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
          <span class="login-text">已有账户？</span>
          <RouterLink :to="{name: 'login'}" class="login-link">登录</RouterLink>
          <ElButton type="primary" native-type="submit" class="register-btn">注册</ElButton>
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

const form = ref(null)
const router = useRouter()
const model = reactive({
  role: 'user', // 默认为普通用户
  username: '',
  name: '',
  password: '',
})

const rules= reactive({
  username: [
    { required:true, message: '请输入用户名。', trigger: 'blur'},
    { min: 2, max:16, message: '用户名仅限2~16个字符', trigger: 'change' },
  ],
  name: [
    { required:false, message: '请输入姓名。', trigger: 'blur'},
    { max:32, message: '姓名最多32个字符', trigger: 'change' },
  ],
  password: [
    { required:true, message:'请输入密码。', trigger: 'blur'},
    { min: 6, max:16, message: '密码仅限6~16个字符', trigger: 'change' },
  ],
})

const save = () => {
  form.value
    .validate()
    .then((result) => {
      if (result === true) {
        // 根据角色选择不同的注册接口
        const registerUrl = model.role === 'admin' ? '/admin/create' : '/user/create'

        request
          .post(registerUrl, {
            username: model.username,
            name: model.name,
            password: model.password
          })
          .then((response) => {
            if (response.data.status === true) {
              ElMessage.success(`${model.role === 'admin' ? '管理员' : '用户'}注册成功！请登录`)
              router.push({ name: 'login' })
            } else {
              ElMessage.error(response.data.message || '注册失败！')
            }
          })
          .catch(() => {
            ElMessage.error('注册失败！')
          })
      }
    })
    .catch(() => {
      ElMessage.error('注册失败！')
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

.register-btn {
  padding: 0 28px;
  min-height: 36px;
}

.login-text {
  color: #666;
}

.login-link {
  color: #409eff;
  text-decoration: none;
}

.login-link:hover {
  text-decoration: underline;
}
</style>
