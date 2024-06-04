package integrationtests.controller.withXml;

import br.com.restwithspringbootandjavaerudio.StartUp;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import config.TestsConfigs;
import integrationtests.DTO.PersonDto;
import integrationtests.DTO.Wrappers.PagedModelPerson;
import integrationtests.DTO.security.AccountCredentialsDto;
import integrationtests.DTO.security.TokenDto;
import integrationtests.testscontainers.AbstractIntegrationTest;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.XmlConfig;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.path.xml.XmlPath;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, classes = StartUp.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PersonControllerXmlTest extends AbstractIntegrationTest {
    private static RequestSpecification specification;
    private static RequestSpecification specificationUnauthorize ;
    private static ObjectMapper mapper;
    private static PersonDto dto;


    @BeforeAll
    public static void setup() {
        mapper = new XmlMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        // ELe por padrão lança uma falha quando tem propiedades desconhecidas, como os links hateoas não estõa no objeto padrão
        // estamos desabilitando essa falha com propiedades desconhecidas

        dto = new PersonDto();
    }

    @Test
    @Order(0)
    public void authorization() throws JsonProcessingException{
        AccountCredentialsDto user =
                new AccountCredentialsDto("joao", "admin123");

        String userInXml = mapper.writeValueAsString(user);

        String userResponse = given()
                .basePath("/auth/signin")
                .port(TestsConfigs.SERVER_PORT)
                .contentType(TestsConfigs.CONTENT_TYPE_XML)
                .accept(TestsConfigs.CONTENT_TYPE_XML)
                .body(userInXml)
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
                .addHeader(TestsConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer "+accessToken)
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
        String dtoInXml = mapper.writeValueAsString(dto);
        var content =
                given()
                        .spec(specification)
                        .contentType(TestsConfigs.CONTENT_TYPE_XML)
                        .accept(TestsConfigs.CONTENT_TYPE_XML)
                        .body(dtoInXml)
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
                        .contentType(TestsConfigs.CONTENT_TYPE_XML)
                        .accept(TestsConfigs.CONTENT_TYPE_XML)
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
                        .contentType(TestsConfigs.CONTENT_TYPE_XML)
                        .accept(TestsConfigs.CONTENT_TYPE_XML)
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

        String dtoInXml = mapper.writeValueAsString(dto);
        var content =
                given()
                        .spec(specification)
                        .contentType(TestsConfigs.CONTENT_TYPE_XML)
                        .accept(TestsConfigs.CONTENT_TYPE_XML)
                        .body(dtoInXml)
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
    public void testDelete(){
        var content =
                given()
                        .spec(specification)
                        .contentType(TestsConfigs.CONTENT_TYPE_XML)
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
        String xmlResponse = given()
                .spec(specification)
                .contentType(TestsConfigs.CONTENT_TYPE_XML)
                .accept(TestsConfigs.CONTENT_TYPE_XML)
                .queryParams("page", 3, "size", 10, "direction", "asc")
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract()
                .body().asString();

        List<PersonDto> personDtos = mapper.readValue(xmlResponse, PagedModelPerson.class).getContent();
        PersonDto personOne = personDtos.get(0);

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
        String dtoInXml = mapper.writeValueAsString(dto);
        var content =
                given()
                        .spec(specificationUnauthorize)
                        .contentType(TestsConfigs.CONTENT_TYPE_XML)
                        .accept(TestsConfigs.CONTENT_TYPE_XML)
                        .body(dtoInXml)
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
                        .contentType(TestsConfigs.CONTENT_TYPE_XML)
                        .accept(TestsConfigs.CONTENT_TYPE_XML)
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
        String dtoInXml = mapper.writeValueAsString(dto);
        var content =
                given()
                        .spec(specificationUnauthorize)
                        .contentType(TestsConfigs.CONTENT_TYPE_XML)
                        .accept(TestsConfigs.CONTENT_TYPE_XML)
                        .body(dtoInXml)
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
                        .contentType(TestsConfigs.CONTENT_TYPE_XML)
                        .accept(TestsConfigs.CONTENT_TYPE_XML)
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
        var xmlResponse =
                given()
                        .spec(specification)
                        .contentType(TestsConfigs.CONTENT_TYPE_XML)
                        .accept(TestsConfigs.CONTENT_TYPE_XML)
                        .pathParams("firstName", "Xena")
                        .when()
                        .get("/findByFirstName/{firstName}")
                        .then()
                        .statusCode(200)
                        .extract()
                        .body().asString();

        PersonDto createdPerson  = mapper.readValue(xmlResponse, PagedModelPerson.class).getContent().getFirst();
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
        String xmlResponse = given()
                .spec(specification)
                .contentType(TestsConfigs.CONTENT_TYPE_XML)
                .accept(TestsConfigs.CONTENT_TYPE_XML)
                .queryParams("page", 3, "size", 10, "direction", "asc")
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract()
                .body().asString();

        System.out.println(xmlResponse);
        //links nos elementos
        assertTrue(xmlResponse.contains("<links><rel>self</rel><href>http://localhost:8888/api/person/v1/406</href></links>"));
        assertTrue(xmlResponse.contains("<links><rel>self</rel><href>http://localhost:8888/api/person/v1/213</href></links>"));
        assertTrue(xmlResponse.contains("<links><rel>self</rel><href>http://localhost:8888/api/person/v1/175</href></links>"));


        //links das paginas
        assertTrue(xmlResponse
                .contains("<links><rel>first</rel><href>http://localhost:8888/api/person/v1?direction=asc&amp;page=0&amp;size=10&amp;sort=firstName,asc</href></links>"));
        assertTrue(xmlResponse
                .contains("<links><rel>prev</rel><href>http://localhost:8888/api/person/v1?direction=asc&amp;page=2&amp;size=10&amp;sort=firstName,asc</href></links>"));
        assertTrue(xmlResponse
                .contains("<links><rel>self</rel><href>http://localhost:8888/api/person/v1?page=3&amp;size=10&amp;direction=asc</href></links>"));
        assertTrue(xmlResponse
                .contains("<links><rel>next</rel><href>http://localhost:8888/api/person/v1?direction=asc&amp;page=4&amp;size=10&amp;sort=firstName,asc</href></links>"));
        assertTrue(xmlResponse
                .contains("<links><rel>last</rel><href>http://localhost:8888/api/person/v1?direction=asc&amp;page=100&amp;size=10&amp;sort=firstName,asc</href></links>"));

        // informações da pagina
        assertTrue(xmlResponse
                .contains("<page><size>10</size><totalElements>1004</totalElements><totalPages>101</totalPages><number>3</number></page>"));
    }

    private void mockPerson() {
        dto.setFirstName("Luffy");
        dto.setLastName("Monkey D.");
        dto.setAddress("Brazil");
        dto.setGender("Male");
    }
}