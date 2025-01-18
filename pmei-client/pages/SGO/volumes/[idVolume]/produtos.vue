<script setup>
import {ref} from 'vue'
import {useRouter, useRoute} from 'vue-router'
import {useAuthStore} from '~/store/auth-store';

const router = useRouter()
const authStore = useAuthStore()

const route = useRoute()
const volumeId = ref(route.params.idVolume)
const products = ref(null)
const volume = ref(null)
const error = ref('')

const config = useRuntimeConfig()
const api = config.public.API_URL

const {token} = storeToRefs(authStore)

async function fetchProducts() {
  try {
    // fetch the sensor
    products.value = await $fetch(`${api}/volumes/${volumeId.value}/produtos`, {
      headers: {
        Authorization: `Bearer ${token.value}`
      }
    })
  } catch (err) {
    error.value = 'Não foi possível carregar os produtos, tente novamente mais tarde.'
  }
}

async function fetchVolume() {
  try {
    // fetch the volume
    volume.value = await $fetch(`${api}/volumes/${volumeId.value}`, {
      headers: {
        Authorization: `Bearer ${token.value}`
      }
    })
  } catch (err) {
    error.value = 'Não foi possível carregar o volume, tente novamente mais tarde.'
  }
}

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

const getVolumeStatus = (status) => {
  return status === true ? 'Sim' : 'Não';
}

onMounted(async () => {
  await fetchVolume()
  await fetchProducts()
})
</script>
<template>
  <div class="min-h-screen bg-gray-100 p-4">
    <div class="max-w-7xl mx-auto">

      <div class="mb-6">
        <NuxtLink to="/SGO" class="text-blue-500 hover:text-blue-600 flex items-center">
          <span class="mr-2">←</span> Voltar à área de gestão
        </NuxtLink>
      </div>

      <div v-if="error" class="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded mb-4">
        {{ error }}
      </div>

      <div v-if="volume" class="bg-white rounded-lg shadow-md p-4 mb-6">
        <div class="flex justify-between items-center mb-4">

          <h2 class="text-xl font-semibold">Volume #{{ volumeId }}</h2>
          <p class="text-gray-600">Tipo embalagem: {{ volume?.tipoEmbalagem }}</p>
          <p class="text-gray-600">Entregue: <span :class="styleStatusBadge(volume?.estregue)">{{ getVolumeStatus(volume.entregue) }}</span></p>
        </div>
      </div>

      <div class="bg-white rounded-lg shadow-md p-4 mb-6">
        <div class="flex justify-between items-center mb-4">
          <h2 class="text-xl font-semibold">Produtos    </h2>
        </div>

        <div class="overflow-x-auto">

          <!-- Sensor Details -->
          <div v-if="products">
            <table class="min-w-full">
              <thead>
              <tr class="bg-gray-50">
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">ID Produto</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Descrição</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Precisa Embalagem</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Quantidade</th>
              </tr>
              </thead>
              <tbody class="bg-white divide-y divide-gray-200">

              <tr v-for="product in products">
                <td class="px-6 py-4 whitespace-nowrap">{{ product.id }}</td>
                <td class="px-6 py-4 whitespace-nowrap">{{ product.descricao }}</td>
                <td class="px-6 py-4 whitespace-nowrap">{{ getVolumeStatus(product.precisaEmbalagemAdicional) }}</td>
                <td class="px-6 py-4 whitespace-nowrap">{{ product.quantidade }}</td>
              </tr>
              </tbody>
            </table>
          </div>
          <div v-else>
            <p class="text-gray-600">Não existem produtos disponíveis.</p>
          </div>

        </div>
      </div>


    </div>
  </div>
</template>