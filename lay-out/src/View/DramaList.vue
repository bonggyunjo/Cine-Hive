<template>
  <div class="drama-list">
    <h1>드라마 목록</h1>

    <div class="button-group">
      <span
          @click="resetSort"
          :class="['rating-button', { active: !popularitySorted }]">전체</span> &nbsp;&nbsp;
      <span
          @click="sortByPopularity"
          :class="['rating-button', { active: popularitySorted }]">인기 순</span>
    </div>

    <div class="top-slider">
      <div
          class="drama-card"
          v-for="drama in sortedDramas"
          :key="drama.id"
          @click="goToDramaDetail(drama.id)"
      >
        <img :src="'https://image.tmdb.org/t/p/w300' + drama.posterPath" alt="drama poster" class="drama-poster" />
        <div class="drama-info">
          <h3 class="drama-title">{{ drama.name }}</h3>
          <p class="info-text">
            <span v-if="drama.directors.length > 0">
              {{ [...new Set(drama.directors.map(director => director.name))].join(', ') }}
            </span>
            <span v-else>정보 없음</span>
          </p>
          <p class="popularity-text" v-if="popularitySorted">인기: {{ drama.popularity.toFixed(1) }}</p> <!-- 인기 표시 -->
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'DramaList',
  data() {
    return {
      dramas: [],
      popularitySorted: false, // 인기 정렬 상태 추가
    };
  },
  created() {
    this.fetchDramas();
  },
  computed: {
    sortedDramas() {
      if (this.popularitySorted) {
        return this.dramas.slice().sort((a, b) => b.popularity - a.popularity); // 인기 기준 정렬
      }
      return this.dramas; // 기본 목록
    }
  },
  methods: {
    async fetchDramas() {
      try {
        const response = await axios.get('http://localhost:8081/dramas');
        this.dramas = response.data;
      } catch (error) {
        console.error('드라마 데이터를 가져오는 중 오류가 발생했습니다:', error);
      }
    },
    goToDramaDetail(dramaId) {
      this.$router.push({ path: `/drama/${dramaId}` });
    },
    resetSort() {
      this.popularitySorted = false; // 인기 정렬 비활성화
      this.fetchDramas(); // 전체 드라마 목록으로 돌아가기
    },
    sortByPopularity() {
      this.popularitySorted = true; // 인기 순위 정렬 활성화
    }
  }
}
</script>

<style scoped>
.drama-list {
  padding: 20px;
  background-color: black;
  color: white;
  overflow-x: hidden;
}

.drama-list h1 {
  text-align: left;
  color: white;
  font-size: 19px;
  position: relative;
  left: 4%;
  margin-bottom: 20px;
}

.button-group {
  display: flex;
  margin-bottom: 20px;
  position: relative;
  left: 4%;
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

.drama-card {
  cursor: pointer;
  width: 220px;
  min-height: 400px;
  border-radius: 12px;
  overflow: hidden;
  transition: transform 0.3s, box-shadow 0.3s;
  background-color: #1e1e1e;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.6);
}

.drama-card:hover {
  transform: translateY(-8px);
  box-shadow: 0 8px 40px rgba(0, 0, 0, 0.8);
}

.drama-poster {
  width: 100%;
  height: 320px;
  object-fit: cover;
  border-bottom: 2px solid #F50000;
}

.drama-info {
  padding: 15px;
  text-align: center;
  background-color: rgba(30, 30, 30, 0.9);
}

.drama-title {
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

.popularity-text {
  font-size: 1rem;
  color: #FFD700; /* 인기 텍스트 색상 */
  margin-top: 5px;
}
</style>
