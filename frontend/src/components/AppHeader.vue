<script setup lang="ts">
import { ref } from 'vue';
import Button from 'primevue/button';

const checkDarkMode = ref(document.documentElement.classList.contains('p-dark'));

function toggleDarkMode() {
  document.documentElement.classList.toggle('p-dark');

  checkDarkMode.value = document.documentElement.classList.contains('p-dark');
}
</script>

<template>
  <header class="flex items-center justify-between px-6 h-14 border-b border-surface-200 dark:border-surface-800 bg-white dark:bg-surface-950">
    <RouterLink to="/" class="text-lg font-semibold no-underline text-surface-900 dark:text-surface-0">
      Programming Source
    </RouterLink>

    <nav class="flex items-center gap-2">
      <Button
        severity="secondary"
        variant="text"
        :icon="checkDarkMode ? 'pi pi-sun' : 'pi pi-moon'"
        @click="toggleDarkMode"
      />
    </nav>

  </header>
</template>
