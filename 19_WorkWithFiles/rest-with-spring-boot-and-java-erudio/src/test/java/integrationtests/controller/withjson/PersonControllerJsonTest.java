package integrationtests.controller.withjson;

import br.com.restwithspringbootandjavaerudio.StartUp;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.TestsConfigs;
import integrationtests.DTO.PersonDto;
import integrationtests.DTO.security.AccountCredentialsDto;
import integrationtests.DTO.security.TokenDto;
import integrationtests.testscontainers.AbstractIntegrationTest;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.common.mapper.TypeRef;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.path.json.JsonPath;
import io.restassured.path.json.config.JsonPathConfig;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.proxy;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, classes = StartUp.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PersonControllerJsonTest extends AbstractIntegrationTest {
    private static RequestSpecification specification;
    private static RequestSpecification specificationUnauthorize;
    private static ObjectMapper mapper;
    private static PersonDto dto;


    @BeforeAll
    public static void setup() {
        mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        // ELe por padrão lança uma falha quando tem propiedades desconhecidas, como os links hateoas não estõa no objeto padrão
        // estamos desabilitando essa falha com propiedades desconhecidas
        mapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        dto = new PersonDto();
    }

    @Test
    @Order(0)
    public void authorization() throws JsonProcessingException {
        AccountCredentialsDto user =
                new AccountCredentialsDto("joao", "admin123");
        String accessToken = given()
                .basePath("/auth/signin")
                .port(TestsConfigs.SERVER_PORT)
                .contentType(TestsConfigs.CONTENT_TYPE_JSON)
                .body(user)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(TokenDto.class)
                .getAccessToken();
        // da para tranforma a response direto pra objeto sem usar o mapper pq todos os atributos do json
        // são iguais aos atributos da clase

        specification = new RequestSpecBuilder()
                .addHeader(TestsConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + accessToken)
                .setBasePath("/api/person/v1")
                .setPort(TestsConfigs.SERVER_PORT)
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .addFilter((new RequestLoggingFilter(LogDetail.ALL)))
                .build();
        specificationUnauthorize = new RequestSpecBuilder()
                .setBasePath("/api/person/v1")
                .setPort(TestsConfigs.SERVER_PORT)
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .addFilter((new RequestLoggingFilter(LogDetail.ALL)))
                .build();

    }

    @Test
    @Order(1)//indica que é o primeiro da ordem
    public void testCreate() throws JsonProcessingException {
        mockPerson();
        //Especificando a requisição:

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
        assertTrue(createdPerson.getEnabled());

    }

    @Test
    @Order(2)//indica que é o primeiro da ordem
    public void testFindByID() throws JsonProcessingException {
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
        assertEquals(dto.getId(), createdPerson.getId());
        assertEquals("Luffy", createdPerson.getFirstName());
        assertEquals("Monkey D.", createdPerson.getLastName());
        assertEquals("Brazil", createdPerson.getAddress());
        assertEquals("Male", createdPerson.getGender());
        assertTrue(createdPerson.getEnabled());
    }

    @Test
    @Order(3)//indica que é o primeiro da ordem
    public void testDisable() throws JsonProcessingException {
        var content =
                given()
                        .spec(specification)
                        .contentType(TestsConfigs.CONTENT_TYPE_JSON)
                        .pathParams("id", dto.getId())
                        .when()
                        .patch("{id}")
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
        assertEquals(dto.getId(), createdPerson.getId());
        assertEquals("Luffy", createdPerson.getFirstName());
        assertEquals("Monkey D.", createdPerson.getLastName());
        assertEquals("Brazil", createdPerson.getAddress());
        assertEquals("Male", createdPerson.getGender());
        assertFalse(createdPerson.getEnabled());
    }


    @Test
    @Order(4)//indica que é o primeiro da ordem
    public void testUpdate() throws JsonProcessingException {

        dto.setFirstName("Dragon");

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
        assertEquals(dto.getId(), createdPerson.getId());
        assertEquals("Dragon", createdPerson.getFirstName());
        assertEquals("Monkey D.", createdPerson.getLastName());
        assertEquals("Brazil", createdPerson.getAddress());
        assertEquals("Male", createdPerson.getGender());
        assertFalse(createdPerson.getEnabled());
    }

    @Test
    @Order(5)//indica que é o primeiro da ordem
    public void testDelete() {
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
    @Order(6)
    public void testFindAll() throws JsonProcessingException {
        List<PersonDto> personDtos = given()
                .spec(specification)
                .contentType(TestsConfigs.CONTENT_TYPE_JSON)
                .queryParams("page",3, "size", 10,"direction", "asc")
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .jsonPath()
                .getList("_embedded.personDtoList", PersonDto.class);


        PersonDto personOne = personDtos.getFirst();
//        "id": 406,
//                "firstName": "Alfie",
//                "lastName": "Gulleford",
//                "address": "55 Fuller Trail",
//                "gender": "Male",
//                "enabled": false,


        assertEquals(406, personOne.getId());
        assertEquals("Alfie", personOne.getFirstName());
        assertEquals("Gulleford", personOne.getLastName());
        assertEquals("Male", personOne.getGender());
        assertEquals("55 Fuller Trail", personOne.getAddress());
        assertFalse(personOne.getEnabled());

//        "id": 175,
//                "firstName": "Alica",
//                "lastName": "McPartlin",
//                "address": "29487 Towne Way",
//                "gender": "Female",
//                "enabled": false,

        PersonDto personTwo = personDtos.get(2);
        assertEquals(175, personTwo.getId());
        assertEquals("Alica", personTwo.getFirstName());
        assertEquals("McPartlin", personTwo.getLastName());
        assertEquals("Female", personTwo.getGender());
        assertEquals("29487 Towne Way", personTwo.getAddress());
        assertFalse(personTwo.getEnabled());


//        "id": 883,
//                "firstName": "Alicea",
//                "lastName": "Willgoose",
//                "address": "584 Larry Circle",
//                "gender": "Female",
//                "enabled": true,

        PersonDto personThree = personDtos.get(3);
        assertEquals(883, personThree.getId());
        assertEquals("Alicea", personThree.getFirstName());
        assertEquals("Willgoose", personThree.getLastName());
        assertEquals("Female", personThree.getGender());
        assertEquals("584 Larry Circle", personThree.getAddress());
        assertTrue(personThree.getEnabled());
    }


    @Test
    @Order(6)
    public void testCreateWithoutAuthorizationToken() throws JsonProcessingException {
        mockPerson();
        var content =
                given()
                        .spec(specificationUnauthorize)
                        .contentType(TestsConfigs.CONTENT_TYPE_JSON)
                        .body(dto)
                        .when()
                        .post()
                        .then()
                        .statusCode(403)
                        .extract()
                        .body()
                        .asString();
        assertEquals("", content);

    }

    @Test
    @Order(6)
    public void testFindByIdWithoutAuthorizationToken() throws JsonProcessingException {
        var content =
                given()
                        .spec(specificationUnauthorize)
                        .contentType(TestsConfigs.CONTENT_TYPE_JSON)
                        .pathParams("id", dto.getId())
                        .when()
                        .get("{id}")
                        .then()
                        .statusCode(403)
                        .extract()
                        .body()
                        .asString();

        assertEquals("", content);

    }

    @Test
    @Order(6)
    public void testUpdateWithoutAuthorizationToken() throws JsonProcessingException {
        dto.setFirstName("1212Dragon");
        var content =
                given()
                        .spec(specificationUnauthorize)
                        .contentType(TestsConfigs.CONTENT_TYPE_JSON)
                        .body(dto)
                        .when()
                        .put()
                        .then()
                        .statusCode(403)
                        .extract()
                        .body()
                        .asString();

        assertEquals("", content);

    }

    @Test
    @Order(6)
    public void testDeleteWithoutAuthorizationToken() throws JsonProcessingException {

        var content =
                given()
                        .spec(specificationUnauthorize)
                        .contentType(TestsConfigs.CONTENT_TYPE_JSON)
                        .pathParams("id", dto.getId())
                        .when()
                        .delete("{id}")
                        .then()
                        .statusCode(403)
                        .extract()
                        .body()
                        .asString();


        assertEquals("", content);
    }

    @Test
    @Order(7)//indica que é o primeiro da ordem
    public void testFindByName() throws JsonProcessingException {
//        "id": 391,
//                "firstName": "Xena",
//                "lastName": "Ianinotti",
//                "address": "64 Anzinger Court",
//                "gender": "Female",
//                "enabled": true,
        PersonDto createdPerson =
                given()
                        .spec(specification)
                        .contentType(TestsConfigs.CONTENT_TYPE_JSON)
                        .pathParams("firstName", "Xena")
                        .when()
                        .get("/findByFirstName/{firstName}")
                        .then()
                        .statusCode(200)
                        .extract()
                        .body()
                        .jsonPath()
                        .getList("_embedded.personDtoList", PersonDto.class).getFirst();
        dto = createdPerson;
        assertTrue(createdPerson.getId() > 0);
        assertNotNull(createdPerson.getFirstName());
        assertNotNull(createdPerson.getLastName());
        assertNotNull(createdPerson.getAddress());
        assertNotNull(createdPerson.getGender());
        assertEquals(391, createdPerson.getId());
        assertEquals("Xena", createdPerson.getFirstName());
        assertEquals("Ianinotti", createdPerson.getLastName());
        assertEquals("64 Anzinger Court", createdPerson.getAddress());
        assertEquals("Female", createdPerson.getGender());
        assertTrue(createdPerson.getEnabled());
    }

    @Test
    @Order(9)
    public void testHateoas() throws JsonProcessingException {

        String responseJson = given()
                .spec(specification)
                .contentType(TestsConfigs.CONTENT_TYPE_JSON)
                .queryParams("page", 3, "size", 10, "direction", "asc")
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        //links nos elementos
        assertTrue(responseJson.contains("\"_links\":{\"self\":{\"href\":\"http://localhost:8888/api/person/v1/406\"}}}"));
        assertTrue(responseJson.contains("\"_links\":{\"self\":{\"href\":\"http://localhost:8888/api/person/v1/213\"}}}"));
        assertTrue(responseJson.contains("\"_links\":{\"self\":{\"href\":\"http://localhost:8888/api/person/v1/315\"}}}"));


        //links das paginas
        assertTrue(responseJson.contains("{\"first\":{\"href\":\"http://localhost:8888/api/person/v1?direction=asc&page=0&size=10&sort=firstName,asc\"}"));
        assertTrue(responseJson.contains("\"last\":{\"href\":\"http://localhost:8888/api/person/v1?direction=asc&page=100&size=10&sort=firstName,asc\"}}"));
        assertTrue(responseJson.contains("\"next\":{\"href\":\"http://localhost:8888/api/person/v1?direction=asc&page=4&size=10&sort=firstName,asc\"}"));
        assertTrue(responseJson.contains("\"self\":{\"href\":\"http://localhost:8888/api/person/v1?page=3&size=10&direction=asc\"}"));
        assertTrue(responseJson.contains("\"prev\":{\"href\":\"http://localhost:8888/api/person/v1?direction=asc&page=2&size=10&sort=firstName,asc\"}"));

        // informações da pagina
        assertTrue(responseJson.contains("\"page\":{\"size\":10,\"totalElements\":1004,\"totalPages\":101,\"number\":3}"));
    }




    private void mockPerson() {
        dto.setFirstName("Luffy");
        dto.setLastName("Monkey D.");
        dto.setAddress("Brazil");
        dto.setGender("Male");
    }
}