<script setup>
import {useAuthStore} from "~/store/auth-store.js"
import { useRouter } from 'vue-router';
const config = useRuntimeConfig()
const api = config.public.API_URL
const router = useRouter()

const loginFormData = reactive({
  username: "",
  password: ""
})

const authStore = useAuthStore()
const {token, user} = storeToRefs(authStore)

const messages = ref([])

onMounted(() => {
  if (authStore.isAuthenticated()) {
    router.push('/')
  }
})

function reset() {
  loginFormData.password = ""
  loginFormData.username = ""
}

async function login() {
  try {
    await $fetch(`${api}/auth/login`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        Accept: 'application/json'
      },
      body: loginFormData,
      onResponse({ request, response, options }) {
        messages.value.push({
          method: options.method,
          request: request,
          status: response.status,
          statusText: response.statusText,
          payload: response._data
        })
        if (response.status === 200) {
          token.value = response._data
        }
      }
    })
    if (token.value) {
      await $fetch(`${api}/auth/user`, {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
          Accept: 'application/json',
          Authorization: `Bearer ${token.value}`
        },
        onResponse({ request, response, options }) {
          messages.value.push({
            method: options.method,
            request: request,
            status: response.status,
            statusText: response.statusText,
            payload: response._data
          })
          if (response.status == 200) {
            authStore.login(token.value, response._data)
            // if role is cliente
            if (authStore.role === 'Cliente') {
              router.push('/SAC')
            } else if (authStore.role === 'Gestor') {
              router.push('/SGO')
            } else if (authStore.role === 'Logistica') {
              router.push('/SDL')
            } else {
              router.push('/')
            }

          }
        }
      })
    }
  } catch (e) {
    console.error('login request failed: ', e)
  }
}
</script>
<template>
  <div class="min-h-[calc(100vh-64px)] flex items-center justify-center bg-gray-100 overflow-hidden-y">
    <div class="bg-white p-8 rounded-lg shadow-md w-full max-w-md">
      <h1 class="text-3xl font-bold mb-6 text-center">Entrar</h1>
      <div class="mb-4">
        <label class="block text-gray-700" for="username">Username:</label>
        <input id="username" name="username" v-model="loginFormData.username" class="w-full px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500" type="text">
      </div>
      <div class="mb-6">
        <label class="block text-gray-700" for="password">Senha:</label>
        <input @keyup.enter="login" id="password" name="password" v-model="loginFormData.password" class="w-full px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500" type="password">
      </div>
      <div class="flex justify-between items-center">
        <button @click="login" class="bg-blue-500 text-white px-4 py-2 rounded-md hover:bg-blue-600 transition">Entrar</button>
        <button @click="reset" class="bg-gray-500 text-white px-4 py-2 rounded-md hover:bg-gray-600 transition">Limpar</button>
      </div>
    </div>
  </div>
</template>