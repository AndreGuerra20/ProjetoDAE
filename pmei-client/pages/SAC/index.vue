<script setup>
import { ref,onMounted } from 'vue'

const error = ref(null)
const messages = ref([])
const encomendas = ref([])
const token = ref(null)

async function fetchEncomendas() {
    error.value = null;
    try {
        await $fetch(`http://localhost:8080/PMEI/monitorizacao/api/auth/login`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                Accept: 'application/json'
            },
            body: JSON.stringify({
                username: 'henri',
                password: '123'
            }),
            onResponse({ request, response, options }) {
                messages.value.push({
                    method: options.method,
                    request: request,
                    status: response.status,
                    statusText: response.statusText,
                    payload: response._data
                })
                if (response.status == 200) {
                    token.value = response._data
                }
            }
        })

        await $fetch('http://localhost:8080/PMEI/monitorizacao/api/encomenda', {
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
        error.value = 'Failed to load encomendas.';
    }
}

onMounted(() => {
    fetchEncomendas()
})
</script>
<template>
    <div class="min-h-screen bg-gray-100 p-4">
        <div class="max-w-4xl mx-auto">
            <h1 class="text-2xl md:text-3xl font-bold mb-6 text-gray-800">Package Tracking</h1>
            
            <div class="bg-white rounded-lg shadow-md p-4 mb-4">
                <div class="mb-4">
                    <input type="text" placeholder="Enter tracking number" 
                        class="w-full p-2 border rounded focus:outline-none focus:ring-2 focus:ring-blue-500">
                </div>
                <button class="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600 transition">
                    Track Package
                </button>
            </div>

            <div class="bg-white rounded-lg shadow-md p-4">
                <h2 class="text-xl font-semibold mb-4">Recent Packages</h2>
                
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