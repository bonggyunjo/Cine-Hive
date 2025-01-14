import Vue from 'vue';
import Router from 'vue-router';
import Home from '@/View/Home.vue';
import SignUp from "@/View/SignUp.vue";

Vue.use(Router);

const routes = [
    { path: '/', component: Home },
    {path: '/signup', component: SignUp}
];

const router = new Router({
    mode: 'history',
    routes,
});

export default router;
