<template>
  <div  class="search-bar">
    <input
        type="text"
        v-model="searchQuery"
        placeholder="search..."
        @keyup.enter="searchMovies"
    />
    <button @click="searchMovies">검색</button>
    <div v-if="loading" class="loading-overlay">
      <span>로딩 중...</span>
    </div>
  </div>

</template>
<script>
import axios from "axios";
import {mapActions} from "vuex";

export default {
  name: 'SearchBar',
  data() {
    return {
      searchQuery: "", // 검색어
      loading: false,  // 로딩 상태
    };
  },
  methods: {
    ...mapActions(['updateSearchResults']),
    async searchMovies() {
      if (!this.searchQuery.trim()) {
        alert("검색어를 입력하세요!");
        return;
      }

      this.loading = true;

      try {
        const response = await axios.post('http://localhost:8081/search', {
          query: this.searchQuery
        });

        this.updateSearchResults(response.data);

        // 현재 경로와 같다면 이동하지 않음
        const newRoute = `/search?q=${encodeURIComponent(this.searchQuery)}`;
        if (this.$route.fullPath !== newRoute) {
          this.$router.push(newRoute);
        }
      } catch (error) {
        console.error("검색 중 오류가 발생했습니다:", error);
      } finally {
        this.loading = false;
      }
    }
  },
};
</script>
<style scoped>
.search-bar {
  flex: 1;
  text-align: left;
  margin: 0 20px;
  position: relative;
  top:-65px;
  left:100px;
  display: flex;
  justify-content: flex-start;
}

.search-bar input {
  width: 100%;
  max-width: 550px;
  padding: 10px;
  border: none;
  border-radius: 5px;
  background-color: #1a1a1a;
  color: white;
  outline: none;
  transition: border-color 0.3s;
  font-size: 14px;
}
@media (max-width: 768px) {
  .search-bar {
    flex-direction: column;
    align-items: stretch;
    gap: 10px;
  }

  .search-bar input {
    max-width: 100%;
    padding: 8px;
    font-size: 13px;
  }

  .search-bar button {
    max-width: 100%;
    padding: 8px 0;
    font-size: 14px;
  }
}
@media (max-width: 480px) {
  .search-bar {
    flex-direction: column;
    gap: 8px;
  }

  .search-bar input {
    max-width: 100%;
    padding: 8px;
    font-size: 12px;
  }

  .search-bar button {
    max-width: 100%;
    padding: 8px 0;
    font-size: 12px;
  }
}


@media (max-width: 768px) {
  .search-bar input {
    max-width: 100%;
    padding: 8px;
    font-size: 13px;
  }
}

@media (max-width: 480px) {
  .search-bar input {
    max-width: 100%;
    padding: 8px;
    font-size: 12px;
  }
}

.search-bar input::placeholder {
  color: #ccc;
}

.search-bar input:focus {
  border-color: #F50000;
}.search-bar button {
   background-color: #393636;
   color: white;
   border: none;
   padding: 10px 18px;
   border-radius: 5px;
   cursor: pointer;
   font-size: 13px;
   margin-left: 10px;
   transition: background-color 0.3s ease;
   position: relative;
   top: 1px;
 }

.search-bar button:hover {
  background-color: #555555;
}

.search-bar button:active {
  background-color: #990000;
}

.loading-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.8);
  color: white;
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
  font-size: 20px;
}
</style>