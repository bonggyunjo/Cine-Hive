import Vue from 'vue';
import Router from 'vue-router';
import Home from '@/View/HomePage.vue';
import AuthView from "@/View/AuthView.vue";
import SocialLoginAdditionalInfo from "@/View/SocialLoginAdditionalInfo.vue"

Vue.use(Router);

const routes = [
    { path: '/', component: Home },
    {path: '/auth', component: AuthView},
    {path: '/additional-info', component: SocialLoginAdditionalInfo},
];

const router = new Router({
    mode: 'history',
    routes,
});

export default router;
