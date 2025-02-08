<template>
  <div class="movie-list">
    <h1>영화 목록</h1>

    <div class="button-group"> <!-- 버튼 그룹 추가 -->
      <span
          @click="resetSort"
          :class="['rating-button', { active: !sorted && !popularitySorted && !decadeFiltered }]">전체</span> &nbsp;&nbsp;
      <span
          @click="sortByRating"
          :class="['rating-button', { active: sorted }]">평점 순</span> &nbsp;&nbsp;
      <span
          @click="sortByPopularity"
          :class="['rating-button', { active: popularitySorted }]">인기 순</span> &nbsp;&nbsp;&nbsp;
      <span
          @click="filterByDecade(2000)"
          :class="['rating-button', { active: decadeFiltered === 2000 }]">2000s</span> &nbsp;&nbsp;&nbsp;
      <span
          @click="filterByDecade(2010)"
          :class="['rating-button', { active: decadeFiltered === 2010 }]">2010s</span> &nbsp;&nbsp;&nbsp;
      <span
          @click="filterByDecade(2020)"
          :class="['rating-button', { active: decadeFiltered === 2020 }]">2020s</span> &nbsp;&nbsp;&nbsp;
      <span
          @click="filterByDecade(2000, true)"
          :class="['rating-button', { active: decadeFiltered === 'before2000' }]">2000s 이하</span>
    </div>

    <div class="separator"></div>

    <div v-if="loading" class="loading">로딩 중...</div>

    <div class="top-slider" v-if="!loading">
      <div
          class="movie-card"
          v-for="movie in filteredMovies"
          :key="movie.id"
          @click="goToMovieDetail(movie.id)"
      >
        <img :src="'https://image.tmdb.org/t/p/w300' + movie.posterPath" alt="movie poster" class="movie-poster"/>
        <div class="movie-info">
          <h3 class="movie-title">{{ movie.title }}</h3>
          <p class="info-text">{{ movie.director ? movie.director.name : '정보 없음' }}</p>
          <p v-if="sorted" class="rating-text">평점: {{ movie.voteAverage }}</p>
          <p v-if="popularitySorted" class="popularity-text">인기: {{ movie.popularity }}</p> <!-- 인기 데이터 표시 -->
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'MovieList',
  data() {
    return {
      movies: [],
      sorted: false,
      popularitySorted: false,
      loading: false,
      decadeFiltered: null,
    };
  },
  created() {
    this.fetchMovies();
  },
  computed: {
    filteredMovies() {
      let filtered = this.movies;

      // 연대 필터링
      if (this.decadeFiltered === 2000) {
        filtered = filtered.filter(movie => {
          const year = parseInt(movie.releaseDate.split('-')[0]);
          return year >= 2000 && year < 2010;
        });
      } else if (this.decadeFiltered === 2010) {
        filtered = filtered.filter(movie => {
          const year = parseInt(movie.releaseDate.split('-')[0]);
          return year >= 2010 && year < 2020;
        });
      } else if (this.decadeFiltered === 2020) {
        filtered = filtered.filter(movie => {
          const year = parseInt(movie.releaseDate.split('-')[0]);
          return year >= 2020;
        });
      } else if (this.decadeFiltered === 'before2000') {
        filtered = filtered.filter(movie => {
          const year = parseInt(movie.releaseDate.split('-')[0]);
          return year < 2000;
        });
      }

      // 정렬 처리
      if (this.popularitySorted) {
        return filtered.slice().sort((a, b) => b.popularity - a.popularity);
      } else if (this.sorted) {
        return filtered.slice().sort((a, b) => b.voteAverage - a.voteAverage);
      }
      return filtered; // 기본 목록
    }
  },
  methods: {
    async fetchMovies() {
      this.loading = true;
      try {
        const response = await axios.get('http://localhost:8081/movies');
        this.movies = response.data;
        console.log("res", response.data);
      } catch (error) {
        console.error('영화 데이터를 가져오는 중 오류가 발생했습니다:', error);
      } finally {
        this.loading = false;
      }
    },
    goToMovieDetail(movieId) {
      this.$router.push({ path: `/movie/${movieId}` });
    },
    sortByRating() {
      this.sorted = true;
      this.popularitySorted = false;
      this.decadeFiltered = null;
    },
    resetSort() {
      this.sorted = false;
      this.popularitySorted = false;
      this.decadeFiltered = null;
      this.fetchMovies();
    },
    sortByPopularity() {
      this.popularitySorted = true;
      this.sorted = false;
      this.decadeFiltered = null;
    },
    filterByDecade(decade, before2000 = false) {
      this.decadeFiltered = before2000 ? 'before2000' : decade;
      this.sorted = false;
      this.popularitySorted = false;
    }
  }
}
</script>


<style scoped>
.movie-list {
  padding: 20px;
  background-color: black;
  color: white;
  overflow-x: hidden;
}

.movie-list h1 {
  text-align: left;
  color: white;
  font-size: 19px;
  margin-bottom: 20px;
  position: relative;
  left:4%;
}
.separator {
  width: 93%;
  border-bottom: 1px solid;
  background-color: white;
  margin-bottom: 20px;
  position: relative;
  left: 4%;
}

.button-group {
  display: flex;
  margin-bottom: 20px; /* 버튼 그룹과 영화 목록 사이 여백 추가 */
  position: relative;
  left:4%;
}

.rating-button {
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s, transform 0.2s; /* 부드러운 전환 효과 */
}

.rating-button:hover {

  transform: scale(1.05); /* 버튼 호버 시 확대 효과 */
}

.rating-button.active {
  font-weight: bold; /* 클릭한 버튼을 진하게 표시 */
  text-decoration: underline; /* 클릭한 버튼에 밑줄 추가 */
}

.loading {
  text-align: center;
  font-size: 18px;
  color: #FFD700; /* 로딩 텍스트 색상 */
  margin-bottom: 20px;
}

.top-slider {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  gap: 30px;
}

.movie-card {
  cursor: pointer;
  width: 220px;
  border-radius: 12px;
  overflow: hidden;
  transition: transform 0.3s, box-shadow 0.3s;
  background-color: #1e1e1e;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.6);
}

.movie-card:hover {
  transform: translateY(-8px);
  box-shadow: 0 8px 40px rgba(0, 0, 0, 0.8);
}

.movie-poster {
  width: 100%;
  height: 320px;
  object-fit: cover;
  border-bottom: 2px solid #F50000;
}

.movie-info {
  padding: 15px;
  text-align: center;
  background-color: rgba(30, 30, 30, 0.9);
}

.movie-title {
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
  color: #FFD700;
  margin-top: 5px;
}

.popularity-text {
  font-size: 1rem;
  color: #FFD700; /* 인기 텍스트 색상 */
  margin-top: 5px;
}
</style>
