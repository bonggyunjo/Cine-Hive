<template>
  <div id="searchpage">
    <h1>검색 결과</h1>

    <div v-if="searchResults.length">
      <div class="movie-slider">
        <div
            class="movie-poster"
            v-for="movie in searchResults"
            :key="movie.id"
            @click="openMovieDetails(movie)"
        >
          <img :src="getImageUrl(movie.posterPath)" alt="movie poster" />
        </div>
      </div>
    </div>

    <div v-else>
      <p>검색 결과가 없습니다.</p>
    </div>

    <!-- 영화 상세 정보 모달 -->
    <div v-if="selectedMovie" class="movie-modal" @click.self="closeMovieDetails">
      <div
          class="movie-modal-backdrop"
          :style="{
          backgroundImage: 'url(https://image.tmdb.org/t/p/original' + selectedMovie.backdropPath + ')',
        }"
      >
        <div class="movie-modal-content">
          <h2>{{ selectedMovie.title }}</h2>
          <p>{{ selectedMovie.overview || '설명 없음' }}</p>
          <p>평점: {{ selectedMovie.voteAverage }}</p>
          <p>출시일: {{ selectedMovie.releaseDate }}</p>
          <button @click="closeMovieDetails">닫기</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      searchQuery: this.$route.query.q,  // 검색어
      searchResults: [],  // 검색된 영화 결과
      selectedMovie: null,  // 선택된 영화
    };
  },
  mounted() {
    this.fetchSearchResults();
  },
  watch: {
    // 검색어가 변경되면 결과를 갱신
    '$route.query.q': function(newQuery) {
      this.searchQuery = newQuery;
      this.fetchSearchResults();
    }
  },
  methods: {
    // 영화 검색 결과 가져오기
    fetchSearchResults() {
      const results = this.$route.query.results;
      if (results) {
        this.searchResults = JSON.parse(results);  // JSON 문자열을 객체로 변환
      }
    },
    // 영화의 포스터 이미지를 올바른 URL로 변환
    getImageUrl(path) {
      return path ? `https://image.tmdb.org/t/p/w500${path}` : '';
    },
    // 영화 포스터 클릭 시 상세 정보 모달 열기
    openMovieDetails(movie) {
      this.$router.push({ name: 'MovieDetail', params: { id: movie.id } });
    },
    // 검색 버튼 클릭 시 새로운 검색어로 URL 갱신
    searchMovies() {
      if (!this.searchQuery.trim()) {
        alert("검색어를 입력하세요!");
        return;
      }
      // 검색 결과를 새로운 URL로 갱신
      this.$router.replace({
        path: '/search',
        query: { q: this.searchQuery, results: JSON.stringify(this.searchResults) }
      });
    }
  }
};
</script>

<style scoped>
#searchpage{
  height: 900px;
  flex: 1;
  background-color : black;
  color: white;
}

.movie-slider {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  gap: 10px;
  margin-bottom: 20px;
}

.movie-poster img:hover {
  transform: scale(1.1);
}

.movie-poster img {
  width: 200px;
  height: 300px;
  cursor: pointer;
  transition: transform 0.3s;
}

/* 영화 정보 모달 */
.movie-modal {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.8);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.movie-modal-backdrop {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.movie-modal-content {
  background-color: rgba(0, 0, 0, 0.8);
  padding: 20px;
  border-radius: 8px;
  max-width: 600px;
  text-align: center;
  color: white;
}

.movie-modal-content button {
  margin-top: 20px;
  padding: 10px;
  background-color: #1a1a1a;
  border: none;
  color: white;
  cursor: pointer;
  border-radius: 5px;
}
.search-bar {
  width: 100%;
  padding: 10px;
  display: flex;
  justify-content: center;
  background-color: black;
  position: sticky;
  top: 0;
  z-index: 100;
}

.search-bar input {
  width: 80%;
  max-width: 600px;
  padding: 10px;
  font-size: 16px;
  border: none;
  border-radius: 5px;
}

.search-bar input:focus {
  outline: none;
  box-shadow: 0 0 5px rgba(255, 0, 0, 0.8); /* 포커스 시 효과 */
}
</style>
