<template>
  <div id="app">
    <!-- 현재 경로의 메타 정보에 따라 헤더를 렌더링 -->
    <HeaderComponent v-if="!hideHeader" />
    <router-view/>
    <!-- 현재 경로의 메타 정보에 따라 푸터를 렌더링 -->
    <FooterComponent v-if="!hideFooter" />
  </div>
</template>

<script>
import HeaderComponent from './components/Header.vue';
import FooterComponent from './components/Footer.vue';

export default {
  name: 'App',
  components: {
    HeaderComponent,
    FooterComponent,
  },
  computed: {
    hideHeader() {
      // 현재 경로의 메타 정보에서 hideHeader 값을 반환
      return this.$route.meta.hideHeader;
    },
    hideFooter() {
      // 현재 경로의 메타 정보에서 hideFooter 값을 반환
      return this.$route.meta.hideFooter;
    }
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
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

#app {
  font-family: 'Inter', sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}
</style>
