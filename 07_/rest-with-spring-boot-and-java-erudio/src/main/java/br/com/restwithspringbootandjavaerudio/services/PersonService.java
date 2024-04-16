package br.com.restwithspringbootandjavaerudio.services;

import br.com.restwithspringbootandjavaerudio.domain.Person;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;


@Service // diz que é o spring que tem que injetar essa calsse
public class PersonService {
    private final AtomicLong counter = new AtomicLong();
    private final Logger logger = Logger.getLogger(PersonService.class.getName());
    public Person findById(String id){

        logger.info("Finding a Person with id: "+id);
        return new Person(counter.incrementAndGet(),
                "João Lucas",
                "Felix de Macena",
                "João Paulo",
                "Male");
    }

    public List<Person> findALL(){
        logger.info("finding all people");
        return mockPersons();
    }

    public Person createPerson(Person p){
        logger.info("creating a new person");
        return p;
    }

    public Person updatePerson(Person person){
        logger.info("updating a person");
        return person;
    }

    public void deletePerson(String id){
        logger.info("Deleting the person withs this id: "+ id);
    }

    @NotNull
    private List<Person> mockPersons() {
        List<Person> mockedPersons = new ArrayList<>();

        for(int i =0; i < 8; i++){
            mockedPersons.add(new Person(
                    counter.incrementAndGet(),
                    "First name "+i,
                    "Last name",
                    "Same address in Brazil",
                    "Any gender"
            ));
        }
        return mockedPersons;
    }
}
