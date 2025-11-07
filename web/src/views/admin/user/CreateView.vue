<template>
    <ElForm ref="form" :model="model" :rules="rules" @submit.prevent="save" label-width="60">
        <ElFormItem prop="avatar" label="头像">
          <FileUploader v-model="model.avatar" :limit="1" upload-path="avatars">
                <template #default="{ file }">
                    <img
                        :src="buildAvatarURL(file.filename)"
                        :alt="file.filename"
                        :title="file.originalFilename"
                    />
                </template>
                <template #trigger>
                    <strong>Upload</strong>
                </template>
            </FileUploader>
        </ElFormItem>
        <ElFormItem prop="username" label="账号" style="width:16rem">
            <ElInput v-model="model.username" placeholder="账号" />
        </ElFormItem>
        <ElFormItem prop="name" label="姓名" style="width:16rem">
            <ElInput v-model="model.name" placeholder="姓名" />
        </ElFormItem>
        <ElFormItem prop="password" label="密码" style="width:16rem">
            <ElInput v-model="model.password" type="password" placeholder="密码" />
        </ElFormItem>
        <ElFormItem>
            <ElButton native-type="submit" type="primary">保存</ElButton>
        </ElFormItem>
    </ElForm>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElForm,
         ElFormItem,
         ElInput,
         ElButton,
         ElMessage,
        } from 'element-plus'

import FileUploader from '@/components/FileUploader.vue'
import request from '@/utils/request'
import { buildAvatarURL } from '@/utils/helper'

const router = useRouter()

const form = ref(null)

const model = reactive({
    avatar: [],
    username: '',
    name: '',
    password: '',
})

const rules = reactive({
    username: [
        { required: true, message: '请输入账号。', trigger: 'blur' },
        { min: 2, max: 16, message: '账号仅限1~16个字符。', trigger: 'change' },
    ],
    password: [
        { required: true, message: '请输入密码。', trigger: 'blur' },
        { min: 6, max: 16, message: '账号仅限6~16个字符。', trigger: 'change' },
    ],
})

const save = () => {
    form.value
        .validate()
        .then((result) => {
            if (result === true) {
                // 处理头像数据
                const userData = {
                    username: model.username,
                    name: model.name,
                    password: model.password,
                    avatar: model.avatar.length > 0 ? model.avatar[0].filename : null
                }

                request
                    .post('/user/create', userData)
                    .then((response) => {
                        if (response.data.status === true) {
                            ElMessage.success('保存成功！')
                            router.push({ name: 'user_index' })
                        } else {
                            ElMessage.error(response.data.message || '保存失败！')
                        }
                    })
                    .catch((error) => {
                        console.error('创建用户失败:', error)
                        ElMessage.error('保存失败！')
                    })
            }
        })
        .catch(() => {})
}
</script>
