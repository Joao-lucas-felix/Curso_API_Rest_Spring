package br.com.restwithspringbootandjavaerudio.services;

import br.com.restwithspringbootandjavaerudio.DataTransfers.PersonDto;
import br.com.restwithspringbootandjavaerudio.Mappers.PersonMapper;
import br.com.restwithspringbootandjavaerudio.controllers.PersonController;
import br.com.restwithspringbootandjavaerudio.domain.Person;
import br.com.restwithspringbootandjavaerudio.exception.InvalidValuesExeception;
import br.com.restwithspringbootandjavaerudio.exception.UnfoundResourceExeception;
import br.com.restwithspringbootandjavaerudio.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;


@Service // diz que é o spring que tem que injetar essa calsse
public class PersonService {
    private final AtomicLong counter = new AtomicLong();
    private final Logger logger = Logger.getLogger(PersonService.class.getName());
    @Autowired
    private PersonRepository repository;

    public PersonDto findById(Long id) {
        if (id == null) throw new InvalidValuesExeception("Invalid Value for a search");
        logger.info("Finding a Person with id: " + id);
        PersonDto person = PersonMapper.parseObject(repository.findById(id)
                .orElseThrow(
                        () -> new UnfoundResourceExeception("Unfound Resource with this ID")
                ), PersonDto.class);
        person.add(linkTo(methodOn(PersonController.class).findByID(person.getKey())).withSelfRel());
        return person;
    }

    public List<PersonDto> findALL() {
        logger.info("finding all people");
        List<PersonDto> personDtos = PersonMapper.parseList(repository.findAll(), PersonDto.class);
        personDtos
                .forEach(p -> p.add(linkTo(methodOn(PersonController.class).findByID(p.getKey())).withSelfRel()));
        return personDtos;
    }


    public PersonDto createPerson(PersonDto p) {
        if (p == null ||
            p.getFirstName() == null ||
            p.getLastName() == null ||
            p.getAddress() == null ||
            p.getGender() == null)       throw new InvalidValuesExeception("Values can not be null");

        logger.info("creating a new person");
        Person person = PersonMapper.parseObject(p, Person.class);
        PersonDto dto = PersonMapper.parseObject(repository.save(person), PersonDto.class);
        dto.add(linkTo(methodOn(PersonController.class).findByID(dto.getKey())).withSelfRel());
        return dto;
    }

    public PersonDto updatePerson(PersonDto p) {
        if (p == null ||
                p.getKey() == null ||
                p.getFirstName() == null ||
                p.getLastName() == null ||
                p.getAddress() == null ||
                p.getGender() == null)
            throw new InvalidValuesExeception("Values can not be null");

        logger.info("updating a person");
        Person person = PersonMapper.parseObject(p,Person.class);
        Person personToUpdate = repository.findById(person.getId())
                .orElseThrow(
                        () -> new UnfoundResourceExeception("Unfound Resource with this ID")
                );
        personToUpdate.setFirstName(person.getFirstName());
        personToUpdate.setLastName(person.getLastName());
        personToUpdate.setAddress(person.getAddress());
        personToUpdate.setGender(person.getGender());
        PersonDto dto = PersonMapper.parseObject(repository.save(personToUpdate), PersonDto.class);
        dto.add(linkTo(methodOn(PersonController.class).findByID(dto.getKey())).withSelfRel());
        return dto;
    }

    public void deletePerson(Long id) {
        logger.info("Deleting the person withs this id: " + id);
        Person personToDelete = repository.findById(id)
                .orElseThrow(
                        () -> new UnfoundResourceExeception("Unfound Resource with this ID")
                );
        repository.delete(personToDelete);
    }

}
