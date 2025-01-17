import Vue from 'vue';
import Vuex from 'vuex';

Vue.use(Vuex);

export default new Vuex.Store({
    state: {
        isLoggedIn: false, // 로그인 상태
        user: null, // 사용자 정보
    },
    mutations: {
        SET_LOGIN(state, payload) {
            console.log('Payload received in SET_LOGIN:', payload); // 추가된 로그
            state.isLoggedIn = payload.isLoggedIn;
            state.user = payload.user; // 사용자 정보를 저장
            console.log('User set in store:', state.user);
        },
    },

    actions: {
        login({ commit }, user) {
            commit('SET_LOGIN', { isLoggedIn: true, user });
        },
        logout({ commit }) {
            commit('SET_LOGIN', { isLoggedIn: false, user: null });
        },
    },
});
