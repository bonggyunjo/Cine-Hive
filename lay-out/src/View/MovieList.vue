<template>
  <div class="movie-list">
    <h1>검색한 영화 목록</h1>
    <div class="top-slider">
      <div
          class="movie-card"
          v-for="movie in movies"
          :key="movie.id"
          @click="goToMovieDetail(movie.id)"
      >
        <img :src="'https://image.tmdb.org/t/p/w300' + movie.posterPath" alt="movie poster" class="movie-poster" />
        <div class="movie-info">
          <h3 class="movie-title">{{ movie.title }}</h3>
          <p class="info-text">{{ movie.director ? movie.director.name : '정보 없음' }}</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'MovieList',
  data() {
    return {
      movies: [],
    };
  },
  created() {
    this.fetchMovies();
  },
  methods: {
    async fetchMovies() {
      try {
        const response = await axios.get('http://localhost:8081/movies'); // 영화 데이터 API 호출
        this.movies = response.data; // 응답 데이터를 저장
      } catch (error) {
        console.error('영화 데이터를 가져오는 중 오류가 발생했습니다:', error);
      }
    },
    goToMovieDetail(movieId) {
      this.$router.push({path: `/movie/${movieId}`}); // 영화 세부 정보 페이지로 이동
    }
  }
}
</script>

<style scoped>
.movie-list {
  padding: 20px;
  background-color: black; /* 어두운 배경 */
  color: white;
}

.movie-list h1{
  text-align: left;
  color: white;
  font-size: 19px;
  position: relative;
  left:3.5%;
}
h1 {
  text-align: center;
  margin-bottom: 30px;
  font-family: 'Arial', sans-serif;
  color: #F50000; /* 제목 색상 */
  font-size: 2.5rem; /* 제목 크기 */
  text-transform: uppercase; /* 대문자 변환 */
}

.top-slider {
  display: flex;
  flex-wrap: wrap;
  justify-content: center; /* 중앙 정렬 */
  gap: 30px; /* 카드 간격 */
}

.movie-card {
  cursor: pointer;
  width: 220px; /* 카드 너비 */
  border-radius: 12px;
  overflow: hidden; /* 테두리 둥글게 */
  transition: transform 0.3s, box-shadow 0.3s; /* 호버 시 효과 */
  background-color: #1e1e1e; /* 카드 배경 */
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.6); /* 그림자 효과 */
  position: relative; /* 포스터 아래 선 위치 설정 */
}

.movie-card:hover {
  transform: translateY(-8px); /* 호버 시 카드 상승 효과 */
  box-shadow: 0 8px 40px rgba(0, 0, 0, 0.8); /* 호버 시 그림자 효과 */
}

.movie-poster {
  width: 100%;
  height: 320px; /* 포스터 높이 */
  object-fit: cover; /* 이미지 비율 유지 */
  border-bottom: 2px solid #F50000; /* 포스터 아래 선 */
}

.movie-info {
  padding: 15px; /* 내부 여백 */
  text-align: center; /* 중앙 정렬 */
  background-color: rgba(30, 30, 30, 0.9); /* 반투명 배경 */
}

.movie-title {
  font-size: 1.5rem; /* 제목 크기 */
  font-weight: bold;
  color: #F50000; /* 제목 색상 */
  margin: 5px 0; /* 위 아래 여백 */
  text-overflow: ellipsis; /* 제목 오버플로우 처리 */
  overflow: hidden; /* 오버플로우 숨기기 */
  white-space: nowrap; /* 한 줄로 표시 */
}

.info-text {
  font-size: 1rem; /* 감독 이름 크기 */
  color: #ccc; /* 감독 색상 */
  margin: 5px 0; /* 위 아래 여백 */
  font-style: italic; /* 이탤릭체로 강조 */
}
</style>
