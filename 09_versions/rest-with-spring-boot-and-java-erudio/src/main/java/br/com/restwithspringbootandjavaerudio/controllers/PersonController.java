package br.com.restwithspringbootandjavaerudio.controllers;

import br.com.restwithspringbootandjavaerudio.DataTransfers.V1.PersonDto;
import br.com.restwithspringbootandjavaerudio.services.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/person")
public class PersonController {
    @Autowired
    private PersonService personService;

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public PersonDto findByID(@PathVariable(value = "id") Long id){
        return personService.findById(id);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<PersonDto> findAll(){
        return personService.findALL();
    }
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
                 produces = MediaType.APPLICATION_JSON_VALUE)
    public PersonDto createPerson(@RequestBody PersonDto person){
        return personService.createPerson(person);
    }
    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public PersonDto updatePerson(
            @RequestBody PersonDto person
    ){
        return personService.updatePerson(person);
    }
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        personService.deletePerson(id);
        return ResponseEntity.noContent().build();
    }
}
