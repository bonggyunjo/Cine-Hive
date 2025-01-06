## `CineHive` Project
- TMDB API를 활용하여 Spring+Java를 이용한 영화 소개 프로젝트 
## 시스템 아키텍처

IDE : IntelliJ 

엔진 및 언어 : Spring & Java (RestfulAPI and TMDB API)

DMBS : MariaDB

클라우드 서비스 : AWS (ec2 + rds)

---

### 시퀸스 다이어그램 & 기능 요구사항
- <a href="https://www.notion.so/15b8dbd47b68804dbbc5fcd38456ac7c?pvs=13" class="no-underline">🔍Data Flow</a>
- <a href="https://o365deu-my.sharepoint.com/:x:/g/personal/20193182_office_deu_ac_kr/EQp7XdyTBDhBiZ5o-DJJEzQBmfE28vab2-ylCRDJKjSFrw" class="no-underline">🔍기능 요구사항</a>
- <a href="https://www.notion.so/15d8dbd47b68804e8740e8b8a5c77c04?pvs=4" class="no-underline">🔍플로우 차트</a> 
### ERD

`회원 테이블`
   
![회원테이블-removebg-preview](https://github.com/user-attachments/assets/707d7942-309b-437a-98f5-b3646881de16)

`게시판 테이블`

![게시판-removebg-preview](https://github.com/user-attachments/assets/1843d57a-2377-4551-8772-1ae8e2dbc384)

`영화 테이블`

![영화-removebg-preview](https://github.com/user-attachments/assets/a0af7c4c-4185-4ccf-856d-f2d069edc742)

### 기능
1. 회원가입
> 아이디(이메일), 비밀번호, 이름, 성별, 생일, 닉네임, 선호 장르, 연락처

> 소셜 로그인 (카카오, 구글, 네이버)

> OTT 계정 연동(해당 영화/드라마 바로보기)

3. 로그인
> 비밀번호 찾기

>  소셜 로그인 (동일)

>  JWT 적용

4. 마이페이지
> 회원 정보 조회

> 회원 정보 수정 (비밀번호, 닉네임, 선호 장르 등)

> 회원 탈퇴

> 영화 평가에 대한 정보 불러오기

- 내가 찜한 목록

- 내가 시청한 목록

- 내가 작성한 리뷰(영화 점수 평가)

- 내가 작성한 감상평

- 내가 평가한 리뷰/감성평 목록 (좋아요/싫어요)

> 게시판에 대한 정보 불러오기

- 내가 작성 게시글
  
- 내가 작성한 댓글/대댓글
  
- 내가 좋아요/싫어요한 게시글

- 내가 좋아요/싫어요한 댓글
  
- 내가 신고한 게시글
  
- 내가 찜한 게시글

5. 게시판
> 게시글 작성
> 게시글 조회/수정/삭제
> 댓글 작성/삭제
> 대댓글 작성/삭제
> 좋아요/ 싫어요 (글 평가)
> 북마크 ( 찜하기 )
> 신고하기(게시글, 댓글)
> 검색
> 정렬 ( 최신 순, 오래된 순, 인기 순 )

6. 영화 (TMDB API)
> 드라마/영화/애니

> 검색 기능은 모두 검색 가능 (화면에 보여줄 땐 구분 )

> 첫 화면에 띄울 때 보여줄 목록

- 최신 인기 순위 (API)
  
- 평점 순위 (API 내부 점수)
  
- 현재 상영중인 영상(영화, 드라마, 애니 다 포함)
  
- ~~맞춤 작품 (내가 찜한 작품 기반으로 추천) → 학습 시켜야 할 데이터 양이 적음~~
- 내가 선호하는 장르 중 인기 작품 출력
  
- 찜한 작품

> 작품 하나 선택해서 들어갔을 때 (상세페이지)
- 작품 제목
  
- 평점 (10점 만점)
- 줄거리
  
- 제작 정보(감독 이름, 출연진 등등…)
  
- 사용자 리뷰 (최근 리뷰 3개 보여주기, 슬라이드해서 더보기, MORE 버튼)
  
- 사진 (포스터)
  
- 트레일러 (TMDB API의 credit 정보)
  
- 감상평 / 작품 해석 버튼 ( 눌렀을 때 상세 페이지로 이동 )
  
+) 번역 기능
- 찜하기
  
- 어디서 볼 수 있는지 OTT 정보 (바로가기 링크 제공해주면 좋을듯)
    - 시청 여부(회의)

> 검색 기능 ( 태그 가능한 것들 )

- 검색으로 관련 정보 모두 나오게
  
- 검색하고 나서 화면에 보여주는 건 각 컨텐츠별로(나오게끔 영화, 드라마, 애니메이션 )
