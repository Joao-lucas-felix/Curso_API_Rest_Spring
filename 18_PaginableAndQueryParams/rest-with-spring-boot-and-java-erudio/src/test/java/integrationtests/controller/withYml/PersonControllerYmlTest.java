package integrationtests.controller.withYml;

import br.com.restwithspringbootandjavaerudio.StartUp;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import config.TestsConfigs;
import integrationtests.DTO.PersonDto;
import integrationtests.DTO.security.AccountCredentialsDto;
import integrationtests.DTO.security.TokenDto;
import integrationtests.testscontainers.AbstractIntegrationTest;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static io.restassured.RestAssured.given;
import static io.restassured.config.EncoderConfig.encoderConfig;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, classes = StartUp.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PersonControllerYmlTest extends AbstractIntegrationTest {
    private static RequestSpecification specification;
    private static RequestSpecification specificationUnauthorize;
    private static ObjectMapper mapper;
    private static PersonDto dto;


    @BeforeAll
    public static void setup() {
        mapper = new YAMLMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        // ELe por padrão lança uma falha quando tem propiedades desconhecidas, como os links hateoas não estõa no objeto padrão
        // estamos desabilitando essa falha com propiedades desconhecidas

        dto = new PersonDto();
    }

    @Test
    @Order(0)
    public void authorization() throws JsonProcessingException {
        AccountCredentialsDto user =
                new AccountCredentialsDto("joao", "admin123");

        String userInYml = mapper.writeValueAsString(user);

        String userResponse = given()
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
                .body(userInYml)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        String accessToken = mapper.readValue(userResponse, TokenDto.class).getAccessToken();

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
        String dtoInYml = mapper.writeValueAsString(dto);
        var content =
                given()
                        .config(
                                RestAssured
                                        .config()
                                        .encoderConfig(
                                                encoderConfig()
                                                        .encodeContentTypeAs("application/x-yaml", ContentType.TEXT)))
                        .spec(specification)
                        .contentType(TestsConfigs.CONTENT_TYPE_YML)
                        .accept(TestsConfigs.CONTENT_TYPE_YML)
                        .body(dtoInYml)
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
                        .config(
                                RestAssured
                                        .config()
                                        .encoderConfig(
                                                encoderConfig()
                                                        .encodeContentTypeAs("application/x-yaml", ContentType.TEXT)))
                        .spec(specification)
                        .contentType(TestsConfigs.CONTENT_TYPE_YML)
                        .accept(TestsConfigs.CONTENT_TYPE_YML)
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
                        .config(
                                RestAssured
                                        .config()
                                        .encoderConfig(
                                                encoderConfig()
                                                        .encodeContentTypeAs("application/x-yaml", ContentType.TEXT)))

                        .spec(specification)
                        .contentType(TestsConfigs.CONTENT_TYPE_YML)
                        .accept(TestsConfigs.CONTENT_TYPE_YML)
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

        String dtoInYml = mapper.writeValueAsString(dto);
        var content =
                given()
                        .config(
                                RestAssured
                                        .config()
                                        .encoderConfig(
                                                encoderConfig()
                                                        .encodeContentTypeAs("application/x-yaml", ContentType.TEXT)))
                        .spec(specification)
                        .contentType(TestsConfigs.CONTENT_TYPE_YML)
                        .accept(TestsConfigs.CONTENT_TYPE_YML)
                        .body(dtoInYml)
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
                        .config(
                                RestAssured
                                        .config()
                                        .encoderConfig(
                                                encoderConfig()
                                                        .encodeContentTypeAs("application/x-yaml", ContentType.TEXT)))
                        .spec(specification)
                        .contentType(TestsConfigs.CONTENT_TYPE_YML)
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
        var result = given()
                .config(
                        RestAssured
                                .config()
                                .encoderConfig(
                                        encoderConfig()
                                                .encodeContentTypeAs("application/x-yaml", ContentType.TEXT)))

                .spec(specification)
                .contentType(TestsConfigs.CONTENT_TYPE_YML)
                .accept(TestsConfigs.CONTENT_TYPE_YML)
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract()
                .body().asString();
        List<PersonDto> personDtos = mapper.readValue(result, new TypeReference<List<PersonDto>>() {
        });
        PersonDto personOne = personDtos.getFirst();
//        "id": 2,
//                "firstName": "Leonardo",
//                "lastName": "Da Vinci",
//                "address": "Italy",
//                "gender": "Male",

        assertEquals(2, personOne.getId());
        assertEquals("Leonardo", personOne.getFirstName());
        assertEquals("Da Vinci", personOne.getLastName());
        assertEquals("Male", personOne.getGender());
        assertEquals("Italy", personOne.getAddress());
        assertTrue(personOne.getEnabled());
//        "id": 6,
//                "firstName": "João Lucas ",
//                "lastName": "Felix",
//                "address": "Italy",
//                "gender": "Male",'

        PersonDto personTwo = personDtos.get(2);
        assertEquals(6, personTwo.getId());
        assertEquals("João Lucas ", personTwo.getFirstName());
        assertEquals("Felix", personTwo.getLastName());
        assertEquals("Male", personTwo.getGender());
        assertEquals("Italy", personTwo.getAddress());
        assertTrue(personTwo.getEnabled());


//        "id": 7,
//                "firstName": "Luffy ",
//                "lastName": "Monkey D.",
//                "address": "Italy",
//                "gender": "Male",

        PersonDto personThree = personDtos.get(3);
        assertEquals(7, personThree.getId());
        assertEquals("Luffy ", personThree.getFirstName());
        assertEquals("Monkey D.", personThree.getLastName());
        assertEquals("Male", personThree.getGender());
        assertEquals("Italy", personThree.getAddress());
        assertTrue(personTwo.getEnabled());

    }


    @Test
    @Order(6)
    public void testCreateWithoutAuthorizationToken() throws JsonProcessingException {
        mockPerson();
        String dtoInYml = mapper.writeValueAsString(dto);
        var content =
                given()
                        .config(
                                RestAssured
                                        .config()
                                        .encoderConfig(
                                                encoderConfig()
                                                        .encodeContentTypeAs("application/x-yaml", ContentType.TEXT)))
                        .spec(specificationUnauthorize)
                        .contentType(TestsConfigs.CONTENT_TYPE_YML)
                        .accept(TestsConfigs.CONTENT_TYPE_YML)
                        .body(dtoInYml)
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
                        .config(
                                RestAssured
                                        .config()
                                        .encoderConfig(
                                                encoderConfig()
                                                        .encodeContentTypeAs("application/x-yaml", ContentType.TEXT)))
                        .spec(specificationUnauthorize)
                        .contentType(TestsConfigs.CONTENT_TYPE_YML)
                        .accept(TestsConfigs.CONTENT_TYPE_YML)
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
        String dtoInYml = mapper.writeValueAsString(dto);
        var content =
                given()
                        .config(
                                RestAssured
                                        .config()
                                        .encoderConfig(
                                                encoderConfig()
                                                        .encodeContentTypeAs("application/x-yaml", ContentType.TEXT)))
                        .spec(specificationUnauthorize)
                        .contentType(TestsConfigs.CONTENT_TYPE_YML)
                        .accept(TestsConfigs.CONTENT_TYPE_YML)
                        .body(dtoInYml)
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
                        .config(
                                RestAssured
                                        .config()
                                        .encoderConfig(
                                                encoderConfig()
                                                        .encodeContentTypeAs("application/x-yaml", ContentType.TEXT)))
                        .spec(specificationUnauthorize)
                        .contentType(TestsConfigs.CONTENT_TYPE_YML)
                        .accept(TestsConfigs.CONTENT_TYPE_YML)
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


    private void mockPerson() {
        dto.setFirstName("Luffy");
        dto.setLastName("Monkey D.");
        dto.setAddress("Brazil");
        dto.setGender("Male");
    }
}