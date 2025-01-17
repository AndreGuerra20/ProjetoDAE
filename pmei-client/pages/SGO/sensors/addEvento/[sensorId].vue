<script setup>
import { ref } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useAuthStore } from '~/store/auth-store';

const router = useRouter()
const authStore = useAuthStore()

const route = useRoute()
const sensorId = ref(route.params.sensorId)

const { token } = storeToRefs(authStore)

const evento = ref({
    valor: null
})

const createEvento = async () => {
    try {
        await $fetch(`http://localhost:8080/PMEI/monitorizacao/api/sensores/${sensorId.value}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                Authorization: `Bearer ${token.value}`
            },
            body: JSON.stringify(evento.value),
            onResponse({ request, response, options }) {
                if (response.status == 200) {
                    router.push('/SGO/sensors/' + sensorId.value)
                }
            }
        })
    } catch (err) {
        console.error('Error creating evento:', err);
    }
}
</script>
<template>
    <div class="min-h-screen bg-gray-100 py-6 px-4 sm:px-6 lg:px-8">
        <div class="max-w-4xl mx-auto">
            <h1 class="text-3xl font-bold text-gray-900 mb-8">Adicionar Evento</h1>

            <form @submit.prevent="createEvento" class="space-y-6 bg-white p-6 rounded-lg shadow">
                <!-- Valor -->
                <div>
                    <label for="valor" class="block text-sm font-medium text-gray-700">Valor</label>
                    <input placeholder="Ex: 31.2" name="valor" id="valor" v-model="evento.valor" type="number"
                        step="0.1"
                        class="mt-1 block w-full p-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                        required>
                </div>

                <div class="flex justify-end mt-6">
                    <button type="submit"
                        class="inline-flex items-center px-4 py-2 border border-transparent text-sm font-medium rounded-md text-white bg-green-600 hover:bg-green-700">
                        Adicionar Evento
                    </button>
                </div>
            </form>
        </div>
    </div>
</template>