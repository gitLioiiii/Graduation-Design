<template>
    <article class="base-article">
      <div class="base-heading">
        <h1>
          <span style="color:#14C9C9">门票管理</span>
        </h1>
        <div>
          <ElButton
            :type="route.name === 'ticket_index' ? 'primary' : 'default'"
            native-type="button"
            @click.stop="router.push({ name: 'ticket_index' })"
            >列表</ElButton
          >
          <ElButton
            :type="route.name === 'ticket_create' ? 'primary' : 'default'"
            native-type="button"
            @click.stop="router.push({ name: 'ticket_create' })"
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
  