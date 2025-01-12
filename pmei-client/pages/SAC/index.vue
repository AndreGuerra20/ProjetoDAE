<script setup>
import { ref,onMounted } from 'vue'
import {useAuthStore} from "~/store/auth-store.js"
import { useRouter } from 'vue-router';

const router = useRouter()
const config = useRuntimeConfig()
const api = config.public.API_URL

const authStore = useAuthStore()
const {token, user} = storeToRefs(authStore)

const error = ref(null)
const messages = ref([])
const encomendas = ref([])

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
                if(response.status == 200) {
                    encomendas.value = response._data
                }
            }
        });
    } catch (err) {
        console.error('Error fetching encomendas:', err);
        error.value = 'Não foi possível carregar as encomendas, por favor tente novamente.';
    }
}

onMounted(() => {
    //authStore.loadUser()
    fetchEncomendas()
})

onBeforeMount(() => {
    if (!authStore.isAuthenticated()) {
        router.push('auth/login')
    }
})
</script>
<template>
    <div class="min-h-screen bg-gray-100 p-4">
        <div class="max-w-4xl mx-auto">
            <h1 class="text-2xl md:text-3xl font-bold mb-6 text-gray-800">Acompanhar Encomenda</h1>
            
            <div class="bg-white rounded-lg shadow-md p-4 mb-4">
                <div class="mb-4">
                    <input type="text" placeholder="Coloque o número da encomenda" 
                        class="w-full p-2 border rounded focus:outline-none focus:ring-2 focus:ring-blue-500">
                </div>
                <button class="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600 transition">
                    Acompanhar
                </button>
            </div>

            <div class="bg-white rounded-lg shadow-md p-4">
                <h2 class="text-xl font-semibold mb-4">Encomendas Recentes</h2>
                
                <div class="space-y-4">
                    <div v-for="encomenda in encomendas" class="border-l-4 border-blue-600 pl-4 py-2">
                        <NuxtLink :to="`/SAC/encomendas/${encomenda.encomendaId}`">
                            <div class="flex justify-between items-center">
                                <div>
                                    <p class="font-medium text-lg">Encomenda #{{ encomenda.encomendaId }}</p>
                                    <p class="text-m text-gray-600">No. de Volumes - {{ encomenda.volumes.length }}</p>
                                </div>
                                <span class="px-3 py-1 rounded-full text-sm" :class="{'bg-green-100 text-green-800': encomenda.estado === 'Entregue', 'bg-yellow-100 text-yellow-800': encomenda.estado === 'Pendente', 'bg-blue-100 text-blue-800': encomenda.estado === 'Despachada'}">
                                    {{ encomenda.estado }}
                                </span>
                            </div>
                        </NuxtLink>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>