import Vue from 'vue';
import Vuex from 'vuex';

Vue.use(Vuex);

export default new Vuex.Store({
    state: {
        isLoggedIn: false, // 로그인 상태
        user: null, // 사용자 정보
        searchResults: null, // 검색 결과 저장
    },
    mutations: {
        SET_LOGIN(state, payload) {
            state.isLoggedIn = payload.isLoggedIn;
            state.user = payload.user;
        },
        SET_SEARCH_RESULTS(state, results) {
            state.searchResults = results;
        }
    },
    actions: {
        login({ commit }, user) {
            commit('SET_LOGIN', { isLoggedIn: true, user });
        },
        logout({ commit }) {
            commit('SET_LOGIN', { isLoggedIn: false, user: null });
        },
        updateSearchResults({ commit }, results) {
            commit('SET_SEARCH_RESULTS', results);
        }
    },
});
