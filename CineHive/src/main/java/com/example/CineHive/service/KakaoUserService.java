package com.example.CineHive.service;

import com.example.CineHive.dto.KakaoUserInfo;
import com.example.CineHive.dto.UserDto;
import com.example.CineHive.entity.KakaoUser;
import com.example.CineHive.entity.User;
import com.example.CineHive.repository.KakaoUserRepository;
import com.example.CineHive.repository.UserRepository;
import lombok.Getter;
import org.json.JSONObject;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.time.LocalDateTime;

@Service
public class KakaoUserService {

    @Getter
    @Value("${kakao.client.id}")
    private String clientId;

    @Getter
    @Value("${kakao.redirect.uri}")
    private String redirectUri;

    @Getter
    @Value("${kakao.logout.redirect.uri}")
    private String logoutRedirectUri;

    @Autowired
    private KakaoUserRepository kakaouserRepository;

    @Autowired
    private UserRepository userRepository;

    private final OkHttpClient client = new OkHttpClient();

    public String getAccessToken(String code) throws IOException {
        String tokenUrl = "https://kauth.kakao.com/oauth/token";
        FormBody formBody = new FormBody.Builder()
                .add("grant_type", "authorization_code")
                .add("client_id", clientId)
                .add("redirect_uri", redirectUri)
                .add("code", code)
                .build();

        Request request = new Request.Builder()
                .url(tokenUrl)
                .post(formBody)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                String responseBody = response.body().string();
                JSONObject jsonObject = new JSONObject(responseBody);
                return jsonObject.getString("access_token");
            } else {
                throw new RuntimeException("Failed to get access token: " + response.message());
            }
        }
    }

    public KakaoUserInfo getUserInfo(String accessToken) throws IOException {
        String userInfoUrl = "https://kapi.kakao.com/v2/user/me";
        Request request = new Request.Builder()
                .url(userInfoUrl)
                .addHeader("Authorization", "Bearer " + accessToken)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                String responseBody = response.body().string();
                System.out.println("Response Body: " + responseBody); // 응답 출력
                JSONObject jsonObject = new JSONObject(responseBody);
                KakaoUserInfo userInfo = new KakaoUserInfo();
                userInfo.setKakaoId(userInfo.getKakaoId());
                userInfo.setKakaoId(String.valueOf(jsonObject.getLong("id")));
                JSONObject properties = jsonObject.getJSONObject("properties");
                userInfo.setNickname(properties.getString("nickname"));

                // 이메일 필드가 있는지 확인
                if (jsonObject.has("kakao_account")) {
                    JSONObject kakaoAccount = jsonObject.getJSONObject("kakao_account");
                    if (kakaoAccount.has("email")) {
                        userInfo.setEmail(kakaoAccount.getString("email"));
                    } else {
                        userInfo.setEmail("이메일 미제공"); // 기본값 설정
                    }
                } else {
                    userInfo.setEmail("이메일 미제공");
                }
                return userInfo;
            } else {
                throw new RuntimeException("Failed to get user info: " + response.message());
            }
        }
    }


    public void registerUser(KakaoUserInfo userInfo) {
        // SocialUser 저장
        KakaoUser socialUser = kakaouserRepository.findByKakaoId(userInfo.getKakaoId())
                .orElse(new KakaoUser(userInfo.getKakaoId(), userInfo.getNickname(), userInfo.getEmail()));
        kakaouserRepository.save(socialUser);

        // users 테이블에 사용자 정보 저장
        User newUser = userRepository.findByKakaoId(userInfo.getKakaoId()).orElse(null);
        UserDto userDto = new UserDto();
        if (newUser == null) {
            newUser = new User();
            newUser.setMemPhone(userDto.getMemPhone());
            newUser.setMemSex(userDto.getMemSex());
            newUser.setMemName(userDto.getMemName());
            newUser.setMemUserid(userDto.getKakaoId());
            // 일단 0 OR default 값으로 설정하고 추후에 클라이언트 구현할 때 수정 필요



            newUser.setMemEmail(userInfo.getEmail()); //이메일
            newUser.setMemPw("0"); //비밀번호는 디폴트 0으로 (소셜로그인은 비밀번호 제공 x)
            newUser.setMemNickname(userInfo.getNickname()); // 닉네임
            newUser.setMemRegisterDatetime(LocalDateTime.now()); //날짜
            newUser.setKakaoId(userInfo.getKakaoId());  // 카카오 아이디
            newUser.setMemType("카카오");  //가입유형
            userRepository.save(newUser);
        }
    }

}