<template>
  <div id="homepage">
    <div class="main-image">
      <img src="@/assets/home/mainImageS2.jpg">
      <img src="@/assets/home/mainImage.jpg">
      <img src="@/assets/home/mainImageS3.jpg">
      <div class="main-text">
        <p>다양한 이야기들이 한자리에,</p>
        <p>이곳에서 진정한 감동과 재미를 느끼고,</p>
        <p>잊지 못할 순간들을 경험해보세요.</p>
      </div>
    </div>

    <div class="movie-container">
      <h2 class="section-title">인기 영화</h2>
      <div class="top-slider">
        <div
            class="movie-card"
            v-for="movie in movies"
            :key="movie.id"
            @click="goToMovieDetail(movie.id)"
        >
          <img :src="'https://image.tmdb.org/t/p/w300' + movie.posterPath" alt="movie poster" />
        </div>
      </div>

      <h2 class="section-title">역대 평점 영화</h2>
      <div class="top-slider">
        <div
            class="movie-card"
            v-for="movie in topmovies"
            :key="movie.id"
            @click="goToMovieDetail(movie.id, 'top')"
        >
          <img :src="'https://image.tmdb.org/t/p/w300' + movie.posterPath" alt="movie poster" />
        </div>
      </div>

      <h2 class="section-title">
        선호 장르
        <span class="more-info" @click="toggleShowMore" v-if="!showSearchButton">
          <span class="plus-sign">+</span>
          <span class="more-text" >{{ showMore ? '접기' : '더 보기' }}</span>
        </span>
      </h2>
      <SearchBar v-if="showSearchButton" @click="searchMovies"></SearchBar>

      <div v-if="!user" class="login-prompt-container">
        <p class="login-prompt">로그인을 하시면 선호하는 장르를 추천해드립니다.</p>
        <button class="login-button" @click="goToLogin">로그인</button>
      </div>
      <div v-else>
        <div class="prefer-slide">
          <div
              class="movie-card"
              v-for="content in prefer"
              :key="content.id"
              @click="goToContentDetail(content.id)"
          >
            <img :src="'https://image.tmdb.org/t/p/w300' + content.posterPath" alt="movie poster" />
          </div>
        </div>

        <!-- 더보기 클릭 후, 추가적으로 선호 장르를 보여주고 '검색하기' 버튼을 나타냄 -->
        <div v-if="showMore" class="prefer-slide">
          <div
              class="movie-card"
              v-for="content in prefer.slice(18)"
              :key="content.id"
              @click="goToMovieDetail(content.id)"
          >
            <img :src="'https://image.tmdb.org/t/p/w300' + content.posterPath" alt="movie poster" />
          </div>
        </div>


      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';
import { mapState } from 'vuex';
import SearchBar from "@/components/SearchBar.vue";
export default {
  components: {SearchBar},
  data() {
    return {
      movies: [],
      topmovies: [],
      prefer: [],
      showMore: false, // 더 보기 상태
      showSearchButton: false, // 검색 버튼 상태
      searchQuery:'',
    };
  },

  computed: {
    ...mapState(['user']),
  },
  methods: {
    async fetchMovies() {
      try {
        const response = await axios.get('http://localhost:8081/now_playing_movies');
        this.movies = response.data.slice(0, 18);
      } catch (error) {
        console.error('영화 데이터를 가져오는 중 오류가 발생했습니다:', error);
      }
    },

    async fetchTopmovies() {
      try {
        const response1 = await axios.get('http://localhost:8081/get_topmovies');
        this.topmovies = response1.data.slice(0, 18);
      } catch (error) {
        console.error('영화 데이터를 가져오는 중 오류가 발생했습니다:', error);
      }
    },
    goToMovieDetail(movieId, movieType) {
      if (movieType === 'top') {
        this.$router.push({name: 'TopMovieDetail', params: {id: movieId}});
      } else {
        this.$router.push({name: 'MovieDetail', params: {id: movieId}});
      }
    },
    async fetchPreferredGenres() {
      try {
        console.log('사용자의 선호 장르:', this.user.preferredGenres);
        const response = await axios.post('http://localhost:8081/preferredGenres', {
          genres: this.user.preferredGenres
        });
        console.log('선호 장르 데이터:', response.data);
        this.prefer = response.data.slice(0, 18);
      } catch (error) {
        console.error('선호 장르 데이터를 가져오는 중 오류가 발생했습니다:', error);
      }
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
    toggleShowMore() {
      this.showMore = !this.showMore; // 더 보기 상태 변경
      if (!this.showSearchButton) {
        this.showSearchButton = true; // '검색하기' 버튼 보이기
      }
    },

    goToContentDetail(movieId) {
      const selectedContent = this.prefer.find(content => content.id === movieId);
      if (selectedContent) {
        switch (selectedContent.genre) {
          case '드라마':
            this.$router.push({ name: 'DramaDetail', params: { id: movieId } });
            break;
          case '애니메이션':
            this.$router.push({ name: 'AnimationDetail', params: { id: movieId } });
            break;
          case '영화':
            this.$router.push({ name: 'MovieDetail', params: { id: movieId } });
            break;
          default:
            console.error('알 수 없는 장르입니다.');
        }
      }
    },

    goToSearchPage() {
      // 검색 페이지로 이동하는 로직 추가
      this.$router.push('/search');
    },

    goToLogin() {
      this.$router.push('/auth');
    },

  },
  watch: {
    user(newUser) {
      if (newUser && newUser.preferredGenres && newUser.preferredGenres.length > 0) {
        this.fetchPreferredGenres();
      }
    }
  },
  mounted() {
    this.fetchMovies();
    this.fetchTopmovies();
    if (this.user && this.user.preferredGenres && this.user.preferredGenres.length > 0) {
      this.fetchPreferredGenres();
    } else {
      console.log('선호 장르가 없거나 user 데이터가 없음');
    }
  }
};
</script>
<style scoped>

#homepage {
  height: 1200px;
  flex: 1;
  background-color : black;
  color: white;
}
.section-title {
  text-align: left;
  font-size: 24px;
  font-weight: bold;
  margin-bottom: 10px;
  padding-left: 10px;
}
.main-image {
  position: relative;
  top: 100px;
  margin-bottom: 150px;
}
.main-image img {
  opacity: 0.3;
}
.main-text {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  text-align: center;
  color: white;
  font-size: 28px;
  font-weight: bolder;
  text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.8);
  line-height: 2.2;
  white-space: pre-wrap;
  opacity: 0;
  transform: translate(-50%, -60%);
  animation: fadeInMove 2s ease-out forwards;
}

@keyframes fadeInMove {
  0% {
    opacity: 0;
    transform: translate(-50%, -60%);
  }
  100% {
    opacity: 1;
    transform: translate(-50%, -50%);
  }
}

@keyframes bounce {
  0% {
    transform: translateY(20px);
  }
  50% {
    transform: translateY(-10px);
    opacity: 1;
  }
  100% {
    transform: translateY(0);
  }
}

.main-text p {
  animation: fadeIn 0.5s forwards;
  opacity: 0;
}

@keyframes fadeIn {
  0% {
    opacity: 0;
  }
  100% {
    opacity: 1;
  }
}

.main-text p:nth-child(1) { animation-delay: 0.1s; }
.main-text p:nth-child(2) { animation-delay: 0.3s; }
.main-text p:nth-child(3) { animation-delay: 0.5s; }

h1 {
  color: red;
  text-align: center;
}

.movie-container {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.top-slider,
.movie-slider {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  gap: 10px;
  margin-bottom: 100px;
}

.movie-card {
  flex: 0 0 auto;
  width: 200px;
  cursor: pointer;
  transition: transform 0.3s ease-in-out;
}

.movie-card img {
  width: 100%;
  border-radius: 12px;
  box-shadow: 0 4px 8px rgba(255, 255, 255, 0.2);
}

.movie-card:hover {
  transform: scale(1.1);
}
.movie-poster img {
  width: 200px;
  height: 300px;
  cursor: pointer;
  transition: transform 0.3s;
}

.movie-poster img:hover {
  transform: scale(1.1);
}

.movie-modal-content button {
  margin-top: 20px;
  padding: 10px;
  background-color : #1a1a1a;
  border: none;
  color: white;
  cursor: pointer;
  border-radius: 5px;
}

.movie-modal-content button:hover {
  background-color: #555;
}

.search-bar input {
  width: 80%;
  max-width: 600px;
  padding: 10px;
  font-size: 16px;
  border: none;
  border-radius: 5px;
}

.search-bar input:focus {
  outline: none;
  box-shadow: 0 0 5px rgba(255, 0, 0, 0.8);
}

.login-prompt-container {
  position: relative;
  text-align: center;
  background-color: rgba(0, 0, 0, 0.7);
  padding: 40px 20px;
  border-radius: 10px;
  margin-top: 20px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.4);
  transform: scale(1.05);
  animation: fadeInContainer 1s ease-out;
}

@keyframes fadeInContainer {
  0% {
    opacity: 0;
    transform: scale(0.95);
  }
  100% {
    opacity: 1;
    transform: scale(1);
  }
}

.login-prompt {
  font-size: 20px;
  color: darkslateblue;
  font-weight: 600;
  line-height: 1.5;
  margin-bottom: 15px;
  animation: fadeInText 1s ease-out, bounceText 0.8s ease-in-out infinite;
}

@keyframes fadeInText {
  0% {
    opacity: 0;
    transform: translateY(10px);
  }
  100% {
    opacity: 1;
    transform: translateY(0);
  }
}

@keyframes bounceText {
  0% {
    transform: translateY(0);
  }
  50% {
    transform: translateY(-10px);
  }
  100% {
    transform: translateY(0);
  }
}

.login-button {
  padding: 15px 50px;
  background-color: darkslateblue;
  color: white;
  font-size: 15px;
  font-weight: bold;
  border: none;
  border-radius: 20px;
  cursor: pointer;
  transition: background-color 0.3s ease, transform 0.3s ease;
  margin-top: 15px;
}

.login-button:hover {
  background-color: #d1bb4a;
  transform: translateY(-2px);
}

.login-button:active {
  transform: translateY(2px);
}

.prefer-slide {
  display: flex;
  flex-wrap: wrap;
  gap: 15px;
  padding: 10px;
  justify-content: center;
}
.movie-card {
  flex: 0 0 calc(11.1% - 15px);
  max-width: calc(11.1% - 15px);
}

.more-info {
  display: inline-flex;
  align-items: center;
  margin-left: 10px;
}

.plus-sign {
  font-size: 22px;
  color: darkslateblue;
  margin-right: 5px;
  animation: pulse 1.5s ease-in-out infinite;
}

.more-text {
  background-color: transparent;
  border: 2px solid darkslateblue;;
  color: darkslateblue;;
  font-size: 15px;
  padding: 5px 15px;
  border-radius: 25px;
  cursor: pointer;
  transition: background-color 0.3s, color 0.3s;
  animation: bounce 1s infinite;
}

.more-text:hover {
  background-color: #1E1E1E;
  color: white;
}

@keyframes pulse {
  0%, 100% {
    transform: scale(1);
  }
  50% {
    transform: scale(1.2);
  }
}

@keyframes bounce {
  0%, 20%, 50%, 80%, 100% {
    transform: translateY(0);
  }
  40% {
    transform: translateY(-5px);
  }
  60% {
    transform: translateY(-3px);
  }
}

</style>