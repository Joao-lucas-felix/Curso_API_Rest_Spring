package integrationtests.controller.withXml;

import br.com.restwithspringbootandjavaerudio.StartUp;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import config.TestsConfigs;
import integrationtests.DTO.security.AccountCredentialsDto;
import integrationtests.DTO.security.TokenDto;
import integrationtests.testscontainers.AbstractIntegrationTest;
import io.restassured.config.XmlConfig;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, classes = StartUp.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthControllerXmlTest extends AbstractIntegrationTest {
    private static TokenDto user;
    private static XmlMapper mapper = new XmlMapper();

    @Test
    @Order(1)
    public void testAuthSigninWhenSuccessful() throws JsonProcessingException {
        AccountCredentialsDto userCredentials =
                new AccountCredentialsDto("joao", "admin123");

        String credentialsInXml = mapper.writeValueAsString(userCredentials);

        String result = given()
                .basePath("/auth/signin")
                .port(TestsConfigs.SERVER_PORT)
                .contentType(TestsConfigs.CONTENT_TYPE_XML)
                .accept(TestsConfigs.CONTENT_TYPE_XML)
                .body(credentialsInXml)
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
    public void testAuthRefreshWhenSuccessful(){
        AccountCredentialsDto userCredentials =
                new AccountCredentialsDto("joao", "admin123");

        TokenDto newUser = given()
                .basePath("/auth/refresh")
                .port(TestsConfigs.SERVER_PORT)
                .pathParams("username", user.getUserName())
                .header(TestsConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + user.getAccessToken())
                .accept(TestsConfigs.CONTENT_TYPE_XML)
                .when()
                .put("{username}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(TokenDto.class);

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
