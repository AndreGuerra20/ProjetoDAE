<script setup>
import {onMounted, ref} from 'vue'

const route = useRoute()
const error = ref('')
const token = ref(null)
const order = ref(null)

async function fetch() {

  try {
    // First get the authentication token
    token.value = await $fetch(`http://localhost:8080/PMEI/monitorizacao/api/auth/login`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        Accept: 'application/json'
      },
      body: JSON.stringify({
        username: 'henri',
        password: '123'
      })
    })

    // fetch the order
    order.value = await $fetch(`http://localhost:8080/PMEI/monitorizacao/api/encomendas/${route.params.id}`, {
      headers: {
        Authorization: `Bearer ${token.value}`
      }
    })
  } catch (err) {
    console.error('Error fetching encomenda:', err)
    error.value = 'Não foi possível carregar a encomenda, tente novamente mais tarde.'
  }
}

const isDelivered = (volume) => {
  return volume === true ? 'Sim' : 'Não'
}

const getSensorStatus = (status) => {
  return status === true ? 'Ativo' : 'Inativo'
}

const styleStatusBadge = (status) => {
  if (status === true) {
    return ['px-2 py-1 text-xs rounded-full bg-green-100 text-green-800'];
  }
  else if (status === false) {
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
                <table class="min-w-full">
                  <thead>
                  <tr class="bg-gray-100">
                    <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">ID Sensor</th>
                    <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Tipo</th>
                    <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Estado</th>
                    <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Ver Sensores</th>
                  </tr>
                  </thead>
                  <tbody class="bg-gray-50 divide-y divide-gray-200">
                  <tr v-for="sensor in volume.sensores" :key="sensor.id">
                    <td class="px-6 py-4 whitespace-nowrap">{{ sensor.id }}</td>
                    <td class="px-6 py-4 whitespace-nowrap">{{ sensor.tipo }}</td>
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
              </div>

          </div>
        </div>
      </div>

    </div>
  </div>
</template>