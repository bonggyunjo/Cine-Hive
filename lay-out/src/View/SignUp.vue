<template>
  <div id="login-container">
    <div class="box-container">
      <div class="left-section">
        <h1 class="login-title-h1">LOGIN</h1>
        <div class="login-buttons">
          <img src="@/assets/Login/kakao.png" alt="Kakao Login" width="30" height="30" class="login-image">
          <img src="@/assets/Login/google.png" alt="Google Login" width="30" height="30" class="login-image">
          <img src="@/assets/Login/naver.png" alt="Naver Login" width="36" height="30" class="login-image">
        </div>
        <p class="or-use-your-account">or use your account</p>
        <div class="form-group">
          <label for="username"></label>
          <input type="text" id="username" required />
        </div>
        <div class="form-group">
          <label for="password"></label>
          <input type="password" id="password" required />
        </div>
        <p class="forgot-password">비밀번호를 잃어버리셨나요?</p>
        <button>로그인</button>
      </div>
      <div class="right-section">
        <h1 class="signup-title">CINEHIVE</h1>
        <div class="signup-prompt">
          <p>회원이 아니신가요?</p>
          <button class="signup-button">회원가입</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
// 필요한 로직이 있다면 여기에 추가
</script>

<style scoped>
#login-container {
  display: flex;
  height: 100vh; /* 전체 화면 높이 */
  justify-content: center; /* 수평 중앙 정렬 */
  align-items: center; /* 수직 중앙 정렬 */
}

.box-container {
  display: flex;
  width: 60%; /* 너비를 60%로 조정 */
  height: 60%; /* 높이를 60%로 조정 */
}

.left-section {
  flex: 1;
  background-color: #D9D9D9;
  display: flex;
  flex-direction: column;
  align-items: center; /* 수평 중앙 정렬 */
  justify-content: center; /* 수직 중앙 정렬 */
  padding: 20px; /* 내부 여백 */
}

.right-section {
  flex: 1;
  background-color: #393636;
  padding: 20px; /* 내부 여백 */
  display: flex;
  flex-direction: column;
  justify-content: center; /* 수직 중앙 정렬 */
  align-items: center; /* 수평 중앙 정렬 */
}
.login-title-h1{
  color: #393636;
  position: relative;
  top:-50px;
}

.login-buttons {
  display: flex;
  justify-content: center; /* 가로로 중앙 정렬 */
  margin-bottom: 10px; /* 이미지 간격 */
}

.login-image {
  margin: 0 20px; /* 이미지 간격 조정 */
  position: relative;
  top:-50px;
}

.or-use-your-account {
  margin: 10px 0; /* 문구 간격 */
  text-align: center; /* 중앙 정렬 */
  position: relative;
  top:-30px;
  font-size: 14px;
  font-weight: bolder;
  color: #393636;
}

.form-group {
  margin-bottom: 15px;
  width: 100%; /* 입력 필드가 섹션 너비에 맞게 조정 */
  text-align: center; /* 중앙 정렬 */
}

label {
  display: block;
  margin-bottom: 5px;
  color: black; /* 검은색 텍스트 */
}

input {
  width: 100%;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 4px;
}

.forgot-password {
  margin: 10px 0; /* 문구 간격 */
  text-align: center; /* 중앙 정렬 */
}


.right-section {
  flex: 1;
  background-color: #393636;
  padding: 20px; /* 내부 여백 */
  display: flex;
  flex-direction: column;
  justify-content: center; /* 수직 중앙 정렬 */
  align-items: center; /* 수평 중앙 정렬 */
}

.signup-title {
  margin-bottom: 20px;
  color: #F50000; /* 빨간색 텍스트 */
}

.signup-prompt {
  margin-top: 20px; /* 로그인 폼과 회원가입 문구 간격 */
  text-align: center; /* 중앙 정렬 */
}

.signup-button {
  margin-top: 10px; /* 문구와 버튼 간격 */
  background-color: #1E1E1E;
  width: 120px;
  height: 40px;
  font-size: 13px;
  font-weight: bolder;
}
</style>
