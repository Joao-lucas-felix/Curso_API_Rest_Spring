package br.com.restwithspringbootandjavaerudio.controllers;

import br.com.restwithspringbootandjavaerudio.domain.Person;
import br.com.restwithspringbootandjavaerudio.services.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/person")
public class PersonController {
    @Autowired
    private PersonService personService;

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Person findByID( @PathVariable(value = "id") String id){
        return personService.findById(id);
    }

    @GetMapping
    public List<Person> findAll(){
        return personService.findALL();
    }
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
                 produces = MediaType.APPLICATION_JSON_VALUE)
    public Person createPerson(@RequestBody Person person){
        return personService.createPerson(person);
    }
    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Person updatePerson(
            @RequestBody Person person
    ){
        return personService.updatePerson(person);
    }
    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable String id){
        personService.deletePerson(id);
    }


}
