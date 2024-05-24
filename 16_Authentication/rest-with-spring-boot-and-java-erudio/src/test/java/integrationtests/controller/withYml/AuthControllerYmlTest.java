package integrationtests.controller.withYml;

import br.com.restwithspringbootandjavaerudio.StartUp;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import config.TestsConfigs;
import integrationtests.DTO.security.AccountCredentialsDto;
import integrationtests.DTO.security.TokenDto;
import integrationtests.testscontainers.AbstractIntegrationTest;
import io.restassured.RestAssured;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;

import static io.restassured.RestAssured.given;
import static io.restassured.config.EncoderConfig.encoderConfig;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, classes = StartUp.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthControllerYmlTest extends AbstractIntegrationTest {
    private static TokenDto user;
    private static final YAMLMapper mapper = new YAMLMapper();

    @Test
    @Order(1)
    public void testAuthSigninWhenSuccessful() throws JsonProcessingException {

        AccountCredentialsDto userCredentials =
                new AccountCredentialsDto("joao", "admin123");

        String credentialsInYml = mapper.writeValueAsString(userCredentials);

        String result = given()
                .config(
                        RestAssured
                                .config()
                                .encoderConfig(
                                        encoderConfig()
                                                .encodeContentTypeAs("application/x-yaml", ContentType.TEXT)))
                .basePath("/auth/signin")
                .port(TestsConfigs.SERVER_PORT)
                .contentType(TestsConfigs.CONTENT_TYPE_YML)
                .accept(TestsConfigs.CONTENT_TYPE_YML)
                .body(credentialsInYml)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        user = mapper.readValue(result, TokenDto.class);
        assertNotNull(user);
        assertNotNull(user.getUserName());
        assertNotNull(user.getAccessToken());
        assertNotNull(user.getRefreshToken());
        assertNotNull(user.getAuthenticated());
        assertNotNull(user.getCreated());
        assertNotNull(user.getExpiration());
        assertEquals(userCredentials.getUserName(), user.getUserName());
        assertTrue(user.getAuthenticated());

    }
    @Test
    @Order(2)
    public void testAuthRefreshWhenSuccessful() throws JsonProcessingException {

        AccountCredentialsDto userCredentials =
                new AccountCredentialsDto("joao", "admin123");

        String result = given()
                .basePath("/auth/refresh")
                .port(TestsConfigs.SERVER_PORT)
                .pathParams("username", user.getUserName())
                .header(TestsConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + user.getAccessToken())
                .accept(TestsConfigs.CONTENT_TYPE_YML)
                .when()
                .put("{username}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();
        TokenDto newUser = mapper.readValue(result, TokenDto.class);

        assertNotNull(newUser);
        assertNotNull(newUser.getUserName());
        assertNotNull(newUser.getAccessToken());
        assertNotNull(newUser.getRefreshToken());
        assertNotNull(newUser.getAuthenticated());
        assertNotNull(newUser.getCreated());
        assertNotNull(newUser.getExpiration());
        assertEquals(userCredentials.getUserName(), newUser.getUserName());
        assertTrue(newUser.getAuthenticated());

    }



}
