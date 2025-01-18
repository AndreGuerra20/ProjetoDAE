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
const idAProcurar = ref(null)

const lastReading = reactive({
  id: null,
  tipo: null,
  estado: null,
  ultimaLeitura: null,
  timestamp: null
})

const getCorrectSensorName = (tipo) => {
  if (tipo === 'Pressao') {
    return 'Pressão';
  } else if (tipo === 'Aceleracao') {
    return 'Aceleração';
  }
  return tipo;
}

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
    // sensors.value = sensors.value.filter(sensor => sensor.status === true)

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
  else if (status === false) {
    return ['px-2 py-1 text-xs rounded-full bg-red-100 text-red-800'];
  }
  else if (status === 'Pendente') {
    return ['px-2 py-1 text-xs rounded-full bg-yellow-100 text-yellow-800'];
  }
  else {
    return ['px-2 py-1 text-xs rounded-full bg-blue-100 text-blue-800'];
  }
};

const getLastReading = async () => {
  if (!idAProcurar.value) {
    return
  }
  try {
    await $fetch(`http://localhost:8080/PMEI/monitorizacao/api/sensores/${idAProcurar.value}/eventoRecente`, {
      headers: {
        Authorization: `Bearer ${token.value}`
      },
      onResponse({ response }) {
        if (response.status == 200) {
          error.value = null
          lastReading.id = response._data.sensorId
          lastReading.ultimaLeitura = response._data.valor
          lastReading.timestamp = response._data.timestamp
          $fetch(`http://localhost:8080/PMEI/monitorizacao/api/sensores/${idAProcurar.value}`, {
            headers: {
              Authorization: `Bearer ${token.value}`
            },
            onResponse({ response }) {
              if (response.status == 200) {
                lastReading.tipo = response._data.tipo
                lastReading.estado = response._data.status
              }
            }
          })
        } else if (response.status == 204) {
          error.value = "Sensor não tem leituras"
          lastReading.id = null
          lastReading.ultimaLeitura = null
          lastReading.timestamp = null
          lastReading.tipo = null
          lastReading.estado = null
        } else if(response.status == 404) {
          error.value = "Sensor não encontrado"
        }
      }
    })
  } catch (err) {
    console.error('Error fetching sensor details:', err)
  }
}

onMounted(async () => {
  if (authStore.token) {
    await fetch()
    sensorsSize.value = sensors.value.length
  }
})

onBeforeMount(() => {
  if (!authStore.token) {
    authStore.loadUser()
  }
  if (!authStore.user) {
    router.push('/auth/login')
  }
  if (authStore.role !== 'Gestor') {
    router.push('/auth/login')
  }
  token.value = authStore.token
})

</script>

<template>
  <div class="min-h-screen bg-gray-100 p-4">
    <div class="max-w-7xl mx-auto">
      <h1 class="text-2xl md:text-3xl font-bold mb-6 text-gray-800">Gestão</h1>

      <!-- Search by Sensor ID -->
      <div class="bg-white rounded-lg shadow-md p-4 mb-6">
        <div class="flex justify-between items-center mb-4">
          <h2 class="text-xl font-semibold">Obter última leitura</h2>
        </div>
        <input v-model="idAProcurar" type="text" class="w-full border p-2 rounded-lg px-3 py-2  focus:outline-none focus:ring-2 focus:ring-blue-500"
          placeholder="ID do sensor" />
        <button @click.prevent="getLastReading" class="bg-blue-500 text-white px-4 py-2 rounded-lg mt-2">Obter</button>
        <div>
          <div v-if="lastReading.ultimaLeitura" class="mt-4 p-3 bg-gray-50 rounded-lg">
            <div class="grid grid-cols-2 gap-2 text-sm">
              <div class="font-medium text-gray-600">Sensor ID:</div>
              <div>{{ lastReading.id }}</div>

              <div class="font-medium text-gray-600">Tipo:</div>
              <div>{{ lastReading.tipo }}</div>

              <div class="font-medium text-gray-600">Estado:</div>
              <div>
                <span :class="styleStatusBadge(lastReading.estado)">
                  {{ getSensorStatus(lastReading.estado) }}
                </span>
              </div>

              <div class="font-medium text-gray-600">Última Leitura:</div>
              <div>{{ lastReading.ultimaLeitura }}</div>

              <div class="font-medium text-gray-600">Timestamp:</div>
              <div>{{ formatDate(lastReading.timestamp) }}</div>
            </div>
          </div>
          <div v-else>
            <p class="text-red-500">{{ error }}</p>
          </div>
        </div>
      </div>

      <!-- Order Management -->
      <div class="bg-white rounded-lg shadow-md p-4 mb-6">
        <div class="flex justify-between items-center mb-4">
          <h2 class="text-xl font-semibold">Encomendas</h2>
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
          <h2 class="text-xl font-semibold">Sensores</h2>
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
                <td class="px-6 py-4 whitespace-nowrap">{{ getCorrectSensorName(sensor.tipo) }}</td>
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