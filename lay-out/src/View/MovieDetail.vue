<template>
  <div class="movie-detail">
    <div class="movie-backdrop">
      <div class="movie-poster">
        <img :src="'https://image.tmdb.org/t/p/original' + movie.posterPath" alt="포스터"/>
      </div>
      <div class="movie-content">
        <div class="info-item">
          <span>제목</span>
          <p>{{ movie.title }}</p>
        </div>
        <div class="movie-info">

          <div class="info-item">
            <span>평점</span>
            <p>{{ movie.voteAverage }}</p>
          </div>
          <div class="info-item">
            <span>출연진</span>
            <div class="actors-list">
              <span v-for="actor in movie.actors" :key="actor.id" class="actor-item">
                {{ actor.name }}
              </span>
            </div>
          </div>


          <div class="info-item">
            <span>감독</span>
            <!-- 감독 정보 추가 -->
          </div>
          <div class="info-item">
            <span>출시일</span>
            <p>{{ movie.releaseDate }}</p>
          </div>
          <div class="info-item">
            <span>줄거리</span>
            <p>{{ movie.overview || '설명 없음' }}</p>
          </div>
        </div>
      </div>
    </div>
    <div class="action-buttons">
      <button @click="viewReviews">감상평 보기</button>
      <button @click="viewReview">리뷰 보기</button>
      <button @click="addToFavorites">찜하기</button>
      <button @click="goBack">뒤로 가기</button>
    </div>
    <div class="bottom-section">
      <h3>바로가기</h3>
      <div class="streaming-services">
        <img class="streaming-logo" src="../assets/movieDetailLogo/wiki.png"  />
        <img class="streaming-logo" src="../assets/movieDetailLogo/bing.png" />
        <img class="streaming-logo" src="../assets/movieDetailLogo/netflix.png" />
        <img class="streaming-logo" src="../assets/movieDetailLogo/tiving.png" />
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
  padding: 40px 60px; /* 위, 아래, 왼쪽, 오른쪽 여백을 조정하여 위치 이동 */
  border-radius: 10px; /* 모서리 둥글게 */
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.5); /* 그림자 효과 */
  margin-left: 40px; /* 왼쪽 여백 추가 */
  margin-top: 40px; /* 위쪽 여백 추가 */
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
  position: relative;
}

.back-button:hover {
  background-color: #555;
}

.bottom-section {
  margin-top: 20px; /* 포스터와 버튼 사이 간격 */
  position: relative;
  top:50px;
}

.streaming-services {
  display: flex;
  margin-top: 10px;
}

.streaming-logo {
  width: 75px; /* 로고 크기 조정 */
  height: 70px; /* 비율 유지 */
  margin-right: 50px; /* 로고 간격 */
}
.actors-list {
  display: flex; /* 가로 방향으로 정렬 */
  flex-wrap: wrap; /* 줄바꿈 허용 */
  margin-top: 10px; /* 위쪽 여백 추가 */
}

.actor-item {
  background-color: rgba(255, 255, 255, 0.1); /* 배경색 추가 */
  border-radius: 5px; /* 둥근 모서리 */
  padding: 5px 10px; /* 패딩 추가 */
  margin-right: 10px; /* 간격 추가 */
  margin-bottom: 5px; /* 아래쪽 간격 추가 */
  color: white; /* 글자색 */
  transition: background-color 0.3s; /* 배경색 변화 효과 */
}

.actor-item:hover {
  background-color: rgba(255, 255, 255, 0.3); /* 호버 시 배경색 변화 */
}

.bottom-section h3{
  position: relative;
  left:-48%;
}
.info-item span{
  font-weight: bolder;
  margin-bottom: 10px;
  font-size: 14px;
}
</style>
