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
      <button @click="displayPreferredGenres" style="position: relative; top:200px;">선호 장르 표시</button>
      <!-- 영화 포스터 -->
      <div class="movie-slider">
        <div
            class="movie-poster"
            v-for="movie in movies"
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
    };
  },
  computed: {
    ...mapState(['user']),
  },
  methods: {
    async fetchMovies() {
      try {
        const response = await axios.get('http://localhost:8081/movies');
        this.movies = response.data;
      } catch (error) {
        console.error('영화 데이터를 가져오는 중 오류가 발생했습니다:', error);
      }
    },
    goToMovieDetail(movieId) {
      this.$router.push({ name: 'MovieDetail', params: { id: movieId } });
    },
    displayPreferredGenres() {
      if (this.user) {
        console.log('선호 장르:', this.user.preferredGenres);
      }
    },
  },
  mounted() {
    this.fetchMovies();
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

.bounce {
  display: inline-block;
  animation: bounce 0.5s ease forwards;
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
  justify-content: center;
  align-items: center;
  flex-direction: column;
}

.movie-slider {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  gap: 10px;
  margin-bottom: 20px;
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

/* 영화 정보 모달 */
.movie-modal {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.8);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.movie-modal-content {
  background-color: rgba(0, 0, 0, 0.8);
  padding: 20px;
  border-radius: 8px;
  max-width: 600px;
  text-align: center;
  color: white;
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

.movie-modal-backdrop {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.movie-modal-content button:hover {
  background-color: #555;
}

.search-bar {
  width: 100%;
  padding: 10px;
  display: flex;
  justify-content: center;
  background-color : black;
  position: sticky; /* 상단 고정 */
  top: 0;
  z-index: 100;
}

.search-bar input {
  width: 80%; /* 검색창 너비 */
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
