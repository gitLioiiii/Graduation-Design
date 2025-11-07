<template>
  <nav class="user-navigator">
    <ElMenu :router="true" mode="horizontal" class="user-navigator-menu">
      <ElMenuItem index="index" :route="{ name: 'index' }">
        <img src="@/assets/icons/卡通海豚-copy.png" alt="介绍" style="width: 16px; height: 16px; margin-right: 8px;" />
        海洋乐园介绍
      </ElMenuItem>

      <ElMenuItem index="user_ticket_index" :route="{ name: 'user_ticket_index' }">
        门票预约
      </ElMenuItem>

      <ElMenuItem index="user_activity_index" :route="{ name: 'user_activity_index' }">
        热门活动
      </ElMenuItem>

      <ElMenuItem index="user_marine_index" :route="{ name: 'user_marine_index' }">
        海洋生物
      </ElMenuItem>
    </ElMenu>
  </nav>
</template>

<script setup>
import { ElMenu, ElMenuItem } from 'element-plus'
</script>

<style scoped>
.user-navigator {
  position: fixed;
  top: 5rem;
  left: 0rem;
  width: 100%;
  background: #fff;
  border-bottom: 1px solid #e6e6e6;
  z-index: 999;
}

.user-navigator-menu {
  width: 100%;
  border-bottom: none;
  display: flex;
  justify-content: flex-start;
  padding: 0 2rem;
}

/* Element Plus 水平菜单样式调整 */
.user-navigator-menu.el-menu--horizontal {
  border-bottom: none;
}
</style>
