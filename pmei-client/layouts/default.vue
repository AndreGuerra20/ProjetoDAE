<template>
  <!-- Navigation Bar -->
  <nav class="bg-white shadow-md ">
    <div class="max-w-7xl mx-auto px-4">
      <div class="flex justify-between h-16">
        <div class="flex items-center">
          <div class="text-xl font-bold text-blue-600">
            <NuxtLink to="/">PMEI</NuxtLink>
          </div>
        </div>
        <div class="flex items-center space-x-4">
          <NuxtLink
              to="/SGO"
              class="px-4 py-2 text-gray-700 hover:bg-blue-50 rounded-md transition">
            Management
          </NuxtLink>
          <NuxtLink
              to="/SDL"
              class="px-4 py-2 text-gray-700 hover:bg-blue-50 rounded-md transition">
            Logistics
          </NuxtLink>
          <NuxtLink
              to="/SAC"
              class="px-4 py-2 text-gray-700 hover:bg-blue-50 rounded-md transition">
            Customer
          </NuxtLink>
          <div class="flex items-center">
            <img class="inline-block size-[55px] rounded-full border-2 border-blue-600"
                 :src="imageUrl" alt="Avatar">
          </div>
          <NuxtLink
              v-if="!authStore.isAuthenticated()"
              to="/auth/login"
              class="px-4 py-2 text-gray-700 hover:bg-blue-50 rounded-md transition">
            Login
          </NuxtLink>
          <a href="#" v-if="authStore.isAuthenticated()" class="px-4 py-2 text-gray-700 hover:bg-blue-50 rounded-md transition" @click.prevent="logout">Logout</a>
        </div>
      </div>
    </div>
  </nav>
  <slot/>

</template>

<script setup>
import {useAuthStore} from "~/store/auth-store.js";
const router = useRouter()
const authStore = useAuthStore()

const baseUrl = "https://thispersondoesnotexist.com";

// Estado reativo para o URL da imagem
const imageUrl = ref(baseUrl);

function logout() {
  authStore.logout()
  router.push('/')
}
</script>