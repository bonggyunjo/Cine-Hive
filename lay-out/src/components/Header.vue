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
          <span>myPage</span>
        </template>
        <template v-else>
          <router-link to="/auth" class="login-link">Login</router-link>
          <router-link to="/auth" style="text-decoration: none"><span>회원이 아니신가요?</span></router-link>
        </template>
      </div>
    </div>
  </header>
</template>

<script>
import axios from 'axios';
import { mapState } from 'vuex';

export default {
  name: 'HeaderComponent',
  data() {
    return {
      searchQuery: "", // 검색어
    };
  },
  computed: {
    ...mapState(['isLoggedIn']),
  },
  methods: {
    logout() {
      this.$store.dispatch('logout');
      localStorage.removeItem('token');
      if (this.$route.path !== '/') {
        this.$router.push('/');
      }
    },
    async searchMovies() {
      if (!this.searchQuery.trim()) {
        alert("검색어를 입력하세요!");
        return;
      }

      try {
        // 서버로 검색어를 보내고 결과를 받음
        const response = await axios.get('http://localhost:8081/search', {
          params: { query: this.searchQuery }
        });

        // 받은 데이터를 SearchPage로 전달
        this.$router.push({
          path: '/search',
          query: { q: this.searchQuery, results: JSON.stringify(response.data) }
        });
      } catch (error) {
        console.error("검색 중 오류가 발생했습니다:", error);
      }
    },
  },
};
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
.logout-link {
  color: white;
  text-decoration: none;
  cursor: pointer;
}

.login-link:hover,
.logout-link:hover {
  color: #F50000;
}

.login-area span {
  color: white;
  font-size: 13px;
  cursor: pointer;
}
.login-area span:hover{
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
</style>
