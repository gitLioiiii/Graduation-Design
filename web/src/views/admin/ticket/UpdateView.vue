<template>
  <ElForm
      ref="form"
      :model="model"
      @submit="save"
      :rules="rules"
      label-width="100"
      label-position="left"
      >
      <ElFormItem prop="ticketName" label="活动门票名称">
        <ElInput v-model="model.ticketName" type="text" placeholder="请输入活动门票名称" />
      </ElFormItem>
      <ElFormItem prop="ticketType" label="门票类型">
        <ElSelect v-model="model.ticketType" placeholder="请选择门票类型">
          <ElOption label="成人票" value="成人票" />
          <ElOption label="儿童票" value="儿童票" />
          <ElOption label="团队票" value="团队票" />
          <ElOption label="学生票" value="学生票" />
        </ElSelect>
      </ElFormItem>
      <ElFormItem prop="ticketImage" label="门票图片">
          <FileUploader v-model="model.ticketImage" :limit="1" upload-path="tickets">
                <template #default="{ file }">
                    <img
                        :src="buildTicketURL(file.filename)"
                        :alt="file.filename"
                        :title="file.originalFilename"
                    />
                </template>
                <template #trigger>
                    <strong>Upload</strong>
                </template>
            </FileUploader>
        </ElFormItem>
      <ElFormItem prop="status" label="状态">
        <ElSelect v-model="model.status" placeholder="请选择状态">
          <ElOption label="上架" value="上架" />
          <ElOption label="下架" value="下架" />
          <ElOption label="售罄" value="售空" />
        </ElSelect>
      </ElFormItem>
      <ElFormItem prop="timeType" label="有效期">
        <ElSelect v-model="model.timeType" placeholder="请选择门票有效期">
          <ElOption label="1天" value="1天" />
          <ElOption label="3天" value="3天" />
          <ElOption label="5天" value="5天" />
          <ElOption label="7天" value="7天" />
        </ElSelect>
      </ElFormItem>
      <ElFormItem prop="dayPrice" label="平日价格">
        <ElInputNumber
          v-model="model.dayPrice"
          :precision="2"
          :min="0.00"
          :step="1"
          placeholder="请输入平日价格"
          style="width: 100%"
        />
      </ElFormItem>
      <ElFormItem prop="weekendPrice" label="周末价格">
        <ElInputNumber
          v-model="model.weekendPrice"
          :precision="2"
          :min="0.00"
          :step="1"
          placeholder="请输入周末价格"
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
import { useRouter, useRoute } from 'vue-router'
import { ElForm, ElFormItem, ElInput, ElInputNumber, ElButton, ElMessage, ElSelect, ElOption } from 'element-plus'
import request from '@/utils/request'
import FileUploader from '@/components/FileUploader.vue'
import { buildTicketURL } from '@/utils/helper'

const router = useRouter()
const route = useRoute()

const form = ref(null)

const model = reactive({
    id: null,
    ticketName: '',
    ticketType: '',
    ticketImage: [],
    status: '',
    timeType: '',
    dayPrice: null,
    weekendPrice: null,
})

const rules = reactive({
  ticketName: [
    { required: true, message: '请输入活动门票名称。', trigger: 'blur' },
    { min: 1, max: 64, message: '活动门票名称仅限1~64个字符。', trigger: 'change'},
  ],
  ticketType: [
    { required: true, message: '请选择门票类型。', trigger: 'change' },
  ],
  status: [
    { required: true, message: '请选择状态。', trigger: 'change' },
  ],
  timeType: [
    { required: true, message: '请选择有效期。', trigger: 'change' },
  ],
  dayPrice: [
    { required: true, message: '请输入平日价格。', trigger: 'blur' },
    { type: 'number', min: 0.00, message: '平日价格必须大于0.00。', trigger: 'change'},
  ],
  weekendPrice: [
    { required: true, message: '请输入周末价格。', trigger: 'blur' },
    { type: 'number', min: 0.00, message: '周末价格必须大于0.00。', trigger: 'change'},
  ],
})

const fetchTicket = () => {
    const id = route.params.id
    if (id) {
        request.get(`/ticket/${id}`).then((response) => {
            if (response.data.status === true && response.data.payload.ticket) {
                const ticket = response.data.payload.ticket
                // 将后端返回的字符串转换为 FileUploader 需要的数组格式
                const ticketImage = ticket.ticketImage
                  ? [{ filename: ticket.ticketImage, originalFilename: ticket.ticketImage }]
                  : []

                Object.assign(model, {
                    id: ticket.id,
                    ticketName: ticket.ticketName || '',
                    ticketType: ticket.ticketType || '',
                    ticketImage: ticketImage,
                    status: ticket.status || '',
                    timeType: ticket.timeType || '',
                    dayPrice: ticket.dayPrice || null,
                    weekendPrice: ticket.weekendPrice || null,
                })
            } else {
                ElMessage.error('获取门票信息失败！')
                router.push({ name: 'ticket_index' })
            }
        }).catch(() => {
            ElMessage.error('获取门票信息失败！')
            router.push({ name: 'ticket_index' })
        })
    } else {
        ElMessage.error('门票ID不存在！')
        router.push({ name: 'ticket_index' })
    }
}

const save = (e) => {
  e.preventDefault()
  form.value
    .validate()
    .then((result) => {
        if (result === true) {
          // 处理 ticketImage：从数组转换为字符串
          const requestData = {
            ...model,
            ticketImage: model.ticketImage.length > 0 ? model.ticketImage[0].filename : null
          }

          request.post('/ticket/update', requestData).then((response) => {
              if (response.data.status === true) {
                ElMessage.success('更新成功！')
                router.push({ name:'ticket_index' })
              } else {
                ElMessage.error('更新失败!')
              }
          })
          .catch((error) => {
              console.error("创建门票失败",error)
              ElMessage.error("保存失败！")
          })
        }
    })
    .catch(() => {})
}

onMounted(() => {
    fetchTicket()
})
</script>

<style scoped>
:deep(.el-form-item) {
  margin-bottom: 22px;
}
:deep(.el-form-item__label):before {
  display: none !important;
}
</style>
