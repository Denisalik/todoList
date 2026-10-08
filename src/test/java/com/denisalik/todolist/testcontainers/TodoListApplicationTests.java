package com.denisalik.todolist.testcontainers;

import com.denisalik.todolist.dto.req.LoginRequest;
import com.denisalik.todolist.dto.req.RegisterRequest;
import com.denisalik.todolist.dto.res.AccessTokenResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.client.EntityExchangeResult;
import org.springframework.test.web.servlet.client.RestTestClient;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ActiveProfiles("test")
@Import(TestcontainersConfiguration.class)
@AutoConfigureRestTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TodoListApplicationTests {

    @Autowired
    private RestTestClient restTestClient;

    @Test
    void contextLoads() {}

    @Test
    void healthEndpointIsUp() {
        restTestClient.get().uri("/actuator/health")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.status").isEqualTo("UP");
    }

    private ResponseCookie register(String username, String password, String email) {
        ResponseCookie refreshCookie = restTestClient.post().uri("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .body(new RegisterRequest(username, email, password))
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.accessToken").isNotEmpty()
                .returnResult()
                .getResponseCookies()
                .getFirst("refreshToken");
        assertThat(refreshCookie).isNotNull();
        return refreshCookie;
    }

    /** GET /auth/csrf and return the XSRF-TOKEN cookie value. */
    private String fetchCsrfToken() {
        EntityExchangeResult<Void> result = restTestClient.get().uri("/auth/csrf")
                .exchange()
                .expectStatus().isNoContent()
                .expectBody(Void.class)
                .returnResult();

        ResponseCookie csrf = result.getResponseCookies().getFirst("XSRF-TOKEN");
        assertThat(csrf).isNotNull();
        return csrf.getValue();
    }

    // ---------- /auth/register ----------

    @Test
    void register_shouldCreateUser_andReturnAccessToken_andSetRefreshCookie() {
        EntityExchangeResult<AccessTokenResponse> result =
                restTestClient.post().uri("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(new RegisterRequest("alice", "alice@mail.ru" ,"password123"))
                        .exchange()
                        .expectStatus().isCreated()
                        .expectBody(AccessTokenResponse.class)
                        .returnResult();

        assertThat(result.getResponseBody().accessToken()).isNotBlank();

        ResponseCookie refresh = result.getResponseCookies().getFirst("refreshToken");
        assertThat(refresh).isNotNull();
        assertThat(refresh.isHttpOnly()).isTrue();
        assertThat(refresh.getPath()).isEqualTo("/auth");
        assertThat(refresh.getSameSite()).isEqualTo("Strict");
        assertThat(refresh.getMaxAge()).isPositive();
        assertThat(refresh.getValue()).isNotBlank();
    }

    @Test
    void register_shouldRejectDuplicateUsername() {
        register("bob", "password123", "bob@mail.ru");

        restTestClient.post().uri("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .body(new RegisterRequest("bob", "bob@mail.ru", "password123"))
                .exchange()
                .expectStatus().isEqualTo(409); // adjust to your actual code
    }

    // ---------- /auth/login ----------

    @Test
    void login_shouldReturnAccessToken_andSetRefreshCookie() {
        register("carol", "password123", "carol@mail.ru");

        EntityExchangeResult<AccessTokenResponse> result =
                restTestClient.post().uri("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(new LoginRequest("carol", "password123"))
                        .exchange()
                        .expectStatus().isCreated()
                        .expectBody(AccessTokenResponse.class)
                        .returnResult();

        assertThat(result.getResponseBody().accessToken()).isNotBlank();

        ResponseCookie refresh = result.getResponseCookies().getFirst("refreshToken");
        assertThat(refresh).isNotNull();
        assertThat(refresh.isHttpOnly()).isTrue();
        assertThat(refresh.getPath()).isEqualTo("/auth");
    }

    @Test
    void login_shouldReturn401_whenPasswordWrong() {
        register("dave", "password123", "dave@mail.ru");

        restTestClient.post().uri("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .body(new LoginRequest("dave", "WRONG"))
                .exchange()
                .expectStatus().isUnauthorized();
    }

    // ---------- /auth/refresh — proves cookie + CSRF actually work ----------

    @Test
    void refresh_shouldIssueNewAccessToken_whenCookieAndCsrfAreValid() {
        ResponseCookie refresh = register("erin", "password123", "erin@mail.ru");

        // Register's Set-Cookie: refreshToken is already in the client's CookieManager,
        // so the next request carries it automatically.
        String csrf = fetchCsrfToken();

        restTestClient.post().uri("/auth/refresh")
                .header("X-XSRF-TOKEN", csrf)
                .cookie("XSRF-TOKEN", csrf)
                .cookie("refreshToken", refresh.getValue())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.accessToken").isNotEmpty();
    }

    @Test
    void refresh_shouldReturn403_whenCsrfHeaderMissing() {
        register("frank", "password123", "frank@mail.ru");
        fetchCsrfToken(); // ensure XSRF-TOKEN cookie exists, but do NOT echo it

        restTestClient.post().uri("/auth/refresh")
                .exchange()
                .expectStatus().isForbidden();
    }

}
