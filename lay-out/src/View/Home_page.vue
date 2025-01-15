<template>
  <div>
    <h1>현재 상영중인 영화</h1>
    <!-- 버튼을 클릭해서 데이터 로드 -->
    <button @click="fetchMovies">영화 데이터 불러오기</button>

    <div v-if="movies.length > 0">
      <ul>
        <li v-for="movie in movies" :key="movie.id">
          <h2>{{ movie.title }}</h2>
          <p>{{ movie.overview || '설명 없음' }}</p>
          <img v-if="movie.poster_path" :src="'https://image.tmdb.org/t/p/w300' + movie.poster_path" alt="poster" />
          <p>평점: {{ movie.vote_average }}</p>
          <p>출시일: {{ movie.release_date }}</p>
          <p>포스터: <img v-if="movie.poster_path" :src="'https://image.tmdb.org/t/p/w300' + movie.poster_path" alt="poster" /></p>
        </li>
      </ul>
    </div>
    <div v-else>
      <p>영화 정보가 없습니다.</p>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      movies: [],
    };
  },
  methods: {
    // 영화 데이터 가져오기
    fetchMovies() {
      axios.get('http://localhost:8081/movies')
          .then(response => {
            this.movies = response.data;
            console.log(response.data);// 받은 데이터로 movies 배열 채우기
          })
          .catch(error => {
            console.error('영화 데이터를 가져오는 중 오류가 발생했습니다:', error);
          });
    },
  },
};
</script>
