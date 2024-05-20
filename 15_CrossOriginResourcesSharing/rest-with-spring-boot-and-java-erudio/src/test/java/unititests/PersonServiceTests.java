package unititests;

import br.com.restwithspringbootandjavaerudio.DataTransfers.PersonDto;
import br.com.restwithspringbootandjavaerudio.Mappers.ObjectMapper;
import br.com.restwithspringbootandjavaerudio.domain.Person;
import br.com.restwithspringbootandjavaerudio.exception.InvalidValuesExeception;
import br.com.restwithspringbootandjavaerudio.exception.UnfoundResourceExeception;
import br.com.restwithspringbootandjavaerudio.repositories.PersonRepository;
import br.com.restwithspringbootandjavaerudio.services.PersonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
public class PersonServiceTests {
    MockPerson input;
    @InjectMocks
    private PersonService service;
    @Mock
    private PersonRepository repository;

    @BeforeEach
    void setUp(){
        input = new MockPerson();
        MockitoAnnotations.openMocks(this);
    }
    @Test
    void testFindByIdWhenSucessful(){
        Person person = input.mockEntity(1);
        person.setId(1L);
        PersonDto dto = input.mockDto(1);

        // Mocando o comportamento do repositorio.
        when(repository.findById(1L)).thenReturn(Optional.of(person));
        // Mocando o comportamento do Dozer
        try (MockedStatic<ObjectMapper> mockStatic = Mockito.mockStatic(ObjectMapper.class)){
            mockStatic.when( () -> ObjectMapper.parseObject(Person.class, PersonDto.class) )
                    .thenReturn(dto);
        }
        //Validando se os resultados não são nulos e se o link hateoas está correto
        //Como os dados estão sendo mockados nunca serão diferentes, não faz sentido testar.
        //Aqui se testaria também outras regras de negocio
        var result = service.findById(1L);
        assertNotNull(result);
        assertNotNull(result.getLinks());
        assertNotNull(result.getFirstName());
        assertNotNull(result.getLastName());
        assertNotNull(result.getAddress());
        assertNotNull(result.getGender());
        assertNotNull(result.getKey());

        assertTrue(result.toString().contains("</api/person/v1/1>;rel=\"self\""));
        assertEquals("1", result.getKey().toString());
        assertEquals("First Name Test1", result.getFirstName());
        assertEquals("Last Name Test1", result.getLastName());
        assertEquals("Addres Test1", result.getAddress());
        assertEquals("Female", result.getGender());
    }
    @Test
    void  testCreateWhenSucessful(){
        Person entity = input.mockEntity(1);
        entity.setId(1L);

        Person persisted = entity;
        persisted.setId(1L);

        PersonDto vo = input.mockDto(1);
        vo.setKey(1L);

        when(repository.save(entity)).thenReturn(persisted);

        var result = service.createPerson(vo);

        assertNotNull(result);
        assertNotNull(result.getLinks());
        assertNotNull(result.getFirstName());
        assertNotNull(result.getLastName());
        assertNotNull(result.getAddress());
        assertNotNull(result.getGender());
        assertNotNull(result.getKey());
        assertTrue(result.toString().contains("</api/person/v1/1>;rel=\"self\""));
        assertEquals("1", result.getKey().toString());
        assertEquals("First Name Test1", result.getFirstName());
        assertEquals("Last Name Test1", result.getLastName());
        assertEquals("Addres Test1", result.getAddress());
        assertEquals("Female", result.getGender());
    }
    @Test
    void testUpdateWhenSucessful(){

        Person entity = input.mockEntity(1);
        entity.setId(1L);

        Person persisted = entity;
        persisted.setId(1L);

        PersonDto vo = input.mockDto(1);
        vo.setKey(1L);

        when(repository.findById(1L)).thenReturn(Optional.of(entity));
        when(repository.save(entity)).thenReturn(persisted);

        var result = service.updatePerson(vo);

        assertNotNull(result);
        assertNotNull(result.getLinks());
        assertNotNull(result.getFirstName());
        assertNotNull(result.getLastName());
        assertNotNull(result.getAddress());
        assertNotNull(result.getGender());
        assertNotNull(result.getKey());
        assertTrue(result.toString().contains("</api/person/v1/1>;rel=\"self\""));
        assertEquals("1", result.getKey().toString());
        assertEquals("First Name Test1", result.getFirstName());
        assertEquals("Last Name Test1", result.getLastName());
        assertEquals("Addres Test1", result.getAddress());
        assertEquals("Female", result.getGender());
    }

    @Test
    void testFindAllWhenSucessful(){
        List<Person> persons = input.mockEntityList();


        when(repository.findAll()).thenReturn(persons);


        var allPersons = service.findALL();
        assertNotNull(allPersons);
        assertEquals(14, allPersons.size());


        PersonDto result = allPersons.get(1);
        assertNotNull(result.getLinks());
        assertNotNull(result.getFirstName());
        assertNotNull(result.getLastName());
        assertNotNull(result.getAddress());
        assertNotNull(result.getGender());
        assertNotNull(result.getKey());
        assertTrue(result.toString().contains("</api/person/v1/1>;rel=\"self\""));
        assertEquals("1", result.getKey().toString());
        assertEquals("First Name Test1", result.getFirstName());
        assertEquals("Last Name Test1", result.getLastName());
        assertEquals("Addres Test1", result.getAddress());
        assertEquals("Female", result.getGender());

        PersonDto result1 = allPersons.get(4);
        assertNotNull(result1.getLinks());
        assertNotNull(result1.getFirstName());
        assertNotNull(result1.getLastName());
        assertNotNull(result1.getAddress());
        assertNotNull(result1.getGender());
        assertNotNull(result1.getKey());
        assertTrue(result1.toString().contains("</api/person/v1/4>;rel=\"self\""));
        assertEquals("4", result1.getKey().toString());
        assertEquals("First Name Test4", result1.getFirstName());
        assertEquals("Last Name Test4", result1.getLastName());
        assertEquals("Addres Test4", result1.getAddress());
        assertEquals("Male", result1.getGender());

        PersonDto result2 = allPersons.get(13);
        assertNotNull(result2.getLinks());
        assertNotNull(result2.getFirstName());
        assertNotNull(result2.getLastName());
        assertNotNull(result2.getAddress());
        assertNotNull(result2.getGender());
        assertNotNull(result2.getKey());
        assertTrue(result2.toString().contains("</api/person/v1/13>;rel=\"self\""));
        assertEquals("13", result2.getKey().toString());
        assertEquals("First Name Test13", result2.getFirstName());
        assertEquals("Last Name Test13", result2.getLastName());
        assertEquals("Addres Test13", result2.getAddress());
        assertEquals("Female", result2.getGender());
    }


    @Test
    void testFindByIdWhenFail(){
        when(repository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(UnfoundResourceExeception.class, () -> service.findById(1L));
    }
    @Test
    void testUpdateWhenFailInFind(){
        when(repository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(UnfoundResourceExeception.class, () -> service.findById(1L));
    }
    @Test
    void testFindByIdWhenNullParam(){
        assertThrows(InvalidValuesExeception.class, () -> service.findById(null));
    }
    @Test
    void testCreateWenNull(){
        assertThrows(InvalidValuesExeception.class, () -> service.createPerson(null));
    }
    @Test
    void testUpdateWenNull(){
        assertThrows(InvalidValuesExeception.class, () -> service.updatePerson(null));
    }


}
