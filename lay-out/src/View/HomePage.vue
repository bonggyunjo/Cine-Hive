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
      <!-- 영화 포스터 -->
      <div class="movie-slider">
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

      <h2 class="section-title">선호 장르</h2>
      <div class="prefer-slide">
        <div
            class="movie-card"
            v-for="movie in prefer"
            :key="movie.id"
            @click="goToMovieDetail(movie.id)"
        >
          <img :src="'https://image.tmdb.org/t/p/w300' + movie.posterPath" alt="movie poster" />
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';
import { mapState } from 'vuex';

export default {
  data() {
    return {
      movies: [],
      topmovies: [],
      prefer: [],
    };
  },
  computed: {
    ...mapState(['user']),
  },
  methods: {
    async fetchMovies() {
      try {
        const response = await axios.get('http://localhost:8081/now_playing_movies');
        this.movies = response.data;
      } catch (error) {
        console.error('영화 데이터를 가져오는 중 오류가 발생했습니다:', error);
      }
    },

    async fetchTopmovies(){
      try {
        const response1 = await axios.get('http://localhost:8081/get_topmovies');
        this.topmovies = response1.data;
      } catch (error) {
        console.error('영화 데이터를 가져오는 중 오류가 발생했습니다:', error);
      }
    },

    goToMovieDetail(movieId, movieType) {
      if (movieType === 'top') {
        this.$router.push({ name: 'TopMovieDetail', params: { id: movieId } });
      } else {
        this.$router.push({ name: 'MovieDetail', params: { id: movieId } });
      }
    },
  },
  mounted() {
    this.fetchMovies();
    this.fetchTopmovies();
  },
};
</script>
<style scoped>


#homepage{
  height: 900px;
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
.main-image{
  position: relative;
  top:100px;
  margin-bottom: 150px;
}
.main-image img{
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

.movie-slider {
  display: flex;
  overflow-x: auto;
  padding: 10px;
  gap: 15px;
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
  box-shadow: 0 0 5px rgba(255, 0, 0, 0.8); /* 포커스 시 효과 */
}

</style>
