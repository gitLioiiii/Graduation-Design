<template>
  <header class="base-header">
    <h1 class="base-header-logo">
      <RouterLink to="/">
        <strong>海洋馆</strong>
      </RouterLink>
    </h1>
    <nav class="base-header-navigator">
      <template v-if="userStore.logged">
          <strong>{{ userStore.name }}</strong>
          <span @click.stop="logout">退出</span>
      </template>
      <RouterLink v-else :to="{ name: 'login' }">登录</RouterLink>
    </nav>
  </header>
</template>

<script setup>
import { RouterLink, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useUserStore } from '@/stores/user'
import request from '@/utils/request'

const router = useRouter()
const userStore = useUserStore()

const logout = () => {
    request
        .post('/logout')
        .then((response) => {
            console.log(response)
        })
        .finally(() => {
            userStore.logout()
            userStore.clear('user')
            ElMessage.success('已成功退出。')
            router.push({ name: 'login' })
        })
}
</script>

<style>
.base-header {
  display: flex;
  flex-flow: row nowrap;
  align-items: center;
  justify-content: space-between;

  background: #7BC0FC;
  height: 5rem;
  width: 100%;
  /* padding: 0rem 2rem; */

  position: fixed;
  top: 0rem;
  left: 0rem;
  z-index: 1000;

  /* width: calc(100vw - 2rem * 2); */
}

.base-header-logo {
  background: url('@/assets/img/logo.jpg') left center no-repeat;
  background-size: contain;
  margin: 0rem 0rem 0rem 2rem;
  height: 4rem;
  width: 20rem;
}

.base-header-logo a {
  display: block;
  outline: none;
  height: 100%;
  width: 100%;
}

.base-header-logo a strong {
  display: none;
}

.base-header-navigator {
  margin: 0rem 2rem 0rem 0rem;
}

/* rgba可设置可见度 */
.base-header-navigator a {
  color: rgba(0, 0, 0, 0.8);
  text-decoration: none;
  transition: all 0.2s;
}

.base-header-navigator a:hover {
  color: rgb(255, 255, 255, 1);
}
</style>
