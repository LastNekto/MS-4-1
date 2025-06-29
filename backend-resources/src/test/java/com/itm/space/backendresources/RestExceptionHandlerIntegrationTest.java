package com.itm.space.backendresources;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
public class RestExceptionHandlerIntegrationTest extends BaseIntegrationTest {

    @Test
    @WithMockUser(roles = "MODERATOR")
    void testBackendResourcesExceptionHandling() throws Exception {
        mvc.perform(get("/api/users/" + java.util.UUID.randomUUID()))
                .andExpect(status().is5xxServerError())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Exception")));
    }

    @Test
    @WithMockUser(roles = "MODERATOR")
    void testValidationExceptionHandling() throws Exception {
        String invalidJson = """
            {
              "username": "",
              "email": "invalid-email",
              "password": "",
              "firstName": "",
              "lastName": ""
            }
            """;

        mvc.perform(
                post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson)
        )
        .andExpect(status().isBadRequest())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithMockUser(roles = "MODERATOR")
    void testHelloEndpointWorksNormally() throws Exception {
        mvc.perform(get("/api/users/hello"))
                .andExpect(status().isOk());
    }
} 