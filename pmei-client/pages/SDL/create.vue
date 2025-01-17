<script setup>
import {ref} from 'vue'
import {useRouter} from 'vue-router'
import {useAuthStore} from '~/store/auth-store';

const router = useRouter()
const authStore = useAuthStore()

const {token, user, role} = storeToRefs(authStore)

const config = useRuntimeConfig()
const api = config.public.API_URL

const produtos = ref([])
const errorList = reactive({
  encomendas: null,
  clientes: null,
  volumes: null,
  quantidade: null,
  sensores: null
})

const encomenda = ref({
  encomendaId: null,
  customerId: null,
  estado: null,
  volumes: []
})

const clientes = ref([])

const addVolume = () => {
  encomenda.value.volumes.push({
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
        tipo: null,
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
    tipo: null,
    status: false
  })
}

const createEncomenda = async () => {

  if (errorList.clientes || errorList.encomendas || errorList.volumes || errorList.quantidade || errorList.sensores) {
    return
  }

  try {
    await $fetch(`http://localhost:8080/PMEI/monitorizacao/api/encomendas`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        Authorization: `Bearer ${token.value}`
      },
      body: JSON.stringify(encomenda.value),
      onResponse({request, response, options}) {
        // messages.value.push({
        //     method: options.method,
        //     request: request,
        //     status: response.status,
        //     statusText: response.statusText,
        //     payload: response._data
        // })
        if (response.status == 201) {
          router.push('/SDL')
        }
      }
    })
  } catch (err) {
    console.error('Error creating encomenda:', err);
  }
}

const fetchProdutos = async () => {
  try {
    await $fetch(`http://localhost:8080/PMEI/monitorizacao/api/produtos`, {
      headers: {
        Authorization: `Bearer ${token.value}`
      },
      onResponse({request, response, options}) {
        // messages.value.push({
        //     method: options.method,
        //     request: request,
        //     status: response.status,
        //     statusText: response.statusText,
        //     payload: response._data
        // })
        if (response.status == 200) {
          const data = response._data
          data.sort((a, b) => a.descricao.localeCompare(b.descricao))
          produtos.value = data
        }
      }
    })

  } catch (err) {
    console.error('Error fetching produtos:', err);
  }
}

const fetchClientes = async () => {
  try {
    await $fetch(`${api}/clientes`, {
      headers: {
        Authorization: `Bearer ${token.value}`
      },
      onResponse({request, response, options}) {
        // messages.value.push({
        //     method: options.method,
        //     request: request,
        //     status: response.status,
        //     statusText: response.statusText,
        //     payload: response._data
        // })
        if (response.status == 200) {
          const data = response._data
          clientes.value = data
        }
      }
    })
  } catch (err) {
    console.error('Error fetching clientes:', err);
  }
}

const isIDbeingUsed = async (classe, id) => {
  if (id == null) {
    return
  }
  try {
    await $fetch(`http://localhost:8080/PMEI/monitorizacao/api/${classe}/${id}`, {
      headers: {
        Authorization: `Bearer ${token.value}`
      },
      onResponse({request, response, options}) {
        // messages.value.push({
        //     method: options.method,
        //     request: request,
        //     status: response.status,
        //     statusText: response.statusText,
        //     payload: response._data
        // })
        if (response.status === 200) {
          switch (classe) {
            case 'encomendas':
              errorList.encomendas = 'ID já está a ser utilizado'
              break
            case 'volumes':
              errorList.volumes = 'ID já está a ser utilizado'
              break
            case 'sensores':
              errorList.sensores = 'ID já está a ser utilizado'
              break
          }
        } else {
          switch (classe) {
            case 'encomendas':
              errorList.encomendas = null
              break
            case 'volumes':
              errorList.volumes = null
              break
            case 'sensores':
              errorList.sensores = null
              break
          }

        }
      }
    })
  } catch (err) {
    console.error('Error checking if ID is being used:', err);
  }
}

onMounted(async () => {
  if (!user.value) {
    router.push('/login')
  }
  if (role.value != 'Logistica') {
    router.push('/')
  }
  await fetchProdutos();
  await fetchClientes();
})
</script>
<template>
  <div class="min-h-screen bg-gray-100 py-6 px-4 sm:px-6 lg:px-8">
    <div class="max-w-4xl mx-auto">
      <h1 class="text-3xl font-bold text-gray-900 mb-8">Nova Encomenda</h1>

      <form @submit.prevent="createEncomenda" class="space-y-6 bg-white p-6 rounded-lg shadow">
        <!-- Encomenda ID -->
        <div>
          <label for="encomendaId" class="block text-sm font-medium text-gray-700">Encomenda ID</label>
          <input placeholder="Ex: 1" name="encomendaId" id="encomendaId" v-model="encomenda.encomendaId"
                 type="number"
                 class="mt-1 block w-full p-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                 required @focusout="isIDbeingUsed('encomendas', encomenda.encomendaId)">
          <p v-if="errorList.encomendas" class="text-red-500 text-sm mt-1">{{ errorList.encomendas }}</p>
        </div>
        <!-- Customer ID -->
        <div>
          <label for="customerId" class="block text-sm font-medium text-gray-700">Cliente</label>
          <select name="customerId" id="customerId" v-model="encomenda.customerId"
                  class="mt-1 block w-full p-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                  required>
            <option v-for="cliente in clientes" :key="cliente.id" :value="cliente.id">{{cliente.username + " - " + cliente.email}}
            </option>
          </select>
        </div>

        <!-- Estado -->
        <div>
          <label for="estado" class="block text-sm font-medium text-gray-700">Estado</label>
          <select name="estado" id="estado" v-model="encomenda.estado"
                  class="mt-1 block w-full p-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                  required>
            <option value="Pendente">Pendente</option>
            <option value="Despachada">Despachada</option>
          </select>
        </div>

        <!-- Volumes -->
        <div class="space-y-4">
          <h2 class="text-xl font-semibold text-gray-800">Volumes</h2>
          <div v-for="(volume, index) in encomenda.volumes" :key="index"
               class="p-4 border rounded-md grid grid-cols-1 gap-4">
            <!-- Volume Info -->
            <div class="grid grid-cols-2 gap-4">
              <div>
                <label for="idVolume" class="block text-sm font-medium text-gray-700">ID Volume</label>
                <input placeholder="Ex: 1" name="idVolume" id="idVolume" v-model="volume.idVolume"
                       type="number" @focusout="isIDbeingUsed('volumes', volume.idVolume)"
                       class="mt-1 block w-full p-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500">
                <p v-if="errorList.volumes" class="text-red-500 text-sm mt-1">{{ errorList.volumes }}
                </p>
              </div>
              <div>
                <label for="tipoEmbalagem" class="block text-sm font-medium text-gray-700">Tipo
                  Embalagem</label>
                <input id="tipoEmbalagem" name="tipoEmbalagem" v-model="volume.tipoEmbalagem"
                       autocomplete="off"
                       class="mt-1 block w-full p-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500">
              </div>
            </div>

            <!-- Produtos -->
            <div class="mt-4">
              <h3 class="text-lg font-medium text-gray-800">Produtos</h3>
              <div v-for="(produto, prodIndex) in volume.produtos" :key="prodIndex"
                   class="mt-2 grid grid-cols-3 gap-4 ">
                <div>
                  <label for="idProduto"
                         class="block text-sm font-medium text-gray-700">Produto</label>
                  <select id="idProduto" placeholder="Ex: 1" name="idProduto" v-model="produto.id" required
                          class="mt-1 block w-full p-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500">
                    <option v-for="produto in produtos" :key="produto.id" :value="produto.id">{{
                        produto.descricao
                      }}
                    </option>
                  </select>
                </div>
                <div>
                  <label for="quantidade"
                         class="block text-sm font-medium text-gray-700">Quantidade</label>
                  <input id="quantidade" placeholder="Ex: 1" name="quantidade"
                         v-model="produto.quantidade" type="number"
                         @change="() => { if (produto.quantidade < 1) errorList.quantidade = 'Quantidade tem de ser maior que 0'; else errorList.quantidade = '' }"
                         class="mt-1 block w-full p-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500">
                  <p v-if="errorList.quantidade" class="text-red-500 text-sm mt-1">{{
                      errorList.quantidade
                    }}</p>
                </div>
                <div>
                  <button type="button"
                          @click="encomenda.volumes[index].produtos.splice(prodIndex, 1); errorList.quantidade = null"
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
                         type="number" @focusout="isIDbeingUsed('sensores', sensor.id)"
                         class="mt-1 block w-full p-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500">
                  <p v-if="errorList.sensores" class="text-red-500 text-sm mt-1">{{
                      errorList.sensores
                    }}</p>
                </div>
                <div>
                  <label for="tipoSensor" class="block text-sm font-medium text-gray-700">Tipo</label>
                  <select id="tipoSensor" name="tipoSensor" v-model="sensor.tipo"
                          class="mt-1 block w-full p-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500">
                    <option value="Aceleracao">Aceleração</option>
                    <option value="Posicionamento Global">Posicionamento Global</option>
                    <option value="Pressao">Pressão</option>
                    <option value="Temperatura">Temperatura</option>
                  </select>
                </div>
                <div>
                  <label for="status" class="block text-sm font-medium text-gray-700">Ativo?</label>
                  <input id="status" name="status" v-model="sensor.status" type="checkbox"
                         class="mt-3 h-5 w-5 text-indigo-600 focus:ring-indigo-500 border-gray-300 rounded">
                </div>
                <div>
                  <button type="button"
                          @click="encomenda.volumes[index].sensores.splice(sensorIndex, 1); errorList.sensores = null"
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
            <div>
              <button type="button"
                      @click="encomenda.volumes.splice(index, 1); errorList.sensores = null; errorList.volumes = null; errorList.quantidade = null"
                      class="mt-6 inline-flex items-center px-3 py-1 border border-transparent text-sm font-medium rounded-md text-red-700 bg-red-100 hover:bg-red-200">
                Remover Volume
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
            Criar Encomenda
          </button>
        </div>
      </form>
    </div>
  </div>
</template>