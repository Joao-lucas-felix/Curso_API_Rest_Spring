package integrationtests.controller.withjson;

import br.com.restwithspringbootandjavaerudio.StartUp;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.TestsConfigs;
import integrationtests.DTO.PersonDto;
import integrationtests.testscontainers.AbstractIntegrationTest;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, classes = StartUp.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PersonControllerJsonTest extends AbstractIntegrationTest {
    private static RequestSpecification specification;
    private static ObjectMapper mapper;
    private static PersonDto dto;


    @BeforeAll
    public static void setup() {
        mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        // ELe por padrão lança uma falha quando tem propiedades desconhecidas, como os links hateoas não estõa no objeto padrão
        // estamos desabilitando essa falha com propiedades desconhecidas

        dto = new PersonDto();
    }

    @Test
    @Order(1)//indica que é o primeiro da ordem
    public void testCreate() throws JsonProcessingException {
        mockPerson();
        //Especificando a requisição:
        specification = new RequestSpecBuilder()
                .addHeader(TestsConfigs.HEADER_PARAM_ORIGIN, TestsConfigs.VALID_ORIGIN)
                .setBasePath("/api/person/v1")
                .setPort(TestsConfigs.SERVER_PORT)
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .addFilter((new RequestLoggingFilter(LogDetail.ALL)))
                .build();

        var content =
                given()
                        .spec(specification)
                        .contentType(TestsConfigs.CONTENT_TYPE_JSON)
                        .body(dto)
                        .when()
                        .post()
                        .then()
                        .statusCode(200)
                        .extract()
                        .body()
                        .asString();


        PersonDto createdPerson = mapper.readValue(content, PersonDto.class);
        dto = createdPerson;
        assertTrue(createdPerson.getId() > 0);
        assertNotNull(createdPerson.getFirstName());
        assertNotNull(createdPerson.getLastName());
        assertNotNull(createdPerson.getAddress());
        assertNotNull(createdPerson.getGender());
        assertEquals("Luffy", createdPerson.getFirstName());
        assertEquals("Monkey D.", createdPerson.getLastName());
        assertEquals("Brazil", createdPerson.getAddress());
        assertEquals("Male", createdPerson.getGender());
    }

    @Test
    @Order(2)//indica que é o primeiro da ordem
    public void testFindByID() throws JsonProcessingException {
        mockPerson();
        //Especificando a requisição:
        specification = new RequestSpecBuilder()
                .addHeader(TestsConfigs.HEADER_PARAM_ORIGIN, TestsConfigs.VALID_ORIGIN)
                .setBasePath("/api/person/v1")
                .setPort(TestsConfigs.SERVER_PORT)
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .addFilter((new RequestLoggingFilter(LogDetail.ALL)))
                .build();

        var content =
                given()
                        .spec(specification)
                        .contentType(TestsConfigs.CONTENT_TYPE_JSON)
                        .pathParams("id", dto.getId())
                        .when()
                        .get("{id}")
                        .then()
                        .statusCode(200)
                        .extract()
                        .body()
                        .asString();


        PersonDto createdPerson = mapper.readValue(content, PersonDto.class);
        dto = createdPerson;
        assertTrue(createdPerson.getId() > 0);
        assertNotNull(createdPerson.getFirstName());
        assertNotNull(createdPerson.getLastName());
        assertNotNull(createdPerson.getAddress());
        assertNotNull(createdPerson.getGender());
        assertEquals("Luffy", createdPerson.getFirstName());
        assertEquals("Monkey D.", createdPerson.getLastName());
        assertEquals("Brazil", createdPerson.getAddress());
        assertEquals("Male", createdPerson.getGender());
    }
    @Test
    @Order(3)//indica que é o primeiro da ordem
    public void testUpdate() throws JsonProcessingException {
        mockPerson();
        //Especificando a requisição:
        specification = new RequestSpecBuilder()
                .addHeader(TestsConfigs.HEADER_PARAM_ORIGIN, TestsConfigs.VALID_ORIGIN)
                .setBasePath("/api/person/v1")
                .setPort(TestsConfigs.SERVER_PORT)
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .addFilter((new RequestLoggingFilter(LogDetail.ALL)))
                .build();
        dto.setFirstName(dto.getFirstName()+"1");

        var content =
                given()
                        .spec(specification)
                        .contentType(TestsConfigs.CONTENT_TYPE_JSON)
                        .body(dto)
                        .when()
                        .put()
                        .then()
                        .statusCode(200)
                        .extract()
                        .body()
                        .asString();


        PersonDto createdPerson = mapper.readValue(content, PersonDto.class);
        dto = createdPerson;
        assertTrue(createdPerson.getId() > 0);
        assertNotNull(createdPerson.getFirstName());
        assertNotNull(createdPerson.getLastName());
        assertNotNull(createdPerson.getAddress());
        assertNotNull(createdPerson.getGender());
        assertEquals("Luffy1", createdPerson.getFirstName());
        assertEquals("Monkey D.", createdPerson.getLastName());
        assertEquals("Brazil", createdPerson.getAddress());
        assertEquals("Male", createdPerson.getGender());
    }
    @Test
    @Order(4)//indica que é o primeiro da ordem
    public void testDelete() throws JsonProcessingException {
        mockPerson();
        //Especificando a requisição:
        specification = new RequestSpecBuilder()
                .addHeader(TestsConfigs.HEADER_PARAM_ORIGIN, TestsConfigs.VALID_ORIGIN)
                .setBasePath("/api/person/v1")
                .setPort(TestsConfigs.SERVER_PORT)
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .addFilter((new RequestLoggingFilter(LogDetail.ALL)))
                .build();

        var content =
                given()
                        .spec(specification)
                        .contentType(TestsConfigs.CONTENT_TYPE_JSON)
                        .pathParams("id", dto.getId())
                        .when()
                        .delete("{id}")
                        .then()
                        .statusCode(204)
                        .extract()
                        .body()
                        .asString();


       assertEquals("", content);
    }


    @Test
    @Order(2)//indica que é o primeiro da ordem
    public void testCreateWhenInvlaidOrigin() throws JsonProcessingException {
        mockPerson();
        //Especificando a requisição:
        specification = new RequestSpecBuilder()
                .addHeader(TestsConfigs.HEADER_PARAM_ORIGIN, TestsConfigs.INVALID_ORIGIN)
                .setBasePath("/api/person/v1")
                .setPort(TestsConfigs.SERVER_PORT)
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .addFilter((new RequestLoggingFilter(LogDetail.ALL)))
                .build();

        var content =
                given()
                        .spec(specification)
                        .contentType(TestsConfigs.CONTENT_TYPE_JSON)
                        .body(dto)
                        .when()
                        .post()
                        .then()
                        .statusCode(403)
                        .extract()
                        .body()
                        .asString();

        assertNotNull(content);
        assertEquals("Invalid CORS request", content);
    }
    @Test
    @Order(2)//indica que é o primeiro da ordem
    public void testFindByIDWhenInvlaidOrigin() throws JsonProcessingException {
        mockPerson();
        //Especificando a requisição:
        specification = new RequestSpecBuilder()
                .addHeader(TestsConfigs.HEADER_PARAM_ORIGIN, TestsConfigs.INVALID_ORIGIN)
                .setBasePath("/api/person/v1")
                .setPort(TestsConfigs.SERVER_PORT)
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .addFilter((new RequestLoggingFilter(LogDetail.ALL)))
                .build();

        var content =
                given()
                        .spec(specification)
                        .contentType(TestsConfigs.CONTENT_TYPE_JSON)
                        .pathParams("id", dto.getId())
                        .when()
                        .get("{id}")
                        .then()
                        .statusCode(403)
                        .extract()
                        .body()
                        .asString();

        assertNotNull(content);
        assertEquals("Invalid CORS request", content);
    }
    @Test
    @Order(2)//indica que é o primeiro da ordem
    public void testUpdateWhenInvlaidOrigin() throws JsonProcessingException {
        mockPerson();
        //Especificando a requisição:
        specification = new RequestSpecBuilder()
                .addHeader(TestsConfigs.HEADER_PARAM_ORIGIN, TestsConfigs.INVALID_ORIGIN)
                .setBasePath("/api/person/v1")
                .setPort(TestsConfigs.SERVER_PORT)
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .addFilter((new RequestLoggingFilter(LogDetail.ALL)))
                .build();
        dto.setFirstName(dto.getFirstName()+"1");
        var content =
                given()
                        .spec(specification)
                        .contentType(TestsConfigs.CONTENT_TYPE_JSON)
                        .body(dto)
                        .when()
                        .put()
                        .then()
                        .statusCode(403)
                        .extract()
                        .body()
                        .asString();

        assertNotNull(content);
        assertEquals("Invalid CORS request", content);
    }
    @Test
    @Order(2)//indica que é o primeiro da ordem
    public void testDeleteWhenInvlaidOrigin() throws JsonProcessingException {
        mockPerson();
        //Especificando a requisição:
        specification = new RequestSpecBuilder()
                .addHeader(TestsConfigs.HEADER_PARAM_ORIGIN, TestsConfigs.INVALID_ORIGIN)
                .setBasePath("/api/person/v1")
                .setPort(TestsConfigs.SERVER_PORT)
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .addFilter((new RequestLoggingFilter(LogDetail.ALL)))
                .build();
        var content =
                given()
                        .spec(specification)
                        .contentType(TestsConfigs.CONTENT_TYPE_JSON)
                        .pathParams("id", dto.getId())
                        .when()
                        .delete("{id}")
                        .then()
                        .statusCode(403)
                        .extract()
                        .body()
                        .asString();

        assertNotNull(content);
        assertEquals("Invalid CORS request", content);
    }

    private void mockPerson() {
        dto.setFirstName("Luffy");
        dto.setLastName("Monkey D.");
        dto.setAddress("Brazil");
        dto.setGender("Male");
    }
}