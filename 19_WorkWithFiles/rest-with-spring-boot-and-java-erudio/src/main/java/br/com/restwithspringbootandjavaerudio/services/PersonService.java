package br.com.restwithspringbootandjavaerudio.services;

import br.com.restwithspringbootandjavaerudio.DataTransfers.PersonDto;
import br.com.restwithspringbootandjavaerudio.Mappers.ObjectMapper;
import br.com.restwithspringbootandjavaerudio.controllers.PersonController;
import br.com.restwithspringbootandjavaerudio.domain.Person;
import br.com.restwithspringbootandjavaerudio.exception.InvalidValuesExeception;
import br.com.restwithspringbootandjavaerudio.exception.UnfoundResourceExeception;
import br.com.restwithspringbootandjavaerudio.repositories.PersonRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@Service // diz que é o spring que tem que injetar essa calsse
public class PersonService {
    private final Logger logger = Logger.getLogger(PersonService.class.getName());
    @Autowired
    private PersonRepository repository;
    @Autowired
    private PagedResourcesAssembler<PersonDto> assembler;

    public PersonDto findById(Long id) {
        if (id == null) throw new InvalidValuesExeception("Invalid Value for a search");
        logger.info("Finding a Person with id: " + id);
        PersonDto person = ObjectMapper.parseObject(repository.findById(id)
                .orElseThrow(
                        () -> new UnfoundResourceExeception("Unfound Resource with this ID")
                ), PersonDto.class);
        person.add(linkTo(methodOn(PersonController.class).findByID(person.getKey())).withSelfRel());
        return person;
    }

    public PagedModel<EntityModel<PersonDto>> findALL(Pageable pageable) {
        logger.info("finding all people");
        Page<Person> pageOfPersons = repository.findAll(pageable);
        Page<PersonDto> pageOfDtos =
                pageOfPersons.map(p -> ObjectMapper.parseObject(p, PersonDto.class))
                .map(p -> p.add(linkTo(methodOn(PersonController.class).findByID(p.getKey())).withSelfRel()));
        Link link =
                linkTo(methodOn(PersonController.class)
                        .findAll(pageable.getPageNumber(), pageable.getPageSize(), "asc")).withSelfRel();

        return assembler.toModel(pageOfDtos, link);
    }


    public PersonDto createPerson(PersonDto p) {
        if (p == null ||
            p.getFirstName() == null ||
            p.getLastName() == null ||
            p.getAddress() == null ||
            p.getGender() == null)       throw new InvalidValuesExeception("Values can not be null");

        logger.info("creating a new person");
        p.setEnabled(true);
        Person person = ObjectMapper.parseObject(p, Person.class);
        PersonDto dto = ObjectMapper.parseObject(repository.save(person), PersonDto.class);
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
        Person person = ObjectMapper.parseObject(p,Person.class);
        Person personToUpdate = repository.findById(person.getId())
                .orElseThrow(
                        () -> new UnfoundResourceExeception("Unfound Resource with this ID")
                );
        personToUpdate.setFirstName(person.getFirstName());
        personToUpdate.setLastName(person.getLastName());
        personToUpdate.setAddress(person.getAddress());
        personToUpdate.setGender(person.getGender());
        PersonDto dto = ObjectMapper.parseObject(repository.save(personToUpdate), PersonDto.class);
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
    @Transactional
    public PersonDto disableById(Long id) {
        if (id == null) throw new InvalidValuesExeception("Invalid Value for a search");
        logger.info("Disable the Person with id: " + id);

        repository.disable(id);
        PersonDto person = ObjectMapper.parseObject(repository.findById(id)
                .orElseThrow(
                        () -> new UnfoundResourceExeception("Unfound Resource with this ID")
                ), PersonDto.class);
        person.add(linkTo(methodOn(PersonController.class).findByID(person.getKey())).withSelfRel());
        return person;
    }

    public PagedModel<EntityModel<PersonDto>> findByFirstName(String firstName, Pageable pageable) {
        logger.info("finding all people with the first name is like: "+ firstName);
        Page<Person> pageOfPersons = repository.findPersonByFirstName(firstName, pageable);
        Page<PersonDto> pageOfDtos =
                pageOfPersons.map(p -> ObjectMapper.parseObject(p, PersonDto.class))
                        .map(p -> p.add(linkTo(methodOn(PersonController.class).findByID(p.getKey())).withSelfRel()));
        Link link =
                linkTo(methodOn(PersonController.class)
                        .findAll(pageable.getPageNumber(), pageable.getPageSize(), "asc")).withSelfRel();

        return assembler.toModel(pageOfDtos, link);
    }
}
