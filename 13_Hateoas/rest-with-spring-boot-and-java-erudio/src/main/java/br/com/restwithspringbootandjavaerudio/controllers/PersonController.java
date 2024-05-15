package br.com.restwithspringbootandjavaerudio.controllers;

import br.com.restwithspringbootandjavaerudio.DataTransfers.PersonDto;
import br.com.restwithspringbootandjavaerudio.services.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import br.com.restwithspringbootandjavaerudio.Util.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/person")
public class PersonController {
    @Autowired
    private PersonService personService;

    @GetMapping(value = "/{id}",
            produces = { MediaType.aplicationJson , MediaType.aplicationXml, MediaType.aplicationYml })
    public PersonDto findByID(@PathVariable(value = "id") Long id){
        return personService.findById(id);
    }

    @GetMapping(produces = { MediaType.aplicationJson , MediaType.aplicationXml, MediaType.aplicationYml })
    public List<PersonDto> findAll(){
        return personService.findALL();
    }
    @PostMapping
            (consumes = { MediaType.aplicationJson , MediaType.aplicationXml, MediaType.aplicationYml },
            produces = { MediaType.aplicationJson , MediaType.aplicationXml, MediaType.aplicationYml })
    public PersonDto createPerson(@RequestBody PersonDto person){
        return personService.createPerson(person);
    }
    @PutMapping(consumes = { MediaType.aplicationJson , MediaType.aplicationXml, MediaType.aplicationYml},
            produces = { MediaType.aplicationJson , MediaType.aplicationXml, MediaType.aplicationYml })
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
