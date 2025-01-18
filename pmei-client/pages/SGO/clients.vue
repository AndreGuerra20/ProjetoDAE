<script setup>
import { useAuthStore } from '~/store/auth-store';
const config = useRuntimeConfig()
const api = config.public.API_URL

const authStore = useAuthStore()
const {token, user} = storeToRefs(authStore)

const clientes = ref(null)
const cliente = ref(null)
const error = ref('')

async function fetchClientes() {
  try {
    // fetch the sensor
    clientes.value = await $fetch(`${api}/clientes`, {
      headers: {
        Authorization: `Bearer ${token.value}`
      }
    })
  } catch (err) {
    error.value = 'Não foi possível carregar os clientes, tente novamente mais tarde.'
  }
}

const clienteByUsername = ref('')

const getCliente = async () => {
  cliente.value = null
  try {
    // fetch the sensor
    cliente.value = await $fetch(`${api}/clientes/${clienteByUsername.value}`, {
      headers: {
        Authorization: `Bearer ${token.value}`
      }
    })
  } catch (err) {
    error.value = 'Não foi possível carregar o cliente, tente novamente mais tarde.'
  }
}


onMounted(() => {
  fetchClientes()
})
</script>
<template>
  <div class="min-h-screen bg-gray-100 p-4">
    <div class="max-w-7xl mx-auto">

      <div class="mb-6">
        <NuxtLink to="/SGO" class="text-blue-500 hover:text-blue-600 flex items-center">
          <span class="mr-2">←</span> Voltar para a área de gestão
        </NuxtLink>
      </div>

      <h1 class="text-2xl md:text-3xl font-bold mb-6 text-gray-800">Clientes</h1>

      <div class="bg-white rounded-lg shadow-md p-4 mb-6">
        <div class="flex justify-between items-center mb-4">
          <h2 class="text-xl font-semibold">Pesquisar cliente</h2>
        </div>
        <input v-model="clienteByUsername" type="text" class="w-full border p-2 rounded-lg px-3 py-2  focus:outline-none focus:ring-2 focus:ring-blue-500"
               placeholder="Username do cliente" />
        <button @click.prevent="getCliente()" class="bg-blue-500 text-white px-4 py-2 rounded-lg mt-2">Obter</button>
        <div>
          <div v-if="cliente" class="mt-4 p-3 bg-gray-50 rounded-lg">
            <p class="text-gray-800"><strong>ID:</strong> {{ cliente.id }}</p>
            <p class="text-gray-800"><strong>Username:</strong> {{ cliente.username }}</p>
            <p class="text-gray-800"><strong>Nome:</strong> {{ cliente.name }}</p>
            <p class="text-gray-800"><strong>Email:</strong> {{ cliente.email }}</p>
            <p class="text-gray-800"><strong>NIF:</strong> {{ cliente.nif }}</p>
          </div>
          <div v-else>
            <p class="text-red-500">{{ error }}</p>
          </div>
        </div>
      </div>

      <div class="bg-white rounded-lg shadow-md p-4 mb-6">

        <div class="overflow-x-auto">
          <table class="min-w-full">
            <thead>
            <tr class="bg-gray-50">
              <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">ID</th>
              <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Username</th>
              <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Nome</th>
              <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Email</th>
              <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">NIF</th>
            </tr>
            </thead>
            <tbody class="bg-white divide-y divide-gray-200">
            <tr v-for="cliente in clientes" :key="cliente.id">
              <td class="px-6 py-4 whitespace-nowrap">{{ cliente.id }}</td>
              <td class="px-6 py-4 whitespace-nowrap">{{ cliente.username }}</td>
              <td class="px-6 py-4 whitespace-nowrap">{{ cliente.name }}</td>
              <td class="px-6 py-4 whitespace-nowrap">{{ cliente.email }}</td>
              <td class="px-6 py-4 whitespace-nowrap">{{ cliente.nif }}</td>
            </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>
  </div>
</template>