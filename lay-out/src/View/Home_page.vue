<template>
  <body>
  <div class="movie-container">
    <h1>CINEHIVE</h1>

    <!-- 검색창 -->
    <div class="search-bar">
      <input
          type="text"
          v-model="searchQuery"
          placeholder="영화 제목을 검색하세요..."
      />
      <button @click="searchMovies">검색</button>
    </div>

    <!-- 영화 포스터 -->
    <div class="movie-slider">
      <div
          class="movie-poster"
          v-for="movie in movies"
          :key="movie.id"
          @click="openMovieDetails(movie)"
      >
        <img :src="'https://image.tmdb.org/t/p/w300' + movie.posterPath" alt="movie poster" />
      </div>
    </div>

    <!-- 영화 정보 모달 -->
    <div v-if="selectedMovie" class="movie-modal" @click.self="closeMovieDetails">
      <!-- 배경 이미지 추가 -->
      <div
          class="movie-modal-backdrop"
          :style="{
            backgroundImage: 'url(https://image.tmdb.org/t/p/original' + selectedMovie.backdropPath + ')',
          }"
      >
        <!-- 모달 컨텐츠 -->
        <div class="movie-modal-content">
          <h2>{{ selectedMovie.title }}</h2>
          <p>{{ selectedMovie.overview || '설명 없음' }}</p>
          <p>평점: {{ selectedMovie.voteAverage }}</p>
          <p>출시일: {{ selectedMovie.releaseDate }}</p>
          <button @click="closeMovieDetails">닫기</button>
        </div>
      </div>
    </div>
  </div>
  </body>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      movies: [],
      selectedMovie: null, // 선택된 영화 정보
      searchQuery: "", // 검색어
    };
  },
  methods: {
    // 영화 데이터 가져오기
    async fetchMovies() {
      axios.get('http://localhost:8081/movies')
          .then(response => {
            this.movies = response.data;
            console.log(response.data); // 받아온 데이터로 movies 배열 채우기
          })
          .catch(error => {
            console.error('영화 데이터를 가져오는 중 오류가 발생했습니다:', error);
          });
    },
    // 영화 클릭 시 상세 정보 모달 표시
    openMovieDetails(movie) {
      this.selectedMovie = movie;
    },
    // 영화 정보 모달 닫기
    closeMovieDetails() {
      this.selectedMovie = null;
    },
  },
  // 컴포넌트가 로드될 때 영화 데이터를 자동으로 가져옴
  mounted() {
    this.fetchMovies();
  },
};
</script>

<style scoped>
/* 전체 화면 검은색 배경 */
html, body {
  height: 100%;
  margin: 0;
  background-color: black;
  color: white;
}

h1 {
  color: red; /* 제목을 빨간색으로 변경 */
  text-align: center;
}

/* 영화 포스터 슬라이드 */
.movie-container {
  padding: 20px;
  display: flex;
  justify-content: center;  /* 가로 중앙 정렬 */
  align-items: center;      /* 세로 중앙 정렬 */
  flex-direction: column;   /* 수직 배치 */
  height: 100%;             /* 부모 컨테이너가 화면 전체를 차지하도록 */
}

.movie-slider {
  display: flex;
  flex-wrap: wrap; /* 줄 바꿈이 가능하게 */
  justify-content: center;  /* 포스터들을 가로로 중앙 정렬 */
  gap: 10px;
  margin-bottom: 20px;
}

.movie-poster img {
  width: 200px;
  height: 300px; /* 세로 크기도 고정 */
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
  background-color: rgba(0, 0, 0, 0.8); /* 투명한 검정 배경 */
  padding: 20px;
  border-radius: 8px;
  max-width: 600px;
  text-align: center;
  color: white;
}

.movie-modal-content button {
  margin-top: 20px;
  padding: 10px;
  background-color: #444;
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
  background-color: black; /* 검색창 배경색 */
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
