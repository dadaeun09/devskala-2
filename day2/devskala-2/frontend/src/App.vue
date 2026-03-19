<template>
  <main class="container">
    <section class="card">
      <header class="header">
        <h1>Todo</h1>
        <p class="subtitle">오늘 할 일을 간단하게 관리해보세요.</p>
      </header>

      <form class="form" @submit.prevent="addTodo">
        <input v-model="newTitle" placeholder="할 일을 입력하세요" />
        <button type="submit">추가</button>
      </form>

      <div class="summary">
        <span>전체 {{ todos.length }}</span>
        <span>완료 {{ completedCount }}</span>
        <span>남은 일 {{ remainingCount }}</span>
      </div>

      <p v-if="loading" class="state">불러오는 중...</p>
      <p v-else-if="todos.length === 0" class="state empty">등록된 Todo가 없습니다.</p>

      <ul v-else class="list">
        <TodoItem
          v-for="todo in todos"
          :key="todo.id"
          :todo="todo"
          @toggle="toggleTodo"
          @remove="deleteTodo"
        />
      </ul>
    </section>
  </main>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue'
import TodoItem from './components/TodoItem.vue'
import api from './services/api'

const todos = ref([])
const newTitle = ref('')
const loading = ref(false)
const completedCount = computed(() => todos.value.filter((todo) => todo.completed).length)
const remainingCount = computed(() => todos.value.length - completedCount.value)

const fetchTodos = async () => {
  loading.value = true
  try {
    const { data } = await api.get('/api/todos')
    todos.value = data
  } finally {
    loading.value = false
  }
}

const addTodo = async () => {
  const title = newTitle.value.trim()
  if (!title) return

  await api.post('/api/todos', { title })
  newTitle.value = ''
  await fetchTodos()
}

const toggleTodo = async (id) => {
  await api.patch(`/api/todos/${id}/toggle`)
  await fetchTodos()
}

const deleteTodo = async (id) => {
  await api.delete(`/api/todos/${id}`)
  await fetchTodos()
}

onMounted(fetchTodos)
</script>

<style scoped>
.container {
  max-width: 640px;
  margin: 48px auto;
  padding: 20px;
  font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, sans-serif;
}

.card {
  background: #ffffff;
  border: 1px solid #e5e7eb;
  border-radius: 16px;
  padding: 22px;
  box-shadow: 0 10px 20px rgba(15, 23, 42, 0.06);
}

.header h1 {
  margin: 0;
}

.subtitle {
  margin: 6px 0 16px;
  color: #6b7280;
  font-size: 14px;
}

.form {
  display: flex;
  gap: 10px;
  margin-bottom: 16px;
}

input {
  flex: 1;
  border: 1px solid #d1d5db;
  border-radius: 8px;
  padding: 10px 12px;
}

button {
  border: 1px solid #2563eb;
  background: #2563eb;
  color: white;
  border-radius: 8px;
  padding: 10px 14px;
  cursor: pointer;
  transition: background-color 0.15s ease;
}

button:hover {
  background: #1d4ed8;
}

.summary {
  display: flex;
  gap: 10px;
  margin-bottom: 14px;
  color: #4b5563;
  font-size: 13px;
}

.summary span {
  background: #f3f4f6;
  border-radius: 999px;
  padding: 4px 10px;
}

.state {
  color: #6b7280;
  padding: 16px 0;
}

.empty {
  text-align: center;
}

.list {
  margin: 0;
  padding: 0;
  list-style: none;
}
</style>
