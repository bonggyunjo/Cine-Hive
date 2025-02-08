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
                userid: payload.user.memUserid, // 사용자 ID
                name: payload.user.name, // 사용자 이름
                nickname: payload.user.nickname, // 사용자 닉네임
                email: payload.user.email, // 사용자 이메일
                preferredGenres: payload.user.preferredGenres || [], // 선호 장르
            };
            console.log("Setting user in Vuex:", state.user);
            state.loginType = payload.loginType; // 로그인 타입 추가
        },
        SET_LOGOUT(state) {
            state.isLoggedIn = false;
            state.user = null;
            state.loginType = null;
            console.log("로그아웃 상태로 변경됨");
        },
        SET_SEARCH_RESULTS(state, results) {
            state.searchResults = results;
        }
    },
    actions: {
        // 로그인 액션
        async login({ commit }, { user, loginType }) {
            try {
                // 로그인 성공 후, 상태 업데이트
                commit('SET_LOGIN', { isLoggedIn: true, user, loginType });

                // 로그인 상태를 localStorage에 저장
                localStorage.setItem('isLoggedIn', 'true');
                localStorage.setItem('user', JSON.stringify(user));
                localStorage.setItem('loginType', loginType);

                // 추가적인 로그인 성공 처리 (예: API 호출 등)
            } catch (error) {
                console.error('로그인 중 오류 발생:', error);
            }
        },
        // 로그아웃 액션
        logout({ commit }) {
            // Vuex에서 로그인 상태 초기화
            commit('SET_LOGOUT');

            // 디버깅용 콘솔 로그 추가
            console.log("로그아웃 실행됨");

            // 로컬 스토리지에서 로그인 정보 제거
            localStorage.removeItem('isLoggedIn');
            localStorage.removeItem('user');
            localStorage.removeItem('loginType');

            console.log("로그아웃 후 localStorage:", localStorage.getItem('isLoggedIn'), localStorage.getItem('user'));
        },

        // 검색 결과 업데이트 액션
        updateSearchResults({ commit }, results) {
            commit('SET_SEARCH_RESULTS', results);
        },

        // 앱 초기화 시 localStorage에서 로그인 정보 복원
// store.js 내 initializeStore 액션 수정
        initializeStore({ commit }) {
            const isLoggedIn = localStorage.getItem('isLoggedIn');
            const user = JSON.parse(localStorage.getItem('user'));
            const loginType = localStorage.getItem('loginType');

            console.log('isLoggedIn:', isLoggedIn);  // 확인: localStorage에서 isLoggedIn 값
            console.log('user:', user);  // 확인: localStorage에서 user 값
            console.log('loginType:', loginType);  // 확인: localStorage에서 loginType 값

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
