import { createRouter, createWebHistory } from 'vue-router'
import WordListView from '../views/WordListView.vue'
import ReciteView from '../views/ReciteView.vue'

const routes = [
  { path: '/', redirect: '/words' },
  { path: '/words', name: 'words', component: WordListView, meta: { title: '单词列表' } },
  { path: '/recite', name: 'recite', component: ReciteView, meta: { title: '背单词' } }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.afterEach((to) => {
  document.title = to.meta?.title ? `${to.meta.title} - 英语单词复习` : '英语单词复习'
})

export default router
