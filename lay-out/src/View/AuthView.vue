<template>
  <div id="login-container">
    <div class="box-container">
      <!-- 왼쪽 섹션 -->
      <div class="left-section" v-if="isLogin">
        <h1 class="login-title-h1">LOGIN</h1>
        <div class="login-buttons">
          <img @click="kakaoLogin" src="@/assets/Login/kakao.png" alt="Kakao Login" width="30" height="30" class="login-image">
          <img @click="googleLogin" src="@/assets/Login/google.png" alt="Google Login" width="30" height="30" class="login-image">
          <img @click="naverLogin" src="@/assets/Login/naver.png" alt="Naver Login" width="36" height="30" class="login-image">
        </div>
        <p class="or-use-your-account">or use your account</p>
        <div class="form-group">
          <input type="text" id="username" class="input-field" v-model="memUserid" placeholder="아이디" required />
        </div>
        <div class="form-group">
          <input type="password" style="position: relative; top:-25px;" id="password" class="input-field" v-model="memPassword" placeholder="비밀번호" required />
        </div>
        <p class="forgot-password">비밀번호를 잃어버리셨나요?</p>
        <button class="login-btn" @click="login">로그인</button>
      </div>


      <div class="right-section" v-else>
        <h1 class="signup-title">CINEHIVE</h1>
        <div class="signup-prompt">
          <p>계정이 있으신가요?</p>
          <button class="signup-button1" @click="toggleForm">로그인</button>
        </div>
      </div>

      <!-- 오른쪽 섹션 -->
      <div class="right-section" v-if="isLogin">
        <h1 class="signup-title">CINEHIVE</h1>
        <div class="signup-prompt">
          <p>회원이 아니신가요?</p>
          <button class="signup-button1" @click="toggleForm">회원가입</button>
        </div>
      </div>

      <div class="left-section"  v-else-if="currentStep === 1">
        <div class="step-indicator">
          {{ currentStep }} / 2
        </div>

        <h1 class="signup-title-h1">SIGN UP</h1>
        <div class="login-buttons">
          <img @click="kakaoLogin" src="@/assets/Login/kakao.png" alt="Kakao Login" width="30" height="30" class="login-image-sign-up">
          <img @click="googleLogin" src="@/assets/Login/google.png" alt="Google Login" width="30" height="30" class="login-image-sign-up">
          <img @click="naverLogin" src="@/assets/Login/naver.png" alt="Naver Login" width="36" height="30" class="login-image-sign-up">
        </div>
        <div class="signup-prompt-1">
          <div class="form-group-signup">
            <input type="text" id="new-username" class="input-field" placeholder="아이디" v-model="memUserid" required />
          </div>
          <div class="form-group-signup">
            <input type="email" id="email" class="input-field" placeholder="이메일" v-model="memEmail" required />
          </div>
          <div class="form-group-signup">
            <input type="password" id="new-password" class="input-field" placeholder="비밀번호" v-model="memPassword" required />
            <span style="font-size:11px; color: #333333; position: relative; top:-15px;">비밀번호는 대,소문자, 특수 문자 포함 8자 이상으로 입력하세요.</span>
          </div>
          <div v-if="passwordError" class="error-message" style="color: red;">
            {{ passwordError }}
          </div>
          <button class="signup-button1" @click="nextStep">계속</button>
        </div>
      </div>

      <!-- 회원가입 단계 2 -->
      <div class="left-section" v-else-if="currentStep === 2">
        <div class="step-indicator2">
          {{ currentStep }} / 2
        </div>
        <h1 class="signup-title-h1">SIGN UP</h1>
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
            <input
                type="text"
                id="contact"
                class="input-field"
                placeholder="연락처"
                v-model="memPhone"
                @input="formatPhoneNumber"
                @blur="validatePhone"
                required
            />
            <div v-if="phoneError" class="error-message" style="color: red;">
              {{ phoneError }}
            </div>
          </div>

          <div class="form-group-signup">
            <input type="text" id="nickname" minlength="4" class="input-field" placeholder="닉네임" v-model="memNickname" required />
            <span style="font-size:11px; color: #333333; position: relative; top:-20px;">닉네임은 4글자 이상으로 입력하세요.</span>
          </div>
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
import { mapState } from 'vuex';

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
      selectedGenres: [], // 선택한 장르
      passwordError: '', // 비밀번호 오류 메시지
      phoneError: '', // 전화번호 오류 메시지
      isPhoneValid: false // 전화번호 유효성 상태
    };
  },
  computed: {
    ...mapState(['isLoggedIn']), // Vuex 상태 가져오기
  },
  methods: {

    validateEmail(email) {
      const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
      return emailPattern.test(email);
    },
    validatePassword(password) {
      const passwordPattern = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[\W_]).{8,}$/;
      return passwordPattern.test(password);
    },
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
    toggleForm() {
      this.isLogin = !this.isLogin;
      this.currentStep = 1;
    },
    async checkDuplicatesName() {
      // 중복 체크 요청
      const memUserid = this.memUserid;

      try {
        const response = await axios.get(`http://localhost:8081/checkuserId/${memUserid}`);
        return response.data;
      } catch (error) {
        if (error.response) {
          console.log('요청 실패: ' + error.response.data);
        }
        return false;
      }
    },

    async checkDuplicatesEmail() {

      const memEmail = this.memEmail;

      try {
        const response = await axios.get(`http://localhost:8081/checkemail/${memEmail}`);
        return response.data;
      } catch (error) {
        if (error.response) {
          console.log('요청 실패: ' + error.response.data);
        }
        return false;
      }
    },

    async checkDuplicatesNickname() {

      const memNickname = this.memNickname;

      try {
        const response = await axios.get(`http://localhost:8081/checknickname/${memNickname}`);
        return response.data;
      } catch (error) {
        if (error.response) {
          console.log('요청 실패: ' + error.response.data);
        }
        return false;
      }
    },
    async nextStep() {
      if (this.currentStep === 1) {

        if (!this.memUserid || !this.memEmail || !this.memPassword) {
          alert('빈칸을 입력해 주세요.');
          return;
        }
        if (!this.validateEmail(this.memEmail)) {
          alert('이메일 형식이 올바르지 않습니다.');
          return;
        }


        const isUniqueName = await this.checkDuplicatesName();
        const isUniqueEmail = await this.checkDuplicatesEmail();

        if (!isUniqueName) {
          alert('이미 존재하는 아이디입니다.');
          return;
        }

        if (!isUniqueEmail) {
          alert('이미 존재하는 이메일입니다.');
          return;
        }

        if (!this.validatePassword(this.memPassword)) {
          this.passwordError = '비밀번호 형식이 일치하지 않습니다.';
          return;
        }

        this.currentStep++;

      } else if (this.currentStep === 2) {

        if (!this.memName || !this.memSex || !this.memPhone || !this.memNickname) {
          alert('빈칸을 입력해 주세요.');
          return;
        }
      }


    },
    async submitForm() {
      if (this.selectedGenres.length === 0) {
        alert('최소 하나의 장르를 선택해야 합니다.');
        return;
      }
      const isUniqueNickname = await this.checkDuplicatesNickname();
      if (!isUniqueNickname) {
        alert('이미 존재하는 닉네임입니다.');
        return;
      }
      if (!this.isPhoneValid) {
        alert('전화번호 형식이 올바르지 않습니다. (예: 010-1234-5678)');
        return; // 함수 종료
      }

      const userData = {
        memUserid: this.memUserid,
        memSex: this.memSex,
        memPhone: this.memPhone,
        memNickname: this.memNickname,
        memName: this.memName,
        memEmail: this.memEmail,
        memPassword: this.memPassword,
        genres: this.selectedGenres,
        loginType: ''
      };
      console.log('Sending User Data:', userData);
      try {
        const response = await axios.post('http://localhost:8081/register', userData);
        alert(response.data); // 성공 메시지 표시
        window.location.reload();
      } catch (error) {
        if (error.response) {
          alert(error.response.data);
          console.log('Sending User Data:', userData);
        } else {
          console.log('Sending User Data:', userData);
          alert('회원가입 중 오류가 발생했습니다. 다시 시도해 주세요.');
        }
      }
    },
    async login() {
      const loginData = {
        memUserid: this.memUserid,
        memPassword: this.memPassword
      };

      try {
        const response = await axios.post('http://localhost:8081/login', loginData);
        console.log(response.data);

        // 로그인 성공 시 사용자 정보를 스토어에 저장
        const user = { userid: this.memUserid }; // 사용자 정보를 필요에 따라 조정
        this.$store.dispatch('login', user); // Vuex 스토어에 로그인 상태 저장

        // 상태 확인
        console.log(this.isLoggedIn); // Vuex 상태 확인 (computed 속성 사용)

        // 메인 화면으로 리다이렉트
        if (this.$route.path !== '/') {
          this.$router.push('/'); // 메인 화면으로 이동
        }
      } catch (error) {
        if (error.response) {
          alert(error.response.data);
        } else {
          alert('로그인 중 오류가 발생했습니다. 다시 시도해 주세요.');
        }
      }
    },
    toggleGenre(genre) {
      const index = this.selectedGenres.indexOf(genre);
      if (index === -1) {
        this.selectedGenres.push(genre);
      } else {
        this.selectedGenres.splice(index, 1);
      }
    },
    kakaoLogin() {
      this.loginType='kakao';
      window.location.href = 'http://localhost:8081/api/auth/kakao'; // 서버의 카카오 로그인 URL로 리다이렉트
    },
    googleLogin() {
      this.loginType='google';
      window.location.href = 'http://localhost:8081/api/auth/google'; // 서버의 카카오 로그인 URL로 리다이렉트
    },
    naverLogin() {
      this.loginType='naver';
      window.location.href = 'http://localhost:8081/api/auth/naver'; // 서버의 카카오 로그인 URL로 리다이렉트
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

.login-image:hover{
  cursor: pointer;
  transform: scale(1.2);
}
.login-image-sign-up {
  margin: 0 10px;
  transition: transform 0.3s;
  position: relative;
}

.login-image-sign-up:hover{
  cursor: pointer;
  transform: scale(1.2);
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
  top: 15px;
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
.signup-prompt p{
  position: relative;
  top:30px;
  font-size: 12.5px;
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
  position: relative;
  top:-20px;
}
.signup-button1{
  margin-top: 10px;
  background-color: #1E1E1E;
  width: 120px;
  height: 40px;
  font-size: 12px;
  font-weight: bolder;
  color: white;
  border: none;
  border-radius: 5px;
  transition: background-color 0.3s;
  position: relative;
  top:40px;
}
.signup-button1:hover {
  background-color: #333;
  cursor: pointer;
}
.signup-button:hover {
  background-color: #333;
  cursor: pointer;
}

.login-btn {
  width: 110px;
  height: 40px;
  background-color: #EB6015;
  border: none;
  border-radius: 20px;
  color: white;
  font-weight: bolder;
  transition: background-color 0.3s;
  position: relative;
  top:30px;
}

.login-btn:hover {
  background-color: #d95a15;
  cursor: pointer;
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
  top: 5px;
  right: 5px;
  font-size: 24px;
  color: green;
}
.error-message{
  font-size:11.5px;
}
.step-indicator{
  position: relative;
  top:-30px;
  font-weight: bolder;
  font-size: 15px;
  color: #333333;
}
.step-indicator2{
  position: relative;
  top:45px;
  font-weight: bolder;
  font-size: 15px;
  color: #333333;
}
</style>
