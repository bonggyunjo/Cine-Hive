<template>
  <div id="additional-info-container">
    <h1>추가 정보 입력</h1>
    <form @submit.prevent="submitAdditionalInfo">
      <div>
        <input type="email" v-model="memUserid" placeholder="이메일" required />
      </div>
      <div>
        <input type="text" v-model="memName" placeholder="이름" required />
      </div>
      <div>
        <select v-model="memSex" required>
          <option value="" disabled selected>성별</option>
          <option value="male">남성</option>
          <option value="female">여성</option>
          <option value="other">기타</option>
        </select>
      </div>
      <div>
        <input type="text" v-model="memPhone" placeholder="연락처" required />
      </div>
      <div>
        <label>관심 장르를 선택하세요:</label>
        <div class="genre-checkboxes">
          <label>
            <input type="checkbox" value="드라마" v-model="selectedGenres" /> 드라마
          </label>
          <label>
            <input type="checkbox" value="애니메이션" v-model="selectedGenres" /> 애니메이션
          </label>
          <label>
            <input type="checkbox" value="영화" v-model="selectedGenres" /> 영화
          </label>
        </div>
      </div>
      <button type="submit" :disabled="!userInfo">회원가입</button>
    </form>
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
      memUserid:'',
      userInfo: null, // 카카오 사용자 정보를 저장할 변수
      selectedGenres: [], // 선택한 장르를 저장할 배열
      memPassword:''
    };
  },
  created() {
    this.getUserInfo(); // 컴포넌트가 생성될 때 사용자 정보 가져오기
  },
  methods: {
    async getUserInfo() {
      try {
        const response = await axios.get('http://localhost:8081/api/auth/kakao/success', { withCredentials: true });
        this.userInfo = response.data; // 사용자 정보 저장

        // 사용자 존재 여부 확인
        const userExistsResponse = await axios.get('http://localhost:8081/api/auth/check-user', {
          params: { kakaoId: this.userInfo.kakaoId }
        });

        if (userExistsResponse.data.exists) { // 수정된 부분
          // 사용자가 이미 가입한 경우 홈으로 리다이렉트
          this.$router.push('/');
        } else {
          // 사용자가 가입하지 않은 경우 추가 정보 입력 필드 설정
          this.memName = this.userInfo.nickname; // 닉네임을 memName에 설정
        }

        console.log('사용자 정보:', this.userInfo);
      } catch (error) {
        if (error.response) {
          console.error('사용자 정보 가져오기 실패:', error.response.data);
          alert(`사용자 정보를 가져올 수 없습니다: ${error.response.data}`);
        } else {
          console.error('요청 실패:', error);
          alert('서버와 연결할 수 없습니다. 다시 시도해주세요.');
        }
        this.$router.push('/login'); // 로그인 페이지로 리다이렉트
      }
    },
    async submitAdditionalInfo() {
      const userData = {
        memUserid: this.memUserid,
        kakaoId: this.userInfo.kakaoId,
        memNickname: this.userInfo.nickname,
        memName: this.memName, // 클라이언트에서 입력받은 이름
        memPhone: this.memPhone, // 클라이언트에서 입력받은 연락처
        memSex: this.memSex, // 클라이언트에서 선택한 성별
        memEmail: this.userInfo.email, // 카카오에서 가져온 이메일
        genres: this.selectedGenres, // 선택한 장르
        memPassword:'0'
      };

      try {
        const response = await axios.post('http://localhost:8081/register', userData);
        alert(response.data); // 성공 메시지 표시
        this.$router.push('/'); // 홈으로 리다이렉트
      } catch (error) {
        alert('정보 제출 중 오류가 발생했습니다.');
      }
    },
  },
};
</script>

<style scoped>
.genre-checkboxes {
  display: flex;
  flex-direction: column;
}
</style>
