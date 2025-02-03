import Vue from 'vue';
import Router from 'vue-router';
import Home from '@/View/HomePage.vue';
import AuthView from "@/View/AuthView.vue";
import SocialLoginAdditionalInfo from "@/View/SocialLoginAdditionalInfo.vue";
import SearchPage from "@/View/SearchPage.vue";
import MovieDetail from "@/View/MovieDetail.vue";
import DramaDetail from "@/View/DramaDetail.vue";
import AnimationDetail from "@/View/AnimationDetail.vue";

Vue.use(Router);

const routes = [
    { path: '/', component: Home },
    { path: '/auth', component: AuthView, meta: { hideHeader: true, hideFooter: true } }, // 헤더와 푸터 숨기기
    { path: '/additional-info', component: SocialLoginAdditionalInfo },
    { path: '/search', component: SearchPage },
    {path: '/movie/:id',
        name: 'MovieDetail',
        component: MovieDetail,
        meta: { hideFooter: true }},
    { path: '/drama/:id',  // 드라마 디테일 경로 추가
        name: 'DramaDetail',
        component: DramaDetail,
        meta: { hideFooter: true }},
    { path: '/animation/:id',  // 드라마 디테일 경로 추가
        name: 'AnimationDetail',
        component: AnimationDetail,
        meta: { hideFooter: true }},
];

const router = new Router({
    mode: 'history',
    routes,
});

export default router;
