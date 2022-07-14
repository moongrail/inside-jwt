package ru.inside.jwt.controllers;

import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;
import ru.inside.jwt.dto.MessageDto;
import ru.inside.jwt.dto.SignUpDto;
import ru.inside.jwt.services.AccountsService;
import ru.inside.jwt.services.DecoderHeaderService;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.is;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class ApiControllerTest {

    static final String HEADER_TOKEN_JWT = "Bearer_eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwibmFtZSI6Ikluc2lkZSJ9.kMY2G7GWI5uKWQ7l3ewF-p9-d0M3FLAaPYGKm4hJmYs";
    static final String TOKEN_JWT = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwibmFtZSI6Ikluc2lkZSJ9.kMY2G7GWI5uKWQ7l3ewF-p9-d0M3FLAaPYGKm4hJmYs";
    static final String TEST_NAME = "Inside";
    static final String TEST_MESSAGE = "TEST";

    static final String TEST_PASSWORD = "12321aaa";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountsService accountsService;

    @MockBean
    private DecoderHeaderService decoderHeaderService;

    @Mock
    private RestTemplate restTemplate;

    @BeforeEach
    void setUp() {

        when(accountsService.saveAccount(SignUpDto.builder()
                .name(TEST_NAME)
                .password(TEST_PASSWORD)
                .build()))
                .thenReturn(Optional.of(SignUpDto.builder()
                .name(TEST_NAME)
                .build()));

        when(restTemplate.getForObject("http://localhost/api/v1/login", String.class)).thenReturn(TOKEN_JWT);
        when(decoderHeaderService.getNameFromJwt(HEADER_TOKEN_JWT)).thenReturn(Optional.of(TEST_NAME));

        when(accountsService.getMessages(TEST_NAME)).thenReturn(Collections.singletonList(MessageDto.builder()
                .name(TEST_NAME)
                .message(TEST_MESSAGE)
                .build()));
    }

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    @DisplayName("Sign Up tests")
    class SignUpTets {
        @Test
        public void status_200_sign_up() throws Exception {
            mockMvc.perform(post("/api/v1/signUp")
                            .content("{\"name\": \"Inside\", \"password\": \"12321aaa\"}")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().is2xxSuccessful())
                    .andExpect(jsonPath("data", is("Account '" + TEST_NAME + "' created.")));
        }

        @Test
        public void status_400_sign_up() throws Exception {
            mockMvc.perform(post("/api/v1/signUp")
                            .content("{\"name\": \"\", \"password\": \"\"}")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().is4xxClientError());
        }
    }

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    @DisplayName("Login tests")
    class LoginTests {

        @Test
        void status_200_login() throws Exception {

            mockMvc.perform(post("/api/v1/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"name\": \"Inside\", \"password\": \"12321aaa\"}"))
                    .andDo(print())
                    .andExpect(status().is2xxSuccessful())
                    .andExpect(jsonPath("token", is(TOKEN_JWT)));
        }

        @Test
        void status_400_login() throws Exception {

            mockMvc.perform(post("/api/v1/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"name\": \"Inside\", \"password\": \"123333333321aaa\"}"))
                    .andDo(print())
                    .andExpect(status().is4xxClientError());
        }
    }

    @Nested
    @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
    @DisplayName("Messages tests")
    class MessagesTests {

        @Test
        void status_200_add_message() throws Exception {

            mockMvc.perform(post("/api/v1/messages")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", HEADER_TOKEN_JWT)
                            .content("{\"name\": \"Inside\", \"message\": \"TEST\"}"))
                    .andDo(print())
                    .andExpect(status().is2xxSuccessful())
                    .andExpect(jsonPath("data", is("Message: '" + TEST_MESSAGE + "' saved.")));
        }

        @Test
        void status_200_get_history() throws Exception {

            mockMvc.perform(post("/api/v1/messages")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", HEADER_TOKEN_JWT)
                            .content("{\"name\": \"Inside\", \"message\": \"history 10\"}"))
                    .andDo(print())
                    .andExpect(status().is2xxSuccessful())
                    .andExpect(jsonPath("success", is(true)))
                    .andExpect(jsonPath("data.[0].name", is("Inside")))
                    .andExpect(jsonPath("data.[0].message", is("TEST")));
        }

        @Test
        void status_403_add_message() throws Exception {

            mockMvc.perform(post("/api/v1/messages")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", TOKEN_JWT)
                            .content("{\"name\": \"Inside\", \"message\": \"TEST\"}"))
                    .andDo(print())
                    .andExpect(status().is4xxClientError());
        }
    }
}