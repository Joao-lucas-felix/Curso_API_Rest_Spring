package integrationtests.repositories;

import br.com.restwithspringbootandjavaerudio.StartUp;
import br.com.restwithspringbootandjavaerudio.domain.Person;
import br.com.restwithspringbootandjavaerudio.repositories.PersonRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import integrationtests.testscontainers.AbstractIntegrationTest;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ContextConfiguration(classes = StartUp.class)
public class PersonRepositoryTest extends AbstractIntegrationTest {
    @Autowired
    public PersonRepository repository;
    private static Person person;

    @BeforeAll
    public static void  setup(){
        person = new Person();
    }
    @Test
    @Order(0)//indica que é o primeiro da ordem
    public void testFindByName() throws JsonProcessingException {
//        "id": 391,
//                "firstName": "Xena",
//                "lastName": "Ianinotti",
//                "address": "64 Anzinger Court",
//                "gender": "Female",
//                "enabled": true,

        Pageable pageable = PageRequest.of(0, 1, Sort.by(Sort.Direction.ASC, "firstName"));
        person = repository.findPersonByFirstName("Xena", pageable).getContent().getFirst();
        assertTrue(person.getId() > 0);
        assertNotNull(person.getFirstName());
        assertNotNull(person.getLastName());
        assertNotNull(person.getAddress());
        assertNotNull(person.getGender());
        assertEquals(391, person.getId());
        assertEquals("Xena", person.getFirstName());
        assertEquals("Ianinotti", person.getLastName());
        assertEquals("64 Anzinger Court", person.getAddress());
        assertEquals("Female", person.getGender());
        assertTrue(person.getEnabled());
    }

    @Test
    @Order(1)//indica que é o primeiro da ordem
    public void testDisable() throws JsonProcessingException {
//        "id": 391,
//                "firstName": "Xena",
//                "lastName": "Ianinotti",
//                "address": "64 Anzinger Court",
//                "gender": "Female",
//                "enabled": false,


        repository.disable(person.getId());

        Pageable pageable = PageRequest.of(0, 1, Sort.by(Sort.Direction.ASC, "firstName"));
        person = repository.findPersonByFirstName("Xena", pageable).getContent().getFirst();
        assertTrue(person.getId() > 0);
        assertNotNull(person.getFirstName());
        assertNotNull(person.getLastName());
        assertNotNull(person.getAddress());
        assertNotNull(person.getGender());
        assertEquals(391, person.getId());
        assertEquals("Xena", person.getFirstName());
        assertEquals("Ianinotti", person.getLastName());
        assertEquals("64 Anzinger Court", person.getAddress());
        assertEquals("Female", person.getGender());
        assertFalse(person.getEnabled());
    }


}
