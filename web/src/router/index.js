import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '@/stores/user'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'index',
      component: () => import('@/views/IndexView.vue'),
    },

    // ========== 管理员路由 ==========
    {
      path: '/admin/ticket',
      name: 'ticket_base',
      component: () => import('@/views/admin/ticket/BaseView.vue'),
      meta: { requiresAdmin: true },
      children: [
        {
          path: '',
          name: 'ticket_index',
          component: () => import('@/views/admin/ticket/IndexView.vue'),
        },
        {
          path: 'create',
          name: 'ticket_create',
          component: () => import('@/views/admin/ticket/CreateView.vue'),
        },
        {
          path: 'update/:id',
          name: 'ticket_update',
          component: () => import('@/views/admin/ticket/UpdateView.vue'),
        },
      ],
    },
    {
      path: '/admin/stock',
      name: 'stock_base',
      component: () => import('@/views/admin/stock/BaseView.vue'),
      meta: { requiresAdmin: true },
      children: [
        {
          path: '',
          name: 'stock_index',
          component: () => import('@/views/admin/stock/IndexView.vue'),
        },
        {
          path: 'create',
          name: 'stock_create',
          component: () => import('@/views/admin/stock/CreateView.vue'),
        },
        {
          path: 'update/:id',
          name: 'stock_update',
          component: () => import('@/views/admin/stock/UpdateView.vue'),
        },
      ],
    },
    {
      path: '/admin/user',
      name: 'user_base',
      component: () => import('@/views/admin/user/BaseView.vue'),
      meta: { requiresAdmin: true },
      children: [
        {
          path: '',
          name: 'user_index',
          component: () => import('@/views/admin/user/IndexView.vue'),
        },
        {
          path: 'create',
          name: 'user_create',
          component: () => import('@/views/admin/user/CreateView.vue'),
        },
      ],
    },

    // ========== 用户路由 ==========
    {
      path: '/user/ticket',
      name: 'user_ticket_base',
      component: () => import('@/views/user/ticket/BaseView.vue'),
      meta: { requiresUser: true },
      children: [
        {
          path: '',
          name: 'user_ticket_index',
          component: () => import('@/views/user/ticket/IndexView.vue'),
        },
      ],
    },

    // ========== 公共路由 ==========
    {
      path: '/login',
      name: 'login',
      component: () => import('@/views/LoginView.vue'),
      meta: { HideChrome: true },
    },
    {
      path: '/register',
      name: 'register',
      component: () => import('@/views/RegisterView.vue'),
      meta: { HideChrome: true },
    },

  ],
})

// 路由守卫
router.beforeEach((to) => {
    const userStore = useUserStore()

    // 检查是否需要登录
    if (to.name !== 'login' && to.name !== 'register' && !userStore.logged) {
        return { name: 'login' }
    }

    // 如果已登录但token过期，自动退出
    if (userStore.logged && userStore.user?.token?.expireAt) {
        const now = new Date()
        const expireAt = new Date(userStore.user.token.expireAt)
        if (now > expireAt) {
            userStore.logout()
            return { name: 'login' }
        }
    }

    // 检查管理员权限
    if (to.meta.requiresAdmin && !userStore.isAdmin) {
        console.warn('需要管理员权限')
        return { name: 'index' }
    }

    // 检查用户权限
    if (to.meta.requiresUser && !userStore.isUser) {
        console.warn('需要用户权限')
        return { name: 'index' }
    }
})

export default router
