<script setup>
import { ref, onMounted } from 'vue';

const encomendas = ref([]);
const error = ref(null);

async function fetchEncomendas() {
    try {
        const { data } = await useFetch('http://localhost:8080/PMEI/monitorizacao/api/encomenda');

        // Safeguard: Ensure data is an array or fallback to an empty array
        encomendas.value = data.value || [];

        if (encomendas.value.length === 0) {
            error.value = 'No encomendas found.';
            return;
        }
    } catch (err) {
        console.error('Error fetching encomendas:', err);
        error.value = 'Failed to load encomendas.';
    }
}

// Fetch data on component mount
onMounted(fetchEncomendas);
</script>

<template>
    <h2 class="text-2xl font-bold mb-4 text-gray-800">Encomendas</h2>
    <p v-if="error" class="text-red-500">{{ error }}</p>
    <ul v-else class="space-y-4">
        <li v-for="encomenda in encomendas" :key="encomenda.id" class="p-4 border rounded-lg shadow-md bg-white">
            <p class="font-semibold text-lg text-gray-700">ID: {{ encomenda.id }}</p>
            <p class="text-gray-600">Cliente: {{ encomenda.clienteUsername || 'Loading...' }}</p>
            <p class="text-gray-600">Estado: {{ encomenda.estado }}</p>
            <p class="text-gray-700 mt-2">Volumes: </p>
            <ul class="space-y-2 ml-4">
                <li v-for="volume in encomenda.volumes" :key="volume" class="p-2 border rounded bg-gray-50">
                    <p class="text-gray-600">ID: {{ volume.id || 'Loading...' }}</p>
                    <p class="text-gray-600">Tipo de Embalagem: {{ volume.tipoEmbalagem || 'Loading...' }}</p>
                    <p class="text-gray-700 mt-2">Produtos: </p>
                    <ul class="space-y-2 ml-4">
                        <li v-for="produto in volume.produtos" :key="produto" class="p-2 border rounded bg-gray-100">
                            <p class="text-gray-600">ID: {{ produto.id || 'Loading...' }}</p>
                            <p class="text-gray-600">Quantidade: {{ produto.quantidade || 'Loading...' }}</p>
                        </li>
                    </ul>
                    <p class="text-gray-700 mt-2">Sensores: </p>
                    <ul class="space-y-2 ml-4">
                        <li v-for="sensor in volume.sensores" :key="sensor" class="p-2 border rounded bg-gray-100">
                            <p class="text-gray-600">ID: {{ sensor.id || 'Loading...' }}</p>
                            <p class="text-gray-600">Tipo: {{ sensor.tipo || 'Loading...' }}</p>
                            <p class="text-gray-600">Status: {{ sensor.status || 'Loading...' }}</p>
                        </li>
                    </ul>
                </li>
            </ul>
        </li>
    </ul>
</template>
