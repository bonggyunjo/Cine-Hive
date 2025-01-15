
package com.example.CineHive.service;

import com.example.CineHive.dto.NaverUserInfo;
import com.example.CineHive.entity.NaverUser;
import com.example.CineHive.repository.NaverUserRepository;
import com.example.CineHive.repository.UserRepository;
import lombok.Getter;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.example.CineHive.entity.User; // 사용자 정의 User 클래스
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Scanner;


@Service
public class NaverUserService {

    @Getter
    @Value("${naver.client.id}")
    private String clientId;

    @Getter
    @Value("${naver.client.secret}")
    private String clientSecret;

    private final NaverUserRepository naverUserRepository;
    private final UserRepository userRepository;

    public NaverUserService(NaverUserRepository naverUserRepository, UserRepository userRepository) {
        this.naverUserRepository = naverUserRepository;
        this.userRepository = userRepository;
    }

    public String getAccessToken(String code) throws IOException {
        String url = "https://nid.naver.com/oauth2.0/token?client_id=" + clientId +
                "&client_secret=" + clientSecret +
                "&grant_type=authorization_code" +
                "&code=" + code;

        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setRequestMethod("GET");

        int responseCode = conn.getResponseCode();
        if (responseCode == 200) {
            Scanner scanner = new Scanner(conn.getInputStream());
            StringBuilder response = new StringBuilder();
            while (scanner.hasNext()) {
                response.append(scanner.nextLine());
            }
            scanner.close();

            JSONObject jsonObject = new JSONObject(response.toString());
            return jsonObject.getString("access_token");
        } else {
            throw new IOException("Failed to get access token: " + responseCode);
        }
    }

    public NaverUserInfo getUserInfo(String accessToken) throws IOException {
        String userInfoUrl = "https://openapi.naver.com/v1/nid/me";
        HttpURLConnection conn = (HttpURLConnection) new URL(userInfoUrl).openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Authorization", "Bearer " + accessToken);

        int responseCode = conn.getResponseCode();
        if (responseCode == 200) {
            Scanner scanner = new Scanner(conn.getInputStream());
            StringBuilder response = new StringBuilder();
            while (scanner.hasNext()) {
                response.append(scanner.nextLine());
            }
            scanner.close();

            JSONObject jsonObject = new JSONObject(response.toString());
            JSONObject responseObject = jsonObject.getJSONObject("response");

            NaverUserInfo userInfo = new NaverUserInfo();
            userInfo.setNaverId(responseObject.getString("id"));
            userInfo.setEmail(responseObject.getString("email"));
            userInfo.setNickname(responseObject.getString("nickname"));

            return userInfo;
        } else {
            throw new IOException("Failed to get user info: " + responseCode);
        }
    }

    public void registerUser(NaverUserInfo userInfo) {
        NaverUser naverUser = naverUserRepository.findByNaverId(userInfo.getNaverId())
                .orElse(new NaverUser(userInfo.getNaverId(), userInfo.getNickname(), userInfo.getEmail()));

        naverUserRepository.save(naverUser);

        User newUser = userRepository.findByNaverId(userInfo.getNaverId()).orElse(null);
        if (newUser == null) {
            newUser = new User();
            newUser.setMemPw("0"); //비밀번호는 디폴트 0으로 (소셜로그인은 비밀번호 제공 x)
            newUser.setMemUserid(userInfo.getNaverId());
            newUser.setMemName("0");
            newUser.setMemSex("0");
            newUser.setMemPhone("0");
            // 일단 0 OR default 값으로 설정하고 추후에 클라이언트 구현할 때 수정 필요

            //네이버에서 동의 항목에서 체크한 목록들
            newUser.setMemEmail(userInfo.getEmail()); // 이메일 추가
            newUser.setMemNickname(userInfo.getNickname());
            newUser.setMemRegisterDatetime(LocalDateTime.now());
            newUser.setNaverId(userInfo.getNaverId());  // 네이버 아이디
            newUser.setMemType("네이버");
            userRepository.save(newUser);
        }
    }
}