<script setup>
const managementStats = [
    {
        title: 'Total Packages',
        value: '1,234',
        trend: '+12%',
        color: 'blue'
    },
    {
        title: 'Active Orders',
        value: '156',
        trend: '+5%',
        color: 'green'
    },
    {
        title: 'Pending Approval',
        value: '23',
        trend: '-2%',
        color: 'yellow'
    }
]

const recentOrders = [
    {
        id: 'ORD001',
        customer: 'Tech Solutions Inc.',
        items: 12,
        status: 'Approved',
        value: '$2,450',
        date: '2024-03-10'
    },
    {
        id: 'ORD002',
        customer: 'Global Logistics Ltd.',
        items: 8,
        status: 'Pending',
        value: '$1,840',
        date: '2024-03-09'
    },
    {
        id: 'ORD003',
        customer: 'Fast Delivery Co.',
        items: 15,
        status: 'Processing',
        value: '$3,200',
        date: '2024-03-08'
    }
]
</script>

<template>
    <div class="min-h-screen bg-gray-100 p-4">
        <div class="max-w-7xl mx-auto">
            <h1 class="text-2xl md:text-3xl font-bold mb-6 text-gray-800">Management Dashboard</h1>

            <!-- Statistics Overview -->
            <div class="grid md:grid-cols-3 gap-4 mb-6">
                <div v-for="stat in managementStats" :key="stat.title" 
                     class="bg-white rounded-lg shadow-md p-4">
                    <h3 class="font-semibold mb-2">{{ stat.title }}</h3>
                    <div class="flex items-center justify-between">
                        <p class="text-3xl font-bold" :class="`text-${stat.color}-600`">{{ stat.value }}</p>
                        <span :class="`text-${stat.color}-600 text-sm`">{{ stat.trend }}</span>
                    </div>
                </div>
            </div>

            <!-- Order Management -->
            <div class="bg-white rounded-lg shadow-md p-4 mb-6">
                <div class="flex justify-between items-center mb-4">
                    <h2 class="text-xl font-semibold">Recent Orders</h2>
                    <button class="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600 transition">
                        New Order
                    </button>
                </div>

                <div class="overflow-x-auto">
                    <table class="min-w-full">
                        <thead>
                            <tr class="bg-gray-50">
                                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Order ID</th>
                                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Customer</th>
                                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Items</th>
                                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Status</th>
                                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Value</th>
                                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Date</th>
                            </tr>
                        </thead>
                        <tbody class="bg-white divide-y divide-gray-200">
                            <tr v-for="order in recentOrders" :key="order.id">
                                <td class="px-6 py-4 whitespace-nowrap">{{ order.id }}</td>
                                <td class="px-6 py-4 whitespace-nowrap">{{ order.customer }}</td>
                                <td class="px-6 py-4 whitespace-nowrap">{{ order.items }}</td>
                                <td class="px-6 py-4 whitespace-nowrap">
                                    <span :class="{
                                        'px-2 py-1 text-xs rounded-full': true,
                                        'bg-green-100 text-green-800': order.status === 'Approved',
                                        'bg-yellow-100 text-yellow-800': order.status === 'Pending',
                                        'bg-blue-100 text-blue-800': order.status === 'Processing'
                                    }">
                                        {{ order.status }}
                                    </span>
                                </td>
                                <td class="px-6 py-4 whitespace-nowrap">{{ order.value }}</td>
                                <td class="px-6 py-4 whitespace-nowrap">{{ order.date }}</td>
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