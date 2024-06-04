package integrationtests.controller.withjson;

import br.com.restwithspringbootandjavaerudio.StartUp;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.TestsConfigs;
import integrationtests.DTO.BookDto;
import integrationtests.DTO.PersonDto;
import integrationtests.DTO.security.AccountCredentialsDto;
import integrationtests.DTO.security.TokenDto;
import integrationtests.testscontainers.AbstractIntegrationTest;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, classes = StartUp.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BookControllerJsonTest extends AbstractIntegrationTest {
    private static RequestSpecification specification;
    private static RequestSpecification specificationUnauthorize ;
    private static ObjectMapper mapper;
    private static BookDto dto;
    private static Date mockedDate;


    @BeforeAll
    public static void setup() {
        mapper = new ObjectMapper();
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
    @Order(5)
    public void testFindAll() throws JsonProcessingException {
        List<BookDto> bookDtos = given()
                .spec(specification)
                .contentType(TestsConfigs.CONTENT_TYPE_JSON)
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .jsonPath()
                .getList("_embedded.bookDtoList", BookDto.class);

        BookDto bookOne = bookDtos.getFirst();
//        "id": 15,
//                "author": "Aguinaldo Aragon Fernandes e Vladimir Ferraz de Abreu",
//                "launchDate": "2017-11-07T03:00:00.000+00:00",
//                "price": 54.0,
//                "title": "Implantando a governança de TI",
        assertEquals(15,bookOne.getId());
        assertEquals("Aguinaldo Aragon Fernandes e Vladimir Ferraz de Abreu", bookOne.getAuthor());
        assertEquals("Implantando a governança de TI", bookOne.getTitle());
        assertEquals(54.0, bookOne.getPrice());
        assertNotNull(bookOne
                .getLaunchDate());


//        "id": 8,
//                "author": "Eric Evans",
//                "launchDate": "2017-11-07T03:00:00.000+00:00",
//                "price": 92.0,
//                "title": "Domain Driven Design",

        BookDto bookTwo = bookDtos.get(3);
        assertEquals(8,bookTwo.getId());
        assertEquals("Eric Evans", bookTwo.getAuthor());
        assertEquals("Domain Driven Design", bookTwo.getTitle());
        assertEquals(92.0, bookTwo.getPrice());
        assertNotNull(bookTwo.getLaunchDate());



//        "id": 13,
//                "author": "Richard Hunter e George Westerman",
//                "launchDate": "2017-11-07T03:00:00.000+00:00",
//                "price": 95.0,
//                "title": "O verdadeiro valor de TI",

        BookDto bookThree = bookDtos.get(9);
        assertEquals(13,bookThree.getId());
        assertEquals("Richard Hunter e George Westerman", bookThree.getAuthor());
        assertEquals("O verdadeiro valor de TI", bookThree.getTitle());
        assertEquals(95.0, bookThree.getPrice());
        assertNotNull(bookThree.getLaunchDate());



        bookDtos.forEach(System.out::println);
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
        dto.setAuthor("1212Dragon");
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


    private void mockPerson() {
        dto.setAuthor("Luffy");
        dto.setTitle("One Piece");
        dto.setPrice(99.99);
        dto.setLaunchDate(mockedDate);
    }
}