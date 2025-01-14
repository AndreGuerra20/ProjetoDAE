<template>
    <div class="min-h-screen bg-gray-100 py-6 px-4 sm:px-6 lg:px-8">
        <div class="max-w-4xl mx-auto">
            <h1 class="text-3xl font-bold text-gray-900 mb-8">Nova Encomenda</h1>
            
            <form @submit.prevent="createEncomenda" class="space-y-6 bg-white p-6 rounded-lg shadow">
                <!-- Customer ID -->
                <div>
                    <label for="customerId" class="block text-sm font-medium text-gray-700">Cliente ID</label>
                    <input 
                        name="customerId"
                        id="customerId"
                        v-model="encomenda.customerId" 
                        type="number" 
                        class="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500"
                        required
                    >
                </div>

                <!-- Estado -->
                <div>
                    <label for="estado" class="block text-sm font-medium text-gray-700">Estado</label>
                    <select 
                        name="estado"
                        id="estado"
                        v-model="encomenda.estado"
                        class="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500"
                        required
                    >
                        <option value="Pendente">Pendente</option>
                        <option value="Despachada">Despachada</option>
                    </select>
                </div>

                <!-- Volumes -->
                <div class="space-y-4">
                    <h2 class="text-xl font-semibold text-gray-800">Volumes</h2>
                    <div v-for="(volume, index) in encomenda.volumes" :key="index" class="p-4 border rounded-md">
                        <!-- Volume Info -->
                        <div class="grid grid-cols-2 gap-4">
                            <div>
                                <label for="idVolume" class="block text-sm font-medium text-gray-700">ID Volume</label>
                                <input 
                                    name="idVolume"
                                    id="idVolume"
                                    v-model="volume.idVolume" 
                                    type="number"
                                    class="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500"
                                >
                            </div>
                            <div>
                                <label for="tipoEmbalagem" class="block text-sm font-medium text-gray-700">Tipo Embalagem</label>
                                <input 
                                    id="tipoEmbalagem"
                                    name="tipoEmbalagem"
                                    v-model="volume.tipoEmbalagem"
                                    class="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500"
                                >
                            </div>
                        </div>

                        <!-- Produtos -->
                        <div class="mt-4">
                            <h3 class="text-lg font-medium text-gray-800">Produtos</h3>
                            <div v-for="(produto, prodIndex) in volume.produtos" :key="prodIndex" class="mt-2 grid grid-cols-2 gap-4">
                                <div>
                                    <label for="idProduto" class="block text-sm font-medium text-gray-700">ID Produto</label>
                                    <input 
                                        id="idProduto"
                                        name="idProduto"
                                        v-model="produto.id" 
                                        type="number"
                                        class="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500"
                                    >
                                </div>
                                <div>
                                    <label for="quantidade" class="block text-sm font-medium text-gray-700">Quantidade</label>
                                    <input 
                                        id="quantidade"
                                        name="quantidade"
                                        v-model="produto.quantidade" 
                                        type="number"
                                        class="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500"
                                    >
                                </div>
                            </div>
                            <button 
                                type="button" 
                                @click="addProduto(index)"
                                class="mt-2 inline-flex items-center px-3 py-1 border border-transparent text-sm font-medium rounded-md text-indigo-700 bg-indigo-100 hover:bg-indigo-200"
                            >
                                Adicionar Produto
                            </button>
                        </div>

                        <!-- Sensores -->
                        <div class="mt-4">
                            <h3 class="text-lg font-medium text-gray-800">Sensores</h3>
                            <div v-for="(sensor, sensorIndex) in volume.sensores" :key="sensorIndex" class="mt-2 grid grid-cols-3 gap-4">
                                <div>
                                    <label for="idSensor" class="block text-sm font-medium text-gray-700">ID Sensor</label>
                                    <input 
                                        id="idSensor"
                                        name="idSensor"
                                        v-model="sensor.id" 
                                        type="number"
                                        class="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500"
                                    >
                                </div>
                                <div>
                                    <label for="tipoSensor" class="block text-sm font-medium text-gray-700">Tipo</label>
                                    <select 
                                        id="tipoSensor"
                                        name="tipoSensor"
                                        v-model="sensor.tipo"
                                        class="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500"
                                    >
                                        <option value="Temperatura">Temperatura</option>
                                        <option value="Aceleracao">Aceleração</option>
                                        <option value="Posicionamento Global">Posicionamento Global</option>
                                    </select>
                                </div>
                                <div>
                                    <label for="status" class="block text-sm font-medium text-gray-700">Status</label>
                                    <input 
                                        id="status"
                                        name="status"
                                        v-model="sensor.status" 
                                        type="checkbox"
                                        class="mt-2 h-4 w-4 text-indigo-600 focus:ring-indigo-500 border-gray-300 rounded"
                                    >
                                </div>
                            </div>
                            <button 
                                type="button" 
                                @click="addSensor(index)"
                                class="mt-2 inline-flex items-center px-3 py-1 border border-transparent text-sm font-medium rounded-md text-indigo-700 bg-indigo-100 hover:bg-indigo-200"
                            >
                                Adicionar Sensor
                            </button>
                        </div>
                    </div>
                    
                    <button 
                        type="button" 
                        @click="addVolume"
                        class="inline-flex items-center px-4 py-2 border border-transparent text-sm font-medium rounded-md text-white bg-indigo-600 hover:bg-indigo-700"
                    >
                        Adicionar Volume
                    </button>
                </div>

                <div class="flex justify-end mt-6">
                    <button 
                        type="submit"
                        class="inline-flex items-center px-4 py-2 border border-transparent text-sm font-medium rounded-md text-white bg-green-600 hover:bg-green-700"
                    >
                        Criar Encomenda
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

const {token, user} = storeToRefs(authStore)

const encomenda = ref({
    customerId: null,
    estado: 'Despachada',
    volumes: [
        {
            idVolume: null,
            tipoEmbalagem: 'Caixa cartão',
            produtos: [
                {
                    id: null,
                    quantidade: null
                }
            ],
            sensores: [
                {
                    id: null,
                    tipo: 'Temperatura',
                    status: false
                }
            ]
        }
    ]
})

const addVolume = () => {
    encomenda.value.volumes.push({
        idVolume: null,
        tipoEmbalagem: 'Caixa cartão',
        produtos: [
            {
                id: null,
                quantidade: null
            }
        ],
        sensores: [
            {
                id: null,
                tipo: 'Temperatura',
                status: false
            }
        ]
    })
}

const addProduto = (volumeIndex) => {
    encomenda.value.volumes[volumeIndex].produtos.push({
        id: null,
        quantidade: null
    })
}

const addSensor = (volumeIndex) => {
    encomenda.value.volumes[volumeIndex].sensores.push({
        id: null,
        tipo: 'Temperatura',
        status: false
    })
}

const createEncomenda = async () => {
    try {
        const response = await fetch(`${api}/encomendas`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                Authorization: `Bearer ${token.value}`
            },
            body: JSON.stringify(encomenda.value)
        })
        if(response.status == 201) {
            router.push('/SDL')
        }
    } catch (err) {
        console.error('Error creating encomenda:', err);
    }
}
</script>