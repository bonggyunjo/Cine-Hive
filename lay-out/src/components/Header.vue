<template>
  <header>
    <div class="header-container">
      <router-link to="/" class="site-title">
        <span @click="goToHome">CINEHIVE</span>
      </router-link>

      <nav class="nav">
        <ul>
          <li>Animation</li>
          <li>Movie</li>
          <li>Drama</li>
          <li>Community</li>
          <li>My List</li>
          <li>Watched List</li>
        </ul>
      </nav>

      <div class="search-bar">
        <input
            type="text"
            v-model="searchQuery"
            placeholder="search..."
            @keyup.enter="searchMovies"
        />
        <button @click="searchMovies">검색</button>
      </div>

      <div class="login-area">
        <template v-if="isLoggedIn">
          <span @click="logout" class="logout-link">Logout</span>
          <router-link to="/mypage" class="nav-link">My Page</router-link>
        </template>
        <template v-else>
          <router-link to="/auth" class="login-link">Login</router-link>
          <router-link to="/auth" class="signup-link">회원이 아니신가요?</router-link>
        </template>
      </div>
    </div>

    <!-- 로딩 메시지 -->
    <div v-if="loading" class="loading-overlay">
      <p>검색 중...</p>
    </div>
  </header>
</template>

<script>
import axios from 'axios';
import { mapActions, mapState } from 'vuex';

export default {
  name: 'HeaderComponent',
  data() {
    return {
      searchQuery: "", // 검색어
      loading: false, // 로딩 상태
    };
  },
  mounted() {
    if (!this.isLoggedIn) { // 로그인 상태가 아닐 경우만 API 호출
      this.getUserInfo('kakao');
      this.getUserInfo('google');
      this.getUserInfo('naver');
    }
  },
  computed: {
    ...mapState(['isLoggedIn', 'user']),
    userId() {
      return this.isLoggedIn ? this.user?.id : null;
    }
  },
  methods: {
    ...mapActions(['updateSearchResults']),

    goToHome() {
      this.$router.push('/');
    },
    async getUserInfo(loginType) {
      try {
        const response = await axios.get(`http://localhost:8081/api/auth/${loginType}/success`, {
          withCredentials: true
        });
        this.userInfo = response.data;
        console.log(`${loginType} 로그인 성공:`, response.data);


        this.$store.commit('SET_LOGIN', {
          isLoggedIn: true,
          user: {
            id: response.data.kakaoId || response.data.naverId,
            email: response.data.email,
            nickname: response.data.nickname
          }
        });

        console.log("Vuex에 저장된 user:", this.$store.state.user);
        console.log("로그인 상태:", this.$store.state.isLoggedIn);

      } catch (error) {
        console.error(`${loginType} 사용자 정보 가져오기 실패:`, error);
      }
    },
    logout() {
      // Vuex에서 로그인 상태 초기화
      this.$store.commit('SET_LOGOUT');

      // 로컬/세션 스토리지 데이터 삭제
      localStorage.removeItem('token');
      sessionStorage.clear();
      window.location.reload();

      axios.get('http://localhost:8081/api/auth/logout', {withCredentials: true})
          .then(() => {
            console.log("로그아웃 성공");

            window.location.reload();
          })
          .catch(error => {
            console.error("로그아웃 오류:", error);
          });
    },
    async searchMovies() {
      if (!this.searchQuery.trim()) {
        alert("검색어를 입력하세요!");
        return;
      }

      this.loading = true;

      try {
        const response = await axios.post('http://localhost:8081/search', {
          query: this.searchQuery
        });


        this.updateSearchResults(response.data);


        this.$router.push({
          path: '/search',
          query: { q: this.searchQuery }
        });
      } catch (error) {
        console.error("검색 중 오류가 발생했습니다:", error);
      } finally {
        this.loading = false;
      }
    },
  },
  created() {
    if (this.isLoggedIn) {
      this.getUserInfo();
    }
  }
};
</script>

<style scoped>
.loading-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.8);
  color: white;
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
  font-size: 20px;
}

.site-title {
  text-decoration: none;
  color: inherit; /* 색상 유지 */
}

header {
  background-color: black;
  padding: 20px;
  text-align: center;
}

.header-container {
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-wrap: wrap;
}

.site-title {
  flex: 1;
  color: #F50000;
  font-size: 22px;
  margin-left: 10px;
  position: absolute;
}

.nav ul {
  list-style: none;
  display: flex;
  gap: 20px;
  margin: 0;
  padding: 0;
  color: white;
  font-size: 14px;
  margin-left: 230px;
}

.search-bar {
  flex: 1;
  text-align: left;
  margin: 0 20px;
}

.search-bar input {
  width: 100%;
  max-width: 750px;
  padding: 10px;
  border: none;
  border-radius: 5px;
  background-color: #1a1a1a;
  color: white;
  outline: none;
  transition: border-color 0.3s;
}

.search-bar input::placeholder {
  color: #ccc;
}

.search-bar input:focus {
  border-color: #F50000;
}

.login-area {
  display: flex;
  align-items: center;
  gap: 15px;
}

.login-link,
.logout-link,
.nav-link {
  color: white;
  text-decoration: none;
  cursor: pointer;
}

.login-link:hover,
.logout-link:hover,
.nav-link:hover {
  color: #F50000;
}

@media (max-width: 768px) {
  .header-container {
    flex-direction: column;
    align-items: flex-start;
    padding: 10px;
  }

  .site-title {
    font-size: 18px;
    margin: 10px 0;
  }

  .nav ul {
    flex-wrap: wrap;
    justify-content: center;
    gap: 10px;
    margin: 10px 0;
  }

  .search-bar {
    width: 100%;
    margin: 10px 0;
  }

  .login-area {
    width: 100%;
    justify-content: flex-start;
  }
}

@media (max-width: 480px) {
  .header-container {
    text-align: center;
  }

  .site-title {
    font-size: 16px;
    margin-bottom: 10px;
  }

  .nav ul {
    display: none;
  }

  .search-bar {
    width: 100%;
  }

  .login-area {
    flex-direction: column;
    align-items: center;
    gap: 5px;
    position: relative;
  }
}

.search-bar button {
  background-color: #393636;
  color: white;
  border: none;
  padding: 10px 18px;
  border-radius: 5px;
  cursor: pointer;
  font-size: 13px;
  margin-left: 10px;
  transition: background-color 0.3s ease;
  position: relative;
  top: 1px;
}

.search-bar button:hover {
  background-color: #555555;
}

.search-bar button:active {
  background-color: #990000;
}
</style>
