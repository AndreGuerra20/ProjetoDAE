// https://nuxt.com/docs/api/configuration/nuxt-config
export default defineNuxtConfig({
  compatibilityDate: "2024-04-03",
  devtools: { enabled: false },
  css: ['~/assets/css/main.css'],

  postcss: {
    plugins: {
      tailwindcss: {},
      autoprefixer: {},
    },
  },

  colorMode: {
    preference: 'light'
  },

  runtimeConfig: {
    public: {
      API_URL: process.env.API_URL || 'http://localhost:8080/PMEI/monitorizacao/api',
      GOOGLE_MAPS_API_KEY: process.env.GOOGLE_MAPS_API_KEY || 'AIzaSyCWw9M1QaF5YgRCtlARHJl1ZBDYMxmMXWg'
    }
  },

  modules: ["@pinia/nuxt",'@nuxtjs/leaflet','@nuxt/ui']
});