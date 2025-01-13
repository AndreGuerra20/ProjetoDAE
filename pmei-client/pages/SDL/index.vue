<script setup>
import { ref,onMounted } from 'vue'

import {useAuthStore} from "~/store/auth-store.js"
import { useRouter } from 'vue-router';
const router = useRouter()

const config = useRuntimeConfig()
const api = config.public.API_URL
const authStore = useAuthStore()

const {token, user, role} = storeToRefs(authStore)
const error = ref(null)

const messages = ref([])
const encomendas = ref([])

encomendas.value = [
  {
    id: 'DEL001',
    status: 'Pending',
    volumes: 1
  },
  {
    id: 'DEL002',
    status: 'Pending',
    volumes: 2
  },
  {
    id: 'DEL003',
    status: 'Pending',
    volumes: 3
  }
]

async function fetchEncomendas() {
  try {
    await $fetch(`${api}/encomendas`, {
      headers: {
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
        if(response.status == 200) {
          //encomendas.value = response._data
        }
      }
    });
  } catch (err) {
    console.error('Error fetching encomendas:', err);
    error.value = 'Não foi possível carregar as encomendas, por favor tente novamente.';
  }
}

onMounted(async () => {
  authStore.loadUser()
  token.value = authStore.token
  if (authStore.token) {
    await fetchEncomendas()
  }

})

onBeforeMount(() => {
  if (!authStore.token) {
    authStore.loadUser()
  }
  if(!authStore.user) {
    router.push('/auth/login')
  }
  if(authStore.role !== 'Logistica') {
    router.push('/auth/login')
  }
  token.value = authStore.token
})
</script>

<template>
    <div class="min-h-screen bg-gray-100 p-4">
        <div class="max-w-7xl mx-auto">
            <h1 class="text-2xl md:text-3xl font-bold mb-6 text-gray-800">Logística</h1>

            <!-- Quick Actions -->
            <div class="grid md:grid-cols-3 gap-4 mb-6">
                <div class="bg-white rounded-lg shadow-md p-4">
                    <h3 class="font-semibold mb-2">Entregas Ativas</h3>
                    <p class="text-3xl font-bold text-blue-600">24</p>
                </div>
                <div class="bg-white rounded-lg shadow-md p-4">
                    <h3 class="font-semibold mb-2">Entregadores Disponíveis</h3>
                    <p class="text-3xl font-bold text-green-600">12</p>
                </div>
                <div class="bg-white rounded-lg shadow-md p-4">
                    <h3 class="font-semibold mb-2">Encomendas Pendentes</h3>
                    <p class="text-3xl font-bold text-yellow-600">45</p>
                </div>
            </div>

            <!-- Delivery Management -->
            <div class="bg-white rounded-lg shadow-md p-4 mb-6">
                <div class="flex justify-between items-center mb-4">
                    <h2 class="text-xl font-semibold">Entregas Atuais</h2>
                    <button class="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600 transition">
                        Nova Encomenda
                    </button>
                </div>

                <div class="overflow-x-auto">
                    <table class="min-w-full">
                        <thead>
                            <tr class="bg-gray-50">
                                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">ID</th>
                                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Estado</th>
                                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Volumes</th>

                            </tr>
                        </thead>
                        <tbody class="bg-white divide-y divide-gray-200">
                            <tr v-for="delivery in encomendas" :key="delivery.id">
                                <td class="px-6 py-4 whitespace-nowrap">{{ delivery.id }}</td>
                                <td class="px-6 py-4 whitespace-nowrap">
                                    <span :class="{
                                        'px-2 py-1 text-xs rounded-full': true,
                                        'bg-green-100 text-green-800': delivery.status === 'Completed',
                                        'bg-blue-100 text-blue-800': delivery.status === 'In Progress',
                                        'bg-yellow-100 text-yellow-800': delivery.status === 'Pending'
                                    }">
                                        {{ delivery.status }}
                                    </span>
                                </td>
                                <td class="px-6 py-4 whitespace-nowrap">{{ delivery.volumes }}</td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>

            <!-- Route Map Placeholder -->
            <div class="bg-white rounded-lg shadow-md p-4">
                <h2 class="text-xl font-semibold mb-4">Mapa das Rotas</h2>
                <div class="bg-gray-100 h-64 rounded-lg flex items-center justify-center">
                    <span class="text-gray-500">Inserir Mapa Aqui pls :D</span>
                </div>
            </div>
        </div>
    </div>
</template>