import Vue from 'vue';
import Router from 'vue-router';
import Home from '@/View/Home.vue';

Vue.use(Router);

const routes = [
    { path: '/', component: Home },
];

const router = new Router({
    mode: 'history',
    routes,
});

export default router;
