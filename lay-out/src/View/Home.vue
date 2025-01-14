// Vue.js 컴포넌트 예시

<template>
  <div>
    <button @click="fetchNowPlayingMovies">Now Playing Movies</button>
    <p>{{ message }}</p>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      message: ''
    };
  },
  methods: {
    fetchNowPlayingMovies() {
      const language = 'en-US'; // 예시 언어 코드
      const page = 1; // 예시 페이지 번호

      axios.get(`http://localhost:8081/now_playing`, {
        params: {
          language: language,
          page: page
        }
      })
          .then(response => {
            // 성공적으로 데이터가 저장되었을 때
            this.message = response.data; // 반환된 메시지 출력
          })
          .catch(error => {
            // 오류 처리
            this.message = 'Error occurred while fetching data.';
            console.error(error);
          });
    }
  }
};
</script>
