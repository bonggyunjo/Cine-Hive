<template>
  <div class="drama-detail">
    <div class="drama-backdrop">
      <div class="drama-poster">
        <img :src="'https://image.tmdb.org/t/p/original' + drama.posterPath" alt="포스터" class="poster-image" />
      </div>
      <div class="drama-content">
        <div class="info-item">
          <span class="info-label">제목</span>
          <p class="info-text">{{ drama.name }}</p>
        </div>
        <div class="drama-info">
          <div class="info-item">
            <span class="info-label">인기 지수</span>
            <p class="info-text">{{ drama.popularity }}</p>
          </div>
          <div class="info-item">
            <span class="info-label">감독</span>
            <p class="info-text">
              <span v-if="drama.directors.length > 0">
                {{ drama.directors.map(director => director.name).join(', ') }}
              </span>
              <span v-else>정보 없음</span>
            </p>
          </div>
          <div class="info-item">
            <span class="info-label">출연진</span>
          </div>
          <div class="info-item">
            <span class="info-label">줄거리</span>
            <p class="info-text">{{ drama.overview || '설명 없음' }}</p>
          </div>
        </div>
      </div>
      <div class="trailer-section" v-if="drama.videos && drama.videos.length > 0">
        <iframe
            width="560"
            height="315"
            :src="'https://www.youtube.com/embed/' + drama.videos[0].videoKey"
            frameborder="0"
            allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture"
            allowfullscreen
            class="trailer-iframe"
        ></iframe>
      </div>
    </div>
    <div class="action-buttons">
      <button class="action-button" @click="viewReviews">감상평 보기</button>
      <button class="action-button" @click="viewReview">리뷰 보기</button>
      <button class="action-button" @click="addToFavorites">찜하기</button>
      <button class="action-button" @click="goBack">뒤로 가기</button>
    </div>
    <div class="bottom-section">
      <h3 class="section-title">바로가기</h3>
      <div class="streaming-services">
        <img class="streaming-logo" src="../assets/movieDetailLogo/bing.png" alt="Wave" @click="goToLink('https://www.watcha.com')" />
        <img class="streaming-logo" src="../assets/movieDetailLogo/wiki.png" alt="Watcha" @click="goToLink('https://www.wavve.com')" />
        <img class="streaming-logo" src="../assets/movieDetailLogo/netflix.png" alt="Netflix" @click="goToLink('https://www.netflix.com')" />
        <img class="streaming-logo" src="../assets/movieDetailLogo/tiving.png" alt="Tiving" @click="goToLink('https://www.tving.com')" />
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
      drama: {}, // 영화 대신 drama로 수정
    };
  },
  created() {
    this.fetchDramaDetails();
  },
  watch: {
    '$route.params.id': 'fetchDramaDetails' // URL 매개변수 변경 시 데이터 다시 로드
  },
  methods: {
    async fetchDramaDetails() {
      const dramaId = this.$route.params.id; // 영화 대신 드라마 아이디 사용
      try {
        const response = await axios.get(`http://localhost:8081/dramas/${dramaId}`); // 드라마 API 엔드포인트로 변경
        this.drama = response.data;
      } catch (error) {
        console.error('드라마 상세 정보를 가져오는 중 오류가 발생했습니다:', error);
      }
    },
    viewReviews() {
      // 여기에 감상평 보기 동작 추가
      console.log('감상평 보기 클릭됨');
    },
    viewReview() {
      // 리뷰 보기 동작 추가
      console.log('리뷰 보기 클릭됨');
    },
    addToFavorites() {
      // 찜하기 동작 추가
      console.log('찜하기 클릭됨');
    },
    goBack() {
      window.location.href = '/'; // 페이지를 새로 고침
    },
    goToLink(url) {
      window.open(url, '_blank'); // 새 탭에서 링크 열기
    }
  }
}
</script>


<style scoped>
.drama-detail {
  color: white;
  background-color: black;
  display: flex;
  flex-direction: column;
  height: 1000px;
  padding: 40px 60px;
  border-radius: 10px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.5);
  margin-left: 40px;
  margin-top: 40px;
}

.drama-backdrop {
  display: flex;
  align-items: flex-start;
}

.drama-poster {
  margin-right: 20px;
}

.poster-image {
  max-width: 300px;
  height: auto;
  border-radius: 8px;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.3);
}

.drama-content {
  max-width: 600px;
  text-align: left;
}

.drama-info {
  margin-top: 20px;
}

.info-item {
  margin-bottom: 15px;
}

.info-label {
  font-weight: bolder;
  margin-bottom: 10px;
  font-size: 14px;
  color: #f0f0f0;
}

.info-text {
  margin: 0;
  font-size: 16px;
  color: #ccc;
}

.action-buttons {
  margin-top: 20px;
  display: flex;
  gap: 10px; /* 버튼 간격 */
}

.action-button {
  padding: 10px 15px;
  background-color: #1a1a1a;
  border: none;
  color: white;
  cursor: pointer;
  border-radius: 5px;
  transition: background-color 0.3s;
}

.action-button:hover {
  background-color: #555;
}

.bottom-section {
  margin-top: 20px;
  position: relative;
  top: 50px;
}

.section-title {
  position: relative;
  left:-47.8%;
  top:-10px;
  margin-bottom: 10px;
  font-size: 18px;
  color: #f0f0f0;
}

.streaming-services {
  display: flex;
  gap: 20px; /* 로고 간격 */
}

.streaming-logo {
  width: 75px;
  height: 70px;
  border-radius: 8px;
  transition: transform 0.3s;
}

.streaming-logo:hover {
  transform: scale(1.1);
}

.trailer-section {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  margin-top: 20px;
  position: relative;
  left: 10%;
  top: -40px;
  position: relative;
}

.trailer-iframe {
  border-radius: 8px;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.3);
}
</style>
