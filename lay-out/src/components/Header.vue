<template>
  <header>
    <div class="header-container">
      <router-link to="/" class="site-title">
        <span @click="goToHome">CINEHIVE</span>
      </router-link>

      <nav class="nav">
        <ul style="cursor: pointer">
          <li @click="goToAnimationsList" >Animation</li>
          <li @click="goToMoviesList" >Movie</li>
          <li @click="goToDramasList">Drama</li>
          <li >Community</li>
          <li >My List</li>
          <li >Watched List</li>
        </ul>
      </nav>


      <SearchBar class="search-bar-info" ></SearchBar>

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

  </header>
</template>

<script>
import axios from 'axios';
import { mapState } from 'vuex';
import SearchBar from "@/components/SearchBar.vue";

export default {
  name: 'HeaderComponent',
  components: {SearchBar},
  data() {
    return {
      searchQuery: "", // 검색어
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

    goToHome() {
      if (this.$route.path !== '/') {
      this.$router.push('/');
      }
    },
    async getUserInfo(loginType) {
      try {
        const response = await axios.get(`http://localhost:8081/api/auth/${loginType}/success`, {
          withCredentials: true
        });

        // API 응답에서 필요한 데이터 추출
        const userData = response.data;
        console.log(`${loginType} 로그인 사용자 데이터:`, userData);

        const finalLoginType = userData.mem_type || loginType;

        // Vuex에 사용자 정보와 loginType 저장
        this.$store.commit('SET_LOGIN', {
          isLoggedIn: true,
          user: {
            id: userData.memUserid,
            email: userData.email || '',
            nickname: userData.nickname,
            name: userData.name || '', // 이름 추가
            preferredGenres: userData.genres || [] // 장르 추가
          },
          loginType: finalLoginType // 여기서 loginType을 추가 (일반 타입 추가)
        });


        // 로컬 스토리지에 사용자 정보와 loginType 저장
        localStorage.setItem('isLoggedIn', 'true');
        localStorage.setItem('user', JSON.stringify({
          id: userData.memUserid,
          email: userData.email || '',
          nickname: userData.nickname,
          name: userData.name || '',
          preferredGenres: userData.genres || [],
          mem_type: userData.mem_type
        }));
        localStorage.setItem('loginType', finalLoginType); // loginType도 저장

      } catch (error) {
        console.error(`${loginType} 사용자 정보 가져오기 실패:`, error);
      }
    },
    logout() {
      // Vuex에서 로그인 상태 초기화
      this.$store.commit('SET_LOGOUT');

      // 로컬/세션 스토리지 데이터 삭제
      localStorage.removeItem('isLoggedIn');
      localStorage.removeItem('user');
      localStorage.removeItem('loginType');

      axios.get('http://localhost:8081/api/auth/logout', {withCredentials: true})
          .then(() => {
            console.log("로그아웃 성공");

            window.location.reload();
          })
          .catch(error => {
            console.error("로그아웃 오류:", error);
          });
    },
    goToMoviesList(){
      if(this.$route.path!=='/movies')
      this.$router.push({ path: '/movies' });
    },
    goToAnimationsList(){
      if(this.$route.path!=='/animations')
      this.$router.push({ path: '/animations' });
    },
    goToDramasList(){
      if(this.$route.path!=='/dramas')
        this.$router.push({ path: '/dramas' });
    },
  },

  created() {
    if (this.isLoggedIn) {
      // 일반 로그인 사용자는 별도 API 호출 없이 localStorage에서 복구
      const user = JSON.parse(localStorage.getItem('user'));
      const loginType = user?.mem_type || localStorage.getItem('loginType');

      this.$store.commit('SET_LOGIN', {
        isLoggedIn: true,
        user,
        loginType
      });
    } else {
      // OAuth 로그인이 아닐 경우, 따로 API 호출 필요 없음
      this.getUserInfo('kakao');
      this.getUserInfo('google');
      this.getUserInfo('naver');
    }
  }
};
</script>

<style scoped>


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

.search-bar-info{
  position: relative;
  top:0px;
  left: 10px;
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
    cursor: pointer;
  }

  .login-area {
    flex-direction: column;
    align-items: center;
    gap: 5px;
    position: relative;
  }
}

.signup-link{
  text-decoration: none;
  color: white;
  position: relative;
  top:0.5px;
  font-size: 14.5px;
}
.signup-link:hover{
  color: red;
}
</style>
