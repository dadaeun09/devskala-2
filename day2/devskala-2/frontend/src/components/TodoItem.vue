<template>
  <li class="todo-item">
    <label class="checkbox-wrap">
      <input :checked="todo.completed" type="checkbox" @change="$emit('toggle', todo.id)" />
      <span :class="{ done: todo.completed }">{{ todo.title }}</span>
    </label>
    <button class="delete" @click="$emit('remove', todo.id)">삭제</button>
  </li>
</template>

<script setup>
defineProps({
  todo: {
    type: Object,
    required: true
  }
})
</script>

<style scoped>
.todo-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 12px;
  padding: 12px 0;
  border-bottom: 1px solid #e5e7eb;
}

.checkbox-wrap {
  display: flex;
  align-items: center;
  gap: 10px;
}

.done {
  text-decoration: line-through;
  color: #9ca3af;
}

.delete {
  border: 1px solid #e5e7eb;
  background: #fff;
  border-radius: 8px;
  padding: 6px 10px;
  cursor: pointer;
  transition: all 0.15s ease;
}

.delete:hover {
  border-color: #fca5a5;
  color: #b91c1c;
  background: #fef2f2;
}
</style>
