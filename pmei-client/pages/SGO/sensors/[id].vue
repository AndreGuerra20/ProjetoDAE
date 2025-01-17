<script setup>
import { onMounted, ref } from 'vue'
import { useAuthStore } from "~/store/auth-store.js";
import { useRoute, useRouter } from 'vue-router'

const authStore = useAuthStore()
const route = useRoute()
const router = useRouter()
const error = ref('')
const token = ref(null)
const sensor = ref(null)

const min = ref(null)
const max = ref(null)
const avg = ref(null)

const getCorrectSensorName = (tipo) => {
  if (tipo === 'Pressao') {
    return 'Pressão';
  } else if (tipo === 'Aceleracao') {
    return 'Aceleração';
  }
  return tipo;
}

const getEvento = () => {
  router.push(`/SGO/sensors/addEvento/${sensor.value.id}`)
}

async function getSensorStats() {
  try {
    if (sensor.value && sensor.value.tipo !== 'Posicionamento Global') {
      min.value = await $fetch(`http://localhost:8080/PMEI/monitorizacao/api/sensores/${sensor.value.id}/min`, {
        headers: {
          Authorization: `Bearer ${token.value}`
        }
      })
      max.value = await $fetch(`http://localhost:8080/PMEI/monitorizacao/api/sensores/${sensor.value.id}/max`, {
        headers: {
          Authorization: `Bearer ${token.value}`
        }
      })
      avg.value = await $fetch(`http://localhost:8080/PMEI/monitorizacao/api/sensores/${sensor.value.id}/avg`, {
        headers: {
          Authorization: `Bearer ${token.value}`
        }
      })
    }
  } catch (err) {
    console.error('Error fetching events:', err)
    error.value = 'Não foi possível carregar os eventos, tente novamente mais tarde.'
  }
}

async function fetchEvents() {
  try {
    // fetch the sensor
    sensor.value = await $fetch(`http://localhost:8080/PMEI/monitorizacao/api/sensores/${route.params.id}`, {
      headers: {
        Authorization: `Bearer ${token.value}`
      }
    })
  } catch (err) {
    console.error('Error fetching events:', err)
    error.value = 'Não foi possível carregar os eventos, tente novamente mais tarde.'
  }
}

onMounted(async () => {
  await fetchEvents()
  console.log(sensor.value)
  await getSensorStats()
})

const getSensorStatus = (status) => {
  return status === true ? 'Ativo' : 'Inativo'
}

const styleStatusBadge = (status) => {
  if (status === true) {
    return ['px-2 py-1 text-xs rounded-full bg-green-100 text-green-800'];
  }
  else if (status === false) {
    return ['px-2 py-1 text-xs rounded-full bg-red-100 text-red-800'];
  }
  else {
    return ['px-2 py-1 text-xs rounded-full bg-blue-100 text-blue-800'];
  }
};

//change timestamp format
const formatDate = (timestamp) => {
  const date = new Date(timestamp)
  let month = date.getMonth() + 1

  // dd-mm-yyyy hh:mm:ss
  const dateString = `${date.getDate() < 10 ? '0' + date.getDate() : date.getDate()}/${month + 1 < 10 ? '0' + month : month}/${date.getFullYear()}`
  const timeString = `${date.getHours() < 10 ? '0' + date.getHours() : date.getHours()}:${date.getMinutes() < 10 ? '0' + date.getMinutes() : date.getMinutes()}:${date.getSeconds() < 10 ? '0' + date.getSeconds() : date.getSeconds()}`

  return `${dateString} ${timeString}`

}

onBeforeMount(async () => {
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
    <div class="max-w-4xl mx-auto">

      <div class="mb-6">
        <NuxtLink to="/SGO" class="text-blue-500 hover:text-blue-600 flex items-center">
          <span class="mr-2">←</span> Voltar à área de gestão
        </NuxtLink>
      </div>

      <div v-if="error" class="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded mb-4">
        {{ error }}
      </div>

      <div class="flex justify-between items-center mb-6">
        <h1 v-if="sensor" class="text-2xl md:text-3xl font-bold text-gray-800">Sensor #{{ sensor.id }}</h1>
        <button @click="getEvento" class="bg-blue-500 hover:bg-blue-600 text-white font-bold py-2 px-4 rounded inline-flex items-center">
          <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6 mr-2" fill="none" viewBox="0 0 24 24" stroke="currentColor">
        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 6v6m0 0v6m0-6h6m-6 0H6"></path>
          </svg>
          Adicionar Evento
        </button>
      </div>

      <!-- Sensor Details -->
      <div v-if="sensor" class="bg-white rounded-lg shadow-md p-4 mb-6">
        <div class="flex items-center justify-between">
          <p class="text-l font-bold">Tipo</p>
          <span class="text-sm">{{ getCorrectSensorName(sensor.tipo) }}</span>
          <p class="text-l font-bold">Estado</p>
          <span :class="styleStatusBadge(sensor.status)">
            {{ getSensorStatus(sensor.status) }}
          </span>
        </div>
      </div>

      <!-- Events -->
      <div v-if="sensor" class="bg-white rounded-lg shadow-md p-6">
        <div class="border-b pb-4 mb-4">
          <h1 class="text-2xl font-bold text-gray-800 pb-4">Eventos</h1>
          <table class="min-w-full">
            <thead>
              <tr class="bg-gray-50">
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Valor</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Timestamp
                </th>
              </tr>
            </thead>
            <tbody class="bg-white divide-y divide-gray-200">
              <tr v-for="event in sensor.eventos" :key="event.id">
                <td class="px-6 py-4 whitespace-nowrap">{{ event.valor }}</td>
                <td class="px-6 py-4 whitespace-nowrap">{{ formatDate(event.timestamp) }}</td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
      <!-- Statistics like Average, Min and Max -->
      <div v-if="sensor" class="bg-white rounded-lg shadow-md p-6 mt-6">
        <div class="border-b pb-4 mb-4">
          <h1 class="text-2xl font-bold text-gray-800 pb-4">Estatísticas</h1>
          <div v-if="min" class="grid grid-cols-3 gap-4">
            <div>
              <p class="text-lg font-bold">Mínimo</p>
              <p class="text-sm">{{ min }}</p>
            </div>
            <div>
              <p class="text-lg font-bold">Média</p>
              <p class="text-sm">{{ avg }}</p>
            </div>
            <div>
              <p class="text-lg font-bold">Máximo</p>
              <p class="text-sm">{{ max }}</p>
            </div>
          </div>
          <div v-else>
            <p>Não existem estatísticas disponíveis para este sensor.</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>