<script setup>
import { ref } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useAuthStore } from '~/store/auth-store';

const router = useRouter()
const authStore = useAuthStore()

const route = useRoute()
const sensorId = ref(route.params.sensorId)
const sensor = ref(null)
const error = ref('')

const config = useRuntimeConfig()
const api = config.public.API_URL

const { token } = storeToRefs(authStore)

const evento = ref({
    valor: null
})

const createEvento = async () => {
  error.value = ''
  if (sensor.value.tipo === 'Posicionamento Global') {
    // check if the value is a valid lat, long pair
    const latLong = evento.value.valor.split(',')
    if (latLong.length !== 2) {
      error.value = 'Por favor insira um valor válido para o posicionamento global.'
      return
    }
    if (isNaN(parseFloat(latLong[0])) || isNaN(parseFloat(latLong[1]))) {
      error.value = 'Por favor insira um valor válido para o posicionamento global.'
      return
    }
    if (parseFloat(latLong[0]) < -90 || parseFloat(latLong[0]) > 90 || parseFloat(latLong[1]) < -180 || parseFloat(latLong[1]) > 180) {
      error.value = 'Por favor insira um valor válido para o posicionamento global.'
      return
    }
  }
  if (sensor.value.tipo !== 'Posicionamento Global' && isNaN(parseFloat(evento.value.valor))) {
    error.value = 'Por favor insira um valor numérico.'
    return
  }
    try {
        await $fetch(`${api}/sensores/${sensorId.value}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                Authorization: `Bearer ${token.value}`
            },
            body: JSON.stringify(evento.value),
            onResponse({ request, response, options }) {
                if (response.status === 200) {
                    router.push('/SGO/sensors/' + sensorId.value)
                }
            }
        })
    } catch (err) {
        console.error('Error creating evento:', err);
        error.value = 'Não foi possível adicionar o evento, tente novamente mais tarde.'
    }
}

async function fetchSensor() {
  try {
    // fetch the sensor
    sensor.value = await $fetch(`${api}/sensores/${sensorId.value}`, {
      headers: {
        Authorization: `Bearer ${token.value}`
      }
    })
  } catch (err) {
    error.value = 'Não foi possível carregar o sensor, tente novamente mais tarde.'
  }
}

onMounted(() => {
  fetchSensor()
})
</script>
<template>
    <div class="min-h-screen bg-gray-100 py-6 px-4 sm:px-6 lg:px-8">
        <div class="max-w-4xl mx-auto">

          <div class="mb-6">
            <NuxtLink :to="`/SGO/sensors/${sensorId}`" class="text-blue-500 hover:text-blue-600 flex items-center">
              <span class="mr-2">←</span> Voltar ao sensor
            </NuxtLink>
          </div>

            <h1 v-if="sensor && sensorId" class="text-3xl font-bold text-gray-900 mb-8">Adicionar Evento ao Sensor {{sensorId}} de {{sensor.tipo}}</h1>

            <form @submit.prevent="createEvento" class="space-y-6 bg-white p-6 rounded-lg shadow">
                <!-- Valor -->
                <div v-if="sensor && sensor.tipo !== 'Posicionamento Global'">
                    <label for="valor" class="block text-sm font-medium text-gray-700">Valor</label>
                    <input placeholder="Ex: 31.2" name="valor" id="valor" v-model="evento.valor" type="number" step=".01"
                        class="mt-1 block w-full p-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                        required>
                </div>
                <div v-else>
                    <label for="valor" class="block text-sm font-medium text-gray-700">Valor</label>
                    <input placeholder="Ex: 39.73451596424165, -8.821039090383264" name="valor" id="valor" v-model="evento.valor" type="text"
                        class="mt-1 block w-full p-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                        required>
                </div>

                <div class="flex justify-end mt-6">
                    <button type="submit"
                        class="inline-flex items-center px-4 py-2 border border-transparent text-sm font-medium rounded-lg text-white bg-blue-500 hover:bg-blue-700">
                        Adicionar Evento
                    </button>
                </div>
            </form>
            <div v-if="error" class="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded mt-4">
                {{ error }}
            </div>
        </div>
    </div>
</template>