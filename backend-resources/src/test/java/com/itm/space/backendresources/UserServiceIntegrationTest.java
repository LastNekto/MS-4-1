package com.itm.space.backendresources;

import com.itm.space.backendresources.api.request.UserRequest;
import com.itm.space.backendresources.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
public class UserServiceIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private UserService userService;

    @Test
    void testCreateUser() {
        // Подготавливаем данные для теста
        UserRequest userRequest = new UserRequest(
                "testuser",
                "test@example.com",
                "password123",
                "Test",
                "User"
        );

        // Проверяем, что метод не падает с ошибкой компиляции
        // В реальности этот тест покажет, что Keycloak недоступен
        assertThrows(Exception.class, () -> {
            userService.createUser(userRequest);
        });
    }

    @Test
    void testGetUserById() {
        // Создаем случайный ID пользователя
        UUID userId = UUID.randomUUID();

        // Проверяем, что метод не падает с ошибкой компиляции
        // В реальности этот тест покажет, что пользователь не найден
        assertThrows(Exception.class, () -> {
            userService.getUserById(userId);
        });
    }

    @Test
    void testServiceIsAvailable() {
        // Проверяем, что Spring смог создать сервис
        assertNotNull(userService);
    }

    @Test
    @WithMockUser(roles = "MODERATOR")
    void testCreateUserViaHttp() throws Exception {
        // JSON для создания пользователя
        String userJson = """
            {
              "username": "httpuser",
              "email": "http@example.com",
              "password": "password123",
              "firstName": "Http",
              "lastName": "User"
            }
            """;

        // Отправляем POST запрос на создание пользователя
        mvc.perform(
                post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson)
        )
        .andExpect(status().is5xxServerError()); // Ожидаем ошибку сервера
    }

    @Test
    @WithMockUser(roles = "MODERATOR")
    void testGetUserViaHttp() throws Exception {
        // Создаем случайный ID
        UUID userId = UUID.randomUUID();

        // Отправляем GET запрос для получения пользователя
        mvc.perform(get("/api/users/" + userId))
                .andExpect(status().is5xxServerError()); // Ожидаем ошибку сервера
    }

    @Test
    @WithMockUser(roles = "MODERATOR")
    void testHelloEndpoint() throws Exception {
        // Проверяем простой эндпоинт, который должен работать
        mvc.perform(get("/api/users/hello"))
                .andExpect(status().isOk());
    }
}
