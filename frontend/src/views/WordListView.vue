<template>
  <div class="page-container">
    <el-card shadow="never">
      <template #header>
        <div class="toolbar">
          <div class="toolbar__left">
            <el-input
              v-model="keyword"
              placeholder="搜索单词原文或译文"
              clearable
              style="width: 240px"
              @keyup.enter="handleSearch"
              @clear="handleSearch"
            />
            <el-button type="primary" @click="handleSearch">查询</el-button>
          </div>
          <el-button type="success" @click="openAddDialog">+ 增加单词</el-button>
        </div>
      </template>

      <el-table v-loading="loading" :data="records" border stripe>
        <el-table-column prop="id" label="ID" width="70" />
        <el-table-column prop="wordText" label="单词" min-width="140" />
        <el-table-column prop="partOfSpeech" label="词性" width="100">
          <template #default="{ row }">
            <el-tag v-if="row.partOfSpeech" type="info">{{ row.partOfSpeech }}</el-tag>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column prop="translation" label="译文" min-width="160" />
        <el-table-column prop="askCount" label="提问次数" width="100" align="center" />
        <el-table-column prop="correctCount" label="正确次数" width="100" align="center" />
        <el-table-column label="正确率" width="110" align="center">
          <template #default="{ row }">{{ formatAccuracy(row) }}</template>
        </el-table-column>
        <el-table-column label="操作" width="90" align="center">
          <template #default="{ row }">
            <el-button link type="danger" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
        <template #empty>
          <span>暂无单词，点击右上角「增加单词」开始录入</span>
        </template>
      </el-table>

      <div class="pagination">
        <el-pagination
          v-model:current-page="current"
          v-model:page-size="size"
          :page-sizes="[10, 20, 50]"
          :total="total"
          layout="total, sizes, prev, pager, next, jumper"
          background
          @current-change="fetchList"
          @size-change="handleSizeChange"
        />
      </div>
    </el-card>

    <el-dialog v-model="dialogVisible" title="增加单词" width="440px" @closed="resetForm">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="单词原文" prop="wordText">
          <el-input v-model="form.wordText" placeholder="如 apple" maxlength="128" />
        </el-form-item>
        <el-form-item label="词性" prop="partOfSpeech">
          <el-select v-model="form.partOfSpeech" placeholder="请选择词性" style="width: 100%">
            <el-option v-for="item in partOfSpeechOptions" :key="item" :label="item" :value="item" />
          </el-select>
        </el-form-item>
        <el-form-item label="译文" prop="translation">
          <el-input v-model="form.translation" placeholder="如 苹果" maxlength="255" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitting" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { wordApi } from '../api'

const partOfSpeechOptions = ['n.', 'v.', 'adj.', 'adv.', 'prep.', 'conj.', 'pron.', 'num.', '其他']

const records = ref([])
const total = ref(0)
const current = ref(1)
const size = ref(10)
const keyword = ref('')
const loading = ref(false)

const dialogVisible = ref(false)
const submitting = ref(false)
const formRef = ref()
const form = reactive({
  wordText: '',
  partOfSpeech: 'n.',
  translation: ''
})

const rules = {
  wordText: [{ required: true, message: '请输入单词原文', trigger: 'blur' }],
  partOfSpeech: [{ required: true, message: '请选择词性', trigger: 'change' }],
  translation: [{ required: true, message: '请输入译文', trigger: 'blur' }]
}

const fetchList = async () => {
  loading.value = true
  try {
    const data = await wordApi.page({ current: current.value, size: size.value, keyword: keyword.value })
    records.value = data.records || []
    total.value = data.total || 0
  } catch (e) {
    records.value = []
    total.value = 0
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  current.value = 1
  fetchList()
}

const handleSizeChange = () => {
  current.value = 1
  fetchList()
}

const openAddDialog = () => {
  dialogVisible.value = true
}

const resetForm = () => {
  form.wordText = ''
  form.partOfSpeech = 'n.'
  form.translation = ''
  formRef.value?.clearValidate()
}

const handleSubmit = async () => {
  if (!formRef.value) return
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return

  submitting.value = true
  try {
    await wordApi.add({
      wordText: form.wordText.trim(),
      partOfSpeech: form.partOfSpeech,
      translation: form.translation.trim()
    })
    ElMessage.success('添加成功')
    dialogVisible.value = false
    // 新增后回到第一页，保证能看到刚添加的记录
    current.value = 1
    fetchList()
  } finally {
    submitting.value = false
  }
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm(`确定删除单词「${row.wordText}」吗？`, '提示', {
      type: 'warning',
      confirmButtonText: '删除',
      cancelButtonText: '取消'
    })
  } catch {
    return
  }
  await wordApi.remove(row.id)
  ElMessage.success('删除成功')
  fetchList()
}

const formatAccuracy = (row) => {
  if (!row.askCount) return '--'
  return `${Math.round((row.correctCount / row.askCount) * 100)}%`
}

onMounted(fetchList)
</script>

<style scoped>
.toolbar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
}

.toolbar__left {
  display: flex;
  align-items: center;
  gap: 8px;
}

.pagination {
  display: flex;
  justify-content: flex-end;
  margin-top: 16px;
}
</style>
