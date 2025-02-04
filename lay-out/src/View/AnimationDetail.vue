<template>
  <div class="animation-detail">
    <div class="animation-backdrop">
      <div class="animation-poster">
        <img :src="'https://image.tmdb.org/t/p/original' + animation.posterPath" alt="포스터" class="poster-image" />
      </div>
      <div class="animation-content">
        <div class="info-item">
          <span class="info-label">제목</span>
          <p class="info-text">{{ animation.name }}</p>
        </div>
        <div class="animation-info">
          <div class="info-item">
            <span class="info-label">평점</span>
            <p class="info-text">{{ animation.voteAverage }}</p>
          </div>
          <div class="info-item">
            <span class="info-label">감독</span>
            <p class="info-text">
              <span v-if="animation.directors.length > 0">
                {{ animation.directors.map(d => d.name).join(', ') }}
              </span>
              <span v-else>정보 없음</span>
            </p>
          </div>
          <div class="info-item">
            <span class="info-label">줄거리</span>
            <p class="info-text">{{ animation.overview || '설명 없음' }}</p>
          </div>
        </div>
      </div>
      <div class="trailer-section" v-if="animation.videos && animation.videos.length > 0">
        <iframe
            width="560"
            height="315"
            :src="'https://www.youtube.com/embed/' + animation.videos[0].videoKey"
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
      animation: {},
    };
  },
  created() {
    this.fetchAnimationDetails();
  },
  watch: {
    '$route.params.id': 'fetchAnimationDetails'
  },
  methods: {
    async fetchAnimationDetails() {
      const animationId = this.$route.params.id;
      try {
        const response = await axios.get(`http://localhost:8081/animations/${animationId}`);
        this.animation = response.data;
      } catch (error) {
        console.error('애니메이션 상세 정보를 가져오는 중 오류가 발생했습니다:', error);
      }
    },
    viewReviews() {
      console.log('감상평 보기 클릭됨');
    },
    viewReview() {
      console.log('리뷰 보기 클릭됨');
    },
    addToFavorites() {
      console.log('찜하기 클릭됨');
    },
    goBack() {
      window.location.href = '/';
    }
  }
}
</script>

<style scoped>
.animation-detail {
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

.animation-backdrop {
  display: flex;
  align-items: flex-start;
}

.animation-poster {
  margin-right: 20px;
}

.poster-image {
  max-width: 300px;
  height: auto;
  border-radius: 8px;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.3);
}

.animation-content {
  max-width: 600px;
  text-align: left;
}

.animation-info {
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
  gap: 10px;
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

.trailer-section {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  margin-top: 20px;
  position: relative;
  left: 10%;
  top: -40px;
}

.trailer-iframe {
  border-radius: 8px;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.3);
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

</style>
