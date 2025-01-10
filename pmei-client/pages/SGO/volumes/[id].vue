<script setup>
import { ref, onMounted } from 'vue'

const route = useRoute()
const error = ref(null)
const token = ref(null)
const sensors = ref(null)

async function fetch() {
  error.value = null;
  try {
    // First get the authentication token
    const authResponse = await $fetch(`http://localhost:8080/PMEI/monitorizacao/api/auth/login`, {
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
    token.value = authResponse

    // fetch the sensor
    const response = await $fetch(`http://localhost:8080/PMEI/monitorizacao/api/volume/${route.params.id}/sensores`, {
      headers: {
        Authorization: `Bearer ${token.value}`
      }
    })

    sensors.value = response
  } catch (err) {
    console.error('Error fetching sensors:', err)
    error.value = 'Failed to load sensors.'
  }
}

onMounted(async () => {
  await fetch()
  console.log(sensors.value)
})
</script>
<template>
  <div class="min-h-screen bg-gray-100 p-4">
    <div class="max-w-4xl mx-auto">

      <div class="mb-6">
        <NuxtLink to="/SGO" class="text-blue-500 hover:text-blue-600 flex items-center">
          <span class="mr-2">←</span> Back to Management Dashboard
        </NuxtLink>
      </div>

      <div v-if="error" class="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded mb-4">
        {{ error }}
      </div>

      <h1 v-if="sensors" class="text-2xl md:text-3xl font-bold mb-6 text-gray-800">Volume #{{ route.params.id }}</h1>

      <!-- Sensores
      <div v-if="order" class="bg-white rounded-lg shadow-md p-6">
        <div class="border-b pb-4 mb-4">
          <h1 class="text-2xl font-bold text-gray-800 pb-4">Volumes</h1>
          <table class="min-w-full">
            <thead>
            <tr class="bg-gray-50">
              <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">ID Volume</th>
              <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Package Type</th>
              <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">See sensors</th>
            </tr>
            </thead>
            <tbody class="bg-white divide-y divide-gray-200">
            <tr v-for="volume in order.volumes" :key="volume.idVolume">
              <td class="px-6 py-4 whitespace-nowrap">{{ volume.idVolume }}</td>
              <td class="px-6 py-4 whitespace-nowrap">{{ volume.tipoEmbalagem }}</td>
              <td class="px-6 py-4 whitespace-nowrap">
                <NuxtLink :to="`/SGO/volumes/${volume.idVolume}`" class="text-blue-500 hover:text-blue-600">See sensors</NuxtLink>
              </td>
            </tr>
            </tbody>
          </table>
        </div>
      </div>-->

    </div>
  </div>
</template>