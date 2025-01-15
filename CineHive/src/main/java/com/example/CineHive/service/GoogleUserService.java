package com.example.CineHive.service;

import com.example.CineHive.dto.GoogleUserInfo;
import com.example.CineHive.entity.GoogleUser;
import com.example.CineHive.entity.User;
import com.example.CineHive.repository.GoogleUserRepository;
import com.example.CineHive.repository.UserRepository;
import org.springframework.stereotype.Service;
import lombok.Getter;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import java.io.IOException;
import java.time.LocalDateTime;
@Service
public class GoogleUserService {


    @Getter
    @Value("${google.client.id}")
    private String clientId;

    @Getter
    @Value("${google.client.secret}")
    private String clientSecret;

    @Getter
    @Value("${google.redirect.uri}")
    private String redirectUri;


    private final GoogleUserRepository googleUserRepository;
    private final UserRepository userRepository;
    private final RestTemplate restTemplate;

    @Autowired
    public GoogleUserService(GoogleUserRepository googleUserRepository, UserRepository userRepository, RestTemplate restTemplate) {
        this.googleUserRepository = googleUserRepository;
        this.userRepository = userRepository;
        this.restTemplate = restTemplate;
    }

    public String getAccessToken(String code) throws IOException {
        String url = "https://oauth2.googleapis.com/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED);

        String requestBody = "client_id=" + clientId +
                "&client_secret=" + clientSecret +
                "&code=" + code +
                "&redirect_uri=" + redirectUri +
                "&grant_type=authorization_code";

        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            JSONObject jsonObject = new JSONObject(response.getBody());
            return jsonObject.getString("access_token");
        } else {
            throw new IOException("Failed to get access token: " + response.getStatusCode());
        }
    }

    public GoogleUserInfo getUserInfo(String accessToken) throws IOException {
        String userInfoUrl = "https://www.googleapis.com/oauth2/v1/userinfo?access_token=" + accessToken;

        ResponseEntity<String> response = restTemplate.getForEntity(userInfoUrl, String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            JSONObject jsonObject = new JSONObject(response.getBody());
            GoogleUserInfo userInfo = new GoogleUserInfo();
            userInfo.setGoogleId(jsonObject.getString("id"));
            userInfo.setNickname(jsonObject.getString("name"));
            userInfo.setEmail(jsonObject.getString("email"));
            return userInfo;
        } else {
            throw new IOException("Failed to get user info: " + response.getStatusCode());
        }
    }

    public void registerUser(GoogleUserInfo userInfo) {
        GoogleUser googleUser = googleUserRepository.findByGoogleId(userInfo.getGoogleId())
                .orElse(new GoogleUser(userInfo.getGoogleId(), userInfo.getNickname(), userInfo.getEmail()));

        googleUserRepository.save(googleUser);

        User newUser = userRepository.findByGoogleId(userInfo.getGoogleId()).orElse(null);
        if (newUser == null) {
            newUser = new User();
            newUser.setMemPw("0"); //비밀번호는 디폴트 0으로 (소셜로그인은 비밀번호 제공 x)
            newUser.setMemName("0");
            newUser.setMemSex("0");
            newUser.setMemPhone("0");
            newUser.setMemUserid(userInfo.getGoogleId());

            // 일단 0 OR default 값으로 설정하고 추후에 클라이언트 구현할 때 수정 필요

            //네이버에서 동의 항목에서 체크한 목록들
            newUser.setMemEmail(userInfo.getEmail()); // 이메일 추가
            newUser.setMemNickname(userInfo.getNickname());
            newUser.setMemRegisterDatetime(LocalDateTime.now());
            newUser.setGoogleId(userInfo.getGoogleId());  // 구글 아이디
            newUser.setMemType("네이버");
            userRepository.save(newUser);
        }
    }
}