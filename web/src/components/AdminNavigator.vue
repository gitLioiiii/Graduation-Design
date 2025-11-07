<template>
  <nav class="admin-navigator">
    <ElMenu :router="true" mode="vertical" class="admin-navigator-menu">
      <ElMenuItem index="index" :route="{ name: 'index' }">
        <img src="@/assets/icons/卡通海豚-copy.png" alt="介绍" style="width: 16px; height: 16px; margin-right: 8px;" />
        海洋乐园介绍
      </ElMenuItem>

      <ElSubMenu index="ticket_Mgr">
        <template #title>门票管理</template>
        <ElMenuItem index="ticket_Mgr_index" :route="{ name: 'ticket_index' }">
          门票列表
        </ElMenuItem>
        <ElMenuItem index="stock_Mgr_index" :route="{ name: 'stock_index' }">
          门票库存
        </ElMenuItem>
      </ElSubMenu>

      <ElSubMenu index="user_Mgr">
        <template #title>用户管理</template>
        <ElMenuItem index="user_Mgr_index" :route="{ name: 'user_index' }">
          用户列表
        </ElMenuItem>
      </ElSubMenu>
    </ElMenu>
  </nav>
</template>

<script setup>
import { ElMenu, ElMenuItem, ElSubMenu } from 'element-plus'
</script>

<style scoped>
.admin-navigator {
  position: fixed;
  top: 5rem;
  left: 0rem;
  height: calc(100vh - 5rem);
  width: 15rem;
  background: #fff;
  border-right: 1px solid #e6e6e6;
  overflow-y: auto;
}

.admin-navigator-menu {
  height: 100%;
  border-right: none;
}
</style>
