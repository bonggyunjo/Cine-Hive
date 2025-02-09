import Vue from 'vue';
import Vuex from 'vuex';

Vue.use(Vuex);

export default new Vuex.Store({
    state: {
        isLoggedIn: false, // 로그인 상태
        user: null, // 사용자 정보
        loginType: null, // 로그인 타입 (kakao, google, naver)
        searchResults: null, // 검색 결과 저장
    },
    mutations: {
        SET_LOGIN(state, payload) {
            state.isLoggedIn = payload.isLoggedIn;
            state.user = {
                name: payload.user.name,
                nickname: payload.user.nickname,
                email: payload.user.email,
                preferredGenres: payload.user.preferredGenres || [],
            };
            console.log("Setting user in Vuex:", state.user);
            state.loginType = payload.loginType;
        },
        SET_LOGOUT(state) {
            state.isLoggedIn = false;
            state.user = null;
            state.loginType = null;
        },
        SET_SEARCH_RESULTS(state, results) {
            state.searchResults = results;
        }
    },
    actions: {
        // 로그인 액션
        async login({ commit }, { user, loginType }) {
            try {
                commit('SET_LOGIN', { isLoggedIn: true, user, loginType });

                // 로그인 상태를 localStorage에 저장
                localStorage.setItem('isLoggedIn', 'true');
                localStorage.setItem('user', JSON.stringify(user));
                localStorage.setItem('loginType', loginType);


            } catch (error) {
                console.error('로그인 중 오류 발생:', error);
            }
        },
        // 로그아웃 액션
        logout({ commit }) {

            commit('SET_LOGOUT');


            // 로컬 스토리지에서 로그인 정보 제거
            localStorage.removeItem('isLoggedIn');
            localStorage.removeItem('user');
            localStorage.removeItem('loginType');

            console.log("로그아웃 후 localStorage:", localStorage.getItem('isLoggedIn'), localStorage.getItem('user'));
        },


        updateSearchResults({ commit }, results) {
            commit('SET_SEARCH_RESULTS', results);
        },


        initializeStore({ commit }) {
            const isLoggedIn = localStorage.getItem('isLoggedIn');
            const user = JSON.parse(localStorage.getItem('user'));
            const loginType = localStorage.getItem('loginType');

            console.log('isLoggedIn:', isLoggedIn);
            console.log('user:', user);
            if (isLoggedIn === 'true' && user) {

                commit('SET_LOGIN', { isLoggedIn: true, user, loginType });
            } else {

                commit('SET_LOGOUT');
            }
        }
    },
    getters: {
        getUserId: (state) => (state.user ? state.user.userid : null),
        getUserInfo: (state) => state.user,
        getLoginType: (state) => state.loginType,
    }
});