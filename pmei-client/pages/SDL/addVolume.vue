<script setup>
import { useRouter } from 'vue-router'
import { useAuthStore } from '~/store/auth-store';

const router = useRouter()
const authStore = useAuthStore()

const config = useRuntimeConfig()
const api = config.public.API_URL

const { token } = storeToRefs(authStore)

const encomendaId = ref(null)
const volumes = ref([])
const produtos = ref([])

const errorList = reactive({
  encomendas: null,
  volumes: [],
})

onMounted(async () => {
    await getAllProducts()
})

const algo = {
  color: {
    white: {
      outline: 'mt-0.5 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500' //alterar aqui daniela
    },
    gray: {
      outline: 'shadow-sm bg-gray-50 dark:bg-gray-800 text-gray-900 dark:text-white ring-1 ring-inset ring-gray-300 dark:ring-gray-700 focus:ring-2 focus:ring-primary-500 dark:focus:ring-primary-400'
    }
  }
}


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
    errorList.volumes.push({
      erroIdVolume: null,
      produtos: [
        {
          erroPrecisaEmbalagem: null,
          erroQuantidade: null
        }
      ],
      sensores: [
        {
          erroIdSensor: null
        }
      ],
    })
}

const addProduto = (volumeIndex) => {
    volumes.value[volumeIndex].produtos.push({
        id: null,
        quantidade: null
    })
    errorList.volumes[volumeIndex].produtos.push({
      erroIdProduto: null,
      erroQuantidade: null,
      erroPrecisaEmbalagem: null,
      precisaEmbalagemAdicional: null
    })
}

const addSensor = (volumeIndex) => {
    volumes.value[volumeIndex].sensores.push({
        id: null,
        tipo: null
    })
    errorList.volumes[volumeIndex].sensores.push({
      erroIdSensor: null
    })
}

const hasErrors = () => {
  for (let i = 0; i < volumes.value.length; i++) {
    if (errorList.volumes[i].erroIdVolume) {
      return true
    }
    for (let j = 0; j < volumes.value[i].produtos.length; j++) {
      if (errorList.volumes[i].produtos[j].erroIdProduto || errorList.volumes[i].produtos[j].erroQuantidade || errorList.volumes[i].produtos[j].erroPrecisaEmbalagem) {
        return true
      }
    }
    for (let j = 0; j < volumes.value[i].sensores.length; j++) {
      if (errorList.volumes[i].sensores[j].erroIdSensor) {
        return true
      }
    }
  }
  return false
}

const addVolumes = async () => {
    if (hasErrors()) {
      return
    }
    try {
        await $fetch(`${api}/encomendas/${encomendaId.value}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                Authorization: `Bearer ${token.value}`
            },
            body: JSON.stringify(volumes.value),
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

async function getAllProducts() {
    try {
        await $fetch(`${api}/produtos`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                Authorization: `Bearer ${token.value}`
            },
            onResponse({ request, response, options }) {
                if (response.status === 200) {
                    produtos.value = response._data
                }
            }
        })
    } catch (err) {
        console.error('Error getting produtos:', err);
    }
}

const isIDbeingUsed = async (classe, id,volumeIndex,sensorIndex) => {
  if (id == null) {
    return
  }
  if (id < 0) {
    switch (classe) {
      case 'encomendas':
        errorList.encomendas = 'ID tem de ser maior que 0'
        break
      case 'volumes':
        errorList.volumes[volumeIndex].erroIdVolume = 'ID tem de ser maior que 0'
        break
      case 'sensores':
        errorList.volumes[volumeIndex].sensores[sensorIndex].erroIdSensor = 'ID tem de ser maior que 0'
        break
    }
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
              errorList.encomendas = null
              break
            case 'volumes':
              errorList.volumes[volumeIndex].erroIdVolume = 'ID já está a ser utilizado'
              break
            case 'sensores':
              errorList.volumes[volumeIndex].sensores[sensorIndex].erroIdSensor = 'ID já está a ser utilizado'
              break
          }
        } else {
          switch (classe) {
            case 'encomendas':
              errorList.encomendas = 'Não existe nenhuma encomenda com esse ID'
              break
            case 'volumes':
              errorList.volumes[volumeIndex].erroIdVolume = null
              break
            case 'sensores':
              errorList.volumes[volumeIndex].sensores[sensorIndex].erroIdSensor = null
              break
          }
        }
      }
    })
  } catch (err) {
    console.error('Error checking if ID is being used:', err);
  }
}
</script>
<template>
    <div class="min-h-screen bg-gray-100 py-6 px-4 sm:px-6 lg:px-8">
        <div class="max-w-4xl mx-auto">
            <h1 class="text-3xl font-bold text-gray-900 mb-8">Adicionar Volumes</h1>

            <form @submit.prevent="addVolumes" class="space-y-6 bg-white p-6 rounded-lg shadow">
                <!-- Encomenda ID -->
                <div>
                    <label for="encomendaId" class="block text-sm font-medium text-gray-700">Encomenda ID</label>
                    <input placeholder="Ex: 1" name="encomendaId" id="encomendaId" v-model="encomendaId" type="number"
                        class="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500"
                        required  @focusout="isIDbeingUsed('encomendas', encomendaId,null,null)">
                    <p v-if="errorList.encomendas" class="text-red-500 text-sm mt-1">{{ errorList.encomendas }}</p>
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
                          <input placeholder="Ex: 1" name="idVolume" id="idVolume" v-model="volume.idVolume" required
                                 type="number" @focusout="isIDbeingUsed('volumes',volume.idVolume,index,null)"
                                 class="mt-1 block w-full p-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500">
                          <p v-if="errorList.volumes[index].erroIdVolume" class="text-red-500 text-sm mt-1">{{ errorList.volumes[index].erroIdVolume }}</p>
                        </div>
                        <div>
                          <label for="tipoEmbalagem" class="block text-sm font-medium text-gray-700">Tipo Embalagem</label>
                          <input id="tipoEmbalagem" name="tipoEmbalagem" v-model="volume.tipoEmbalagem"
                                 autocomplete="off"
                                 class="mt-1 block w-full p-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                                 @change="() => {
                          if (volume.tipoEmbalagem != null && volume.tipoEmbalagem !== ''){
                            errorList.volumes[index].produtos.forEach(p => p.erroPrecisaEmbalagem = null);
                          }else{
                            errorList.volumes[index].produtos.forEach(p => p.precisaEmbalagemAdicional ? p.erroPrecisaEmbalagem = 'Produto precisa de embalagem' : p.erroPrecisaEmbalagem = null);
                          }
                       }">
                          <p v-if="errorList.volumes[index].produtos.filter(p => p.erroPrecisaEmbalagem).length > 0" class="text-red-500 text-sm mt-1">
                            O volume tem produtos que precisam de embalagem adicional
                          </p>
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
                            <USelectMenu searchable searchable-placeholder="Procurar produto" name="idProduto"
                                         placeholder="Selecione um produto" :options="produtos" value-attribute="id"
                                         option-attribute="descricao" v-model="produto.id" size="xl" :ui="algo" required
                                         @change="() => {
                                  errorList.volumes[index].produtos[prodIndex].erroPrecisaEmbalagem = null
                                  errorList.volumes[index].produtos[prodIndex].precisaEmbalagemAdicional = produtos.find(p => p.id === produto.id).precisaEmbalagemAdicional;
                                  if (produtos.find(p => p.id === produto.id).precisaEmbalagemAdicional && (volume.tipoEmbalagem == null || volume.tipoEmbalagem === '')){
                                    errorList.volumes[index].produtos[prodIndex].erroPrecisaEmbalagem = 'Produto precisa de embalagem';
                                  }
                                  else errorList.volumes[index].produtos[prodIndex].erroPrecisaEmbalagem = null
                               }"/>
                            <p v-if="errorList.volumes[index].produtos[prodIndex].erroPrecisaEmbalagem" class="text-red-500 text-sm mt-1">{{ errorList.volumes[index].produtos[prodIndex].erroPrecisaEmbalagem }}</p>
                          </div>
                          <div>
                            <label for="quantidade"
                                   class="block text-sm font-medium text-gray-700">Quantidade</label>
                            <input id="quantidade" placeholder="Ex: 1" name="quantidade"
                                   v-model="produto.quantidade" type="number"
                                   @change="() => { if (produto.quantidade < 1) errorList.volumes[index].produtos[prodIndex].erroQuantidade = 'Quantidade tem de ser maior que 0'; else errorList.volumes[index].produtos[prodIndex].erroQuantidade = '' }"
                                   class="mt-1 block w-full p-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                                   required>
                            <p v-if="errorList.volumes[index].produtos[prodIndex].erroQuantidade" class="text-red-500 text-sm mt-1">{{
                                errorList.volumes[index].produtos[prodIndex].erroQuantidade
                              }}</p>
                          </div>
                          <div>
                            <button type="button"
                                    @click="volumes[index].produtos.splice(prodIndex, 1); errorList.volumes[index].produtos.pop(prodIndex); errorList.volumes[index].erroTipoEmbalagemCount--;"
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
                                   type="number" @focusout="isIDbeingUsed('sensores', sensor.id,index,sensorIndex)"
                                   class="mt-1 block w-full p-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500" required>
                            <p v-if="errorList.volumes[index].sensores[sensorIndex].erroIdSensor" class="text-red-500 text-sm mt-1">{{
                                errorList.volumes[index].sensores[sensorIndex].erroIdSensor
                              }}</p>
                          </div>
                          <div>
                            <label for="tipoSensor" class="block text-sm font-medium text-gray-700">Tipo</label>
                            <select id="tipoSensor" name="tipoSensor" v-model="sensor.tipo"
                                    class="mt-1 block w-full p-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500" required>
                              <option value="Aceleracao">Aceleração</option>
                              <option value="Posicionamento Global">Posicionamento Global</option>
                              <option value="Pressao">Pressão</option>
                              <option value="Temperatura">Temperatura</option>
                            </select>
                          </div>
                          <div>
                            <button type="button"
                                    @click="volumes[index].sensores.splice(sensorIndex, 1); errorList.volumes[index].sensores.pop(sensorIndex)"
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
                                @click="volumes.splice(index, 1); errorList.volumes.pop(index)"
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
                        Adicionar Volumes
                    </button>
                </div>
            </form>
        </div>
    </div>
</template>