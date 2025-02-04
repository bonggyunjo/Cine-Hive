package com.example.CineHive.service.oauthService;

import com.example.CineHive.dto.oauth.GoogleUserInfo;
import com.example.CineHive.entity.oauth.GoogleUser;
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
    }
}