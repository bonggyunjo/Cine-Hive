<template>
  <div class="animation-list">
    <h1>애니메이션 목록</h1>
    <div class="top-slider">
      <div
          class="animation-card"
          v-for="animation in animations"
          :key="animation.id"
          @click="goToAnimationDetail(animation.id)"
      >
        <img :src="'https://image.tmdb.org/t/p/w300' + animation.posterPath" alt="animation poster" class="animation-poster" />
        <div class="animation-info">
          <h3 class="animation-title">{{ animation.name }}</h3>
          <p class="info-text">
            <span v-if="animation.directors.length > 0">
              {{ animation.directors.map(d => d.name).join(', ') }}
            </span>
            <span v-else>정보 없음</span>
          </p>
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
    };
  },
  created() {
    this.fetchAnimations();
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
    }
  }
}
</script>

<style scoped>
.animation-list {
  padding: 20px;
  background-color: black;
  color: white;
}

.animation-list h1 {
  text-align: left;
  color: white;
  font-size: 19px;
  position: relative;
  left: 3.5%;
  margin-bottom: 30px;
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
</style>
