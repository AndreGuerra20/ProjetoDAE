<script setup>
import {ref, onMounted} from 'vue'
import {useAuthStore} from "~/store/auth-store.js"
import {useRouter} from 'vue-router';

const router = useRouter()
const config = useRuntimeConfig()
const api = config.public.API_URL

const authStore = useAuthStore()
const isGestor = authStore.role === 'Gestor'

const formData = reactive({
  nome: "",
  codFuncionario: "",
  username: "",
  password: "",
  email: ""
})

const tipouser = ref('')

const confirmPassword = ref("")
const errorUser = ref('')
const sucessUser = ref(false)

const types = ref([
  { value: 'gestor', text: 'Gestor' },
  { value: 'logistica', text: 'Logística' }
]);

const addNewUser = async () => {
  errorUser.value = ''
  sucessUser.value = false
  if (formData.password !== confirmPassword.value) {
    errorUser.value = 'As passwords não coincidem.'
    return
  }
  if (tipouser.value === '') {
    errorUser.value = 'Escolha um tipo de utilizador.'
    return
  }
  if (tipouser.value === 'gestor') {
    try {
      await $fetch(`${api}/auth/gestor`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          Authorization: `Bearer ${authStore.token}`
        },
        body: formData,
        onResponse({request, response, options}) {
          if (response.status === 200) {
            sucessUser.value = true
            errorUser.value = ''
          } else {
            errorUser.value = 'Erro ao criar utilizador.'
          }
        }
      })
    } catch (err) {
      console.error('Error fetching users:', err);
      errorUser.value = 'Erro ao criar utilizador.'
    }

  } else if (tipouser.value === 'logistica') {
    try {
      await $fetch(`${api}/auth/logistica`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          Authorization: `Bearer ${authStore.token}`
        },
        body: formData,
        onResponse({request, response, options}) {
          if (response.status === 200) {
            sucessUser.value = true
            errorUser.value = ''
          } else {
            errorUser.value = 'Erro ao criar utilizador.'
          }
        }
      })
    } catch (err) {
      console.error('Error fetching users:', err);
      errorUser.value = 'Erro ao criar utilizador.'
    }

  }
}


</script>
<template>
  <div class="min-h-screen bg-gray-100 p-4">
    <div class="max-w-4xl mx-auto">
      <h1 class="text-2xl md:text-3xl font-bold mb-6 text-gray-800">Adicionar um novo utilizador</h1>

      <div class="flex gap-2 justify-end mb-4">
        <NuxtLink to="/auth/settings" class="px-4 py-2 bg-blue-500 text-white rounded-lg">Voltar às definições</NuxtLink>
      </div>
      <div v-if="isGestor" class="bg-white rounded-lg shadow-md p-4 mb-5">
        <div class="space-y-4">
          <div>
            <label for="tipouser" class="block text-sm font-medium text-gray-700">Escolher tipo utilizador</label>
            <select v-model="tipouser" id="user" name="user" class="mt-1 block w-full px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500">
              <option v-for="type in types" :value="`${type.value}`">{{type.text}}</option>
            </select>
          </div>
          <div>
            <label for="user" class="block text-sm font-medium text-gray-700">Código de funcionário</label>
            <input v-model="formData.codFuncionario" type="text" id="new-code" name="new-code"
                   class="mt-1 block w-full px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"/>
          </div>
          <div>
            <label for="user" class="block text-sm font-medium text-gray-700">Nome</label>
            <input v-model="formData.nome" type="text" id="new-name" name="new-name"
                   class="mt-1 block w-full px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"/>
          </div>
          <div>
            <label for="user" class="block text-sm font-medium text-gray-700">Username</label>
            <input v-model="formData.username" type="text" id="new-username" name="new-username"
                   class="mt-1 block w-full px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"/>
          </div>
          <div>
            <label for="user" class="block text-sm font-medium text-gray-700">Email</label>
            <input v-model="formData.email" type="text" id="new-email" name="new-email"
                   class="mt-1 block w-full px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"/>
          </div>
          <div>
            <label for="new-password" class="block text-sm font-medium text-gray-700">Nova password</label>
            <input v-model="formData.password" type="password" id="new-password" name="new-password"
                   class="mt-1 block w-full px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"/>
          </div>
          <div>
            <label for="confirm-password" class="block text-sm font-medium text-gray-700">Confirmar nova password</label>
            <input v-model="confirmPassword" type="password" id="confirm-password" name="confirm-password"
                   class="mt-1 block w-full px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"/>
          </div>
          <button @click="addNewUser" class="bg-blue-500 text-white px-4 py-2 rounded-lg">Adicionar utilizador</button>
        </div>
        <div v-if="errorUser" class="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded mt-4">
          {{ errorUser }}
        </div>
        <div v-if="sucessUser" class="bg-green-100 border border-green-400 text-green-700 px-4 py-3 rounded mt-4">
          Utilizador criado com sucesso.
        </div>
      </div>
    </div>
  </div>

</template>