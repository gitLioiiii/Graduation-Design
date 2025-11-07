<template>
    <ElForm :model="filterModel" :rules="filterRules" @submit.prevent="fetch" inline>
        <ElFormItem prop="ticketName" label="活动门票名称">
            <ElInput v-model="filterModel.ticketName" placeholder="关键字 活动门票名称 " clearable />
        </ElFormItem>
        <ElFormItem prop="ticketType" label="门票类型">
            <ElInput v-model="filterModel.ticketType" placeholder="关键字 门类类型 " clearable />
        </ElFormItem>
        <ElFormItem>
            <ElButton native-type="submit" type="primary">筛选</ElButton>
        </ElFormItem>
    </ElForm>
    <ElTable :data="tickets" style="width: 100%" stripe border :show-header="true">
        <ElTableColumn prop="id" label="#" />
        <ElTableColumn prop="ticketName" label="活动门票名称" />
        <ElTableColumn prop="ticketType" label="门票类型" />
        <ElTableColumn prop="ticketImage" label="门票图片">
        <template #default="{ row }">
          <ElImage
            v-if="row.ticketImage"
            style="width: 50px; height: 50px"
            :src="buildTicketURL(row.ticketImage)"
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
        <ElTableColumn prop="status" label="状态" />
        <ElTableColumn prop="timeType" label="有效期" />
        <ElTableColumn prop="dayPrice" label="平日价格" />
        <ElTableColumn prop="weekendPrice" label="周末价格" />
        <ElTableColumn label="设置" width="200" header-align="center">
            <template #default="{ row }">
                <ElButton
                    type="primary"
                    @click.stop="router.push({ name: 'ticket_update', params: { id: row.id } })"
                    >修改</ElButton
                >
                <ElPopconfirm
                    title="确认要移除该门票吗？"
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
</template>

<script setup>
import { reactive, ref, watch } from 'vue'
import { useRouter } from 'vue-router'
import {
    ElForm,
    ElFormItem,
    ElInput,
    ElTable,
    ElTableColumn,
    ElPopconfirm,
    ElButton,
    ElPagination,
    ElMessage,
    ElImage,
} from 'element-plus'
import request from '@/utils/request'
import { buildTicketURL } from '@/utils/helper'
import noImage from '@/assets/img/图片未上传.png'

const router = useRouter()

const tickets = ref([])
const srcList = ref([])

const filterModel = reactive({
    ticketName: '',
    ticketType: '',
})

const filterRules = reactive({
    ticketName: [{ min: 1, max: 16, message: '关键字仅限1~16个字符。', trigger: 'change' }],
    ticketType: [{ min: 1, max: 16, message: '关键字仅限1~16个字符。', trigger: 'change' }],
})

const pagination = reactive({
    currentPage: 1,
    pageSize: 5,
    total: 0,
})

const fetch = () => {
    let params = new URLSearchParams()
    params.append('page', pagination.currentPage)
    params.append('pageSize', pagination.pageSize)

    if (filterModel.ticketName.length > 0) {
        params.append('ticketName', filterModel.ticketName)
    }
    if (filterModel.ticketType.length > 0) {
        params.append('ticketType', filterModel.ticketType)
    }

    request.get('/ticket', { params }).then((response) => {
        if (response.data.status === true) {
            tickets.value = response.data.payload.tickets
            Object.assign(pagination, response.data.payload.pagination)
            // 更新门票图片预览列表
            srcList.value = tickets.value
                .filter(ticket => ticket.ticketImage)
                .map(ticket => buildTicketURL(ticket.ticketImage))
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
    request.post('/ticket/remove', row).then((response) => {
        if (response.data.status === true) {
            fetch()
            ElMessage.success('移除成功！')
        } else {
            ElMessage.error('移除失败！')
        }
    })
}
</script>
