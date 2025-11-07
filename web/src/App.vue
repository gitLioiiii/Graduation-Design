<template>
    <BaseHeader v-if="!HideChrome" />
    <main :class="['base-main', { 'no-component': HideChrome, 'admin-layout': isAdmin, 'user-layout': isUser }]">
    <!-- 管理员：左侧垂直导航 -->
    <AdminNavigator v-if="!HideChrome && isAdmin" />
    <!-- 用户：顶部水平导航 -->
    <UserNavigator v-if="!HideChrome && isUser" />
    <RouterView />
  </main>
  <BaseFooter />
</template>

<script setup>
import { RouterView, useRoute } from 'vue-router'
import { computed } from 'vue'
import { useUserStore } from '@/stores/user'

import BaseHeader from '@/components/BaseHeader.vue'
import AdminNavigator from '@/components/AdminNavigator.vue'
import UserNavigator from '@/components/UserNavigator.vue'
import BaseFooter from '@/components/BaseFooter.vue'

const route = useRoute()
const userStore = useUserStore()

const HideChrome = computed(() => route.meta?.HideChrome === true)
const isAdmin = computed(() => userStore.isAdmin)
const isUser = computed(() => userStore.isUser)
</script>

<style lang="scss">

.el-pagination {
    margin: 2rem 0rem;
}

body {
  margin: 0rem;
}

/* 管理员布局：左侧导航 */
.base-main.admin-layout > .base-article {
  margin: 5rem 0rem 0rem 15rem;
  padding: 2rem;
}

/* 用户布局：顶部导航 */
.base-main.user-layout > .base-article {
  margin: 11rem 0rem 0rem 0rem;
  padding: 2rem;
}

/* 无导航布局 */
.base-main.no-component > .base-article {
  margin: 0rem;
}
</style>
