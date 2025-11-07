<template>
  <nav class="base-navigator">
    <!-- <ElMenu :router="true" :style="{ height: '100%' }"> -->
    <ElMenu :router="true" :class="['base-navigator-menu']" >
      <ElMenuItem index="index" :route="{ name: 'index' }">
        <img src="@/assets/icons/卡通海豚-copy.png" alt="介绍" style="width: 16px; height: 16px; margin-right: 8px;" />
        海洋乐园介绍
      </ElMenuItem>
    </ElMenu>
  </nav>
</template>

<script setup>
import { ElMenu, ElMenuItem } from 'element-plus'
</script>

<style>
.base-navigator-menu {
  height: 100%;
}

/* .base-navigator {
  flex: 0 0 15rem;

  position: relative;
}

.base-navigator-menu {
  height: 100%;

  position: absolute;
  top: 0rem;
  left: 0rem;

  width: 100%;
} */
</style>
