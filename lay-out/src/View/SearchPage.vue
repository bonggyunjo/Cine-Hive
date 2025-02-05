<template>
  <div id="searchpage">
    <h1>"{{ searchQuery }}" 검색 결과</h1>

    <div v-if="movies.length || dramas.length || animations.length">
      <div class="category">
        <h2 class="title">영화</h2>
        <div class="grid-container">
          <div
              class="poster"
              v-for="movie in movies"
              :key="movie.id"
              @click="openMovieDetails(movie.id)"
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
              @click="openDramaDetails(drama.id)"
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
              @click="openAnimationDetails(animation.id)"
          >
            <img :src="getImageUrl(animation.posterPath)" alt="animation poster" />
          </div>
        </div>
        <p v-if="!animations.length" class="no-results">정보가 없습니다.</p>
      </div>
    </div>

    <p v-else class="no-results">검색 결과가 없습니다.</p>
  </div>
</template>

<script>
import { mapState, mapActions } from 'vuex';
import axios from 'axios';

export default {
  name: 'SearchPage',
  computed: {
    ...mapState(['searchResults']),
    searchQuery() {
      return this.$route.query.q || "";
    },
    movies() {
      return this.searchResults?.movies || [];
    },
    dramas() {
      return this.searchResults?.dramas || [];
    },
    animations() {
      return this.searchResults?.animations || [];
    }
  },
  methods: {
    ...mapActions(['updateSearchResults']),

    async fetchSearchResults() {
      if (!this.searchQuery) return;

      try {
        const response = await axios.post('http://localhost:8081/search', {
          query: this.searchQuery
        });

        console.log("서버 응답 데이터:", response.data);
        this.updateSearchResults(response.data);
      } catch (error) {
        console.error("검색 결과 오류:", error);
      }
    },

    // ✅ getImageUrl 함수 추가
    getImageUrl(path) {
      if (!path) return "https://via.placeholder.com/150"; // 기본 이미지 처리
      return `https://image.tmdb.org/t/p/w500${path}`; // TMDB API 이미지 URL 예제
    }
  },

  created() {
    console.log("페이지 로드됨. 검색어:", this.searchQuery);
    if (this.searchQuery) {
      this.fetchSearchResults();
    }
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


.category {
  margin-bottom: 30px;
}


.title {
  font-size: 24px;
  font-weight: bold;
  margin-bottom: 10px;
  padding-left: 10px;
  text-align: left;
}



.grid-container {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(150px, 1fr));
  gap: 15px;
  justify-content: center;
}


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


.no-results {
  text-align: center;
  color: gray;
  font-size: 18px;
  margin-top: 10px;
}


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
