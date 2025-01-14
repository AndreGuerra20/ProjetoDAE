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
          console.log(response._data)
          encomendas.value = response._data
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
                    <h3 class="font-semibold mb-2">Encomendas Pendentes</h3>
                    <p class="text-3xl font-bold text-yellow-600">{{ encomendas.filter((encomenda) => {
                        return encomenda.estado === 'Pendente'
                    }).length }}</p>
                </div>
                <div class="bg-white rounded-lg shadow-md p-4">
                    <h3 class="font-semibold mb-2">Encomendas Despachadas</h3>
                    <p class="text-3xl font-bold text-blue-600">{{ encomendas.filter((encomenda) => {
                        return encomenda.estado === 'Despachada'
                    }).length }}</p>
                </div>
                <div class="bg-white rounded-lg shadow-md p-4">
                    <h3 class="font-semibold mb-2">Encomendas Entregues</h3>
                    <p class="text-3xl font-bold text-green-600">{{ encomendas.filter((encomenda) => {
                        return encomenda.estado === 'Entregue'
                    }).length }}</p>
                </div>
            </div>

            <!-- Delivery Management -->
            <div class="bg-white rounded-lg shadow-md p-4 mb-6">
                <div class="flex justify-between items-center mb-4">
                    <h2 class="text-xl font-semibold">Entregas Atuais</h2>
                    <NuxtLink to="/SDL/create" class="px-4 py-2 bg-blue-500 text-white rounded-lg">Nova Entrega</NuxtLink>
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
                            <tr v-for="encomenda in encomendas" :key="encomenda.encomendaId">
                                <td class="px-6 py-4 whitespace-nowrap">{{ encomenda.encomendaId }}</td>
                                <td class="px-6 py-4 whitespace-nowrap">
                                    <span :class="{
                                        'px-2 py-1 text-xs rounded-full': true,
                                        'bg-green-100 text-green-800': encomenda.estado === 'Entregue',
                                        'bg-blue-100 text-blue-800': encomenda.estado === 'Despachada',
                                        'bg-yellow-100 text-yellow-800': encomenda.estado === 'Pendente'
                                    }">
                                        {{ encomenda.estado }}
                                    </span>
                                </td>
                                <td class="px-6 py-4 whitespace-nowrap">{{ encomenda.volumes.length }}</td>
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