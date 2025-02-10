import Vue from 'vue'
import App from './App.vue'
import router from './router/router';
import store from './store/store';




Vue.config.productionTip = false

store.dispatch('initializeStore').then(() => {
  new Vue({
    store,
    router,
    render: h => h(App)
  }).$mount('#app');
});