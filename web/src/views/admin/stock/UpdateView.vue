<template>
    <ElForm
        ref="form"
        :model="model"
        @submit="save"
        :rules="rules"
        label-width="100"
        label-position="left"
    >
        <ElFormItem prop="ticketId" label="门票名称">
            <ElSelect v-model="model.ticketId" placeholder="请选择门票" filterable>
                <ElOption
                    v-for="ticket in tickets"
                    :key="ticket.id"
                    :label="ticket.ticketName"
                    :value="ticket.id"
                />
            </ElSelect>
        </ElFormItem>
        <ElFormItem prop="totalStock" label="总库存">
            <ElInputNumber
                v-model="model.totalStock"
                :min="0"
                :step="1"
                placeholder="请输入总库存"
                style="width: 100%"
            />
        </ElFormItem>
        <ElFormItem prop="dayStock" label="每日库存">
            <ElInputNumber
                v-model="model.dayStock"
                :min="0"
                :step="1"
                placeholder="请输入每日库存（可选）"
                style="width: 100%"
            />
        </ElFormItem>
        <ElFormItem label="">
            <ElButton type="primary" native-type="submit">保存</ElButton>
        </ElFormItem>
    </ElForm>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElForm, ElFormItem, ElInputNumber, ElButton, ElMessage, ElSelect, ElOption } from 'element-plus'
import request from '@/utils/request'

const route = useRoute()
const router = useRouter()

const form = ref(null)
const tickets = ref([])

const model = reactive({
    id: null,
    ticketId: null,
    totalStock: null,
    dayStock: null,
})

const rules = reactive({
    ticketId: [
        { required: true, message: '请选择关联的门票。', trigger: 'change' },
    ],
    totalStock: [
        { required: true, message: '请输入总库存。', trigger: 'blur' },
        { type: 'number', min: 0, message: '总库存必须大于等于0。', trigger: 'change'},
    ],
    dayStock: [
        { type: 'number', min: 0, message: '每日库存必须大于等于0。', trigger: 'change'},
    ],
})

// 获取门票列表
const fetchTickets = () => {
    request.get('/ticket', { params: { page: 1, pageSize: 1000 } }).then((response) => {
        if (response.data.status === true) {
            tickets.value = response.data.payload.tickets || []
        }
    })
}

// 获取库存详情
const fetch = (id) => {
    request.get(`/stock/${id}`).then((response) => {
        if (response.data.status === true) {
            const stock = response.data.payload.stock
            model.id = stock.id
            model.ticketId = stock.ticketId
            model.totalStock = stock.totalStock
            model.dayStock = stock.dayStock
        }
    })
}

onMounted(() => {
    fetchTickets()
    fetch(route.params.id)
})

const save = (e) => {
    e.preventDefault()
    form.value
        .validate()
        .then((result) => {
            if (result === true) {
                request.post('/stock/update', { ...model }).then((response) => {
                    if (response.data.status === true) {
                        ElMessage.success('保存成功！')
                        router.push({ name: 'stock_index' })
                    } else {
                        ElMessage.error('保存失败！')
                    }
                })
            }
        })
        .catch(() => {
            ElMessage.error('保存失败！')
        })
}
</script>

<style scoped>
:deep(.el-form-item) {
    margin-bottom: 22px;
}
:deep(.el-form-item__label):before {
    display: none !important;
}
</style>
