<script setup>
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router';

import { useAuthStore } from '~/store/auth-store';

const authStore = useAuthStore()
const route = useRoute()
const router = useRouter()
const error = ref(null)
const sensors = ref(null)
const orders = ref(null)
const token = ref(null)
const sensorsSize = ref(null)

async function fetch() {
  error.value = null;
  try {
    // Then fetch the sensors
    sensors.value = await $fetch(`http://localhost:8080/PMEI/monitorizacao/api/sensores`, {
      headers: {
        Authorization: `Bearer ${token.value}`
      }
    })

    //filter sensors
    sensors.value = sensors.value.filter(sensor => sensor.status === true)

    orders.value = await $fetch(`http://localhost:8080/PMEI/monitorizacao/api/encomendas`, {
      headers: {
        Authorization: `Bearer ${token.value}`
      }
    })

  } catch (err) {
    console.error('Error fetching encomenda details:', err)
    error.value = 'Não foi possível carregar os detalhes da encomenda, tente novamente mais tarde.'
  }
}

const getSensorStatus = (status) => {
  return status === true ? 'Ativo' : 'Inativo';
}

const styleStatusBadge = (status) => {
  if (status === true || status === 'Entregue') {
    return ['px-2 py-1 text-xs rounded-full bg-green-100 text-green-800'];
  }
  else if (status === false || status === 'Pendente') {
    return ['px-2 py-1 text-xs rounded-full bg-yellow-100 text-yellow-800'];
  }
  else {
    return ['px-2 py-1 text-xs rounded-full bg-blue-100 text-blue-800'];
  }
};

onMounted(async () => {
  if (authStore.token) {
    await fetch()
    console.log(orders.value)
    sensorsSize.value = sensors.value.length
  }
})

onBeforeMount(() => {
  if (!authStore.token) {
    authStore.loadUser()
  }
  if(!authStore.user) {
    router.push('/auth/login')
  }
  if(authStore.role !== 'Gestor') {
    router.push('/auth/login')
  }
  token.value = authStore.token
})

</script>

<template>
  <div class="min-h-screen bg-gray-100 p-4">
    <div class="max-w-7xl mx-auto">
      <h1 class="text-2xl md:text-3xl font-bold mb-6 text-gray-800">Gestão</h1>

      <!-- Order Management -->
      <div class="bg-white rounded-lg shadow-md p-4 mb-6">
        <div class="flex justify-between items-center mb-4">
          <h2 class="text-xl font-semibold">Encomendas Recentes</h2>
        </div>

        <div class="overflow-x-auto">
          <table class="min-w-full">
            <thead>
              <tr class="bg-gray-50">
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">ID da
                  encomenda
                </th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">ID do cliente
                </th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Estado</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Ver Detalhes
                </th>
              </tr>
            </thead>
            <tbody class="bg-white divide-y divide-gray-200">
              <tr v-for="order in orders" :key="order.id">
                <td class="px-6 py-4 whitespace-nowrap">{{ order.encomendaId }}</td>
                <td class="px-6 py-4 whitespace-nowrap">{{ order.customerId }}</td>
                <td class="px-6 py-4 whitespace-nowrap">
                  <span :class="styleStatusBadge(order.estado)">
                    {{ order.estado }}
                  </span>
                </td>
                <td class="px-6 py-4 whitespace-nowrap">
                  <NuxtLink :to="`/SGO/orders/${order.encomendaId}`" class="text-blue-500 hover:text-blue-600">
                    +
                  </NuxtLink>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>

      <!-- Sensors -->
      <div class="bg-white rounded-lg shadow-md p-4 mb-6">
        <div class="flex justify-between items-center mb-4">
          <h2 class="text-xl font-semibold">Todos Sensores Ativos</h2>
        </div>

        <div class="overflow-x-auto">
          <table class="min-w-full">
            <thead>
              <tr class="bg-gray-50">
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">ID</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Tipo</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Estado</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Ver Eventos
                </th>
              </tr>
            </thead>
            <tbody class="bg-white divide-y divide-gray-200">
              <tr v-for="sensor in sensors" :key="sensor.id">
                <td class="px-6 py-4 whitespace-nowrap">{{ sensor.id }}</td>
                <td class="px-6 py-4 whitespace-nowrap">{{ sensor.tipo }}</td>
                <td class="px-6 py-4 whitespace-nowrap">
                  <span :class="styleStatusBadge(sensor.status)">
                    {{ getSensorStatus(sensor.status) }}
                  </span>
                </td>
                <td class="px-6 py-4 whitespace-nowrap">
                  <NuxtLink :to="`/SGO/sensors/${sensor.id}`" class="text-blue-500 hover:text-blue-600">
                    +
                  </NuxtLink>
                </td>
              </tr>
              <tr>
                <td colspan="4" class="px-6 py-4 whitespace-nowrap"># de sensores: {{ sensorsSize }}</td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>
  </div>
</template>