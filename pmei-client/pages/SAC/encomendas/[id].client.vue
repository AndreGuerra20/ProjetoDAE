<script setup>
import {onMounted, ref} from 'vue'
import L from 'leaflet'
const route = useRoute()
const error = ref(null)
const encomenda = ref(null)
const token = ref(null)
const config = useRuntimeConfig()
const api = config.public.API_URL
const marcadores = ref([])
const showMapText = ref('Show Map')

async function fetchEncomendaDetails() {
    error.value = null;
    try {
        // First get the authentication token
        const authResponse = await $fetch(`${api}/auth/login`, {
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

        // Then fetch the encomenda details
        const response = await $fetch(`${api}/encomenda/${route.params.id}`, {
            headers: {
                Authorization: `Bearer ${token.value}`
            }
        })
        encomenda.value = response
    } catch (err) {
        console.error('Error fetching encomenda details:', err)
        error.value = 'Failed to load encomenda details.'
    }
    for (const volume of encomenda.value.volumes) {
        for (const sensor of volume.sensores) {
            if(sensor.tipo === 'Posicionamento Global') {
                const json = JSON.parse(`{"volumeId": ${volume.idVolume}, "sensorid": ${sensor.id}}`)
                marcadores.value.push(json);
                try {
                    const response = await $fetch(`${api}/sensor/${sensor.id}/eventos`, {
                        headers: {
                            Authorization: `Bearer ${token.value}`
                        }
                    })
                  if (response !== undefined) {
                    marcadores.value.find(marcador => marcador.sensorid === sensor.id).eventos = response
                    marcadores.value.find(marcador => marcador.sensorid === sensor.id).ultimoEvento = response[response.length - 1]
                    marcadores.value.find(marcador => marcador.sensorid === sensor.id).showMap = false
                  }else{
                    marcadores.value.find(marcador => marcador.sensorid === sensor.id).eventos = []
                    marcadores.value.find(marcador => marcador.sensorid === sensor.id).ultimoEvento = null
                  }
                } catch (err) {
                    console.error('Error fetching sensor locations:', err)
                    error.value = 'Failed to load sensor locations.'
                }
            }
        }
    }
}

function volumeHasSensor(volumeId) {
    return marcadores.value.find(marcador => marcador.volumeId === volumeId).eventos.length > 0
}

//Dá erro mas funciona B) (Nuxt é horrível)
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
    marcadores.value.find(marcador => marcador.volumeId === volumeId).showMap = !marcadores.value.find(marcador => marcador.volumeId === volumeId).showMap
}

const map = ref(null);

onMounted(async () => {
    await fetchEncomendaDetails()
})
</script>

<template>
  <ClientOnly>
    <div class="min-h-screen bg-gray-100 p-4">
        <div class="max-w-4xl mx-auto">
            <div class="mb-6">
                <NuxtLink to="/SAC" class="text-blue-500 hover:text-blue-600 flex items-center">
                    <span class="mr-2">←</span> Back to Package Tracking
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
                                                    {{ sensor.tipo }} - Status: {{ sensor.status }}
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
                                <!-- Map Section -->
                                <!-- TODO: Tornar o mapa responsivo -->
                                <div>
                                  <button @click="toggleMap(volume.idVolume)" class="bg-blue-500 text-white px-4 py-2 rounded-md mt-2">
                                    {{ marcadores.find(marcador => marcador.volumeId === volume.idVolume).showMap ? 'Hide Map' : 'Show Map' }}</button>
                                </div>
                                <div style="height:60vh; width: 100%;@media (max-width: 1000px) {.sm-h-40vh {height: 400px;}}" class="mt-1" v-if="volumeHasSensor(volume.idVolume) && marcadores.find(marcador => marcador.volumeId === volume.idVolume).showMap">
                                  <LMap
                                      ref="map"
                                      :zoom="calculateZoom(marcadores.find(marcador => marcador.volumeId === volume.idVolume).eventos)"
                                      :max-zoom="18"
                                      :center="calculateCenter(marcadores.find(marcador => marcador.volumeId === volume.idVolume).eventos)"
                                      :use-global-leaflet="true"
                                      @ready="onMapReady"
                                  >
                                    <LTileLayer
                                        url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
                                        attribution="&amp;copy; <a href=&quot;https://www.openstreetmap.org/&quot;>OpenStreetMap</a> contributors"
                                        layer-type="base"
                                        name="OpenStreetMap"
                                    />
                                    <LMarker v-for="(evento,index) in marcadores.find(marcador => marcador.volumeId === volume.idVolume)?.eventos" :lat-lng="[parseFloat(evento.valor.split(',')[0]), parseFloat(evento.valor.split(',')[1])]" :key="evento.timestamp">
                                      <LPopup>Data de Registo
                                        {{ new Date(evento.timestamp).toLocaleString('pt-PT', { day: '2-digit', month: '2-digit', year: 'numeric', hour: '2-digit', minute: '2-digit', second: '2-digit' }) }}
                                      </LPopup>
                                    </LMarker>
                                    <LPolyline :lat-lngs="marcadores.find(marcador => marcador.volumeId === volume.idVolume)?.eventos.map(evento => ({ lat: parseFloat(evento.valor.split(',')[0]), lng: parseFloat(evento.valor.split(',')[1]) }))"></LPolyline>
                                  </LMap>
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
  </ClientOnly>
</template>