package integrationtests.swaggerintergration;

import br.com.restwithspringbootandjavaerudio.StartUp;
import config.TestsConfigs;
import integrationtests.testscontainers.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest( webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT ,classes = StartUp.class)
public class SwaggerIntegrationTest extends AbstractIntegrationTest {

        @Test
        public void shouldDisplaySwaggerUiPage() {
            var content =
                    given()
                            .basePath("/swagger-ui/index.html")
                            .port(TestsConfigs.SERVER_PORT)
                            .when()
                            .get()
                            .then()
                            .statusCode(200)
                            .extract()
                            .body()
                            .asString();
            assertTrue(content.contains("Swagger UI"));
        }
}