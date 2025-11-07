<template>
  <ElTable :data="users" style="width: 80%" stripe border :show-header="true">
      <ElTableColumn prop="id" label="#" />
      <ElTableColumn prop="avatar" label="头像">
        <template #default="{ row }">
          <!-- <img v-if="row.avatar" :src="buildURL(row.avatar)" :alt="row.username" :title="row.username"
           style="width: 50px; height: 50px; object-fit: cover;" />
          <span v-else>无头像</span> -->
          <ElImage
            v-if="row.avatar"
            style="width: 50px; height: 50px"
            :src="buildURL(row.avatar)"
            :preview-src-list="srcList"
            fit="cover"
            show-progress
          />
          <span v-else>
            <ElImage
              style="width: 50px; height: 50px"
              :src="noImage"
              fit="cover"
            />
          </span>
        </template>
      </ElTableColumn>
      <ElTableColumn prop="username" label="账号"/>
      <ElTableColumn prop="name" label="姓名"/>
      <ElTableColumn prop="password" label="密码">
        <!-- <template #default>
          <span>未对密码序列化</span> -->
        <template #default="{ row }">
          <span v-if="row.password">{{ row.password }}</span>
          <span v-else style="color: #999;">设置为不可读取</span>
        </template>
      </ElTableColumn>
      <ElTableColumn label="设置" width="200" header-align="center">
        <template #default = "{ row }">
          <ElButton
            type="primary"
            @click="openEdit(row)"
            style="margin-left: 1rem;"
            >修改</ElButton
          >
          <ElPopconfirm
            title="确认移除该角色吗？"
            confirm-button-text="确定"
            cancel-button-text="取消"
            @confirm="remove(row)"
          >
            <template #reference>
              <ElButton type="danger">移除</ElButton>
            </template>
          </ElPopconfirm>
        </template>
      </ElTableColumn>
    </ElTable>

    <ElPagination
        layout="prev, pager, next, jumper, sizes, ->, total"
        :page-sizes="[1, 5, 10, 20, 50, 100]"
        v-model:current-page="pagination.currentPage"
        v-model:page-size="pagination.pageSize"
        :total="pagination.total"
        background
    />

<!-- z-index放高点覆盖元素 -->
    <ElDrawer
      v-model="drawer"
      title="编辑用户"
      :with-header="true"
      :teleported="true"
      :z-index="3000"
      direction="rtl"
      size="30%"
    >
      <ElForm ref="form" :model="model" :rules="rules" label-width="80">
        <ElFormItem label="账号">
          <ElInput v-model="model.username" disabled />
        </ElFormItem>
        <ElFormItem label="姓名">
          <ElInput v-model="model.name" placeholder="修改用户姓名" />
        </ElFormItem>
        <ElFormItem label="密码">
          <ElInput v-model="model.password" type="password" placeholder="修改用户密码" />
        </ElFormItem>
        <ElFormItem label="头像">
          <FileUploader v-model="model.avatarFiles" :limit="1" upload-path="avatars">
            <template #default="{ file }">
              <ElImage :src="buildAvatarURL(file.filename)" style="width: 80px; height: 80px" fit="cover" />
            </template>
            <template #trigger>
              <ElButton type="primary" size="small">上传头像</ElButton>
            </template>
          </FileUploader>
        </ElFormItem>
      </ElForm>
      <template #footer>
        <ElButton type="default" @click.stop="closeDrawer">取消</ElButton>
        <ElButton type="primary" @click.stop="save">保存</ElButton>
      </template>
    </ElDrawer>
</template>

<script setup>
import { ref, reactive, watch } from 'vue'
import {
  ElTable,
  ElTableColumn,
  ElPopconfirm,
  ElButton,
  ElMessage,
  ElImage,
  ElDrawer,
  ElForm,
  ElFormItem,
  ElPagination,
  ElInput
} from 'element-plus'
import request from '@/utils/request'
import { buildURL, buildAvatarURL } from '@/utils/helper'
import noImage from '@/assets/img/图片未上传.png'
import FileUploader from '@/components/FileUploader.vue'


const users = ref([])
const srcList = ref([])

const pagination = reactive({
    currentPage: 1,
    pageSize: 5,
    total: 0,
})

const fetch = () => {
    let params = new URLSearchParams()
    params.append('page', pagination.currentPage)
    params.append('pageSize', pagination.pageSize)

    request.get('/user', { params }).then((response) => {
        if (response.data.status === true) {
            users.value = response.data.payload.users
            Object.assign(pagination, response.data.payload.pagination)
            // 更新头像预览列表
            srcList.value = users.value
                .filter(user => user.avatar)
                .map(user => buildURL(user.avatar))
        }
    })
}

watch(
    () => [pagination.currentPage, pagination.pageSize],
    () => {
        fetch()
    },
    { immediate: true },
)

const remove = (row) => {
    request.post('/user/remove', row).then((response) => {
        if (response.data.status === true) {
            fetch()
            ElMessage.success('移除成功！')
        } else {
            ElMessage.error('移除失败！')
        }
    })
}

// 抽屉
const drawer = ref(false)
const form = ref(null)
const model = reactive({
  id: null,
  username: '',
  password: '',
  name: null,
  avatar: null,
  avatarFiles: [],
})

const rules = reactive({
  name: [
    { max: 32, message: '姓名最多32个字符。', trigger: 'change' },
  ],
  password: [
    { min: 6, max: 16, message: '密码仅限6~16个字符。', trigger: 'change' },
  ],
})

const openEdit = (row) => {
  model.id = row.id
  model.username = row.username
  model.password = ''
  model.name = row.name || null
  model.avatar = row.avatar || null
  model.avatarFiles = row.avatar ? [{ filename: row.avatar }] : []
  drawer.value = true
}

const closeDrawer = () => {
  drawer.value = false
}

const save = () => {
    form.value
        .validate()
        .then((result) => {
            if (result === true) {
                // 构建更新数据，确保头像字段正确映射
                const updateData = {
                    id: model.id,
                    name: model.name,
                    password: model.password,
                    avatar: model.avatarFiles.length > 0 ? model.avatarFiles[0].filename : model.avatar
                }

                request
                    .post('/user/update', updateData)
                    .then((response) => {
                        if (response.data.status === true) {
                            ElMessage.success('保存成功！')
                            closeDrawer()
                            fetch() // 刷新列表
                        } else {
                            ElMessage.error('保存失败！')
                        }
                    })
                    .catch(() => {
                        ElMessage.error('保存失败！')
                    })
            }
        })
        .catch(() => {})
}

</script>
