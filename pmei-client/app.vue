<template>
    <h2>Encomenda #1</h2>

</template>
<script setup>
const { data: students, error, refresh } = await useFetch('http://localhost:8080/academics/api/student')
</script>