<template>
  <div id="add-info">
  <div id="additional-info-container" class="container">
    <h1 class="signup-title">CINEHIVE</h1>
    <form @submit.prevent="submitAdditionalInfo" class="form">
      <div class="form-group-signup">
        <input type="text" id="text" class="input-field" placeholder="아이디" v-model="memUserid" />
      </div>
      <div class="form-group-signup">
        <input type="text" id="name" class="input-field" placeholder="이름" v-model="memName" />
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
        <input
            type="text"
            id="contact"
            class="input-field"
            placeholder="연락처"
            v-model="memPhone"
            @input="formatPhoneNumber"
            @blur="validatePhone"
        />
        <div v-if="phoneError" class="error-message" style="color: red;">
          {{ phoneError }}
        </div>
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
      memPassword: '',
      loginType: '', // 초기값을 빈 문자열로 설정
      phoneError: '', // 전화번호 오류 메시지
      isPhoneValid: false // 전화번호 유효성 상태
    };
  },
  created() {
    this.loginType = this.$route.query.loginType; // 쿼리 파라미터에서 loginType 초기화
    this.getUserInfo();
  },
  methods: {
    validatePhone() {
      const phonePattern = /^\d{3}-\d{4}-\d{4}$/;
      if (!phonePattern.test(this.memPhone)) {
        this.phoneError = '전화번호 형식이 올바르지 않습니다.';
        this.isPhoneValid = false; // 유효하지 않음
      } else {
        this.phoneError = '';
        this.isPhoneValid = true; // 유효함
      }
    },
    formatPhoneNumber() {
      this.memPhone = this.memPhone.replace(/\D/g, '').replace(/(\d{3})(\d{4})(\d+)/, '$1-$2-$3');
    },
    async getUserInfo() {
      console.log('로그인 타입:', this.loginType); // 로그인 타입 출력
      try {
        const response = await axios.get(`http://localhost:8081/api/auth/${this.loginType}/success`, { withCredentials: true });
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
      if (!this.memUserid) {
        alert('아이디를 입력해 주세요.');
        return;
      }
      if (!this.memName) {
        alert('이름을 입력해 주세요.');
        return;
      }
      if (!this.memSex) {
        alert('성별을 선택해 주세요.');
        return;
      }
      if (!this.memPhone) {
        alert('연락처를 입력해 주세요.');
        return;
      }
      if (!this.selectedGenres.length) {
        alert('최소 하나의 장르를 선택해 주세요.');
        return;
      }

      // 전화번호 유효성 검사
      this.validatePhone();
      if (!this.isPhoneValid) {
        alert('전화번호 형식이 올바르지 않습니다. (예: 010-1234-5678)');
        return; // 전화번호가 유효하지 않은 경우 종료
      }
      try {
        let userExistsResponse;

        // loginType이 google이면 구글 API를 호출
        if (this.loginType === 'google') {
          userExistsResponse = await axios.get(`http://localhost:8081/api/auth/google/check-user`, {
            params: {
              googleId: this.userInfo.googleId // 구글 ID 전송
            }
          });
        }
        if (this.loginType === 'kakao'){
          // 카카오 ID를 확인하는 경우
          userExistsResponse = await axios.get(`http://localhost:8081/api/auth/kakao/check-user`, {
            params: {
              kakaoId: this.userInfo.kakaoId // 카카오 ID 전송
            }
          });
        }
        if(this.loginType === 'naver'){
          // 카카오 ID를 확인하는 경우
          userExistsResponse = await axios.get(`http://localhost:8081/api/auth/naver/check-user`, {
            params: {
              naverId: this.userInfo.naverId // 카카오 ID 전송
            }
          });
        }
        if (!userExistsResponse.data) {
          const userData = {
            memUserid: this.memUserid,
            [this.loginType + 'Id']: this.userInfo[this.loginType + 'Id'], // 카카오 또는 구글 ID
            memNickname: this.userInfo.nickname,
            memName: this.memName,
            memPhone: this.memPhone,
            memSex: this.memSex,
            memEmail: this.userInfo.email,
            genres: this.selectedGenres,
            memPassword: '0'
          };
          const response = await axios.post(`http://localhost:8081/api/auth/${this.loginType}/register`, userData);
          alert(response.data);
          this.$router.push('/');
        } else {
          alert('사용자가 이미 존재합니다. 추가 정보 입력은 필요하지 않습니다.');
          this.$router.push('/');
        }
      } catch (error) {
        alert('정보 제출 중 오류가 발생했습니다.');
      }
    },
    toggleGenre(genre) {
      const index = this.selectedGenres.indexOf(genre);
      if (index === -1) {
        this.selectedGenres.push(genre);
      } else {
        this.selectedGenres.splice(index, 1);
      }
      console.log(this.selectedGenres); // 추가된 로그
    },

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
  top:-60px;
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

.error-message{
  font-size: 13px;
  position: relative;
  top:-40px;
}
</style>
