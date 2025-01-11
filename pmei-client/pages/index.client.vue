<script setup>
import L from 'leaflet'
import { ref, onMounted } from 'vue';

const map = ref(null);

const locations = [
  { name: 'Nantes', lat: 47.218371, lng: -1.553621 },
  { name: 'Saint-Nazaire', lat: 47.273018, lng: -2.213733 },
  { name: 'La Baule', lat: 47.286835, lng: -2.393108 },
  { name: 'Pornic', lat: 47.112, lng: -2.102 },
  { name: 'Guérande', lat: 47.328, lng: -2.429 },
  { name: 'Clisson', lat: 47.087, lng: -1.276 },
  { name: 'Ancenis', lat: 47.366, lng: -1.176 },
  { name: 'Châteaubriant', lat: 47.716, lng: -1.376 },
  { name: 'Redon', lat: 47.652, lng: -2.084 },
  { name: 'Pontchâteau', lat: 47.433, lng: -2.117 },
  { name: 'Savenay', lat: 47.327, lng: -1.952 },
  { name: 'Rezé', lat: 47.183, lng: -1.55 },
  { name: 'Vertou', lat: 47.166, lng: -1.466 },
  { name: 'Carquefou', lat: 47.283, lng: -1.5 },
  { name: 'Orvault', lat: 47.283, lng: -1.633 },
  { name: 'Saint-Herblain', lat: 47.216, lng: -1.65 },
  { name: 'Sainte-Luce-sur-Loire', lat: 47.233, lng: -1.483 },
  { name: 'Bouguenais', lat: 47.183, lng: -1.583 },
  { name: 'Saint-Sébastien-sur-Loire', lat: 47.183, lng: -1.483 },
  { name: 'Basse-Goulaine', lat: 47.2, lng: -1.483 }
];

const coordinates = [
  { lat: 39.74335365871343, lng: -8.794312605631447 },
  { lat: 39.743001743412144, lng: -8.79325771624433 },
  { lat: 39.74242426788271, lng: -8.793574216908228 },
  { lat: 39.74153329614267, lng: -8.793354275758379 },
  { lat: 39.740893934335354, lng: -8.793198707633433 },
  { lat: 39.73949969250403, lng: -8.793402555517487 },
  { lat: 39.73707600966279, lng: -8.794055719074873 },
  { lat: 39.736167169036, lng: -8.795517257114422 },
  { lat: 39.73566764848646, lng: -8.797222384856108 },
  { lat: 39.73548726517331, lng: -8.798918490731804 },
  { lat: 39.73458534152421, lng: -8.803014406017748 },
  { lat: 39.734127437316886, lng: -8.804241376244569 },
  { lat: 39.733620963948425, lng: -8.807047168411684 },
  { lat: 39.73350431519367, lng: -8.808491475626528 },
  { lat: 39.73331669891445, lng: -8.809334971717563 },
  { lat: 39.73154221087, lng: -8.80995761749507 },
  { lat: 39.73006546901546, lng: -8.810110723191576 },
  { lat: 39.729511069395805, lng: -8.810710387183958 }
];

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
              <client-only>
              <div style="height:50vh; width:50vw">
                <h1>Marker Cluster</h1>
                <LMap
                    ref="map"
                    :zoom="6"
                    :max-zoom="18"
                    :center="[47.21322, -1.559482]"
                    :use-global-leaflet="true"
                    @ready="onMapReady"
                >
                  <LTileLayer
                      url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
                      attribution="&amp;copy; <a href=&quot;https://www.openstreetmap.org/&quot;>OpenStreetMap</a> contributors"
                      layer-type="base"
                      name="OpenStreetMap"
                  />
                  <LMarker v-for="location in locations" :lat-lng="[location.lat, location.lng]" :key="location.name">
                    <LPopup>{{ location.name }}</LPopup>
                  </LMarker>
                  <LPolyline :lat-lngs="coordinates"></LPolyline>
                </LMap>
              </div>
              </client-only>
            </div>
        </div>
    </div>
</template>