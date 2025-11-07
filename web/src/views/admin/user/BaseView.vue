<template>
  <article class="base-article">
    <div class="base-heading">
      <h1>
        <span style="color:#14C9C9">用户列表</span>
      </h1>
      <div>
        <ElButton
          :type="route.name === 'user_index' ? 'primary' : 'default'"
          native-type="button"
          @click.stop="router.push({ name: 'user_index' })"
          >列表</ElButton
        >
        <ElButton
          :type="route.name === 'user_create' ? 'primary' : 'default'"
          native-type="button"
          @click.stop="router.push({ name: 'user_create' })"
          >新增</ElButton
        >
      </div>
    </div>
    <RouterView />
  </article>
</template>

<script setup>
import { RouterView, useRoute, useRouter } from 'vue-router'
import { ElButton } from 'element-plus'

const route = useRoute()
const router = useRouter()
</script>

<style>
.base-heading {
  display: flex;
  flex-flow: row nowrap;
  align-items: center;
  justify-content: space-between;
}
</style>
