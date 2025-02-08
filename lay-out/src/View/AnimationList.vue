<template>
  <div class="animation-list">
    <h1>애니메이션 목록</h1>

    <div class="button-group">
      <span
          @click="resetSort"
          :class="['rating-button', { active: !sorted && !popularitySorted }]">전체</span> &nbsp;&nbsp;
      <span
          @click="sortByRating"
          :class="['rating-button', { active: sorted }]">평점 순</span> &nbsp;&nbsp;
      <span
          @click="sortByPopularity"
          :class="['rating-button', { active: popularitySorted }]">인기 순</span>
    </div>

    <div class="top-slider">
      <div
          class="animation-card"
          v-for="animation in sortedAnimations"
          :key="animation.id"
          @click="goToAnimationDetail(animation.id)"
      >
        <img :src="'https://image.tmdb.org/t/p/w300' + animation.posterPath" alt="animation poster" class="animation-poster" />
        <div class="animation-info">
          <h3 class="animation-title">{{ animation.name }}</h3>
          <p class="info-text">
            <span v-if="animation.directors.length > 0">
              {{ [...new Set(animation.directors.map(d => d.name))].join(', ') }}
            </span>
            <span v-else>정보 없음</span>
          </p>
          <p class="rating-text" v-if="sorted">평점: {{ animation.voteAverage.toFixed(1) }}</p> <!-- 평점 표시 (평점 순일 때만) -->
          <p class="popularity-text" v-if="popularitySorted">인기: {{ animation.popularity.toFixed(1) }}</p> <!-- 인기 표시 (인기 순일 때만) -->
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'AnimationList',
  data() {
    return {
      animations: [],
      sorted: false,
      popularitySorted: false, // 인기 정렬 상태 추가
    };
  },
  created() {
    this.fetchAnimations();
  },
  computed: {
    sortedAnimations() {
      if (this.popularitySorted) {
        return this.animations.slice().sort((a, b) => b.popularity - a.popularity); // 인기 기준 정렬
      } else if (this.sorted) {
        return this.animations.slice().sort((a, b) => b.voteAverage - a.voteAverage); // 평점 기준 정렬
      }
      return this.animations; // 기본 목록
    }
  },
  methods: {
    async fetchAnimations() {
      try {
        const response = await axios.get('http://localhost:8081/animations');
        this.animations = response.data;
      } catch (error) {
        console.error('애니메이션 데이터를 가져오는 중 오류가 발생했습니다:', error);
      }
    },
    goToAnimationDetail(animationId) {
      this.$router.push({ path: `/animation/${animationId}` });
    },
    sortByRating() {
      this.sorted = true; // 평점 순위 정렬 활성화
      this.popularitySorted = false; // 인기 정렬 비활성화
    },
    resetSort() {
      this.sorted = false; // 정렬 상태 초기화
      this.popularitySorted = false; // 인기 정렬 비활성화
      this.fetchAnimations(); // 전체 애니메이션 목록으로 돌아가기
    },
    sortByPopularity() {
      this.popularitySorted = true; // 인기 순위 정렬 활성화
      this.sorted = false; // 평점 정렬 비활성화
    }
  }
}
</script>

<style scoped>
.animation-list {
  padding: 20px;
  background-color: black;
  color: white;
  overflow-x: hidden; /* 좌우 스크롤 숨기기 */
}

.animation-list h1 {
  text-align: left;
  color: white;
  font-size: 19px;
  position: relative;
  left: 3.5%;
  margin-bottom: 30px;
}

.button-group {
  display: flex;
  margin-bottom: 20px; /* 버튼 그룹과 애니메이션 목록 사이 여백 추가 */
}

.rating-button {
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s, transform 0.2s; /* 부드러운 전환 효과 */
}

.rating-button:hover {
  background-color: #d40000;
  transform: scale(1.05); /* 버튼 호버 시 확대 효과 */
}

.rating-button.active {
  font-weight: bold; /* 클릭한 버튼을 진하게 표시 */
  text-decoration: underline; /* 클릭한 버튼에 밑줄 추가 */
}

.top-slider {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  gap: 30px;
}

.animation-card {
  cursor: pointer;
  width: 220px;
  min-height: 400px;
  border-radius: 12px;
  overflow: hidden;
  transition: transform 0.3s, box-shadow 0.3s;
  background-color: #1e1e1e;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.6);
}

.animation-card:hover {
  transform: scale(1.05);
  box-shadow: 0 8px 40px rgba(0, 0, 0, 0.8);
}

.animation-poster {
  width: 100%;
  height: 320px;
  object-fit: cover;
  border-bottom: 2px solid #F50000;
}

.animation-info {
  padding: 15px;
  text-align: center;
  background-color: rgba(30, 30, 30, 0.9);
}

.animation-title {
  font-size: 14px;
  font-weight: bold;
  color: #F50000;
  margin: 5px 0;
  text-overflow: ellipsis;
  overflow: hidden;
  white-space: nowrap;
}

.info-text {
  font-size: 1rem;
  color: #ccc;
  margin: 5px 0;
  font-style: italic;
}

.rating-text {
  font-size: 1rem;
  color: #FFD700; /* 평점 텍스트 색상 */
  margin-top: 5px;
}

.popularity-text {
  font-size: 1rem;
  color: #FFD700; /* 인기 텍스트 색상 */
  margin-top: 5px;
}
</style>

