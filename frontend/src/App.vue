<template>
  <div class="app">
    <el-menu
      class="app-menu"
      mode="horizontal"
      :default-active="activePath"
      :router="true"
      :ellipsis="false"
    >
      <div class="app-menu__brand">英语单词复习</div>
      <el-menu-item index="/words">单词列表</el-menu-item>
      <el-menu-item index="/recite">背单词</el-menu-item>
    </el-menu>

    <main class="app-main">
      <router-view />
    </main>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute } from 'vue-router'

const route = useRoute()
const activePath = computed(() => route.path)
</script>

<style scoped>
.app {
  min-height: 100%;
}

.app-menu {
  padding: 0 24px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
}

.app-menu__brand {
  display: flex;
  align-items: center;
  margin-right: 32px;
  font-size: 18px;
  font-weight: 600;
  color: #409eff;
  white-space: nowrap;
}

.app-main {
  min-height: calc(100vh - 61px);
}
</style>
