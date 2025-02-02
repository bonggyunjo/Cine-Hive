<template>
  <div class="movie-detail">
    <div class="movie-backdrop" :style="{ backgroundImage: 'url(https://image.tmdb.org/t/p/original' + movie.backdropPath + ')' }">
      <div class="movie-content">
        <h2>{{ movie.title }}</h2>
        <p>{{ movie.overview || '설명 없음' }}</p>
        <p>평점: {{ movie.voteAverage }}</p>
        <p>출시일: {{ movie.releaseDate }}</p>
        <button @click="goBack">뒤로 가기</button>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      movie: {},
    };
  },
  created() {
    this.fetchMovieDetails();
  },
  methods: {
    async fetchMovieDetails() {
      const movieId = this.$route.params.id;
      try {
        const response = await axios.get(`http://localhost:8081/movies/${movieId}`);
        console.log('API 응답 데이터:', response.data); // 응답 데이터 로그 출력
        this.movie = response.data;
      } catch (error) {
        console.error('영화 상세 정보를 가져오는 중 오류가 발생했습니다:', error);
      }
    },
    goBack() {
      this.$router.go(-1); // 뒤로 가기
    },
  },
};
</script>

<style scoped>
.movie-detail {
  color: white;
}

.movie-backdrop {
  position: relative;
  height: 100vh;
  background-size: cover;
  background-position: center;
  display: flex;
  justify-content: center;
  align-items: center;
}

.movie-content {
  background-color: rgba(0, 0, 0, 0.8);
  padding: 20px;
  border-radius: 8px;
  max-width: 600px;
  text-align: center;
}

.movie-content button {
  margin-top: 20px;
  padding: 10px;
  background-color: #1a1a1a;
  border: none;
  color: white;
  cursor: pointer;
  border-radius: 5px;
}

.movie-content button:hover {
  background-color: #555;
}
</style>