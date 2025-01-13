<template>
  <ClientOnly>
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
          <NuxtLink v-show="authStore.role === 'Gestor' || !authStore.role"
              to="/SGO"
              class="px-4 py-2 text-gray-700 hover:bg-blue-50 rounded-md transition">
            Gestão
          </NuxtLink>
          <NuxtLink v-show="authStore.role === 'Logistica' || !authStore.role"
              to="/SDL"
              class="px-4 py-2 text-gray-700 hover:bg-blue-50 rounded-md transition">
            Logística
          </NuxtLink>
          <NuxtLink v-show="authStore.role === 'Cliente' || !authStore.role"
              to="/SAC"
              class="px-4 py-2 text-gray-700 hover:bg-blue-50 rounded-md transition">
            Cliente
          </NuxtLink>
          <NuxtLink
              v-if="!authStore.isAuthenticated()"
              to="/auth/login"
              class="px-4 py-2 text-gray-700 hover:bg-blue-50 rounded-md transition">
            Entrar
          </NuxtLink>
          <a href="#" v-if="authStore.isAuthenticated()" class="px-4 py-2 text-gray-700 hover:bg-blue-50 rounded-md transition" @click.prevent="logout">Sair</a>
        </div>
      </div>
    </div>
  </nav>
  <slot/>
  </ClientOnly>

</template>

<script setup>
import {useAuthStore} from "~/store/auth-store.js";
import {onMounted} from "vue";
const router = useRouter()
const authStore = useAuthStore()

function logout() {
  authStore.logout()
  router.push('/')
  //location.reload()
}

</script>