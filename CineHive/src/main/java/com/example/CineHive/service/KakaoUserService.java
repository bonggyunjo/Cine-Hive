package com.example.CineHive.service;

import com.example.CineHive.dto.KakaoUserInfo;
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
    private KakaoUserRepository userRepository;

    @Autowired
    private UserRepository user1Repository;

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
                JSONObject jsonObject = new JSONObject(responseBody);
                KakaoUserInfo userInfo = new KakaoUserInfo();
                userInfo.setId(String.valueOf(jsonObject.getLong("id")));
                userInfo.setKakaoId(userInfo.getId());
                JSONObject properties = jsonObject.getJSONObject("properties");
                userInfo.setNickname(properties.getString("nickname"));
                userInfo.setEmail(properties.getString("email"));
                return userInfo;
            } else {
                throw new RuntimeException("Failed to get user info: " + response.message());
            }
        }
    }

    public void registerUser(KakaoUserInfo userInfo) {
        // SocialUser 저장
        KakaoUser socialUser = userRepository.findByKakaoId(userInfo.getKakaoId())
                .orElse(new KakaoUser(userInfo.getKakaoId(), userInfo.getNickname(), userInfo.getEmail()));
        userRepository.save(socialUser);

        // users 테이블에 사용자 정보 저장
        User newUser = user1Repository.findByKakaoId(userInfo.getKakaoId()).orElse(null);
        if (newUser == null) {
            newUser = new User();
            newUser.setMem_email(userInfo.getEmail());
            newUser.setMem_pw("0");
            newUser.setMem_nickname(userInfo.getNickname());
            newUser.setMem_register_datetime(LocalDateTime.now());
            newUser.setKakaoId(userInfo.getKakaoId());
            newUser.setMem_type("카카오");
            user1Repository.save(newUser);
        }
    }

}