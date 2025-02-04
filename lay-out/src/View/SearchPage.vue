<template>
  <div id="searchpage">
    <div v-if="movies.length || dramas.length || animations.length">
      <div class="category">
        <h2 class="title">영화</h2>
        <div class="grid-container">
          <div
              class="poster"
              v-for="movie in movies"
              :key="movie.id"
              @click="openMovieDetails(movie)"
          >
            <img :src="getImageUrl(movie.posterPath)" alt="movie poster" />
          </div>
        </div>
        <p v-if="!movies.length" class="no-results">정보가 없습니다.</p>
      </div>

      <div class="category">
        <h2 class="title">드라마</h2>
        <div class="grid-container">
          <div
              class="poster"
              v-for="drama in dramas"
              :key="drama.id"
              @click="openDramaDetails(drama)"
          >
            <img :src="getImageUrl(drama.posterPath)" alt="drama poster" />
          </div>
        </div>
        <p v-if="!dramas.length" class="no-results">정보가 없습니다.</p>
      </div>

      <div class="category">
        <h2 class="title">애니메이션</h2>
        <div class="grid-container">
          <div
              class="poster"
              v-for="animation in animations"
              :key="animation.id"
              @click="openAnimationDetails(animation)"
          >
            <img :src="getImageUrl(animation.posterPath)" alt="animation poster" />
          </div>
        </div>
        <p v-if="!animations.length" class="no-results">정보가 없습니다.</p>
      </div>
    </div>
    <p v-else class="no-results">검색 결과가 없습니다.</p>

    <!-- 영화 상세 정보 모달 -->
    <div v-if="selectedMovie" class="modal" @click.self="closeMovieDetails">
      <div class="modal-content">
        <h2>{{ selectedMovie.title }}</h2>
        <p>{{ selectedMovie.overview || '설명 없음' }}</p>
        <p>평점: {{ selectedMovie.voteAverage }}</p>
        <p>출시일: {{ selectedMovie.releaseDate }}</p>
        <button @click="closeMovieDetails">닫기</button>
      </div>
    </div>

    <div v-if="selectedDrama" class="modal" @click.self="closeDramaDetails">
      <div class="modal-content">
        <h2>{{ selectedDrama.name }}</h2>
        <p>{{ selectedDrama.overview || '설명 없음' }}</p>
        <p>평점: {{ selectedDrama.voteAverage }}</p>
        <p>출시일: {{ selectedDrama.releaseDate }}</p>
        <button @click="closeDramaDetails">닫기</button>
      </div>
    </div>

    <div v-if="selectedAnimation" class="modal" @click.self="closeAnimationDetails">
      <div class="modal-content">
        <h2>{{ selectedAnimation.name }}</h2>
        <p>{{ selectedAnimation.overview || '설명 없음' }}</p>
        <p>평점: {{ selectedAnimation.voteAverage }}</p>
        <p>출시일: {{ selectedAnimation.releaseDate }}</p>
        <button @click="closeAnimationDetails">닫기</button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      searchQuery: this.$route.query.q,
      movies: [],
      dramas: [],
      animations: [],
      selectedMovie: null,
      selectedDrama: null,
      selectedAnimation: null,
    };
  },
  mounted() {
    this.fetchSearchResults();
  },
  watch: {
    '$route.query.q': function (newQuery) {
      this.searchQuery = newQuery;
      this.fetchSearchResults();
    }
  },
  methods: {
    fetchSearchResults() {
      const moviesData = this.$route.query.movies;
      const dramasData = this.$route.query.dramas;
      const animationsData = this.$route.query.animations;

      this.movies = moviesData ? JSON.parse(moviesData) : [];
      this.dramas = dramasData ? JSON.parse(dramasData) : [];
      this.animations = animationsData ? JSON.parse(animationsData) : [];
    },
    getImageUrl(path) {
      return path ? `https://image.tmdb.org/t/p/w500${path}` : '/default-poster.jpg';
    },
    openMovieDetails(movie) {
      this.selectedMovie = movie;
    },
    openDramaDetails(drama) {
      this.selectedDrama = drama;
    },
    openAnimationDetails(animation) {
      this.selectedAnimation = animation;
    },
    closeMovieDetails() {
      this.selectedMovie = null;
    },
    closeDramaDetails() {
      this.selectedDrama = null;
    },
    closeAnimationDetails() {
      this.selectedAnimation = null;
    },
  }
};
</script>

<style scoped>
#searchpage {
  min-height: 100vh;
  background-color: black;
  color: white;
  padding: 20px;
}

/* 카테고리 스타일 */
.category {
  margin-bottom: 30px;
  text-align: left; /* 추가: 카테고리를 왼쪽 정렬 */
}


/* 제목 스타일 */
.title {
  font-size: 24px;
  font-weight: bold;
  margin-bottom: 10px;
  padding-left: 10px;
  text-align: left; /* 추가: 텍스트를 왼쪽 정렬 */
}


/* 포스터 컨테이너 */
.grid-container {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(150px, 1fr));
  gap: 15px;
  justify-content: center;
}

/* 포스터 스타일 */
.poster img {
  width: 100%;
  border-radius: 10px;
  transition: transform 0.3s, box-shadow 0.3s;
}

.poster img:hover {
  transform: scale(1.05);
  box-shadow: 0 4px 10px rgba(255, 255, 255, 0.2);
  cursor: pointer;
}

/* 검색 결과 없을 때 */
.no-results {
  text-align: center;
  color: gray;
  font-size: 18px;
  margin-top: 10px;
}

/* 모달 스타일 */
.modal {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.7);
  display: flex;
  justify-content: center;
  align-items: center;
}

.modal-content {
  background: black;
  padding: 20px;
  border-radius: 10px;
  text-align: center;
  width: 400px;
}

.modal-content button {
  margin-top: 15px;
  padding: 10px 20px;
  background: red;
  border: none;
  color: white;
  cursor: pointer;
  border-radius: 5px;
}

.modal-content button:hover {
  background: darkred;
}
</style>
