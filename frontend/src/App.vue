<script setup lang="ts">
import AppHeader from './components/AppHeader.vue';
</script>

<template>
  <div class="min-h-screen flex flex-col bg-white dark:bg-surface-950 text-surface-900 dark:text-surface-0">
    <AppHeader />
    <main class="flex-1 flex flex-col">
      <RouterView />
    </main>
  </div>
</template>

<style scoped></style>
