<script setup>
import {onMounted, ref} from 'vue'

const route = useRoute()
const error = ref(null)
const sensors = ref(null)
const orders = ref(null)
const token = ref(null)
const sensorsSize = ref(null)

async function fetch() {
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
    sensors.value = await $fetch(`http://localhost:8080/PMEI/monitorizacao/api/sensor`, {
      headers: {
        Authorization: `Bearer ${token.value}`
      }
    })

    orders.value = await $fetch(`http://localhost:8080/PMEI/monitorizacao/api/encomenda`, {
      headers: {
        Authorization: `Bearer ${token.value}`
      }
    })

  } catch (err) {
    console.error('Error fetching encomenda details:', err)
    error.value = 'Failed to load encomenda details.'
  }
}

onMounted(async () => {
  await fetch()
  console.log(orders.value)
  sensorsSize.value = sensors.value.length
})

const getSensorStatus = (status) => {
  return status === true ? 'Active' : 'Inative'
}

const styleStatusBadge = (status) => {
  if (status === true || status === 'Entregue') {
    return ['px-2 py-1 text-xs rounded-full bg-green-100 text-green-800'];
  }
  else if (status === false || status === 'Pendente') {
    return ['px-2 py-1 text-xs rounded-full bg-yellow-100 text-yellow-800'];
  }
  else {
    return ['px-2 py-1 text-xs rounded-full bg-blue-100 text-blue-800'];
  }
};

</script>

<template>
  <div class="min-h-screen bg-gray-100 p-4">
    <div class="max-w-7xl mx-auto">
      <h1 class="text-2xl md:text-3xl font-bold mb-6 text-gray-800">Management Dashboard</h1>

      <!-- Order Management -->
      <div class="bg-white rounded-lg shadow-md p-4 mb-6">
        <div class="flex justify-between items-center mb-4">
          <h2 class="text-xl font-semibold">Recent Orders</h2>
        </div>

        <div class="overflow-x-auto">
          <table class="min-w-full">
            <thead>
            <tr class="bg-gray-50">
              <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Customer ID
              </th>
              <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Order ID</th>
              <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Status</th>
              <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">See order</th>
            </tr>
            </thead>
            <tbody class="bg-white divide-y divide-gray-200">
            <tr v-for="order in orders" :key="order.id">
              <td class="px-6 py-4 whitespace-nowrap">{{ order.encomendaId }}</td>
              <td class="px-6 py-4 whitespace-nowrap">{{ order.customerId }}</td>
              <td class="px-6 py-4 whitespace-nowrap">
                                    <span :class="styleStatusBadge(order.estado)">
                                        {{ order.estado }}
                                    </span>
              </td>
              <td class="px-6 py-4 whitespace-nowrap">
                <NuxtLink :to="`/SGO/orders/${order.encomendaId}`" class="text-blue-500 hover:text-blue-600">
                  View Order
                </NuxtLink>
              </td>
            </tr>
            </tbody>
          </table>
        </div>
      </div>

      <!-- Sensors -->
      <div class="bg-white rounded-lg shadow-md p-4 mb-6">
        <div class="flex justify-between items-center mb-4">
          <h2 class="text-xl font-semibold">All Sensors</h2>
        </div>

        <div class="overflow-x-auto">
          <table class="min-w-full">
            <thead>
            <tr class="bg-gray-50">
              <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">ID</th>
              <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Type</th>
              <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Status</th>
              <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">See events</th>
            </tr>
            </thead>
            <tbody class="bg-white divide-y divide-gray-200">
            <tr v-for="sensor in sensors" :key="sensor.id">
              <td class="px-6 py-4 whitespace-nowrap">{{ sensor.id }}</td>
              <td class="px-6 py-4 whitespace-nowrap">{{ sensor.tipo }}</td>
              <td class="px-6 py-4 whitespace-nowrap">
                  <span :class="styleStatusBadge(sensor.status)">
                      {{ getSensorStatus(sensor.status) }}
                  </span>
              </td>
              <td class="px-6 py-4 whitespace-nowrap">
                <NuxtLink :to="`/SGO/sensors/${sensor.id}`" class="text-blue-500 hover:text-blue-600">
                  View Events
                </NuxtLink>
              </td>
            </tr>
            <tr>
              <td colspan="4" class="px-6 py-4 whitespace-nowrap"># of sensors: {{ sensorsSize }}</td>
            </tr>
            </tbody>
          </table>
        </div>
      </div>


      <!-- Performance Metrics -->
      <div class="bg-white rounded-lg shadow-md p-4">
        <h2 class="text-xl font-semibold mb-4">Performance Metrics</h2>
        <div class="grid md:grid-cols-2 gap-4">
          <div class="bg-gray-50 p-4 rounded-lg">
            <h3 class="font-semibold mb-2">Order Processing Time</h3>
            <div class="bg-gray-200 h-4 rounded-full overflow-hidden">
              <div class="bg-blue-500 h-full w-3/4"></div>
            </div>
            <p class="text-sm text-gray-600 mt-2">Average: 2.5 hours</p>
          </div>
          <div class="bg-gray-50 p-4 rounded-lg">
            <h3 class="font-semibold mb-2">Customer Satisfaction</h3>
            <div class="bg-gray-200 h-4 rounded-full overflow-hidden">
              <div class="bg-green-500 h-full w-4/5"></div>
            </div>
            <p class="text-sm text-gray-600 mt-2">Rating: 4.5/5</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>