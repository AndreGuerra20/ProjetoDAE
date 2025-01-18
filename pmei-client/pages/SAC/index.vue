<script setup>
import { ref, onMounted } from 'vue'
import { useAuthStore } from "~/store/auth-store.js"
import { useRouter } from 'vue-router';

const router = useRouter()
const config = useRuntimeConfig()
const api = config.public.API_URL

const authStore = useAuthStore()
const { token, user } = storeToRefs(authStore)

const tipos = ref(null)

const error = reactive({
  encomendas: '',
  leituras: ''
})

const messages = ref([])
const encomendas = ref([])
const leituras = ref()

let username = null

const formData = reactive({
  selectedTipo: ''
})

async function fetchEncomendas() {
  try {
    await $fetch(`${api}/encomendas/cliente`, {
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
        if (response.status === 200) {
          encomendas.value = response._data
        }
      }
    });
  } catch (err) {
    console.error('Error fetching encomendas:', err);
    error.leituras = 'Não foi possível carregar as encomendas, por favor tente novamente.';
  }
}

const encomendasHaveAnySensores = computed(() => {
  if (!encomendas.value && encomendas.value.length === 0) {
    return false
  }
  return encomendas.value.some(encomenda => encomenda.volumes.some(volume => volume.sensores.length > 0))
})

const encomendaSensoresHaveAnyEvents = computed(() => {
  if (!encomendas.value && encomendas.value.length === 0) {
    return false
  }
  return encomendas.value.some(encomenda => encomenda.volumes.some(volume => volume.sensores.some(sensor => sensor.eventos.length > 0)))
})

async function getTypes() {
  tipos.value = null
  try {
    await $fetch(`${api}/sensores/cliente/${username}/tipossensores`, {
      headers: {
        Authorization: `Bearer ${token.value}`
      },
      onResponse({ response }) {
        if (response.status === 200) {
          tipos.value = response._data
        }
      }
    });
  } catch (err) {
    console.error('Error fetching types:', err);
  }
}

const getLeituras = async () => {
  leituras.value = null
  if (!formData.selectedTipo) {
    return
  }
  try {
    await $fetch(`${api}/sensores/cliente/${username}/${formData.selectedTipo}`, {
      headers: {
        Authorization: `Bearer ${token.value}`
      },
      onResponse({ response }) {
        if (response.status === 200) {
          leituras.value = response._data
          console.log(leituras.value)
        }
      }
    });
  } catch (err) {
    console.error('Error fetching readings:', err);
    error.encomendas = 'Não foi possível carregar as leituras, por favor tente novamente.';
  }
}

const getCurrentUser = async () => {
  try {
    await $fetch(`${api}/auth/user`, {
      headers: {
        Authorization: `Bearer ${token.value}`
      },
      onResponse({ response }) {
        if (response.status === 200) {
          username = response._data.username
        }
      }
    })
  } catch (err) {
    console.error('Error fetching user:', err);
  }
}

onMounted(async () => {
  if (authStore.token) {
    await fetchEncomendas()
    await getTypes()
  }
})

onBeforeMount(async () => {
  if (!authStore.token) {
    authStore.loadUser()
  }
  if (!authStore.user) {
    router.push('/auth/login')
  }
  if (authStore.role !== 'Cliente') {
    router.push('/auth/login')
  }
  token.value = authStore.token
  await getCurrentUser()
})
</script>
<template>
  <div class="min-h-screen bg-gray-100 p-4">
    <div class="max-w-4xl mx-auto">
      <h1 class="text-2xl md:text-3xl font-bold mb-6 text-gray-800">Acompanhar Encomenda</h1>

      <div class="bg-white rounded-lg shadow-md p-4">
        <h2 class="text-xl font-semibold mb-4">Encomendas Recentes</h2>

        <div v-if="error.encomendas" class="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded mt-4">
          {{ error.encomendas }}
        </div>

        <div class="space-y-4">
          <div v-if="encomendas && encomendas.length > 0" v-for="encomenda in encomendas"
            class="border-l-4 border-blue-600 pl-4 py-2">
            <NuxtLink :to="`/SAC/encomendas/${encomenda.encomendaId}`">
              <div class="flex justify-between items-center">
                <div>
                  <p class="font-medium text-lg">Encomenda #{{ encomenda.encomendaId }}</p>
                  <p class="text-m text-gray-600">No. de Volumes - {{ encomenda.volumes.length }}</p>
                </div>
                <span class="px-3 py-1 rounded-full text-sm"
                  :class="{ 'bg-green-100 text-green-800': encomenda.estado === 'Entregue', 'bg-yellow-100 text-yellow-800': encomenda.estado === 'Pendente', 'bg-blue-100 text-blue-800': encomenda.estado === 'Despachada' }">
                  {{ encomenda.estado }}
                </span>
              </div>
            </NuxtLink>
          </div>
          <div v-else>
            <p class="text-gray-600">Não existem encomendas disponíveis.</p>
          </div>
        </div>
      </div>

      <div v-if="encomendasHaveAnySensores && encomendaSensoresHaveAnyEvents" class="bg-white rounded-lg shadow-md p-4 mt-5">
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
            <div v-for="leitura in leituras" class="text-sm mb-3">
              <table class="min-w-full">
                <thead>
                  <tr class="bg-gray-50">
                    <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">ID Sensor
                    </th>
                    <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">ID
                      Encomenda</th>
                    <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">ID Volume
                    </th>
                    <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Última
                      Leitura</th>
                    <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Timestamp
                    </th>
                  </tr>
                </thead>
                <tbody class="bg-white divide-y divide-gray-200">
                  <tr v-for="eachleitura in leitura" :key="eachleitura.idSensor">
                    <td class="px-6 py-4 whitespace-nowrap">{{ eachleitura.idSensor }}</td>
                    <td class="px-6 py-4 whitespace-nowrap">{{ eachleitura.idEncomenda }}</td>
                    <td class="px-6 py-4 whitespace-nowrap">{{ eachleitura.idVolume }}</td>
                    <td class="px-6 py-4 whitespace-nowrap">{{ eachleitura.ultimaLeitura }}
                      {{ getMeasureText(formData.selectedTipo) }}</td>
                    <td class="px-6 py-4 whitespace-nowrap">{{ formatDate(eachleitura.timestamp) }}</td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>
        </div>
      </div>
      <div v-if="error.leituras" class="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded mt-4">
        {{ error.leituras }}
      </div>

    </div>
  </div>
</template>