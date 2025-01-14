<template>
  <div id="app">
    <router-view/>
  </div>
</template>

<script>


export default {
  name: 'App',
  components: {
    // eslint-disable-next-line vue/no-unused-components
  },
  created() {
    const token = localStorage.getItem('token');
    if (token) {
      this.$store.commit('SET_LOGIN', true);
    }
  }
}
</script>

<style>

#app {
  font-family: "Pretendard Variable", Pretendard, Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}
</style>