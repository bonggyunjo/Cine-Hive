<template>
  <div id="searchpage">
    <div v-if="movies.length || dramas.length">
      <h2 class="movie-title">영화</h2>
      <div class="movie-slider" v-if="movies.length">

        <div
            class="movie-poster"
            v-for="movie in movies"
            :key="movie.id"
            @click="openMovieDetails(movie)"
        >
          <img :src="getImageUrl(movie.posterPath)" alt="movie poster" />
        </div>
      </div>

      <h2 class="drama-title">드라마</h2>
      <div class="drama-slider" v-if="dramas.length">

        <div
            class="drama-poster"
            v-for="drama in dramas"
            :key="drama.id"
            @click="openDramaDetails(drama)"
        >
          <img :src="getImageUrl(drama.posterPath)" alt="drama poster" />
      </div>
    </div>

      <h2 class="animation-title">애니메이션</h2>
      <div class="animation-slider" v-if="animations.length">

        <div
            class="animation-poster"
            v-for="animation in animations"
            :key="animation.id"
            @click="openAnimationDetails(animation)"
        >
          <img :src="getImageUrl(animation.posterPath)" alt="animation poster" />
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


    <div v-if="selectedDrama" class="drama-modal" @click.self="closeDramaDetails">
      <div
          class="drama-modal-backdrop"
          :style="{
          backgroundImage: 'url(https://image.tmdb.org/t/p/original' + selectedDrama.backdropPath + ')',
        }"
      >
        <div class="drama-modal-content">
          <h2>{{ selectedDrama.name }}</h2>
          <p>{{ selectedDrama.overview || '설명 없음' }}</p>
          <p>평점: {{ selectedDrama.voteAverage }}</p>
          <p>출시일: {{ selectedDrama.releaseDate }}</p>
          <button @click="closeDramaDetails">닫기</button>
          </div>
      </div>
      </div>


      <div v-if="selectedAnimation" class="animation-modal" @click.self="closeAnimationDetails">
        <div
            class="animation-modal-backdrop"
            :style="{
            backgroundImage: 'url(https://image.tmdb.org/t/p/original' + selectedAnimation.backdropPath + ')',
            }"
        >
          <div class="animation-modal-content">
            <h2>{{ selectedAnimation.name }}</h2>
            <p>{{ selectedAnimation.overview || '설명 없음' }}</p>
            <p>평점: {{ selectedAnimation.voteAverage }}</p>
            <p>출시일: {{ selectedAnimation.releaseDate }}</p>
            <button @click="closeDramaDetails">닫기</button>
          </div>
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
      movies: [],  // 검색된 영화 결과
      dramas: [], // 검색된 드라마 결과
      animations: [], // 검색된 애니메이션 결과
      selectedMovie: null,  // 선택된 영화
      selectedDrama: null,  // 선택된 드라마
      selectedAnimation: null, // 선택된 애니메이션
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

      const moviesData = this.$route.query.movies;
      const dramasData = this.$route.query.dramas;
      const animationsData = this.$route.query.animations;

      if (moviesData) {
        this.movies = JSON.parse(moviesData);  // JSON 문자열을 객체로 변환
      }
      if(dramasData){
        this.dramas = JSON.parse(dramasData);
      }
      if(animationsData){
        this.animations = JSON.parse(animationsData);
      }
    },
    // 영화의 포스터 이미지를 올바른 URL로 변환
    getImageUrl(path) {
      return path ? `https://image.tmdb.org/t/p/w500${path}` : '/default-poster.jpg';
    },
    // 영화 포스터 클릭 시 상세 정보 모달 열기
    openMovieDetails(movie) {
      this.$router.push({ name: 'MovieDetail', params: { id: movie.id } });
    },
    // 드라마 포스터 클릭 시 상세 정보 모달 열기
    openDramaDetails(drama) {
      this.$router.push({ name: 'DramaDetail', params: { id: drama.id } });
    },
    // 애니메이션 포스터 클릭 시 상세 정보 모달 열기
    openAnimationDetails(animation) {
      this.$router.push({ name: 'AnimationDetail', params: { id: animation.id } });
    },
    // 검색 버튼 클릭 시 새로운 검색어로 URL 갱신
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

.animation-title,
.movie-title,
.drama-title{
  position: relative;
  bottom: 10px;
  left: 0.5%;
  text-align: left;
}
#searchpage{
  height: 900px;
  flex: 1;
  background-color : black;
  color: white;
}
.animation-slider,
.drama-slider,
.movie-slider {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  gap: 10px;
  margin-bottom: 20px;
}
.animation-poster img:hover,
.drama-poster img:hover,
.movie-poster img:hover {
  transform: scale(1.1);
}

.animation-poster img,
.drama-poster img,
.movie-poster img {
  width: 200px;
  height: 300px;
  cursor: pointer;
  transition: transform 0.3s;
}

/* 정보 모달 */
.animation-modal,
.drama-modal,
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

.animation-modal-backdrop,
.drama-modal-backdrop,
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

.animation-modal-content,
.drama-modal-content,
.movie-modal-content {
  background-color: rgba(0, 0, 0, 0.8);
  padding: 20px;
  border-radius: 8px;
  max-width: 600px;
  text-align: center;
  color: white;
}

.animation-modal-content button,
.drama-modal-content button,
.movie-modal-content button {
  margin-top: 20px;
  padding: 10px;
  background-color: #1a1a1a;
  border: none;
  color: white;
  cursor: pointer;
  border-radius: 5px;
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
