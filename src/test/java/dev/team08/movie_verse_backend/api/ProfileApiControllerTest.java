package dev.team08.movie_verse_backend.api;

import dev.team08.movie_verse_backend.controller.api.ProfileApiController;
import dev.team08.movie_verse_backend.dto.request.GenreRequest;
import dev.team08.movie_verse_backend.dto.request.UserProfileRequest;
import dev.team08.movie_verse_backend.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProfileApiControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private ProfileApiController controller;

    // 模拟传入的 token 字符串
    private final String token = "Bearer validToken";

    /**
     * 测试获取用户资料接口：
     * 当调用 getProfile 时，控制器应调用 userService.getUserProfile(token) 并返回对应的 UserProfileRequest
     */
    @Test
    void getProfile_ReturnsUserProfileRequest() {
        // 创建一个模拟的 UserProfileRequest，使用 6 个参数构造
        UUID id = UUID.randomUUID();
        String username = "testUser";
        String email = "test@example.com";
        String password = "secretPassword";
        String avatar = "avatarUrl";
        // 此处假设不返回任何 Genre，传入空列表
        UserProfileRequest expectedProfile = new UserProfileRequest(id, username, email, password, avatar, Collections.emptyList());

        // 配置 userService 行为：当传入 token 时返回 expectedProfile
        when(userService.getUserProfile(token)).thenReturn(expectedProfile);

        // 调用控制器接口
        UserProfileRequest result = controller.getProfile(token);

        // 验证结果
        assertNotNull(result);
        assertEquals(username, result.getUsername());
        assertEquals(email, result.getEmail());
        assertEquals(password, result.getPassword());
        assertEquals(avatar, result.getAvatar());
        assertEquals(Collections.emptyList(), result.getFavouriteGenres());
    }

    /**
     * 测试更新用户资料接口，更新成功时返回 200 状态码和成功消息
     */
    @Test
    void updateUserProfile_Success_ReturnsOk() {
        // 构造一个模拟的更新请求对象
        UUID id = UUID.randomUUID();
        String username = "updatedUser";
        String email = "updated@example.com";
        String password = "updatedPassword";
        String avatar = "updatedAvatar";
        UserProfileRequest updatedProfile = new UserProfileRequest(id, username, email, password, avatar, Collections.emptyList());

        // 模拟 service 更新成功，返回 true
        when(userService.updateUserProfile(token, updatedProfile)).thenReturn(true);

        // 调用控制器的更新接口
        ResponseEntity<?> response = controller.updateUserProfile(token, updatedProfile);

        // 验证响应状态和消息
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, String> expectedBody = Collections.singletonMap("message", "Profile updated successfully");
        assertEquals(expectedBody, response.getBody());
    }

    /**
     * 测试更新用户资料接口，更新失败时返回 400 状态码和错误消息
     */
    @Test
    void updateUserProfile_Failure_ReturnsBadRequest() {
        // 构造一个模拟的更新请求对象
        UUID id = UUID.randomUUID();
        String username = "updatedUser";
        String email = "updated@example.com";
        String password = "updatedPassword";
        String avatar = "updatedAvatar";
        UserProfileRequest updatedProfile = new UserProfileRequest(id, username, email, password, avatar, Collections.emptyList());

        // 模拟 service 更新失败，返回 false
        when(userService.updateUserProfile(token, updatedProfile)).thenReturn(false);

        // 调用控制器接口
        ResponseEntity<?> response = controller.updateUserProfile(token, updatedProfile);

        // 验证响应状态和错误消息
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Map<String, String> expectedBody = Collections.singletonMap("error", "Failed to update profile");
        assertEquals(expectedBody, response.getBody());
    }
}
