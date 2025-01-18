<script setup>
import { ref, onMounted } from 'vue'

import { useAuthStore } from "~/store/auth-store.js"
import { useRouter } from 'vue-router';
const router = useRouter()

const config = useRuntimeConfig()
const api = config.public.API_URL
const authStore = useAuthStore()

const { token, user, role } = storeToRefs(authStore)
const error = ref(null)

const messages = ref([])
const encomendas = ref([])
const volumesPorEntregar = ref([])
const encomendasComVolumesPorEntregar = ref(new Set)
const eventos = ref([])

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
        if (response.status == 200) {
          encomendas.value = response._data
          for (let encomenda of encomendas.value) {
            for (let volume of encomenda.volumes) {
              for (let sensor of volume.sensores) {
                if (sensor.tipo === 'Posicionamento Global' && sensor.eventos.length > 0) {
                  sensor.color = getRandomHex()
                  eventos.value = eventos.value.concat(sensor.eventos)
                }
              }
            }
          }
        }
        console.log('Encomendas:', encomendas.value)
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
    encomendas.value.forEach(encomenda => {
      encomenda.volumes.forEach(volume => {
        if (!volume.entregue) {
          volumesPorEntregar.value.push(volume)
          encomendasComVolumesPorEntregar.value.add(encomenda)
        }
      });
    });
    console.log('Volumes por entregar:', volumesPorEntregar.value)
    console.log('Encomendas com volumes por entregar:', encomendasComVolumesPorEntregar.value)
  }
})

onBeforeMount(() => {
  if (!authStore.token) {
    authStore.loadUser()
  }
  if (!authStore.user) {
    router.push('/auth/login')
  }
  if (authStore.role !== 'Logistica') {
    router.push('/auth/login')
  }
  token.value = authStore.token
})

function getRandomHex() {
  var letters = '0123456789ABCDEF'.split('');
  var color = '#';
  for (var i = 0; i < 6; i++) {
    color += letters[Math.floor(Math.random() * 16)];
  }
  return color;
}

watch(encomendas, async (newValue) => {
  volumesPorEntregar.value = []
  encomendasComVolumesPorEntregar.value.clear()
  newValue.forEach(encomenda => {
    encomenda.volumes.forEach(volume => {
      if (!volume.entregue) {
        volumesPorEntregar.value.push(volume)
        encomendasComVolumesPorEntregar.value.add(encomenda)
      }
    });
  });
})

const entregarVolume = async (id) => {
  try {
    await $fetch(`${api}/volumes/${id}`, {
      method: 'PATCH',
      headers: {
        Authorization: `Bearer ${token.value}`
      },
      body: JSON.stringify({ estado: "Entregue" }),
    });
    fetchEncomendas()
  } catch (err) {
    console.error('Error delivering volume:', err);
    error.value = 'Não foi possível entregar o volume, por favor tente novamente.';
  }
}

function calculateCenter(eventos) {
  const latitudes = eventos.map(evento => parseFloat(evento.valor.split(',')[0]))
  const longitudes = eventos.map(evento => parseFloat(evento.valor.split(',')[1]))
  const lat = (Math.min(...latitudes) + Math.max(...latitudes)) / 2
  const lng = (Math.min(...longitudes) + Math.max(...longitudes)) / 2
  if (isNaN(lat) || isNaN(lng)) {
    return [38.7223, -9.1393]
  }
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

function volumeHasGPS(volume) {
  return volume.sensores.find(sensor => sensor.tipo === 'Posicionamento Global' && sensor.eventos.length > 0)
}
function encomendaHasGPS(encomenda) {
  return encomenda.volumes.find(volume => volumeHasGPS(volume))
}
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

      <div class="bg-white rounded-lg shadow-md p-4 mb-6">
        <div class="flex justify-between items-center mb-4">
          <h2 class="text-xl font-semibold">Encomendas Atuais</h2>
          <div class="flex gap-2">
            <NuxtLink to="/SDL/addVolume" class="px-4 py-2 bg-blue-500 text-white rounded-lg">Adicionar Volume
            </NuxtLink>
            <NuxtLink to="/SDL/create" class="px-4 py-2 bg-blue-500 text-white rounded-lg">Nova Encomenda</NuxtLink>
          </div>
        </div>

        <div class="overflow-x-auto">
          <table v-if="encomendas && encomendas.filter(encomenda => encomenda.estado !== 'Entregue').length > 0"
            class="min-w-full">
            <thead>
              <tr class="bg-gray-50">
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">ID</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Estado</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Volumes</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Legenda Rotas
                  no Mapa</th>
              </tr>
            </thead>
            <tbody class="bg-white divide-y divide-gray-200">
              <tr v-for="encomenda in encomendas.filter(encomenda => encomenda.estado !== 'Entregue')"
                :key="encomenda.encomendaId">
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
                <td>
                  <table v-if="encomendaHasGPS(encomenda)">
                    <thead>
                      <tr>
                        <th class="text-right px-6 py-3  text-xs font-medium text-gray-500 uppercase tracking-wider">
                          Volume</th>
                        <th class="text-center px-6 py-3  text-xs font-medium text-gray-500 uppercase tracking-wider">
                          Cores</th>
                      </tr>
                    </thead>
                    <tbody class="bg-gray-50 divide-y divide-gray-200 ">
                      <tr v-for="volume in encomenda.volumes.filter(volume => volumeHasGPS(volume))">
                        <td class="text-center pl-1">{{ volume.idVolume }}</td>
                        <td class="text-center">
                          <div
                            v-for="sensor in volume.sensores.filter(sensor => sensor.tipo === 'Posicionamento Global' && sensor.eventos.length > 0)"
                            class="inline-flex">
                            <div class="mt-1 w-4 h-4 rounded-full mr-2" :style="{ backgroundColor: sensor.color }">
                            </div>
                          </div>
                        </td>
                      </tr>
                    </tbody>
                  </table>
                </td>
              </tr>
            </tbody>
          </table>
          <div v-else>
            <p>Não existem encomendas pendentes para mostrar.</p>
          </div>
        </div>
      </div>

      <div class="bg-white rounded-lg shadow-md p-4">
        <h2 class="text-xl font-semibold mb-4">Mapa das Rotas</h2>
        <div style="height:60vh; width: 100%;@media (max-width: 1000px) {.sm-h-40vh {height: 400px;}}" class="mt-1">
          <LMap ref="map" :zoom="calculateZoom(eventos)" :max-zoom="18" :min-zoom="3" :center="calculateCenter(eventos)"
            :use-global-leaflet="false">
            <LTileLayer url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
              attribution="&amp;copy; <a href=&quot;https://www.openstreetmap.org/&quot;>OpenStreetMap</a> contributors"
              layer-type="base" name="OpenStreetMap" />
            <div v-for="encomenda in encomendas">
              <div v-for="volume in encomenda.volumes">
                <div
                  v-for="sensor in volume.sensores.filter(sensor => sensor.tipo === 'Posicionamento Global' && sensor.eventos.length > 0)">
                  <LMarker :lat-lng="[
                    parseFloat(sensor.eventos[0].valor.split(',')[0]),
                    parseFloat(sensor.eventos[0].valor.split(',')[1])
                  ]" :key="sensor.eventos[0].timestamp">
                    <LPopup>Primeiro evento registado pelo sensor de GPS {{ sensor.id }} do volume {{ volume.idVolume }}
                      da encomenda {{ encomenda.encomendaId }} na data {{ new
                        Date(sensor.eventos[0].timestamp).toLocaleString('pt-PT', {
                          day: '2-digit', month: '2-digit',
                          year: 'numeric', hour: '2-digit', minute: '2-digit', second: '2-digit' }) }}
                    </LPopup>
                  </LMarker>
                  <LMarker :lat-lng="[
                    parseFloat(sensor.eventos[sensor.eventos.length - 1].valor.split(',')[0]),
                    parseFloat(sensor.eventos[sensor.eventos.length - 1].valor.split(',')[1])
                  ]" :key="sensor.eventos[0].valor.timestamp">
                    <LPopup>Último evento registado pelo sensor de GPS {{ sensor.id }} do volume {{ volume.idVolume }}
                      da encomenda {{ encomenda.encomendaId }} na data {{ new Date(sensor.eventos[sensor.eventos.length -
                        1].timestamp).toLocaleString('pt-PT', {
                          day: '2-digit', month: '2-digit',
                          year: 'numeric', hour: '2-digit', minute: '2-digit', second: '2-digit' }) }}
                    </LPopup>
                  </LMarker>
                  <LPolyline
                    :lat-lngs="sensor.eventos.map(evento => ({ lat: parseFloat(evento.valor.split(',')[0]), lng: parseFloat(evento.valor.split(',')[1]) }))"
                    :color="sensor.color"></LPolyline>
                </div>
              </div>
            </div>
          </LMap>
        </div>
      </div>
      <!-- Volumes por entregar -->
      <div class="bg-white rounded-lg shadow-md p-4 mt-6">
        <h2 class="text-xl font-semibold mb-4">Encomendas com Volumes por Entregar</h2>
        <div v-if="encomendasComVolumesPorEntregar && encomendasComVolumesPorEntregar.size > 0" class="overflow-x-auto">
          <table class="min-w-full">
            <thead>
              <tr class="bg-gray-50">
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">ID encomenda
                </th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Volumes por
                  entregar</th>
              </tr>
            </thead>
            <tbody class="bg-white divide-y divide-gray-200">
              <tr v-for="encomenda of encomendasComVolumesPorEntregar" :key="encomenda.encomendaId">
                <td class="px-6 py-4 whitespace-nowrap">{{ encomenda.encomendaId }}</td>
                <td class="px-6 py-4 whitespace-nowrap">
                  <table>
                    <thead>
                      <tr>
                        <th class="text-right px-6 py-3  text-xs font-medium text-gray-500 uppercase tracking-wider">
                          Volume</th>
                        <th class="text-center px-6 py-3  text-xs font-medium text-gray-500 uppercase tracking-wider">
                          Ação</th>
                      </tr>
                    </thead>
                    <tbody class="bg-gray-50 divide-y divide-gray-200 ">
                      <tr v-for="volume in encomenda.volumes.filter(volume => !volume.entregue)">
                        <td class="text-center pl-1">{{ volume.idVolume }}</td>
                        <td class="text-center">
                          <button @click="entregarVolume(volume.idVolume)"
                            class="px-4 py-2 bg-blue-500 text-white rounded-lg">Entregar</button>
                        </td>
                      </tr>
                    </tbody>
                  </table>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
        <div v-else>
          <p>Não existem volumes por entregar.</p>
        </div>
      </div>
    </div>
  </div>
</template>