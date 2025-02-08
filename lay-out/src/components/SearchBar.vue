<template>
  <div  class="search-bar">
    <input
        type="text"
        v-model="searchQuery"
        placeholder="search..."
        @keyup.enter="searchMovies"
    />
    <button @click="searchMovies">검색</button>
  </div>
</template>
<script>
export default {
  name: 'SearchBar',
  data() {
    return {
      searchQuery: "", // 검색어
    };
  },
  methods: {
    searchMovies() {
      if (!this.searchQuery.trim()) {
        alert("검색어를 입력하세요!");
        return;
      }

      this.$emit('search', this.searchQuery); // Emit the search query to the parent component
    },
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
</style>