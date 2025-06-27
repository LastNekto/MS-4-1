package com.itm.space.backendresources;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserControllerIntegrationTest extends BaseIntegrationTest {
    @Test
    @WithMockUser(roles = "MODERATOR")
    void hello_shouldReturnOk() throws Exception{
        mvc.perform(get("/api/users/hello"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "MODERATOR")
    void getUserById_shouldReturnOk() throws Exception {
        UUID dummyId = UUID.randomUUID();
        mvc.perform(get("/api/users/" + dummyId))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "MODERATOR")
    void createUser_shouldReturnCreated() throws Exception {
        String json = """
            {
              "username": "usertest",
              "email": "test@example.com",
              "password": "testpassword",
              "firstName": "Test",
              "lastName": "User"
            }
            """;

        mvc.perform(
                        post("/api/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
                )
                .andExpect(status().isOk());
    }
}
