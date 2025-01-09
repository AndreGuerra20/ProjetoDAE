<script setup>
</script>

<template>
    <div class="min-h-screen bg-gray-100">
        <!-- Main Content -->
        <div class="max-w-7xl mx-auto px-4 py-8">
            <div class="text-center mb-12">
                <h1 class="text-4xl font-bold text-gray-800 mb-4">Welcome to PMEI</h1>
                <p class="text-xl text-gray-600">Package Management and Express Integration</p>
            </div>

            <div class="grid md:grid-cols-3 gap-8">
                <!-- Management Card -->
                <div class="bg-white rounded-lg shadow-md p-6 hover:shadow-lg transition">
                    <h2 class="text-2xl font-semibold text-gray-800 mb-4">Management</h2>
                    <p class="text-gray-600 mb-4">Manage your packages, track deliveries, and handle operations efficiently.</p>
                    <NuxtLink 
                        to="/SGO" 
                        class="inline-block bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600 transition">
                        Access Management
                    </NuxtLink>
                </div>

                <!-- Logistics Card -->
                <div class="bg-white rounded-lg shadow-md p-6 hover:shadow-lg transition">
                    <h2 class="text-2xl font-semibold text-gray-800 mb-4">Logistics</h2>
                    <p class="text-gray-600 mb-4">Optimize routes, manage fleet, and coordinate deliveries seamlessly.</p>
                    <NuxtLink 
                        to="/SDL" 
                        class="inline-block bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600 transition">
                        Access Logistics
                    </NuxtLink>
                </div>

                <!-- Customer Card -->
                <div class="bg-white rounded-lg shadow-md p-6 hover:shadow-lg transition">
                    <h2 class="text-2xl font-semibold text-gray-800 mb-4">Customer</h2>
                    <p class="text-gray-600 mb-4">Track your packages, manage preferences, and view delivery status.</p>
                    <NuxtLink 
                        to="/SAC"
                        class="inline-block bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600 transition">
                        Access Customer Area
                    </NuxtLink>
                </div>
            </div>
        </div>
    </div>
</template>