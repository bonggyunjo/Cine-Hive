import Vue from 'vue';
import Router from 'vue-router';
import Home from '@/View/Home.vue';
import AuthView from "@/View/AuthView.vue";

Vue.use(Router);

const routes = [
    { path: '/', component: Home },
    {path: '/auth', component: AuthView}
];

const router = new Router({
    mode: 'history',
    routes,
});

export default router;
