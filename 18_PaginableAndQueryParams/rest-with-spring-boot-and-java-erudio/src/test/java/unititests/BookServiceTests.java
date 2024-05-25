package unititests;

import br.com.restwithspringbootandjavaerudio.DataTransfers.BookDto;
import br.com.restwithspringbootandjavaerudio.DataTransfers.PersonDto;
import br.com.restwithspringbootandjavaerudio.Mappers.ObjectMapper;
import br.com.restwithspringbootandjavaerudio.domain.Book;
import br.com.restwithspringbootandjavaerudio.domain.Person;
import br.com.restwithspringbootandjavaerudio.exception.InvalidValuesExeception;
import br.com.restwithspringbootandjavaerudio.exception.UnfoundResourceExeception;
import br.com.restwithspringbootandjavaerudio.repositories.BooksRepository;
import br.com.restwithspringbootandjavaerudio.services.BooksService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
public class BookServiceTests {
    MockBook input;

    @InjectMocks
    private BooksService service;
    @Mock
    private BooksRepository repository;

    @BeforeEach
    void setUp(){
        input = new MockBook();
        MockitoAnnotations.openMocks(this);
    }
    @Test
    void testFindByIdWhenSucessful(){
        Book book = input.mockEntity(1);
        book.setId(1);
        BookDto dto = input.mockDto(1);

        // Mocando o comportamento do repositorio.
        when(repository.findById(1)).thenReturn(Optional.of(book));
        // Mocando o comportamento do Dozer
        try (MockedStatic<ObjectMapper> mockStatic = Mockito.mockStatic(ObjectMapper.class)){
            mockStatic.when( () -> ObjectMapper.parseObject(Person.class, PersonDto.class) )
                    .thenReturn(dto);
        }
        //Validando se os resultados não são nulos e se o link hateoas está correto
        //Como os dados estão sendo mockados nunca serão diferentes, não faz sentido testar.
        //Aqui se testaria também outras regras de negocio
        var result = service.findById(1);
        assertNotNull(result);
        assertNotNull(result.getLinks());
        assertNotNull(result.getTitle());
        assertNotNull(result.getAuthor());
        assertNotNull(result.getPrice());
        assertNotNull(result.getLaunchDate());
        assertNotNull(result.getKey());
        assertTrue(result.toString().contains("</api/books/v1/1>;rel=\"self\""));
        assertEquals("1", result.getKey().toString());
        assertEquals("Author Test1", result.getAuthor());
        assertEquals("Title Test1", result.getTitle());
        assertEquals(99.99, result.getPrice());

    }
    @Test
    void  testCreateWhenSucessful(){
        Book entity = input.mockEntity(1);
        entity.setId(1);

        Book persisted = entity;
        persisted.setId(1);

        BookDto vo = input.mockDto(1);
        vo.setKey(1);

        when(repository.save(entity)).thenReturn(persisted);

        var result = service.createBook(vo);

        assertNotNull(result);
        assertNotNull(result.getLinks());
        assertNotNull(result.getTitle());
        assertNotNull(result.getAuthor());
        assertNotNull(result.getPrice());
        assertNotNull(result.getLaunchDate());
        assertNotNull(result.getKey());
        assertTrue(result.toString().contains("</api/books/v1/1>;rel=\"self\""));
        assertEquals("1", result.getKey().toString());
        assertEquals("Author Test1", result.getAuthor());
        assertEquals("Title Test1", result.getTitle());
        assertEquals(99.99, result.getPrice());
    }
    @Test
    void testUpdateWhenSucessful(){

        Book entity = input.mockEntity(1);
        entity.setId(1);

        Book persisted = entity;
        persisted.setId(1);

        BookDto vo = input.mockDto(1);
        vo.setKey(1);

        when(repository.findById(1)).thenReturn(Optional.of(entity));
        when(repository.save(entity)).thenReturn(persisted);

        var result = service.updateBook(vo);

        assertNotNull(result);
        assertNotNull(result.getLinks());
        assertNotNull(result.getTitle());
        assertNotNull(result.getAuthor());
        assertNotNull(result.getPrice());
        assertNotNull(result.getLaunchDate());
        assertNotNull(result.getKey());
        assertTrue(result.toString().contains("</api/books/v1/1>;rel=\"self\""));
        assertEquals("1", result.getKey().toString());
        assertEquals("Author Test1", result.getAuthor());
        assertEquals("Title Test1", result.getTitle());
        assertEquals(99.99, result.getPrice());
    }
/*
    @Test
    void testFindAllWhenSucessful(){
        List<Book> persons = input.mockEntityList();


        when(repository.findAll()).thenReturn(persons);


        var allPersons = service.findAll();
        assertNotNull(allPersons);
        assertEquals(14, allPersons.size());


        BookDto result = allPersons.get(1);
        assertNotNull(result);
        assertNotNull(result.getLinks());
        assertNotNull(result.getTitle());
        assertNotNull(result.getAuthor());
        assertNotNull(result.getPrice());
        assertNotNull(result.getLaunchDate());
        assertNotNull(result.getKey());
        assertTrue(result.toString().contains("</api/books/v1/1>;rel=\"self\""));
        assertEquals("1", result.getKey().toString());
        assertEquals("Author Test1", result.getAuthor());
        assertEquals("Title Test1", result.getTitle());
        assertEquals(99.99, result.getPrice());

        BookDto result1 = allPersons.get(4);
        assertNotNull(result1);
        assertNotNull(result1.getLinks());
        assertNotNull(result1.getTitle());
        assertNotNull(result1.getAuthor());
        assertNotNull(result1.getPrice());
        assertNotNull(result1.getLaunchDate());
        assertNotNull(result1.getKey());
        assertTrue(result1.toString().contains("</api/books/v1/4>;rel=\"self\""));
        assertEquals("4", result1.getKey().toString());
        assertEquals("Author Test4", result1.getAuthor());
        assertEquals("Title Test4", result1.getTitle());
        assertEquals(99.99, result1.getPrice());

        BookDto result2 = allPersons.get(13);
        assertNotNull(result2);
        assertNotNull(result2.getLinks());
        assertNotNull(result2.getTitle());
        assertNotNull(result2.getAuthor());
        assertNotNull(result2.getPrice());
        assertNotNull(result2.getLaunchDate());
        assertNotNull(result2.getKey());
        assertTrue(result2.toString().contains("</api/books/v1/13>;rel=\"self\""));
        assertEquals("13", result2.getKey().toString());
        assertEquals("Author Test13", result2.getAuthor());
        assertEquals("Title Test13", result2.getTitle());
        assertEquals(99.99, result2.getPrice());
    }

*/
    @Test
    void testFindByIdWhenFail(){
        when(repository.findById(1)).thenReturn(Optional.empty());
        assertThrows(UnfoundResourceExeception.class, () -> service.findById(1));
    }
    @Test
    void testUpdateWhenFailInFind(){
        when(repository.findById(1)).thenReturn(Optional.empty());
        assertThrows(UnfoundResourceExeception.class, () -> service.findById(1));
    }
    @Test
    void testFindByIdWhenNullParam(){
        assertThrows(InvalidValuesExeception.class, () -> service.findById(null));
    }
    @Test
    void testCreateWenNull(){
        assertThrows(InvalidValuesExeception.class, () -> service.createBook(null));
    }
    @Test
    void testUpdateWenNull(){
        assertThrows(InvalidValuesExeception.class, () -> service.updateBook(null));
    }

}
