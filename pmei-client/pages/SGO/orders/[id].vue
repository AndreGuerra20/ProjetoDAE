<script setup>
import {onMounted, ref} from 'vue'
import { useAuthStore} from "~/store/auth-store.js";

const authStore = useAuthStore()
const route = useRoute()
const error = ref('')
const {token, user} = storeToRefs(authStore)
const order = ref(null)

const config = useRuntimeConfig()
const api = config.public.API_URL

async function fetch() {

  try {
    // fetch the order
    order.value = await $fetch(`${api}/encomendas/${route.params.id}`, {
      headers: {
        Authorization: `Bearer ${token.value}`
      }
    })
  } catch (err) {
    console.error('Error fetching encomenda:', err)
    error.value = 'Não foi possível carregar a encomenda, tente novamente mais tarde.'
  }
}

const getCorrectSensorName = (tipo) => {
  if(tipo === 'Pressao') {
    return 'Pressão';
  } else if(tipo === 'Aceleracao') {
    return 'Aceleração';
  }
  return tipo;
}

const isDelivered = (volume) => {
  return volume === true ? 'Sim' : 'Não'
}

const getSensorStatus = (status) => {
  return status === true ? 'Ativo' : 'Inativo'
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
  await fetch()
  console.log(order.value)
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
    <div class="max-w-4xl mx-auto">

      <div class="mb-6">
        <NuxtLink to="/SGO" class="text-blue-500 hover:text-blue-600 flex items-center">
          <span class="mr-2">←</span> Voltar para a área de gestão
        </NuxtLink>
      </div>

      <div v-if="error" class="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded mb-4">
        {{ error }}
      </div>

      <h1 v-if="order" class="text-2xl md:text-3xl font-bold mb-6 text-gray-800">Encomenda #{{ order.encomendaId }}</h1>

      <div v-if="order" class="bg-white rounded-lg shadow-md p-6 mb-5">
          <h1 class="text-2xl font-bold text-gray-800 pb-4">Detalhes da Encomenda</h1>
          <div class="grid grid-cols-3 gap-4">
            <div>
              <p class="text-lg font-semibold text-gray-800">ID Cliente</p>
              <p class="text-sm text-gray-600">{{ order.customerId }}</p>
            </div>
            <div>
              <p class="text-lg font-semibold text-gray-800">Estado</p>
              <p class="text-sm text-gray-600">
                <span :class="styleStatusBadge(order.estado)">{{ order.estado }}</span>
              </p>
            </div>
            <div>
              <p class="text-lg font-semibold text-gray-800">Entregue</p>
              <p class="text-sm text-gray-600">{{ isDelivered(order.entregue) }}</p>
            </div>
          </div>
      </div>

      <!-- Sensores -->
      <div v-if="order" class="bg-white rounded-lg shadow-md p-6">
        <div class="border-b pb-4 mb-4">
          <h1 class="text-2xl font-bold text-gray-800 pb-4">Volumes</h1>
          <div v-for="volume in order.volumes" :key="volume.idVolume">

              <div class="pb-6">
                <div class="flex items-center justify-between py-2">
                  <p class="text-lg font-semibold text-gray-800">ID {{ volume.idVolume }}</p>
                  <p class="text-sm text-gray-600 ml-2 font-medium">Tipo da Embalagem: {{ volume.tipoEmbalagem }}</p>
                  <p class="text-sm text-gray-600 ml-2 font-medium">Entregue: {{ isDelivered(volume) }}</p>
                </div>
                <table v-if="volume.sensores.length > 0" class="min-w-full">
                  <thead>
                  <tr class="bg-gray-100">
                    <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">ID Sensor</th>
                    <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Tipo</th>
                    <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Estado</th>
                    <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Ver Eventos</th>
                  </tr>
                  </thead>
                  <tbody class="bg-gray-50 divide-y divide-gray-200">
                  <tr v-for="sensor in volume.sensores" :key="sensor.id">
                    <td class="px-6 py-4 whitespace-nowrap">{{ sensor.id }}</td>
                    <td class="px-6 py-4 whitespace-nowrap">{{ getCorrectSensorName(sensor.tipo) }}</td>
                    <td class="px-6 py-4 whitespace-nowrap">
                      <span :class="styleStatusBadge(sensor.status)">
                      {{ getSensorStatus(sensor.status) }}
                      </span>
                    </td>
                    <td class="px-6 py-4 whitespace-nowrap">
                      <NuxtLink :to="`/SGO/sensors/${sensor.id}`" class="text-blue-500 hover:text-blue-600">+</NuxtLink>
                    </td>
                  </tr>
                  </tbody>
                </table>
                <!-- No sensors yellow warning-->
                <div v-else class="bg-yellow-100 border border-yellow-400 text-yellow-700 px-4 py-3 rounded mt-4">
                  Não foram encontrados sensores para este volume.
                </div>

              </div>

          </div>
        </div>
      </div>

    </div>
  </div>
</template>