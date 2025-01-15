<template>
  <div id="add-info">
  <div id="additional-info-container" class="container">
    <h1 class="signup-title">CINEHIVE</h1>
    <p class="add-info-title">회원 가입을 위해 추가 정보를 입력해 주세요.</p>
    <form @submit.prevent="submitAdditionalInfo" class="form">
      <div class="form-group-signup">
        <input type="email" id="email" class="input-field" placeholder="이메일" v-model="memUserid" required />
      </div>
      <div class="form-group-signup">
        <input type="text" id="name" class="input-field" placeholder="이름" v-model="memName" required />
      </div>
      <div class="form-group-signup">
        <select id="gender" class="input-field" v-model="memSex">
          <option value="" disabled selected>성별</option>
          <option value="male">남성</option>
          <option value="female">여성</option>
          <option value="other">기타</option>
        </select>
      </div>
      <div class="form-group-signup">
        <input type="text" id="contact" class="input-field" placeholder="연락처" v-model="memPhone" required />
      </div>
      <label style="position: relative; left:-150px; font-size: 13px; font-weight: bolder">Preferred Genres</label>
      <div class="genre-images-container">
        <div class="genre-item" @click="toggleGenre('드라마')">
          <img :src="require('@/assets/selectGenre/드라마.jpg')" alt="드라마" class="genre-image" width="120" height="110">
          <span class="genre-label">드라마</span>
          <div v-if="selectedGenres.includes('드라마')" class="checkmark">✔</div>
        </div>
        <div class="genre-item" @click="toggleGenre('애니메이션')">
          <img :src="require('@/assets/selectGenre/애니메이션.jpg')" alt="애니메이션" class="genre-image" width="140" height="110">
          <span class="genre-label">애니메이션</span>
          <div v-if="selectedGenres.includes('애니메이션')" class="checkmark">✔</div>
        </div>
        <div class="genre-item" @click="toggleGenre('영화')">
          <img :src="require('@/assets/selectGenre/영화.png')" alt="영화" class="genre-image" width="110" height="145">
          <span class="genre-label">영화</span>
          <div v-if="selectedGenres.includes('영화')" class="checkmark">✔</div>
        </div>
      </div>
      <button type="submit" :disabled="!userInfo" class="submit-btn">회원가입</button>
    </form>
  </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      memName: '',
      memSex: '',
      memPhone: '',
      memUserid: '',
      userInfo: null,
      selectedGenres: [],
      memPassword: ''
    };
  },
  created() {
    this.getUserInfo();
  },
  methods: {
    async getUserInfo() {
      try {
        const response = await axios.get('http://localhost:8081/api/auth/kakao/success', { withCredentials: true });
        this.userInfo = response.data;
        console.log('사용자 정보:', this.userInfo);
      } catch (error) {
        if (error.response) {
          console.error('사용자 정보 가져오기 실패:', error.response.data);
          alert(`사용자 정보를 가져올 수 없습니다: ${error.response.data}`);
        } else {
          console.error('요청 실패:', error);
          alert('서버와 연결할 수 없습니다. 다시 시도해주세요.');
        }
        this.$router.push('/login');
      }
    },
    async submitAdditionalInfo() {
      try {
        const userExistsResponse = await axios.get('http://localhost:8081/api/auth/check-user', {
          params: { kakaoId: this.userInfo.kakaoId }
        });

        if (!userExistsResponse.data) {
          const userData = {
            memUserid: this.memUserid,
            kakaoId: this.userInfo.kakaoId,
            memNickname: this.userInfo.nickname,
            memName: this.memName,
            memPhone: this.memPhone,
            memSex: this.memSex,
            memEmail: this.userInfo.email,
            genres: this.selectedGenres,
            memPassword: '0'
          };

          const response = await axios.post('http://localhost:8081/api/auth/kakao/register', userData);
          alert(response.data);
          this.$router.push('/');
        } else {
          alert('사용자가 이미 존재합니다. 추가 정보 입력은 필요하지 않습니다.');
          this.$router.push('/');
        }
      } catch (error) {
        alert('정보 제출 중 오류가 발생했습니다.');
      }
    }
  },
};
</script>

<style scoped>
#add-info{
  background-color: #393636;
  display: flex;
  height: 100vh;
  justify-content: center;
  align-items: center;
}
.container {
  max-width: 1000px;
  margin: 0 auto;
  padding: 130px;
  background-color: #393636;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(20, 1, 20, 50);
}

h1 {
  text-align: center;
  margin-bottom: 20px;
  color: #333;
}

.form {
  display: flex;
  flex-direction: column;
}

.input-group input,
.input-group select {
  width: 100%;
  padding: 10px;
  border: 1px solid #ced4da;
  border-radius: 5px;
  font-size: 16px;
}

.input-group select {
  cursor: pointer;
}


.genre-checkboxes label {
  margin-bottom: 5px;
}

.submit-btn {
  margin-top: 10px;
  background-color: #d95a15;
  width: 140px;
  height: 50px;
  font-size: 13px;
  font-weight: bolder;
  color: white;
  border: none;
  border-radius: 5px;
  transition: background-color 0.3s;
  position: relative;
  top:40px;
  margin: auto;
}
.submit-btn:hover{
  background-color: peru;
  cursor: pointer;
}

.signup-title {
  margin-bottom: 20px;
  color: #F50000;
  font-size: 25px;
  position: relative;
  top:-100px;
}
.add-info-title {
  position: relative;
  top: -80px;
  font-size: 14px;
  color: black;
  text-align: center;
  animation: fadeIn 0.5s ease-in-out; /* 애니메이션 추가 */
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(-10px); /* 위에서 아래로 떨어지는 효과 */
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.genre-label {
  margin-top: 10px;
  font-size: 16px;
  color: #555;
}

.genre-images-container {
  display: flex;
  justify-content: space-around;
  margin-bottom: 20px;
}

.genre-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  cursor: pointer;
  transition: transform 0.3s;
  margin: 10px;
}

.genre-item:hover {
  transform: scale(1.05);
}

.genre-image {
  border-radius: 10px;
}

.genre-label {
  margin-top: 10px;
  font-size: 13.5px;
  color: white;
  text-align: center;
}

.form-group-signup {
  margin-bottom: 30px;
  width: 60%;
  position: relative;
  margin: auto;
}
.input-field {
  width: 100%;
  padding: 12px;
  border: 1px solid #ccc;
  border-radius: 4px;
  background-color: #f0f8ff;
  color: #333;
  transition: border-color 0.3s;
  font-size: 13px;
  margin-bottom: 30px;
  position: relative;
  top: -20px;
}

.input-field:focus {
  border-color: #007bff;
  outline: none;
}

</style>
