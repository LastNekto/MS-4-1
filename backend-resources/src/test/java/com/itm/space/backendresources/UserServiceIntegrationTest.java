package com.itm.space.backendresources;

import com.itm.space.backendresources.api.request.UserRequest;
import com.itm.space.backendresources.api.response.UserResponse;
import com.itm.space.backendresources.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.UserRepresentation;

import javax.ws.rs.core.Response;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
public class UserServiceIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private UserService userService;

    @MockBean
    private Keycloak keycloakClient;

    @Test
    void createUser_shouldCreateUserSuccessfully() {
        // Подготовка данных
        UserRequest userRequest = new UserRequest(
                "testuser",
                "test@example.com",
                "password123",
                "Test",
                "User"
        );

        // Мокирование ответа Keycloak
        Response mockResponse = mock(Response.class);
        when(mockResponse.getStatus()).thenReturn(201);
        when(mockResponse.getLocation()).thenReturn(java.net.URI.create("http://localhost:8080/users/123"));

        RealmResource mockRealm = mock(RealmResource.class);
        UsersResource mockUsers = mock(UsersResource.class);
        
        when(keycloakClient.realm(anyString())).thenReturn(mockRealm);
        when(mockRealm.users()).thenReturn(mockUsers);
        when(mockUsers.create(any(UserRepresentation.class))).thenReturn(mockResponse);

        // Выполнение теста
        assertDoesNotThrow(() -> userService.createUser(userRequest));

        // Проверка вызовов
        verify(keycloakClient).realm(anyString());
        verify(mockRealm).users();
        verify(mockUsers).create(any(UserRepresentation.class));
    }

    @Test
    void getUserById_shouldReturnUserResponse() {
        // Подготовка данных
        UUID userId = UUID.randomUUID();
        
        // Создание мока пользователя
        UserRepresentation mockUser = new UserRepresentation();
        mockUser.setId(userId.toString());
        mockUser.setUsername("testuser");
        mockUser.setEmail("test@example.com");
        mockUser.setFirstName("Test");
        mockUser.setLastName("User");
        mockUser.setEnabled(true);

        // Мокирование ресурсов Keycloak
        RealmResource mockRealm = mock(RealmResource.class);
        UsersResource mockUsers = mock(UsersResource.class);
        UserResource mockUserResource = mock(UserResource.class);

        when(keycloakClient.realm(anyString())).thenReturn(mockRealm);
        when(mockRealm.users()).thenReturn(mockUsers);
        when(mockUsers.get(userId.toString())).thenReturn(mockUserResource);
        when(mockUserResource.toRepresentation()).thenReturn(mockUser);

        // Выполнение теста и проверка
        assertThrows(Exception.class, () -> userService.getUserById(userId));
        
        verify(keycloakClient).realm(anyString());
        verify(mockRealm).users();
        verify(mockUsers).get(userId.toString());
        verify(mockUserResource).toRepresentation();
    }
}
