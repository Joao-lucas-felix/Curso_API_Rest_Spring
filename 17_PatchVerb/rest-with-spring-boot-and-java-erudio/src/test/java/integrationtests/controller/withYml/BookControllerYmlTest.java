package integrationtests.controller.withYml;

import br.com.restwithspringbootandjavaerudio.StartUp;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import config.TestsConfigs;
import integrationtests.DTO.BookDto;
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

import java.util.Date;
import java.util.List;

import static io.restassured.RestAssured.given;
import static io.restassured.config.EncoderConfig.encoderConfig;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, classes = StartUp.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BookControllerYmlTest extends AbstractIntegrationTest {
    private static RequestSpecification specification;
    private static RequestSpecification specificationUnauthorize ;
    private static ObjectMapper mapper;
    private static BookDto dto;
    private static Date mockedDate;


    @BeforeAll
    public static void setup() {
        mapper = new YAMLMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        // ELe por padrão lança uma falha quando tem propiedades desconhecidas, como os links hateoas não estõa no objeto padrão
        // estamos desabilitando essa falha com propiedades desconhecidas

        dto = new BookDto();
        mockedDate = new Date();
    }

    @Test
    @Order(0)
    public void authorization() throws JsonProcessingException{
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
                .addHeader(TestsConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer "+accessToken)
                .setBasePath("/api/books/v1")
                .setPort(TestsConfigs.SERVER_PORT)
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .addFilter((new RequestLoggingFilter(LogDetail.ALL)))
                .build();
        specificationUnauthorize = new RequestSpecBuilder()
                .setBasePath("/api/books/v1")
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
        String objectInString = mapper.writeValueAsString(dto);
        System.out.println(objectInString);
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
                        .body(objectInString)
                        .when()
                        .post()
                        .then()
                        .statusCode(200)
                        .extract()
                        .body()
                        .asString();
        BookDto createdBook = mapper.readValue(content, BookDto.class);
        dto = createdBook;
        assertTrue(createdBook.getId() > 0);
        assertNotNull(createdBook.getAuthor());
        assertNotNull(createdBook.getTitle());
        assertNotNull(createdBook.getPrice());
        assertNotNull(createdBook.getLaunchDate());
        assertEquals("Luffy", createdBook.getAuthor());
        assertEquals("One Piece", createdBook.getTitle());
        assertEquals(99.99, createdBook.getPrice());
        assertEquals(mockedDate, createdBook.getLaunchDate());
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


        BookDto createdBook = mapper.readValue(content, BookDto.class);
        dto = createdBook;
        assertTrue(createdBook.getId() > 0);
        assertNotNull(createdBook.getAuthor());
        assertNotNull(createdBook.getTitle());
        assertNotNull(createdBook.getPrice());
        assertNotNull(createdBook.getLaunchDate());
        assertEquals("Luffy", createdBook.getAuthor());
        assertEquals("One Piece", createdBook.getTitle());
        assertEquals(99.99, createdBook.getPrice());
    }
    @Test
    @Order(3)//indica que é o primeiro da ordem
    public void testUpdate() throws JsonProcessingException {

        dto.setAuthor("Dragon");
        String objectInString = mapper.writeValueAsString(dto);
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
                        .body(objectInString)
                        .when()
                        .put()
                        .then()
                        .statusCode(200)
                        .extract()
                        .body()
                        .asString();


        BookDto createdBook = mapper.readValue(content, BookDto.class);
        dto = createdBook;
        assertTrue(createdBook.getId() > 0);
        assertNotNull(createdBook.getAuthor());
        assertNotNull(createdBook.getTitle());
        assertNotNull(createdBook.getPrice());
        assertNotNull(createdBook.getLaunchDate());
        assertEquals("Dragon", createdBook.getAuthor());
        assertEquals("One Piece", createdBook.getTitle());
        assertEquals(99.99, createdBook.getPrice());
        assertNotNull(createdBook.getLaunchDate());
    }
    @Test
    @Order(4)//indica que é o primeiro da ordem
    public void testDelete(){
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
    @Order(5)
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
        List<BookDto> personDtos = mapper.readValue(result, new TypeReference<List<BookDto>>() {});
        BookDto bookOne = personDtos.getFirst();
//        "id": 1,
//                "author": "Michael C. Feathers",
//                "launch_date": "2017-11-29T03:00:00.000+00:00",
//                "price": 49.0,
//                "title": "Working effectively with legacy code",

        assertEquals(1,bookOne.getId());
        assertEquals("Michael C. Feathers", bookOne.getAuthor());
        assertEquals("Working effectively with legacy code", bookOne.getTitle());
        assertEquals(49.0, bookOne.getPrice());
        assertNotNull(bookOne.getLaunchDate());

//        "id": 4,
//                "author": "Crockford",
//                "launch_date": "2017-11-07T03:00:00.000+00:00",
//                "price": 67.0,
//                "title": "JavaScript",

        BookDto bookTwo = personDtos.get(3);
        assertEquals(4,bookTwo.getId());
        assertEquals("Crockford", bookTwo.getAuthor());
        assertEquals("JavaScript", bookTwo.getTitle());
        assertEquals(67.0, bookTwo.getPrice());
        assertNotNull(bookTwo.getLaunchDate());



//        "id": 15,
//                "author": "Aguinaldo Aragon Fernandes e Vladimir Ferraz de Abreu",
//                "launch_date": "2017-11-07T03:00:00.000+00:00",
//                "price": 54.0,
//                "title": "Implantando a governança de TI",

        BookDto bookThree = personDtos.get(14);
        assertEquals(15,bookThree.getId());
        assertEquals("Aguinaldo Aragon Fernandes e Vladimir Ferraz de Abreu", bookThree.getAuthor());
        assertEquals("Implantando a governança de TI", bookThree.getTitle());
        assertEquals(54.0, bookThree.getPrice());
        assertNotNull(bookThree.getLaunchDate());

        personDtos.forEach(System.out::println);
    }


    @Test
    @Order(6)
    public void testCreateWithoutAuthorizationToken() throws JsonProcessingException {
        mockPerson();
        String objectInString = mapper.writeValueAsString(dto);
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
                        .body(objectInString)
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
        dto.setAuthor("1212Dragon");
        String objectInString = mapper.writeValueAsString(dto);
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
                        .body(objectInString)
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
        dto.setAuthor("Luffy");
        dto.setTitle("One Piece");
        dto.setPrice(99.99);
        dto.setLaunchDate(mockedDate);
    }
}