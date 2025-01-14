<template>
  <div>
    <h1>Movies List</h1>
    <button @click="fetchMovies">Get Movies</button>
    <ul>
      <li v-for="movie in movies" :key="movie.id">
        {{ movie.title }} - {{ movie.overview }}
      </li>
    </ul>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      movies: [],  // 영화 목록을 저장할 배열
    };
  },
  methods: {
    fetchMovies() {
      axios
          .get('http://localhost:8080/save-movies')  // Spring 백엔드 API 호출
          .then(response => {
            console.log(response.data);
            // 영화 목록을 받아서 movies 배열에 저장
            this.movies = response.data.results;
          })
          .catch(error => {
            console.error("There was an error fetching the movies:", error);
          });
    },
  },
};
</script>

<style scoped>
/* 이곳에서 CSS 스타일을 추가할 수 있습니다 */
</style>
