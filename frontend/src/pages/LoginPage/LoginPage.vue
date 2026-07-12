<script setup lang="ts">
import { ref } from 'vue';
import Card from 'primevue/card';
import InputText from 'primevue/inputtext';
import Button from 'primevue/button';

const email = ref('');
const password = ref('');
</script>

<template>
  <div class="flex-1 flex justify-center items-center">
    <Card
      class="max-w-sm w-full bg-surface-50! dark:bg-surface-900! border border-surface-200 dark:border-surface-700 shadow-md"
      :pt="{ caption: { class: 'gap-0!' }, subtitle: { class: 'text-sm!' } }"
    >
      <template #title>Welcome back to app</template>
      <template #subtitle>Authorize by email and your password</template>
      <template #content>
        <form class="space-y-6 mt-3">
          <div class="flex flex-col gap-1">
            <label for="email">Email</label>
            <InputText id="email" v-model="email" type="email" />
          </div>
          <div class="flex flex-col gap-1 mb-0">
            <div class="flex items-center justify-between">
              <label for="password" class="flex-1">Password</label>
            </div>
            <InputText id="password" v-model="password" type="password" />
          </div>
        </form>
      </template>

      <template #footer>
        <div class="flex flex-col gap-4">
          <Button class="w-full mt-3">Login</Button>
          <RouterLink to="/">
            <Button severity="secondary" variant="outlined" class="w-full">Back to home page</Button>
          </RouterLink>
          <div class="mt-2 text-center text-surface-500 text-sm">
            Don't have an account?
            <Button variant="link" class="p-0">Sign up</Button>
          </div>
        </div>
      </template>

    </Card>
  </div>
</template>

<style scoped>

</style>
