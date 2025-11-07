<template>
    <ElTable :data="stocks" style="width: 100%" stripe border :show-header="true">
        <ElTableColumn prop="id" label="库存ID" />
        <ElTableColumn prop="ticketName" label="门票名称" />
        <ElTableColumn prop="ticketType" label="门票类型" />
        <ElTableColumn prop="totalStock" label="总库存" />
        <ElTableColumn prop="dayStock" label="每日库存" />
        <ElTableColumn label="设置" width="200" header-align="center">
            <template #default="{ row }">
                <ElButton
                    type="primary"
                    @click.stop="router.push({ name: 'stock_update', params: { id: row.id } })"
                    >修改</ElButton
                >
                <ElPopconfirm
                    title="确认要移除该库存吗？"
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
    ElTable,
    ElTableColumn,
    ElPopconfirm,
    ElButton,
    ElPagination,
    ElMessage,
} from 'element-plus'
import request from '@/utils/request'

const router = useRouter()

const stocks = ref([])

const pagination = reactive({
    currentPage: 1,
    pageSize: 5,
    total: 0,
})

const fetch = () => {
    let params = new URLSearchParams()
    params.append('page', pagination.currentPage)
    params.append('pageSize', pagination.pageSize)

    request.get('/stock', { params }).then((response) => {
        if (response.data.status === true) {
            stocks.value = response.data.payload.stocks
            Object.assign(pagination, response.data.payload.pagination)
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
    request.post('/stock/remove', row).then((response) => {
        if (response.data.status === true) {
            fetch()
            ElMessage.success('移除成功！')
        } else {
            ElMessage.error('移除失败！')
        }
    })
}
</script>

<style>
.el-table__inner-wrapper {
    color: #4e5969;
}
</style>
