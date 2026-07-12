<script setup lang="ts">
import Button from 'primevue/button';
</script>

<template>
  <div class="flex-1 flex flex-col justify-center items-center gap-4">
    <h1 class="text-2xl font-semibold">Programming Source</h1>
    <p class="text-surface-500">Welcome to the app</p>

    <RouterLink to="/login">
      <Button>Login</Button>
    </RouterLink>

  </div>
</template>
