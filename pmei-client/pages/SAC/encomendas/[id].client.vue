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

function volumeHasSensor(volumeId) {
  const marcador = marcadores.value.find(marcador => marcador.volumeId === volumeId);

  if (marcador) {
    const neventos = marcador.eventos;
    return !!(neventos && neventos.length > 0);
  } else {
    return false;
  }
}

function volumeHasSensorGPS(volumeId) {
  const marcador = marcadores.value.find(marcador => marcador.volumeId === volumeId && marcador.showMap !== undefined);
  if (marcador) {
    const neventos = marcador.eventos;
    return !!(neventos && neventos.length > 0);
  } else {
    return false;
  }
}

function volumeHasSensorTemperature(volumeId) {
  const marcador = marcadores.value.find(marcador => marcador.volumeId === volumeId && marcador.showTemperatureChart !== undefined);
  if (marcador) {
    const neventos = marcador.eventos;
    return !!(neventos && neventos.length > 0);
  } else {
    return false;
  }
}

function volumeHasSensorAcceleration(volumeId) {
  const marcador = marcadores.value.find(marcador => marcador.volumeId === volumeId && marcador.showAccelerationChart !== undefined);
  if (marcador) {
    const neventos = marcador.eventos;
    return !!(neventos && neventos.length > 0);
  } else {
    return false;
  }
}

function volumeHasSensorPressure(volumeId) {
  const marcador = marcadores.value.find(marcador => marcador.volumeId === volumeId && marcador.showPressureChart !== undefined);
  if (marcador) {
    const neventos = marcador.eventos;
    return !!(neventos && neventos.length > 0);
  } else {
    return false;
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

function toggleMap(volumeId) {
  if (marcadores.value.find(marcador => marcador.volumeId === volumeId && marcador.showMap !== undefined)) {
    marcadores.value.find(marcador => marcador.volumeId === volumeId && marcador.showMap !== undefined).showMap =
        !marcadores.value.find(marcador => marcador.volumeId === volumeId && marcador.showMap !== undefined).showMap
  }
}

const btnMapText = (marcadores, volume) => {
  const marcador = marcadores.find(marcador => marcador.volumeId === volume.idVolume && marcador.showMap !== undefined)
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
    return '';
  }
};

function toggleTemperatureChart(volumeId) {
  if (marcadores.value.find(marcador => marcador.volumeId === volumeId && marcador.showTemperatureChart !== undefined)) {
    marcadores.value.find(marcador => marcador.volumeId === volumeId && marcador.showTemperatureChart !== undefined).showTemperatureChart =
        !marcadores.value.find(marcador => marcador.volumeId === volumeId && marcador.showTemperatureChart !== undefined).showTemperatureChart
  }
}

const btnTemperatureText = (marcadores, volume) => {
  const marcador = marcadores.find(marcador => marcador.volumeId === volume.idVolume && marcador.showTemperatureChart !== undefined)
  if (marcador) {
    if (marcador.hasOwnProperty('showTemperatureChart')) {
      const showMap = marcador.showTemperatureChart
      if (showMap) {
        return 'Esconder Gráfico Temperatura'
      } else {
        return 'Mostrar Gráfico Temperatura'
      }
    } else {
      return '';
    }
  } else {
    return '';
  }
};

function toggleAccelerationChart(volumeId) {
  if (marcadores.value.find(marcador => marcador.volumeId === volumeId && marcador.showAccelerationChart !== undefined)) {
    marcadores.value.find(marcador => marcador.volumeId === volumeId && marcador.showAccelerationChart !== undefined).showAccelerationChart =
        !marcadores.value.find(marcador => marcador.volumeId === volumeId && marcador.showAccelerationChart !== undefined).showAccelerationChart
  }
}

const btnAccelerationText = (marcadores, volume) => {
  const marcador = marcadores.find(marcador => marcador.volumeId === volume.idVolume && marcador.showAccelerationChart !== undefined)
  if (marcador) {
    if (marcador.hasOwnProperty('showAccelerationChart')) {
      const showMap = marcador.showAccelerationChart
      if (showMap) {
        return 'Esconder Gráfico Aceleração'
      } else {
        return 'Mostrar Gráfico Aceleração'
      }
    } else {
      return '';
    }
  } else {
    return '';
  }
};

function togglePressureChart(volumeId) {
  if (marcadores.value.find(marcador => marcador.volumeId === volumeId && marcador.showPressureChart !== undefined)) {
    marcadores.value.find(marcador => marcador.volumeId === volumeId && marcador.showPressureChart !== undefined).showPressureChart =
        !marcadores.value.find(marcador => marcador.volumeId === volumeId && marcador.showPressureChart !== undefined).showPressureChart
  }
}

function btnPressureText(marcadores, volume) {
  const marcador = marcadores.find(marcador => marcador.volumeId === volume.idVolume && marcador.showPressureChart !== undefined)
  if (marcador) {
    if (marcador.hasOwnProperty('showPressureChart')) {
      const showMap = marcador.showPressureChart
      if (showMap) {
        return 'Esconder Gráfico Pressão'
      } else {
        return 'Mostrar Gráfico Pressão'
      }
    } else {
      return '';
    }
  } else {
    return '';
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
                  <div>
                    <p class="font-medium">Volume #{{ index + 1 }}</p>
                    <div class="mt-2">
                      <p class="font-medium text-sm">Produtos:</p>
                      <ul class="list-disc list-inside text-sm text-gray-600 ml-2">
                        <li v-for="produto in volume.produtos" :key="produto.id">
                          ID: {{ produto.id }} - Quantidade: {{ produto.quantidade }}
                        </li>
                      </ul>
                    </div>
                    <div class="mt-2">
                      <p class="font-medium text-sm">Sensores:</p>
                      <ul class="list-disc list-inside text-sm text-gray-600 ml-2">
                        <li v-for="sensor in volume.sensores" :key="sensor.id">
                          {{ sensor.tipo }} - {{ sensor.status ? 'Ativo' : 'Inativo' }}
                        </li>
                      </ul>
                    </div>
                  </div>
                  <span class="px-3 py-1 rounded-full text-sm" :class="{
                    'bg-green-100 text-green-800': volume.isEntregue,
                    'bg-yellow-100 text-yellow-800': !volume.isEntregue
                  }">
                    {{ volume.isEntregue ? 'Entregue' : 'Pendente' }}
                  </span>
                </div>
                <div class="w-full flex items-center gap-1.5">
                  <div v-if="volumeHasSensorGPS(volume.idVolume)">
                    <button @click="toggleMap(volume.idVolume)"
                            class="bg-blue-500 text-white px-4 py-2 rounded-md mt-2">
                      {{ btnMapText(marcadores, volume) }}</button>
                  </div>
                  <div v-if="volumeHasSensorTemperature(volume.idVolume)">
                    <button @click="toggleTemperatureChart(volume.idVolume)"
                            class="bg-blue-500 text-white px-4 py-2 rounded-md mt-2">
                      {{ btnTemperatureText(marcadores, volume) }}</button>
                  </div>
                  <div v-if="volumeHasSensorAcceleration(volume.idVolume)">
                    <button @click="toggleAccelerationChart(volume.idVolume)"
                            class="bg-blue-500 text-white px-4 py-2 rounded-md mt-2">
                      {{ btnAccelerationText(marcadores, volume) }}</button>
                  </div>
                  <div v-if="volumeHasSensorPressure(volume.idVolume)">
                    <button @click="togglePressureChart(volume.idVolume)"
                            class="bg-blue-500 text-white px-4 py-2 rounded-md mt-2">
                      {{ btnPressureText(marcadores, volume) }}</button>
                  </div>
                </div>
                <div v-if="volumeHasSensor(volume.idVolume)">
                  <div v-if="volumeHasSensorGPS(volume.idVolume)">
                    <!-- Map Section -->
                    <!-- TODO: Tornar o mapa responsivo -->
                    <div style="height:60vh; width: 100%;@media (max-width: 1000px) {.sm-h-40vh {height: 400px;}}"
                      class="mt-1"
                      v-if="marcadores.find(marcador => marcador.volumeId === volume.idVolume && marcador.showMap !== undefined).showMap">
                      <LMap ref="map"
                        :zoom="calculateZoom(marcadores.find(marcador => marcador.volumeId === volume.idVolume  && marcador.showMap !== undefined).eventos)"
                        :max-zoom="18"
                        :center="calculateCenter(marcadores.find(marcador => marcador.volumeId === volume.idVolume  && marcador.showMap !== undefined).eventos)"
                        :use-global-leaflet="false">
                        <LTileLayer url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
                          attribution="&amp;copy; <a href=&quot;https://www.openstreetmap.org/&quot;>OpenStreetMap</a> contributors"
                          layer-type="base" name="OpenStreetMap" />
                        <LMarker
                          v-for="(evento, index) in marcadores.find(marcador => marcador.volumeId === volume.idVolume  && marcador.showMap !== undefined)?.eventos"
                          :lat-lng="[parseFloat(evento.valor.split(',')[0]), parseFloat(evento.valor.split(',')[1])]"
                          :key="evento.timestamp">
                          <LPopup>Data de Registo
                            {{ new Date(evento.timestamp).toLocaleString('pt-PT', {
                              day: '2-digit', month: '2-digit',
                              year: 'numeric', hour: '2-digit', minute: '2-digit', second: '2-digit' }) }}
                          </LPopup>
                        </LMarker>
                        <LPolyline
                          :lat-lngs="marcadores.find(marcador => marcador.volumeId === volume.idVolume  && marcador.showMap !== undefined)?.eventos.map(evento => ({ lat: parseFloat(evento.valor.split(',')[0]), lng: parseFloat(evento.valor.split(',')[1]) }))">
                        </LPolyline>
                      </LMap>
                    </div>
                  </div>
                  <div v-if="volumeHasSensorTemperature(volume.idVolume) && marcadores.find(marcador => marcador.volumeId === volume.idVolume && marcador.showTemperatureChart !== undefined).showTemperatureChart">
                    <Line
                        :data="marcadores.find(marcador => marcador.volumeId === volume.idVolume && marcador.showTemperatureChart !== undefined).chartData"
                        :options="chartOptions"
                    />
                  </div>
                  <div v-if="volumeHasSensorAcceleration(volume.idVolume) && marcadores.find(marcador => marcador.volumeId === volume.idVolume && marcador.showAccelerationChart !== undefined).showAccelerationChart">
                    <Line
                        :data="marcadores.find(marcador => marcador.volumeId === volume.idVolume && marcador.showAccelerationChart !== undefined).chartData"
                        :options="chartOptions"
                    />
                  </div>
                  <div v-if="volumeHasSensorPressure(volume.idVolume) && marcadores.find(marcador => marcador.volumeId === volume.idVolume && marcador.showPressureChart !== undefined).showPressureChart">
                    <Line
                        :data="marcadores.find(marcador => marcador.volumeId === volume.idVolume && marcador.showPressureChart !== undefined).chartData"
                        :options="chartOptions"
                    />
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