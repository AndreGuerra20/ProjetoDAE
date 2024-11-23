<script setup>
import { ref, onMounted } from 'vue';

const encomendas = ref([]); // Reactive variable to store encomendas
const clienteInfo = ref({});
const volumeInfo = ref({});
const produtoInfo = ref({});
const sensorInfo = ref({});
const error = ref(null);

async function fetchEncomendas() {
    try {
        const { data } = await useFetch('http://localhost:8080/PMEI/api/encomenda');

        // Safeguard: Ensure data is an array or fallback to an empty array
        encomendas.value = data.value || [];

        if (encomendas.value.length === 0) {
            console.warn('No encomendas found.');
            return;
        }

        // Fetch cliente and volume info for all encomendas
        for (const encomenda of encomendas.value) {
            if (!clienteInfo.value[encomenda.clienteUsername]) {
                clienteInfo.value[encomenda.clienteUsername] = await fetchClienteInfo(encomenda.clienteUsername);
            }
            for (const volumeId of encomenda.lista_volumes) {
                if (!volumeInfo.value[volumeId]) {
                    volumeInfo.value[volumeId] = await fetchVolumeInfo(volumeId);
                }
                for (const produtoId of volumeInfo.value[volumeId].lista_produtos) {
                    if (!produtoInfo.value[produtoId]) {
                        produtoInfo.value[produtoId] = await fetchProdutoInfo(produtoId);
                    }
                }
                for (const sensorId of volumeInfo.value[volumeId].lista_sensores) {
                    if (!sensorInfo.value[sensorId]) {
                        sensorInfo.value[sensorId] = await fetchSensorInfo(sensorId);
                    }
                }
            }
        }
    } catch (err) {
        console.error('Error fetching encomendas:', err);
        error.value = 'Failed to load encomendas.';
    }
}

async function fetchClienteInfo(username) {
    try {
        const response = await $fetch(`http://localhost:8080/PMEI/api/cliente/${username}`);
        return response;
    } catch (err) {
        console.error(`Error fetching cliente info for ${username}:`, err);
        return { name: 'Unknown' };
    }
}

async function fetchVolumeInfo(volumeId) {
    try {
        const response = await $fetch(`http://localhost:8080/PMEI/api/volume/${volumeId}`);
        return response;
    } catch (err) {
        console.error(`Error fetching volume info for ${volumeId}:`, err);
        return { tipoEmbalagem: 'Unknown' };
    }
}

async function fetchProdutoInfo(produtoId) {
    try {
        const response = await $fetch(`http://localhost:8080/PMEI/api/produto/${produtoId}`);
        return response;
    } catch (err) {
        console.error(`Error fetching produto info for ${produtoId}:`, err);
        return { quantidade: 'Unknown' };
    }
}

async function fetchSensorInfo(sensorId) {
    try {
        const response = await $fetch(`http://localhost:8080/PMEI/api/sensor/${sensorId}`);
        return response;
    } catch (err) {
        console.error(`Error fetching sensor info for ${sensorId}:`, err);
        return { tipo: 'Unknown' };
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
            <p class="text-gray-600">Cliente: {{ clienteInfo[encomenda.clienteUsername]?.name || 'Loading...' }}</p>
            <p class="text-gray-600">Estado: {{ encomenda.estado }}</p>
            <p class="text-gray-700 mt-2">Volumes: </p>
            <ul class="space-y-2 ml-4">
                <li v-for="volumeId in encomenda.lista_volumes" :key="volumeId" class="p-2 border rounded bg-gray-50">
                    <p class="text-gray-600">ID: {{ volumeInfo[volumeId]?.id || 'Loading...' }}</p>
                    <p class="text-gray-600">Tipo de Embalagem: {{ volumeInfo[volumeId]?.tipoEmbalagem || 'Loading...' }}</p>
                    <p class="text-gray-700 mt-2">Produtos: </p>
                    <ul class="space-y-2 ml-4">
                        <li v-for="produtoId in volumeInfo[volumeId]?.lista_produtos" :key="produtoId" class="p-2 border rounded bg-gray-100">
                            <p class="text-gray-600">ID: {{ produtoInfo[produtoId]?.id || 'Loading...' }}</p>
                            <p class="text-gray-600">Quantidade: {{ produtoInfo[produtoId]?.quantidade || 'Loading...' }}</p>
                        </li>
                    </ul>
                    <p class="text-gray-700 mt-2">Sensores: </p>
                    <ul class="space-y-2 ml-4">
                        <li v-for="sensorId in volumeInfo[volumeId]?.lista_sensores" :key="sensorId" class="p-2 border rounded bg-gray-100">
                            <p class="text-gray-600">ID: {{ sensorInfo[sensorId]?.id || 'Loading...' }}</p>
                            <p class="text-gray-600">Tipo: {{ sensorInfo[sensorId]?.tipo || 'Loading...' }}</p>
                            <p class="text-gray-600">Status: {{ sensorInfo[sensorId]?.status || 'Loading...' }}</p>
                        </li>
                    </ul>
                </li>
            </ul>
        </li>
    </ul>
</template>
