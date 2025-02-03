<template>
  <div class="movie-detail">
    <div class="movie-backdrop">
      <div class="movie-poster">
        <img :src="'https://image.tmdb.org/t/p/original' + movie.posterPath" alt="포스터" />
      </div>
      <div class="movie-content">
        <h2>{{ movie.title }}</h2>
        <div class="movie-info">
          <div class="info-item">
            <h4>평점</h4>
            <p>{{ movie.voteAverage }}</p>
          </div>
          <div class="info-item">
            <h4>출연진</h4>
            <ul>
              <li v-for="actor in movie.actors" :key="actor.id">
                {{ actor.name }} (역: {{ actor.role }})
              </li>
            </ul>
          </div>
          <div class="info-item">
            <h4>감독</h4>
            <!-- 감독 정보 추가 -->
          </div>
          <div class="info-item">
            <h4>출시일</h4>
            <p>{{ movie.releaseDate }}</p>
          </div>
          <div class="info-item">
            <h5>줄거리</h5>
            <p>{{ movie.overview || '설명 없음' }}</p>
          </div>
        </div>
      </div>
    </div>
    <div class="action-buttons">
      <button @click="viewReviews">감상평 보기</button>
      <button @click="viewReview">리뷰 보기</button>
      <button @click="addToFavorites">찜하기</button>
    </div>
    <div class="bottom-section">
      <button @click="goBack" class="back-button">뒤로 가기</button>
      <h3>바로가기</h3>
      <div class="streaming-services">
        <img class="streaming-logo" src="path/to/tving-logo.png" alt="티빙" />
        <img class="streaming-logo" src="path/to/netflix-logo.png" alt="넷플릭스" />
        <!-- 추가 스트리밍 서비스 이미지 -->
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
  watch: {
    '$route.params.id': 'fetchMovieDetails' // URL 매개변수 변경 시 데이터 다시 로드
  },
  methods: {
    async fetchMovieDetails() {
      const movieId = this.$route.params.id;
      try {
        const response = await axios.get(`http://localhost:8081/movies/${movieId}`);
        this.movie = response.data;
      } catch (error) {
        console.error('영화 상세 정보를 가져오는 중 오류가 발생했습니다:', error);
      }
    },
    goBack() {
      window.location.href = '/'; // 페이지를 새로 고침
    }
  }
}
</script>

<style scoped>
.movie-detail {
  color: white;
  background-color: black; /* 배경을 어두운 색으로 설정 */
  display: flex;
  flex-direction: column; /* 세로 방향으로 정렬 */
  height: 1000px;
  padding: 20px;
  border-radius: 10px; /* 모서리 둥글게 */
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.5); /* 그림자 효과 */
}

.movie-backdrop {
  display: flex;
  align-items: flex-start; /* 포스터와 내용 정렬 */
}

.movie-poster {
  margin-right: 20px; /* 포스터와 내용 간 간격 */
}

.movie-poster img {
  max-width: 300px; /* 포스터의 최대 너비 설정 */
  height: auto; /* 비율 유지 */
  border-radius: 8px; /* 포스터의 모서리 둥글게 */
}

.movie-content {
  max-width: 600px;
  text-align: left; /* 왼쪽 정렬 */
}

.movie-info {
  margin-top: 20px;
}

.info-item {
  margin-bottom: 15px; /* 항목 간 간격 */
}

.info-item h3 {
  margin: 0;
  font-size: 1.2em; /* 제목 크기 조정 */
  color: #f0f0f0; /* 제목 색상 */
}

.action-buttons {
  margin-top: 20px; /* 포스터 아래 간격 */
  display: flex; /* 버튼을 가로로 나열 */
}

.action-buttons button {
  margin-right: 10px; /* 버튼 간격 */
  padding: 10px 15px;
  background-color: #1a1a1a;
  border: none;
  color: white;
  cursor: pointer;
  border-radius: 5px;
}

.action-buttons button:hover {
  background-color: #555;
}

.back-button {
  margin-top: 20px;
  padding: 10px 15px;
  background-color: #1a1a1a;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

.back-button:hover {
  background-color: #555;
}

.bottom-section {
  margin-top: 20px; /* 포스터와 버튼 사이 간격 */
}

.streaming-services {
  display: flex;
  margin-top: 10px;
}

.streaming-logo {
  width: 150px; /* 로고 크기 조정 */
  height: auto; /* 비율 유지 */
  margin-right: 10px; /* 로고 간격 */
}
</style>
