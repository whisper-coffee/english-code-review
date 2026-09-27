<template>
  <div class="page-container">
    <el-card v-loading="loading" shadow="never" class="quiz-card">
      <template #header>
        <div class="quiz-header">
          <span>随机背单词</span>
          <span class="quiz-header__stat">
            本轮：{{ stats.total }} 题 / 答对 {{ stats.correct }} 题
          </span>
        </div>
      </template>

      <div v-if="errorMessage" class="quiz-empty">
        <el-empty :description="errorMessage">
          <el-button type="primary" @click="loadQuiz">重试</el-button>
        </el-empty>
      </div>

      <div v-else-if="quiz" class="quiz-body">
        <div class="quiz-word">
          <span class="quiz-word__text">{{ quiz.wordText }}</span>
          <el-tag v-if="quiz.partOfSpeech" type="info" size="large">
            {{ quiz.partOfSpeech }}
          </el-tag>
        </div>

        <div class="quiz-options">
          <el-button
            v-for="option in quiz.options"
            :key="option.key"
            class="quiz-option"
            :class="optionClass(option)"
            :disabled="answered"
            @click="handleSelect(option)"
          >
            <span class="quiz-option__key">{{ option.key }}.</span>
            <span>{{ option.translation }}</span>
          </el-button>
        </div>

        <div v-if="result" class="quiz-result">
          <el-alert
            :title="result.correct ? '回答正确！' : '回答错误'"
            :type="result.correct ? 'success' : 'error'"
            :description="`正确答案：${result.correctTranslation}`"
            show-icon
            :closable="false"
          />
          <div class="quiz-result__meta">
            该单词累计提问 {{ result.word?.askCount ?? 0 }} 次，答对
            {{ result.word?.correctCount ?? 0 }} 次
          </div>
          <el-button type="primary" @click="loadQuiz">下一个单词</el-button>
        </div>
      </div>

      <div v-else class="quiz-empty">
        <el-empty description="点击按钮开始背单词">
          <el-button type="primary" @click="loadQuiz">开始</el-button>
        </el-empty>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { onMounted, reactive, ref } from 'vue'
import { quizApi } from '../api'

const quiz = ref(null)
const result = ref(null)
const loading = ref(false)
const answered = ref(false)
const errorMessage = ref('')

const stats = reactive({ total: 0, correct: 0 })

const loadQuiz = async () => {
  loading.value = true
  errorMessage.value = ''
  quiz.value = null
  result.value = null
  answered.value = false
  try {
    quiz.value = await quizApi.next()
  } catch (e) {
    errorMessage.value = e.message || '出题失败，请稍后重试'
  } finally {
    loading.value = false
  }
}

const handleSelect = async (option) => {
  if (answered.value || !quiz.value) return
  answered.value = true
  try {
    const data = await quizApi.answer({
      wordId: quiz.value.wordId,
      selectedTranslation: option.translation
    })
    result.value = data
    stats.total += 1
    if (data.correct) stats.correct += 1
  } catch (e) {
    // 提交失败时允许重新选择，避免卡死在已作答状态
    answered.value = false
    errorMessage.value = e.message || '提交答案失败，请重试'
  }
}

const optionClass = (option) => {
  if (!result.value) return ''
  if (option.translation === result.value.correctTranslation) return 'is-correct'
  if (option.translation === result.value.selectedTranslation) return 'is-wrong'
  return ''
}

onMounted(loadQuiz)
</script>

<style scoped>
.quiz-card {
  max-width: 720px;
  margin: 0 auto;
}

.quiz-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.quiz-header__stat {
  font-size: 13px;
  color: #909399;
}

.quiz-body {
  padding: 8px 0;
}

.quiz-word {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 12px;
  margin-bottom: 28px;
}

.quiz-word__text {
  font-size: 40px;
  font-weight: 600;
  letter-spacing: 1px;
  color: #303133;
}

.quiz-options {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 12px;
}

.quiz-option {
  display: flex;
  justify-content: flex-start;
  height: 52px;
  font-size: 15px;
}

.quiz-option__key {
  display: inline-block;
  width: 28px;
  text-align: left;
  font-weight: 600;
}

.quiz-option.is-correct {
  background-color: #f0f9eb;
  border-color: #67c23a;
  color: #67c23a;
}

.quiz-option.is-wrong {
  background-color: #fef0f0;
  border-color: #f56c6c;
  color: #f56c6c;
}

.quiz-result {
  margin-top: 24px;
  display: flex;
  flex-direction: column;
  gap: 12px;
  align-items: flex-start;
}

.quiz-result__meta {
  font-size: 13px;
  color: #909399;
}

.quiz-empty {
  padding: 24px 0;
}
</style>
