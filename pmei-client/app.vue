<template>
    <NuxtLayout>
      <NuxtPage />
    </NuxtLayout>
</template>

<script setup>
    import { useAuthStore } from "~/store/auth-store.js";
    const authStore = useAuthStore();
    authStore.loadUser();
</script>