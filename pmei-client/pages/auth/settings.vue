<script setup>
import { ref, onMounted } from 'vue'
import { useAuthStore } from "~/store/auth-store.js"
import { useRouter } from 'vue-router';

const router = useRouter()
const config = useRuntimeConfig()
const api = config.public.API_URL

const authStore = useAuthStore()
const isGestor = authStore.role === 'Gestor'

console.log('User:', authStore.role)

const passwordFormData = reactive({
  oldPassword: "",
  password: "",
  confirmPassword: ""
})

const passwordUserFormData = reactive({
  username: "",
  password: "",
  confirmPassword: ""
})

const errorUserPassword = ref('')
const errorPassword = ref('')

const users = ref([])
async function fetchUsers() {
  try {
    await $fetch(`${api}/users`, {
      headers: {
        Authorization: `Bearer ${authStore.token}`
      },
      onResponse({ request, response, options }) {
        console.log('Users:', response._data)
        users.value = response._data
        //remove gestor from users list
        users.value = users.value.filter(user => user.role !== 'Gestor')
        //order users by role and id
        users.value.sort((a, b) => {
          if (a.role < b.role) {
            return -1
          }
          if (a.role > b.role) {
            return 1
          }
          if (a.username < b.username) {
            return -1
          }
          if (a.username > b.username) {
            return 1
          }
          return 0
        })
      }
    });
  } catch (err) {
    console.error('Error fetching users:', err);
  }
}

const changeUserPassword = async () => {
  try {
    if (passwordUserFormData.password !== passwordUserFormData.confirmPassword) {
      errorUserPassword.value = 'As passwords não coincidem.'
    } else {
      await $fetch(`${api}/auth/setPassword`, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
          Accept: 'application/json',
          Authorization: `Bearer ${authStore.token}`
        },
        body: passwordUserFormData
      })
      console.log('Changing user password:', passwordUserFormData.password + ' ' + passwordUserFormData.confirmPassword + ' ' + passwordUserFormData.username)
    }
  } catch (err) {
    console.error('Error changing password:', err);
  }
}

const changePassword = async () => {
  try {
    if (passwordFormData.password !== passwordFormData.confirmPassword) {
      errorPassword.value = 'As passwords não coincidem.'
    } else {
      await $fetch(`${api}/auth/setPassword`, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
          Accept: 'application/json',
          Authorization: `Bearer ${authStore.token}`
        },
        body: passwordFormData
      })
      console.log('Changing password:', passwordFormData.oldPassword + ' ' + passwordFormData.password + ' ' + passwordFormData.confirmPassword)
    }
  } catch (err) {
    console.error('Error changing password:', err);
  }
}

onMounted(async () => {
  if (isGestor) {
    await fetchUsers()
  }
})

</script>
<template>
  <div class="min-h-screen bg-gray-100 p-4">
    <div class="max-w-4xl mx-auto">
      <h1 class="text-2xl md:text-3xl font-bold mb-6 text-gray-800">Definições</h1>

      <div v-if="isGestor" class="flex gap-2 justify-end mb-4">
        <NuxtLink to="/" class="px-4 py-2 bg-blue-500 text-white rounded-lg">Adicionar Novo Gestor</NuxtLink>
      </div>
      <div v-if="isGestor" class="bg-white rounded-lg shadow-md p-4 mb-5">
        <h2 class="text-xl font-semibold mb-4">Mudar password de utilizador</h2>
        <div class="space-y-4">
          <div>
            <label for="user" class="block text-sm font-medium text-gray-700">Utilizador</label>
            <select v-model="passwordUserFormData.username" id="user" name="user" class="mt-1 block w-full px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500">
              <option v-for="user in users" :value="`${user.username}`">{{user.username}} ({{user.role}})</option>
            </select>
          </div>
          <div>
            <label for="new-password" class="block text-sm font-medium text-gray-700">Nova password</label>
            <input v-model="passwordUserFormData.password" type="password" id="new-user-password" name="new-password" class="mt-1 block w-full px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500" />
          </div>
          <div>
            <label for="confirm-password" class="block text-sm font-medium text-gray-700">Confirmar nova password</label>
            <input v-model="passwordUserFormData.confirmPassword" type="password" id="confirm-user-password" name="confirm-password" class="mt-1 block w-full px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500" />
          </div>
          <button @click="changeUserPassword" class="bg-blue-500 text-white px-4 py-2 rounded-lg">Mudar password</button>
        </div>
        <div v-if="errorUserPassword" class="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded mt-4">
          {{ errorUserPassword }}
        </div>
      </div>
      <div class="bg-white rounded-lg shadow-md p-4">
        <h2 class="text-xl font-semibold mb-4">Mudar password</h2>
        <div class="space-y-4">
          <div>
            <label for="current-password" class="block text-sm font-medium text-gray-700">Password atual</label>
            <input v-model="passwordFormData.oldPassword" type="password" id="current-password" name="current-password" class="mt-1 block w-full px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500" />
          </div>
          <div>
            <label for="new-password" class="block text-sm font-medium text-gray-700">Nova password</label>
            <input v-model="passwordFormData.password" type="password" id="new-password" name="new-password" class="mt-1 block w-full px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500" />
          </div>
          <div>
            <label for="confirm-password" class="block text-sm font-medium text-gray-700">Confirmar nova password</label>
            <input v-model="passwordFormData.confirmPassword" type="password" id="confirm-password" name="confirm-password" class="mt-1 block w-full px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500" />
          </div>
          <button @click="changePassword" class="bg-blue-500 text-white px-4 py-2 rounded-lg">Mudar password</button>
        </div>
        <div v-if="errorPassword" class="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded mt-4">
          {{ errorPassword }}
        </div>
      </div>
    </div>
  </div>
</template>