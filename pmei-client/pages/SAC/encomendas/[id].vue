<script setup>
import { ref, onMounted } from 'vue'

const route = useRoute()
const error = ref(null)
const encomenda = ref(null)
const token = ref(null)

async function fetchEncomendaDetails() {
    error.value = null;
    try {
        // First get the authentication token
        const authResponse = await $fetch(`http://localhost:8080/PMEI/monitorizacao/api/auth/login`, {
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
        const response = await $fetch(`http://localhost:8080/PMEI/monitorizacao/api/encomenda/${route.params.id}`, {
            headers: {
                Authorization: `Bearer ${token.value}`
            }
        })
        encomenda.value = response
    } catch (err) {
        console.error('Error fetching encomenda details:', err)
        error.value = 'Failed to load encomenda details.'
    }
}

onMounted(async () => {
    await fetchEncomendaDetails()
})
</script>

<template>
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