import Vue from 'vue';
import Router from 'vue-router';
import Home from '@/View/HomePage.vue';
import AuthView from "@/View/AuthView.vue";
import SocialLoginAdditionalInfo from "@/View/SocialLoginAdditionalInfo.vue";
import SearchPage from "@/View/SearchPage.vue";

Vue.use(Router);

const routes = [
    { path: '/', component: Home },
    { path: '/auth', component: AuthView, meta: { hideHeader: true, hideFooter: true } }, // 헤더와 푸터 숨기기
    { path: '/additional-info', component: SocialLoginAdditionalInfo },
    { path: '/search', component: SearchPage },
];

const router = new Router({
    mode: 'history',
    routes,
});

export default router;
