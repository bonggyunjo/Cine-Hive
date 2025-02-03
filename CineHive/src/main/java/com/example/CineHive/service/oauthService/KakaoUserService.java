package com.example.CineHive.service.oauthService;

import com.example.CineHive.dto.oauthInfo.KakaoUserInfo;
import com.example.CineHive.entity.oauth.KakaoUser;
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
        }
}