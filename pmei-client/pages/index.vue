<script setup>
import {useAuthStore} from '~/store/auth-store';
import {useRouter} from 'vue-router';
import {onMounted} from "vue";

const authStore = useAuthStore();
const router = useRouter();

const logout = () => {
  authStore.logout();
  router.push('/login');
};

const btnSGO = ref(false)
const btnSDL = ref(false)
const btnSAC = ref(false)

onMounted(() => {
  if (authStore.user) {
    if (authStore.role === 'Gestor') {
      btnSGO.value = false
      btnSAC.value = true
      btnSDL.value = true
    } else if (authStore.role === 'Logistica') {
      btnSGO.value = true
      btnSDL.value = false
      btnSAC.value = true
    } else if (authStore.role === 'Cliente') {
      btnSGO.value = true
      btnSAC.value = false
      btnSDL.value = true
    }
  }

})

</script>
<template>
  <div class="min-h-screen bg-gray-100">
    <!-- Main Content -->
    <div class="max-w-7xl mx-auto px-4 py-8">
      <div class="text-center mb-12">
        <h1 class="text-4xl font-bold text-gray-800 mb-4">Bem-Vindo à PMEI!</h1>
        <p class="text-xl text-gray-600">Plataforma de Monitorização de Embalagens Inteligentes</p>
      </div>

      <div class="grid md:grid-cols-3 gap-8">
        <!-- Management Card -->
        <div class="bg-white rounded-lg shadow-md p-6 hover:shadow-lg transition flex flex-col items-center">
          <h2 class="text-2xl font-semibold text-gray-800 mb-4">Gestão</h2>
          <img src="/working-area.png" alt="Management Icon" class="mx-auto mb-4 w-24 h-24"/>
          <p class="text-gray-600 mb-4 text-center">Visualize as encomendas existentes, aceda às informações dos
            sensores e observe estatísticas sobre a PMEI</p>
          <NuxtLink :class="{ 'hidden' : btnSGO }"
                    to="/SGO"
                    class="inline-block bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600 transition">
            Aceder à Área de Gestão
          </NuxtLink>
        </div>

        <!-- Logistics Card -->
        <div class="bg-white rounded-lg shadow-md p-6 hover:shadow-lg transition flex flex-col items-center">
          <h2 class="text-2xl font-semibold text-gray-800 mb-4">Logística</h2>
          <img src="/management.png" alt="Logistics Icon" class="mx-auto mb-4 w-24 h-24"/>
          <p class="text-gray-600 mb-4 text-center">Crie novas encomendas, ajuste rotas e visualize dados sobre o
            posicionamento global de entregadores</p>
          <NuxtLink :class="{ 'hidden' : btnSDL }"
              to="/SDL"
              class="inline-block bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600 transition">
            Aceder à Área de Logística
          </NuxtLink>
        </div>

        <!-- Customer Card -->
        <div class="bg-white rounded-lg shadow-md p-6 hover:shadow-lg transition flex flex-col items-center">
          <h2 class="text-2xl font-semibold text-gray-800 mb-4">Cliente</h2>
          <img src="/client.png" alt="Client Icon" class="mx-auto mb-4 w-24 h-24"/>
          <p class="text-gray-600 mb-4 text-center">Visualize, obtenha dados e verifique o estado das suas
            encomendas</p>
          <NuxtLink :class="{ 'hidden' : btnSAC }"
              to="/SAC"
              class="inline-block bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600 transition">
            Aceder à Área de Cliente
          </NuxtLink>
        </div>
      </div>
    </div>
  </div>
</template>