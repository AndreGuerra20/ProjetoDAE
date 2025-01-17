<template>
    <div class="min-h-screen bg-gray-100 py-6 px-4 sm:px-6 lg:px-8">
        <div class="max-w-4xl mx-auto">
            <h1 class="text-3xl font-bold text-gray-900 mb-8">Adicionar Volumes</h1>

            <form @submit.prevent="addVolumes" class="space-y-6 bg-white p-6 rounded-lg shadow">
                <!-- Encomenda ID -->
                <div>
                    <label for="encomendaId" class="block text-sm font-medium text-gray-700">Encomenda ID</label>
                    <input placeholder="Ex: 1" name="encomendaId" id="encomendaId" v-model="encomendaId" type="number" min="1"
                        class="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500"
                        required>
                </div>

                <!-- Volumes -->
                <div class="space-y-4">
                    <h2 class="text-xl font-semibold text-gray-800">Volumes</h2>
                    <div v-for="(volume, index) in volumes" :key="index"
                        class="p-4 border rounded-md grid grid-cols-1 gap-4">
                        <!-- Volume Info -->
                        <div class="grid grid-cols-2 gap-4">
                            <div>
                                <label for="idVolume" class="block text-sm font-medium text-gray-700">ID Volume</label>
                                <input placeholder="Ex: 1" name="idVolume" id="idVolume" v-model="volume.idVolume"
                                    type="number" min="1"
                                    class="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500">
                            </div>
                            <div>
                                <label for="tipoEmbalagem" class="block text-sm font-medium text-gray-700">Tipo
                                    Embalagem</label>
                                <input id="tipoEmbalagem" name="tipoEmbalagem" v-model="volume.tipoEmbalagem"
                                    autocomplete="off"
                                    class="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500">
                            </div>
                        </div>

                        <!-- Produtos -->
                        <div class="mt-4">
                            <h3 class="text-lg font-medium text-gray-800">Produtos</h3>
                            <div v-for="(produto, prodIndex) in volume.produtos" :key="prodIndex"
                                class="mt-2 grid grid-cols-3 gap-4">
                                <div>
                                    <label for="idProduto"
                                        class="block text-sm font-medium text-gray-700">Produto</label>
                                    <input id="idProduto" placeholder="Ex: 1" name="idProduto" v-model="produto.id"
                                        type="number"
                                        class="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500">
                                </div>
                                <div>
                                    <label for="quantidade"
                                        class="block text-sm font-medium text-gray-700">Quantidade</label>
                                    <input id="quantidade" placeholder="Ex: 1" name="quantidade"
                                        v-model="produto.quantidade" type="number"
                                        class="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500">
                                </div>
                                <div>
                                    <button type="button" @click="volume.produtos.splice(prodIndex, 1)"
                                        class="mt-6 inline-flex items-center px-3 py-1 border border-transparent text-sm font-medium rounded-md text-red-700 bg-red-100 hover:bg-red-200">
                                        Remover Produto
                                    </button>
                                </div>
                            </div>
                            <button type="button" @click="addProduto(index)"
                                class="mt-2 inline-flex items-center px-3 py-1 border border-transparent text-sm font-medium rounded-md text-indigo-700 bg-indigo-100 hover:bg-indigo-200">
                                Adicionar Produto
                            </button>
                        </div>

                        <!-- Sensores -->
                        <div class="mt-4">
                            <h3 class="text-lg font-medium text-gray-800">Sensores</h3>
                            <div v-for="(sensor, sensorIndex) in volume.sensores" :key="sensorIndex"
                                class="mt-2 grid grid-cols-4 gap-4">
                                <div>
                                    <label for="idSensor" class="block text-sm font-medium text-gray-700">ID
                                        Sensor</label>
                                    <input id="idSensor" placeholder="Ex: 1" name="idSensor" v-model="sensor.id"
                                        type="number"
                                        class="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500">
                                </div>
                                <div>
                                    <label for="tipoSensor" class="block text-sm font-medium text-gray-700">Tipo</label>
                                    <select id="tipoSensor" name="tipoSensor" v-model="sensor.tipo"
                                        class="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500">
                                        <option value="Aceleracao">Aceleração</option>
                                        <option value="Posicionamento Global">Posicionamento Global</option>
                                        <option value="Pressao">Pressão</option>
                                        <option value="Temperatura">Temperatura</option>
                                    </select>
                                </div>
                                <div>
                                    <button type="button" @click="volume.sensores.splice(sensorIndex, 1)"
                                        class="mt-6 inline-flex items-center px-3 py-1 border border-transparent text-sm font-medium rounded-md text-red-700 bg-red-100 hover:bg-red-200">
                                        Remover Sensor
                                    </button>
                                </div>
                            </div>
                            <button type="button" @click="addSensor(index)"
                                class="mt-2 inline-flex items-center px-3 py-1 border border-transparent text-sm font-medium rounded-md text-indigo-700 bg-indigo-100 hover:bg-indigo-200">
                                Adicionar Sensor
                            </button>
                        </div>
                    </div>

                    <button type="button" @click="addVolume"
                        class="inline-flex items-center px-4 py-2 border border-transparent text-sm font-medium rounded-md text-white bg-indigo-600 hover:bg-indigo-700">
                        Adicionar Volume
                    </button>
                </div>

                <div class="flex justify-end mt-6">
                    <button type="submit"
                        class="inline-flex items-center px-4 py-2 border border-transparent text-sm font-medium rounded-md text-white bg-green-600 hover:bg-green-700">
                        Adicionar Volumes
                    </button>
                </div>
            </form>
        </div>
    </div>
</template>
<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '~/store/auth-store';

const router = useRouter()
const authStore = useAuthStore()

const config = useRuntimeConfig()
const api = config.public.API_URL

const { token } = storeToRefs(authStore)

const encomendaId = ref(null)
const volumes = ref([])

const addVolume = () => {
    volumes.value.push({
        idVolume: null,
        tipoEmbalagem: null,
        produtos: [
            {
                id: null,
                quantidade: null
            }
        ],
        sensores: [
            {
                id: null,
                tipo: null
            }
        ]
    })
}

const addProduto = (volumeIndex) => {
    volumes.value[volumeIndex].produtos.push({
        id: null,
        quantidade: null
    })
}

const addSensor = (volumeIndex) => {
    volumes.value[volumeIndex].sensores.push({
        id: null,
        tipo: null
    })
}

const addVolumes = async () => {
    try {
        await $fetch(`${api}/encomendas/${encomendaId.value}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                Authorization: `Bearer ${token.value}`
            },
            body: JSON.stringify( volumes.value ),
            onResponse({ request, response, options }) {
                if (response.status == 201) {
                    router.push('/SDL')
                }
            }
        })
    } catch (err) {
        console.error('Error adding volumes:', err);
    }
}
</script>