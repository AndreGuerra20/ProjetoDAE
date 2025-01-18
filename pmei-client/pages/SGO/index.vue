<script setup>
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router';

import { useAuthStore } from '~/store/auth-store';
const config = useRuntimeConfig()
const api = config.public.API_URL
const googleMapsApiKey = config.public.GOOGLE_MAPS_API_KEY

const authStore = useAuthStore()
const route = useRoute()
const router = useRouter()
const error = ref(null)
const sensors = ref(null)
const orders = ref(null)
const token = ref(null)
const sensorsSize = ref(null)
const idAProcurar = ref(null)
const volumes = ref(null)
const tipos = ref(null)
const leituras = ref()

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
    sensors.value = await $fetch(`${api}/sensores`, {
      headers: {
        Authorization: `Bearer ${token.value}`
      }
    })
    tipos.value = sensors.value.map(sensor => sensor.tipo)
    tipos.value = [...new Set(tipos.value)]

    //filter sensors
    // sensors.value = sensors.value.filter(sensor => sensor.status === true)

    orders.value = await $fetch(`${api}/encomendas`, {
      headers: {
        Authorization: `Bearer ${token.value}`
      }
    })

  } catch (err) {
    console.error('Error fetching encomenda details:', err)
    error.value = 'Não foi possível carregar os detalhes da encomenda, tente novamente mais tarde.'
  }
}

const getVolumeStatus = (status) => {
  return status === true ? 'Sim' : 'Não';
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
    await $fetch(`${api}/sensores/${idAProcurar.value}/eventoRecente`, {
      headers: {
        Authorization: `Bearer ${token.value}`
      },
      onResponse({ response }) {
        if (response.status == 200) {
          error.value = null
          lastReading.id = response._data.sensorId
          lastReading.ultimaLeitura = response._data.valor
          lastReading.timestamp = response._data.timestamp
          $fetch(`${api}/sensores/${idAProcurar.value}`, {
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

const getVolumes = async () => {
  try {
    volumes.value = await $fetch(`${api}/volumes/`, {
      headers: {
        Authorization: `Bearer ${token.value}`
      }
    })
  } catch (err) {
    console.error('Error fetching volumes:', err)
  }
}

const getColor = (length) => {
  return length === 0 ? 'px-6 py-4 whitespace-nowrap text-yellow-500' : 'px-6 py-4 whitespace-nowrap'
}

const getLeituras = async () => {
  leituras.value = null;
  if (!formData.selectedTipo) {
    return;
  }
  try {
    await $fetch(`${api}/sensores/tipo/${formData.selectedTipo}`, {
      headers: {
        Authorization: `Bearer ${token.value}`
      },
      async onResponse({ response }) {
        if (response.status === 200) {
          leituras.value = response._data;
        }
      }
    });
  } catch (err) {
    console.error('Error fetching readings:', err);
    error.encomendas = 'Não foi possível carregar as leituras, por favor tente novamente.';
  }
};

const formData = reactive({
  selectedTipo: ''
})

onMounted(async () => {
  if (authStore.token) {
    await fetch()
    sensorsSize.value = sensors.value.length
  }
  await getVolumes()
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

      <div class="flex gap-2 justify-end mb-4">
        <NuxtLink to="/SGO/clients" class="px-4 py-2 bg-blue-500 text-white rounded-lg">Ver Clientes</NuxtLink>
      </div>

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
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Ver Sensores</th>
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
                  <NuxtLink :to="`/SGO/orders/${order.encomendaId}/sensors`" class="text-blue-500 hover:text-blue-600">
                    +
                  </NuxtLink>
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

      <!-- Volumes -->
      <div class="bg-white rounded-lg shadow-md p-4 mb-6">
        <div class="flex justify-between items-center mb-4">
          <h2 class="text-xl font-semibold">Volumes</h2>
        </div>

        <div class="overflow-x-auto">
          <table class="min-w-full">
            <thead>
              <tr class="bg-gray-50">
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">ID do Volume
                </th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Tipo da
                  Embalagem
                </th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase
                  tracking-wider">Entregue</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Ver Sensores</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Ver Produtos</th>
              </tr>
            </thead>
            <tbody class="bg-white divide-y divide-gray-200">
              <tr v-for="volume in volumes" :key="volume.id">
                <td class="px-6 py-4 whitespace-nowrap">{{ volume.idVolume }}</td>
                <td class="px-6 py-4 whitespace-nowrap" :class="getColor(volume.tipoEmbalagem.length)">{{ volume.tipoEmbalagem.length > 0 ? volume.tipoEmbalagem : 'Sem tipo de embalagem definido' }}</td>
                <td class="px-6 py-4 whitespace-nowrap">
                  <span :class="styleStatusBadge(volume.entregue)">
                    {{ getVolumeStatus(volume.entregue) }}
                  </span>
                </td>
                <td class="px-6 py-4 whitespace-nowrap">
                  <NuxtLink :to="`/SGO/volumes/${volume.idVolume}`" class="text-blue-500 hover:text-blue-600">
                    +
                  </NuxtLink>
                </td>
                <td class="px-6 py-4 whitespace-nowrap">
                  <NuxtLink :to="`/SGO/volumes/${volume.idVolume}/produtos`" class="text-blue-500 hover:text-blue-600">
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

      <div class="bg-white rounded-lg shadow-md p-4 mt-5">
        <div class="space-y-4">
          <form>
            <div class="flex justify-between items-center mb-2">
              <p class="font-medium text-lg">Visualizar últimas leituras</p>
            </div>
            <select v-model="formData.selectedTipo" @change="getLeituras"
                    class="w-full bg-white border border-gray-300 rounded-lg px-3 py-2 focus:outline-none focus:border-blue-500">
              <option value="" selected>Selecione um tipo</option>
              <option v-for="tipo in tipos" :value="tipo">{{ tipo }}</option>
            </select>
          </form>
        </div>
        <div>
          <div v-if="leituras" class="mt-4 p-3 bg-gray-50 rounded-lg">
            <div class="text-sm mb-3">
              <table class="min-w-full">
                <thead>
                <tr class="bg-gray-50">
                  <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">ID Sensor
                  </th>
                  <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">ID Encomenda</th>
                  <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">ID Volume
                  </th>
                  <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Última
                    Leitura</th>
                  <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Timestamp
                  </th>
                </tr>
                </thead>
                <tbody class="bg-white divide-y divide-gray-200">
                <tr v-for="eachleitura in leituras" :key="eachleitura.idSensor">
                  <td class="px-6 py-4 whitespace-nowrap">{{ eachleitura.sensorId }}</td>
                  <td class="px-6 py-4 whitespace-nowrap">{{ eachleitura.idEncomenda }}</td>
                  <td class="px-6 py-4 whitespace-nowrap">{{ eachleitura.idVolume }}</td>
                  <td class="px-6 py-4 whitespace-nowrap">{{ eachleitura.valor }}
                    {{ getMeasureText(formData.selectedTipo) }}</td>
                  <td class="px-6 py-4 whitespace-nowrap">{{ formatDate(eachleitura.timestamp) }}</td>
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