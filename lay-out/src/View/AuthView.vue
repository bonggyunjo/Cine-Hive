<template>
  <div id="login-container">
    <div class="box-container">
      <!-- 왼쪽 섹션 -->
      <div class="left-section" v-if="isLogin">
        <h1 class="login-title-h1">LOGIN</h1>
        <div class="login-buttons">
          <img src="@/assets/Login/kakao.png" alt="Kakao Login" width="30" height="30" class="login-image">
          <img src="@/assets/Login/google.png" alt="Google Login" width="30" height="30" class="login-image">
          <img src="@/assets/Login/naver.png" alt="Naver Login" width="36" height="30" class="login-image">
        </div>
        <p class="or-use-your-account">or use your account</p>
        <div class="form-group">
          <input type="text" id="username" class="input-field" required />
        </div>
        <div class="form-group">
          <input type="password" id="password" class="input-field" required />
        </div>
        <p class="forgot-password">비밀번호를 잃어버리셨나요?</p>
        <button class="login-btn" @click="toggleForm">로그인</button>
      </div>

      <div class="right-section" v-else>
        <h1 class="signup-title">CINEHIVE</h1>
        <div class="signup-prompt">
          <p>로그인하시겠습니까?</p>
          <button class="signup-button" @click="toggleForm">로그인</button>
        </div>
      </div>

      <!-- 오른쪽 섹션 -->
      <div class="right-section" v-if="isLogin">
        <h1 class="signup-title">CINEHIVE</h1>
        <div class="signup-prompt">
          <p>회원이 아니신가요?</p>
          <button class="signup-button" @click="toggleForm">회원가입</button>
        </div>
      </div>

      <div class="left-section"  v-else-if="currentStep === 1">
        <h1 class="signup-title-h1">SIGN UP</h1>
        <div class="login-buttons">
          <img src="@/assets/Login/kakao.png" alt="Kakao Login" width="30" height="30" class="login-image-sign-up">
          <img src="@/assets/Login/google.png" alt="Google Login" width="30" height="30" class="login-image-sign-up">
          <img src="@/assets/Login/naver.png" alt="Naver Login" width="36" height="30" class="login-image-sign-up">
        </div>
        <div class="signup-prompt-1">
          <div class="form-group-signup">
            <input type="text" id="new-username" class="input-field" placeholder="id" v-model="memUserid" required />
          </div>
          <div class="form-group-signup">
            <input type="email" id="email" class="input-field" placeholder="email" v-model="memEmail" required />
          </div>
          <div class="form-group-signup">
            <input type="password" id="new-password" class="input-field" placeholder="Password" v-model="memPassword" required />
            <span style="font-size:11px; color: #333333; position: relative; top:-15px;">비밀번호는 대,소문자, 특수 문자 포함 8자 이상으로 입력하세요.</span>
          </div>
          <button class="signup-button" @click="nextStep">계속</button>
        </div>
      </div>

      <!-- 회원가입 단계 2 -->
      <div class="left-section" v-else-if="currentStep === 2">
        <h1 class="signup-title-h1">회원가입</h1>
        <div class="signup-prompt-1">
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
          <div class="form-group-signup">
            <input type="text" id="nickname" minlength="4" class="input-field" placeholder="닉네임" v-model="memNickname" required />
            <span style="font-size:11px; color: #333333; position: relative; top:-20px; left:-35px;">닉네임은 4글자 이상으로 입력하세요.</span>
          </div>
          <button class="signup-button" @click="goToGenreSelection">계속</button>
        </div>
      </div>

      <div class="left-section" v-else-if="currentStep === 3">
        <h1 class="genre-title-h1">장르 선택</h1>
        <div class="signup-prompt-1">
          <p class="genre-instruction">선호하는 장르를 선택하세요</p>
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
          <button class="signup-button" @click="submitForm">회원가입</button>
        </div>
      </div>

    </div>
  </div>
</template>
<script>
import axios from 'axios';

export default {
  name: 'AuthComponent',
  data() {
    return {
      isLogin: true, // 로그인 상태
      currentStep: 1, // 현재 단계 (1: 회원가입 단계 1, 2: 회원가입 단계 2, 3: 장르 선택)
      memSex: '', // 성별
      memPhone: '', // 연락처
      memNickname: '', // 닉네임
      memName: '', // 이름
      memUserid: '', // 로그인 아이디
      memEmail: '', // 회원가입 이메일
      memPassword: '', // 회원가입 비밀번호
      selectedGenres: [] // 선택한 장르
    };
  },
  methods: {
    toggleForm() {
      this.isLogin = !this.isLogin; // 로그인/회원가입 토글
      this.currentStep = 1; // 초기 단계로 리셋
    },
    nextStep() {
      if (this.currentStep === 1) {
        if (!this.memUserid || !this.memEmail || !this.memPassword) {
          alert('빈칸을 입력해 주세요.'); // 입력 값이 비어있을 경우 경고
          return;
        }
      } else if (this.currentStep === 2) {
        if (!this.memName || !this.memSex || !this.memPhone || !this.memNickname) {
          alert('빈칸을 입력해 주세요.'); // 입력 값이 비어있을 경우 경고
          return;
        }
      }

      this.currentStep++; // 다음 단계로 이동
    },
    goToGenreSelection() {
      if (!this.memName || !this.memSex || !this.memPhone || !this.memNickname) {
        alert('모든 필드를 입력하세요.'); // 입력 값이 비어있을 경우 경고
        return;
      }

      this.currentStep = 3; // 장르 선택 단계로 이동
    },
    async submitForm() {
      if (this.selectedGenres.length === 0) {
        alert('최소 하나의 장르를 선택해야 합니다.');
        return;
      }

      const userData = {
        memUserid:this.memUserid,
        memSex: this.memSex,
        memPhone: this.memPhone,
        memNickname: this.memNickname,
        memName: this.memName,
        memEmail: this.memEmail,
        memPassword: this.memPassword,
        genres: this.selectedGenres // 선택한 장르 ID를 포함
      };
      console.log('Sending User Data:', userData); // 전송할 데이터 확인
      try {
        const response = await axios.post('http://localhost:8081/register', userData);
        alert(response.data); // 성공 메시지 표시
      } catch (error) {
        if (error.response) {
          alert(error.response.data); // 에러 메시지 표시
          console.log('Sending User Data:', userData); // userData 확인
        } else {
          console.log('Sending User Data:', userData); // userData 확인
          alert('회원가입 중 오류가 발생했습니다. 다시 시도해 주세요.');
        }
      }
    },
    login() {
      const loginData = {
        username: this.username,
        password: this.password
      };

      console.log('로그인 데이터:', loginData);
      // API 호출 등 추가 처리
    },
    toggleGenre(genre) {
      const index = this.selectedGenres.indexOf(genre);
      if (index === -1) {
        this.selectedGenres.push(genre); // 장르 추가
      } else {
        this.selectedGenres.splice(index, 1); // 장르 제거
      }
    },
  }
}
</script>


<style scoped>
#login-container {
  display: flex;
  height: 100vh;
  justify-content: center;
  align-items: center;
  background-color: #393636;
}

.box-container {
  display: flex;
  width: 60%;
  height: 60%;
  border-radius: 10px;
  overflow: hidden;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
}

.left-section, .right-section {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 20px;
}

.left-section {
  background-color: #ffffff;
}

.right-section {
  background-color: #393636;
}

.login-title-h1 {
  color: #333;
  font-size: 25px;
  margin-bottom: 20px;
  position: relative;
  top: -80px;
}

.signup-title-h1 {
  color: #333;
  font-size: 22px;
  margin-bottom: 20px;
}

.login-buttons {
  display: flex;
  justify-content: center;
  margin-bottom: 10px;
}

.login-image {
  margin: 0 10px;
  transition: transform 0.3s;
  position: relative;
  top: -50px;
}

.login-image-sign-up {
  margin: 0 10px;
  transition: transform 0.3s;
  position: relative;
}

.login-image:hover {
  transform: scale(1.1);
}

.or-use-your-account {
  margin: 10px 0;
  text-align: center;
  font-size: 14px;
  font-weight: bolder;
  color: #393636;
  position: relative;
  top: -10px;
}

.form-group {
  margin-bottom: 15px;
  width: 55%;
  text-align: center;
}

.form-group-signup {
  margin-bottom: 10px;
  width: 55%;
  text-align: center;
}

.form-group-signup input {
  height: 10px;
}

.input-field {
  width: 100%;
  padding: 12px;
  border: 1px solid #ccc;
  border-radius: 4px;
  background-color: #f0f8ff;
  color: #333;
  transition: border-color 0.3s;
  font-size: 16px;
}

.input-field:focus {
  border-color: #007bff;
  outline: none;
}

.forgot-password {
  margin: 10px 0;
  text-align: center;
  font-weight: bolder;
  color: #393636;
  font-size: 12px;
  position: relative;
  top: 20px;
}

.signup-title {
  margin-bottom: 20px;
  color: #F50000;
}

.signup-prompt {
  margin-top: 20px;
  text-align: center;
  color: white;
  font-size: 11px;
}

.signup-button {
  margin-top: 10px;
  background-color: #1E1E1E;
  width: 120px;
  height: 40px;
  font-size: 11.5px;
  font-weight: bolder;
  color: white;
  border: none;
  border-radius: 5px;
  transition: background-color 0.3s;
}

.signup-button:hover {
  background-color: #333;
}

.login-btn {
  position: relative;
  top: 35px;
  width: 110px;
  height: 40px;
  background-color: #EB6015;
  border: none;
  border-radius: 20px;
  color: white;
  font-weight: bolder;
  transition: background-color 0.3s;
}

.login-btn:hover {
  background-color: #d95a15;
}

.signup-all-title {
  font-size: 11.5px;
  position: relative;
  left: -100px;
}

.signup-prompt-1 {
  width: 80%;
}

.form-group-signup {
  margin-bottom: 30px;
  width: 60%;
  position: relative;
  margin: auto;
}

.signup-all-title {
  margin-bottom: 10px;
  font-weight: bold;
  color: #333;
  position: relative;
  left: -100px;
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
  top: 10px;
}

.input-field:focus {
  border-color: #007bff;
  outline: none;
}

.genre-title-h1 {
  position: relative;
  top: -120px;
  font-size: 25px;
  color: #333333;
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
}

.genre-item:hover {
  transform: scale(1.05);
}

.genre-image {
  border-radius: 10px;
}

.genre-label {
  margin-top: 10px;
  font-size: 16px;
  color: #555;
  text-align: center;
}
.checkmark {
  position: absolute;
  top: 5px; /* 이미지 위 */
  right: 5px; /* 오른쪽에 위치 */
  font-size: 24px; /* 체크 표시 크기 */
  color: green; /* 체크 표시 색상 */
}
.genre-instruction{
  position: relative;
  top:-70px;
  font-size: 13px;
  color: #333333;
}
</style>
