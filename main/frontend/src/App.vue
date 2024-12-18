<template>
  <div class="app">
    <Header></Header>
    <router-view></router-view>
  </div>
</template>

<script setup lang="ts">
import Header from './components/Header/Header.vue';


</script>

<style scoped>
.app {
  margin: 0;
  padding: 0;
}
</style>