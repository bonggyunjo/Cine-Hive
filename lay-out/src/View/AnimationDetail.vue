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
<!--          <div class="info-item">-->
<!--            <span class="info-label">제작사</span>-->
<!--            <p class="info-text">{{ animation.productionCompany || '정보 없음' }}</p>-->
<!--          </div>-->
<!--          <div class="info-item">-->
<!--            <span class="info-label">개봉일</span>-->
<!--            <p class="info-text">{{ animation.releaseDate }}</p>-->
<!--          </div>-->
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
  height: auto;
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
}

.trailer-iframe {
  border-radius: 8px;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.3);
}
</style>
