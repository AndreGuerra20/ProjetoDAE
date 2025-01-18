<script setup>
import { onMounted, ref } from 'vue'
import { useAuthStore} from "~/store/auth-store.js";

const route = useRoute()
const error = ref(null)
const authStore = useAuthStore()
const encomenda = ref(null)
const token = ref(null)
const config = useRuntimeConfig()
const api = config.public.API_URL
const marcadores = ref([])

import { Chart as ChartJS, Title, Tooltip, Legend, LineElement, CategoryScale, LinearScale, PointElement } from 'chart.js'
import { Line } from 'vue-chartjs'

// Register
ChartJS.register(Title, Tooltip, Legend, LineElement, CategoryScale, LinearScale, PointElement)

const chartOptions = ref({
  responsive: true,
  maintainAspectRatio: false,
})

const getCorrectSensorName = (tipo) => {
  if(tipo === 'Pressao') {
    return 'Pressão';
  } else if(tipo === 'Aceleracao') {
    return 'Aceleração';
  }
  return tipo;
}

async function fetchEncomendaDetails() {
  error.value = null;
  try {
    // Then fetch the encomenda details
    encomenda.value = await $fetch(`${api}/encomendas/${route.params.id}`, {
      headers: {
        Authorization: `Bearer ${token.value}`
      }
    })
  } catch (err) {
    console.error('Error fetching encomenda details:', err)
    error.value = 'Não foi possível carregar os detalhes da encomenda, por favor tente novamente.'
  }
  for (const volume of encomenda.value.volumes) {
    for (const sensor of volume.sensores) {
        const json = JSON.parse(`{"volumeId": ${volume.idVolume}, "sensorid": ${sensor.id}}`)
        marcadores.value.push(json);
          if (sensor.eventos.length > 0) {
            marcadores.value.find(marcador => marcador.sensorid === sensor.id).eventos = sensor.eventos
            switch (sensor.tipo){
              case 'Posicionamento Global':
                marcadores.value.find(marcador => marcador.sensorid === sensor.id).showMap = false
                marcadores.value.find(marcador => marcador.sensorid === sensor.id).mapCenter = calculateCenter(sensor.eventos)
                marcadores.value.find(marcador => marcador.sensorid === sensor.id).mapZoom = calculateZoom(sensor.eventos)
                break;
              case 'Temperatura':
                marcadores.value.find(marcador => marcador.sensorid === sensor.id).showTemperatureChart = false
                marcadores.value.find(marcador => marcador.sensorid === sensor.id).chartData = {
                  labels: sensor.eventos.map(evento => new Date(evento.timestamp).toLocaleString('pt-PT', {
                    day: '2-digit', month: '2-digit',
                    year: 'numeric', hour: '2-digit', minute: '2-digit', second: '2-digit'
                  })),
                  datasets: [
                    {
                      label: 'Temperatura',
                      backgroundColor: '#f87979',
                      data: sensor.eventos.map(evento => parseFloat(evento.valor)),
                    },
                  ],
                }
                break;
              case 'Pressao':
                marcadores.value.find(marcador => marcador.sensorid === sensor.id).showPressureChart = false
                marcadores.value.find(marcador => marcador.sensorid === sensor.id).chartData = {
                  labels: sensor.eventos.map(evento => new Date(evento.timestamp).toLocaleString('pt-PT', {
                    day: '2-digit', month: '2-digit',
                    year: 'numeric', hour: '2-digit', minute: '2-digit', second: '2-digit'
                  })),
                  datasets: [
                    {
                      label: 'Pressão',
                      backgroundColor: '#f87979',
                      data: sensor.eventos.map(evento => parseFloat(evento.valor)),
                    },
                  ],
                }
                break;
              case 'Aceleracao':
                marcadores.value.find(marcador => marcador.sensorid === sensor.id).showAccelerationChart = false
                marcadores.value.find(marcador => marcador.sensorid === sensor.id).chartData = {
                  labels: sensor.eventos.map(evento => new Date(evento.timestamp).toLocaleString('pt-PT', {
                    day: '2-digit', month: '2-digit',
                    year: 'numeric', hour: '2-digit', minute: '2-digit', second: '2-digit'
                  })),
                  datasets: [
                    {
                      label: 'Aceleração',
                      backgroundColor: '#f87979',
                      data: sensor.eventos.map(evento => parseFloat(evento.valor)),
                    },
                  ],
                }
                break;
            }
          } else {
            marcadores.value.pop(marcador => marcador.sensorid === sensor.id)
          }
    }
  }
}

function calculateCenter(eventos) {
  const latitudes = eventos.map(evento => parseFloat(evento.valor.split(',')[0]))
  const longitudes = eventos.map(evento => parseFloat(evento.valor.split(',')[1]))
  const lat = (Math.min(...latitudes) + Math.max(...latitudes)) / 2
  const lng = (Math.min(...longitudes) + Math.max(...longitudes)) / 2
  return [lat, lng]
}

function calculateZoom(eventos) {
  const latitudes = eventos.map(evento => parseFloat(evento.valor.split(',')[0]))
  const longitudes = eventos.map(evento => parseFloat(evento.valor.split(',')[1]))
  const latDiff = Math.max(...latitudes) - Math.min(...latitudes)
  const lngDiff = Math.max(...longitudes) - Math.min(...longitudes)
  const latZoom = Math.floor(Math.log2(360 / latDiff)) - 1
  const lngZoom = Math.floor(Math.log2(360 / lngDiff)) - 1
  return Math.min(latZoom, lngZoom)
}

function toggleMapOrChart(sensorId) {
  if (marcadores.value.find(marcador => marcador.sensorid === sensorId && marcador.showMap !== undefined)) {
    marcadores.value.find(marcador => marcador.sensorid === sensorId && marcador.showMap !== undefined).showMap =
        !marcadores.value.find(marcador => marcador.sensorid === sensorId && marcador.showMap !== undefined).showMap
  }else if (marcadores.value.find(marcador => marcador.sensorid === sensorId && marcador.showTemperatureChart !== undefined)) {
    marcadores.value.find(marcador => marcador.sensorid === sensorId && marcador.showTemperatureChart !== undefined).showTemperatureChart =
        !marcadores.value.find(marcador => marcador.sensorid === sensorId && marcador.showTemperatureChart !== undefined).showTemperatureChart
  }else if (marcadores.value.find(marcador => marcador.sensorid === sensorId && marcador.showAccelerationChart !== undefined)) {
    marcadores.value.find(marcador => marcador.sensorid === sensorId && marcador.showAccelerationChart !== undefined).showAccelerationChart =
        !marcadores.value.find(marcador => marcador.sensorid === sensorId && marcador.showAccelerationChart !== undefined).showAccelerationChart
  }
  else if (marcadores.value.find(marcador => marcador.sensorid === sensorId && marcador.showPressureChart !== undefined)) {
    marcadores.value.find(marcador => marcador.sensorid === sensorId && marcador.showPressureChart !== undefined).showPressureChart =
        !marcadores.value.find(marcador => marcador.sensorid === sensorId && marcador.showPressureChart !== undefined).showPressureChart
  }
}

const btnMapOrChartText = (marcadores, sensor) => {
  const marcador = marcadores.find(marcador => marcador.sensorid === sensor.id && marcador.showMap !== undefined)
  if (marcador) {
    if (marcador.hasOwnProperty('showMap')) {
      const showMap = marcador.showMap
      if (showMap) {
        return 'Esconder Mapa'
      } else {
        return 'Mostrar Mapa'
      }
    } else {
      return '';
    }
  } else {
    const marcador2 = marcadores.find(marcador => marcador.sensorid === sensor.id && marcador.showMap === undefined)
    if (marcador2 && (marcador2.hasOwnProperty('showTemperatureChart') || marcador2.hasOwnProperty('showAccelerationChart') || marcador2.hasOwnProperty('showPressureChart'))) {
      const showGraph = marcador2.showTemperatureChart || marcador2.showAccelerationChart || marcador2.showPressureChart
      if (showGraph) {
        return 'Esconder Gráfico'
      } else {
        return 'Mostrar Gráfico'
      }
    } else {
      return '';
    }
  }
};

const map = ref(null);

onMounted(async () => {
  // authStore.loadUser()
  // token.value = authStore.token
  await fetchEncomendaDetails()
})

onBeforeMount(() => {
  if (!authStore.token) {
    authStore.loadUser()
  }
  if(!authStore.user) {
    router.push('/auth/login')
  }
  if(authStore.role !== 'Cliente') {
    router.push('/auth/login')
  }
  token.value = authStore.token
})

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

</script>

<template>

  <div v-if="encomenda" class="min-h-screen bg-gray-100 p-4">
    <div class="max-w-4xl mx-auto">
      <div class="mb-6">
        <NuxtLink to="/SAC" class="text-blue-500 hover:text-blue-600 flex items-center">
          <span class="mr-2">←</span> Voltar ao Acompanhamento de Encomendas
        </NuxtLink>
      </div>

      <div v-if="error" class="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded mb-4">
        {{ error }}
      </div>

      <div v-if="encomenda" class="bg-white rounded-lg shadow-md p-6">
        <div class="border-b pb-4 mb-4">
          <h1 class="text-2xl font-bold text-gray-800">Encomenda #{{ encomenda.encomendaId }}</h1>
          <span class="px-3 py-1 rounded-full text-sm inline-block mt-2" :class="{
            'bg-green-100 text-green-800': encomenda.estado === 'Entregue',
            'bg-yellow-100 text-yellow-800': encomenda.estado === 'Pendente',
            'bg-blue-100 text-blue-800': encomenda.estado === 'Despachada'
          }">
            {{ encomenda.estado }}
          </span>
        </div>

        <div class="space-y-6">
          <!-- Volumes Section -->
          <div>
            <h2 class="text-xl font-semibold mb-3">Volumes</h2>
            <div class="grid gap-4">
              <div v-for="(volume, index) in encomenda.volumes" :key="volume.idVolume"
                class="bg-gray-50 p-4 rounded-lg">
                <div class="flex justify-between items-start">
                    <p class="font-medium text-lg">Volume #{{ index + 1 }}</p>
                    <span class="px-3 py-1 rounded-full text-sm" :class="{
                    'bg-green-100 text-green-800': volume.isEntregue,
                    'bg-yellow-100 text-yellow-800': !volume.isEntregue
                  }">
                    {{ volume.isEntregue ? 'Entregue' : 'Pendente' }}
                  </span>
                </div>
                <div class="flex justify-between items-start mb-4">
                  <div class="mt-2">
                    <p class="font-medium text-md mb-1">Produtos:</p>
                    <ul class="list-disc list-inside text-sm text-gray-600 ml-2">
                      <li v-for="produto in volume.produtos" :key="produto.id">
                        <strong>Nome:</strong> {{ produto.descricao }} - <strong>Quantidade:</strong> {{ produto.quantidade }}
                      </li>
                    </ul>
                  </div>
                </div>
                <div>
                  <div class="mt-2 flex">
                    <p class="font-medium text-md">Sensores:</p>
                  </div>
                  <div>
                    <ul class="list-disc list-inside text-sm text-gray-600 ml-2">
                      <li class="flex items-center justify-between gap-2 mb-2" v-for="sensor in volume.sensores" :key="sensor.id">
                        <div>
                          <span class="font-medium pr-2">{{ getCorrectSensorName(sensor.tipo) }}</span>
                          <span :class="styleStatusBadge(sensor.status)"> {{ sensor.status ? 'Ativo' : 'Inativo' }}</span>
                        </div>
                        <div v-if="sensor.tipo === 'Posicionamento Global' && sensor.eventos.length > 0">
                          <button @click="toggleMapOrChart(sensor.id)"
                                  class="bg-blue-500 text-white px-4 py-2 rounded-md">
                            {{ btnMapOrChartText(marcadores,sensor) }}</button>
                        </div>
                        <div v-else-if="sensor.tipo === 'Temperatura' && sensor.eventos.length > 0">
                          <button @click="toggleMapOrChart(sensor.id)"
                                  class="bg-blue-500 text-white px-4 py-2 rounded-md">
                            {{ btnMapOrChartText(marcadores,sensor) }}</button>
                        </div>
                        <div v-else-if="sensor.tipo === 'Aceleracao' && sensor.eventos.length > 0">
                          <button @click="toggleMapOrChart(sensor.id)"
                                  class="bg-blue-500 text-white px-4 py-2 rounded-md">
                            {{ btnMapOrChartText(marcadores,sensor) }}</button>
                        </div>
                        <div v-else-if="sensor.tipo === 'Pressao' && sensor.eventos.length > 0">
                          <button @click="toggleMapOrChart(sensor.id)"
                                  class="bg-blue-500 text-white px-4 py-2 rounded-md">
                            {{ btnMapOrChartText(marcadores,sensor) }}</button>
                        </div>
                        <div v-else>
                          <p>Sem dados para mostrar</p>
                        </div>
                      </li>
                    </ul>
                  </div>
                </div>
                <div v-for="sensor in volume.sensores">
                  <div id="IMPORTANTE">
                    <div v-if="sensor.tipo === 'Posicionamento Global' && sensor.eventos.length > 0 && marcadores.find(marcador => marcador.sensorid == sensor.id && marcador.showMap !== undefined).showMap">
                      <!-- Map Section -->
                      <!-- TODO: Tornar o mapa responsivo -->
                      <div style="height:60vh; width: 100%;@media (max-width: 1000px) {.sm-h-40vh {height: 400px;}}"
                           class="mt-1">
                        <LMap ref="map"
                              :zoom="marcadores.find(marcador => marcador.sensorid == sensor.id  && marcador.showMap !== undefined).mapZoom"
                              :max-zoom="18"
                              :center="marcadores.find(marcador => marcador.sensorid == sensor.id  && marcador.showMap !== undefined).mapCenter"
                              :use-global-leaflet="false">
                          <LTileLayer url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
                                      attribution="&amp;copy; <a href=&quot;https://www.openstreetmap.org/&quot;>OpenStreetMap</a> contributors"
                                      layer-type="base" name="OpenStreetMap" />
                          <LMarker
                              v-for="(evento, index) in marcadores.find(marcador => marcador.sensorid == sensor.id  && marcador.showMap !== undefined)?.eventos"
                              :lat-lng="[parseFloat(evento.valor.split(',')[0]), parseFloat(evento.valor.split(',')[1])]"
                              :key="evento.timestamp">
                            <LPopup>Data de Registo
                              {{ new Date(evento.timestamp).toLocaleString('pt-PT', {
                                day: '2-digit', month: '2-digit',
                                year: 'numeric', hour: '2-digit', minute: '2-digit', second: '2-digit' }) }}
                            </LPopup>
                          </LMarker>
                          <LPolyline
                              :lat-lngs="marcadores.find(marcador => marcador.sensorid === sensor.id  && marcador.showMap !== undefined)?.eventos.map(evento => ({ lat: parseFloat(evento.valor.split(',')[0]), lng: parseFloat(evento.valor.split(',')[1]) }))">
                          </LPolyline>
                        </LMap>
                      </div>
                    </div>
                    <div style="height:40vh" v-else-if="sensor.tipo === 'Temperatura'  && sensor.eventos.length > 0 && marcadores.find(marcador => marcador.sensorid === sensor.id && marcador.showTemperatureChart !== undefined).showTemperatureChart">
                      <Line
                          :data="marcadores.find(marcador => marcador.sensorid === sensor.id && marcador.showTemperatureChart !== undefined).chartData"
                          :options="chartOptions"
                      />
                    </div>
                    <div v-else-if="sensor.tipo === 'Aceleracao' && sensor.eventos.length > 0 && marcadores.find(marcador => marcador.sensorid === sensor.id && marcador.showAccelerationChart !== undefined).showAccelerationChart">
                      <Line
                          :data="marcadores.find(marcador => marcador.sensorid === sensor.id && marcador.showAccelerationChart !== undefined).chartData"
                          :options="chartOptions"
                      />
                    </div>
                    <div v-else-if="sensor.tipo === 'Pressao' && sensor.eventos.length > 0 && marcadores.find(marcador => marcador.sensorid === sensor.id && marcador.showPressureChart !== undefined).showPressureChart">
                      <Line
                          :data="marcadores.find(marcador => marcador.sensorid === sensor.id && marcador.showPressureChart !== undefined).chartData"
                          :options="chartOptions"
                      />
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <!-- Additional Details -->
          <div class="grid gap-4">
            <div class="bg-gray-50 p-4 rounded-lg">
              <h3 class="font-semibold mb-2">Total de Volumes</h3>
              <p>{{ encomenda.volumes.length }}</p>
            </div>
          </div>
        </div>
      </div>

      <div v-else-if="!error" class="flex justify-center items-center h-64">
        <div class="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-500"></div>
      </div>
    </div>
  </div>

</template>