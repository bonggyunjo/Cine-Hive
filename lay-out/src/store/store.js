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
                userid: payload.user.userid, // 사용자 ID
                name: payload.user.name, // 사용자 이름
                nickname: payload.user.nickname, // 사용자 닉네임
                email: payload.user.email, // 사용자 이메일
                preferredGenres: payload.user.preferredGenres || [], // 선호 장르
            };
            state.loginType = payload.loginType; // 로그인 타입 추가
        },
        SET_SEARCH_RESULTS(state, results) {
            state.searchResults = results;
        }
    },
    actions: {
        // 로그인 액션
        login({ commit }, { user, loginType }) {
            commit('SET_LOGIN', { isLoggedIn: true, user, loginType });
            // 로그인 타입을 localStorage에 저장
            localStorage.setItem('isLoggedIn', 'true');
            localStorage.setItem('user', JSON.stringify(user));
            localStorage.setItem('loginType', loginType);
        },

        // 로그아웃 액션
        logout({ commit }) {
            commit('SET_LOGIN', { isLoggedIn: false, user: null, loginType: null });
            // 로컬 스토리지에서 로그인 정보 및 타입 제거
            localStorage.removeItem('isLoggedIn');
            localStorage.removeItem('user');
            localStorage.removeItem('loginType');
        },

        // 검색 결과 업데이트 액션
        updateSearchResults({ commit }, results) {
            commit('SET_SEARCH_RESULTS', results);
        },

        // 앱 초기화 시 localStorage에서 로그인 정보 복원
        initializeStore({ commit }) {
            const isLoggedIn = localStorage.getItem('isLoggedIn');
            const user = JSON.parse(localStorage.getItem('user'));
            const loginType = localStorage.getItem('loginType');

            if (isLoggedIn === 'true' && user) {
                commit('SET_LOGIN', { isLoggedIn: true, user, loginType });
            }
        }
    },
    getters: {
        getUserId: (state) => (state.user ? state.user.userid : null),
        getUserInfo: (state) => state.user,
        getLoginType: (state) => state.loginType, // 로그인 타입 getter 추가
    }
});
