import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import vueDevTools from 'vite-plugin-vue-devtools'

import ElementPlus from 'unplugin-element-plus/vite'

// https://vite.dev/config/
export default defineConfig({
  css: {
    preprocessorOptions: {
      scss: {
        additionalData: `@use "@/styles/element/rootConfig" as *;`,
      },
    },
  },
  plugins: [
    vue(),
    vueDevTools(),
    [ElementPlus()],
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    },
  },
  // 使用.env文件需要加下面这个，才能使用APP_BASE_URL=http://localhost:8080
  // 否则使用VITE_APP_BASE_URL=http://localhost:8080来暴露
  envPrefix: 'APP_',
})
