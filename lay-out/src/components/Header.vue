<template>
  <header>
    <div class="header-container">
      <h1 class="site-title">CINEHIVE</h1>
      <nav class="nav">
        <ul>
          <li>Home</li>
          <li>Animation</li>
          <li>Movie</li>
          <li>Drama</li>
          <li>Community</li>
          <li>My List</li>
          <li>Watched List</li>
        </ul>
      </nav>
      <div class="search-bar">
        <input type="text" placeholder="search..." />
        <button class="search-button">🔍</button>
      </div>
      <div class="login-area">
        <!-- 로그인 상태에 따라 링크 변경 -->
        <template v-if="isLoggedIn">
          <span @click="logout" class="logout-link">Logout</span>
          <span>myPage</span>
        </template>
        <template v-else>
          <router-link to="/auth" class="login-link">Login</router-link>
          <span>회원이 아니신가요?</span>
        </template>
      </div>
    </div>
  </header>
</template>

<script>
import { mapState } from 'vuex'; // mapState 임포트

export default {
  name: 'HeaderComponent',
  computed: {
    ...mapState(['isLoggedIn']), // Vuex 상태 가져오기
  },
  methods: {
    // 로그아웃 메서드
    logout() {
      this.$store.dispatch('logout'); // Vuex 액션 호출
      localStorage.removeItem('token'); // 로컬 스토리지에서 토큰 제거

      // 현재 경로가 홈이 아닐 경우에만 리다이렉트
      if (this.$route.path !== '/') {
        this.$router.push('/'); // 홈으로 리다이렉트
      }
    },
  }
}
</script>

<style scoped>
header {
  background-color: black;
  padding: 20px;
  text-align: center;
}

.header-container {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.site-title {
  flex: 1;
  color: #F50000;
  font-size: 22px;
}

.nav {
  position: relative;
  left: -180px;
}

.nav ul {
  list-style: none;
  display: flex;
  gap: 30px;
  margin: 0;
  padding: 0 0 0 20px;
  color: white;
  font-size: 14.5px;
}

.search-bar {
  flex: 1;
  text-align: center;
  position: relative;
}

.search-bar input {
  border: none;
  padding: 10px;
  width: 450px;
  border-radius: 5px; /* 전체 모서리 둥글게 */
  outline: none; /* 포커스 시 테두리 제거 */
  background-color: #1a1a1a;
  color: white; /* 글자 색상 */
  transition: border-color 0.3s; /* 테두리 색상 변화 애니메이션 */
  position: relative;
  left: -150px;
}

.search-bar input::placeholder {
  color: #ccc; /* 플레이스홀더 색상 */
}

.search-bar input:focus {
  border-color: #F50000; /* 포커스 시 테두리 색상 변화 */
}

.search-button {
  padding: 9px 13.5px; /* 버튼 패딩 */
  border: none;
  border-radius: 5px; /* 전체 모서리 둥글게 */
  color: white; /* 버튼 글자 색상 */
  cursor: pointer; /* 커서 변경 */
  transition: background-color 0.3s, transform 0.2s; /* 배경 색상 변화 애니메이션 */
  position: relative;
  left: -140px;
}

.search-button:hover {
  transform: scale(1.05); /* 호버 시 버튼 확대 효과 */
}

.login-area {
  display: flex;
  align-items: center;
  gap: 30px;
  position: relative;
  left: -90px;
  font-size: 15px;
}

.login-link {
  color: white; /* 링크 색상 */
  text-decoration: none; /* 밑줄 제거 */
  cursor: pointer; /* 커서 변경 */
}

.login-link:hover {
  color: #F50000; /* 호버 시 색상 변화 */
}
.logout-link{
  color: white; /* 링크 색상 */
  text-decoration: none; /* 밑줄 제거 */
  cursor: pointer; /* 커서 변경 */
}
.logout-link:hover{
  color: #F50000; /* 호버 시 색상 변화 */
}
.login-area span {
  color: white;
  font-size: 13.5px;
}
</style>
