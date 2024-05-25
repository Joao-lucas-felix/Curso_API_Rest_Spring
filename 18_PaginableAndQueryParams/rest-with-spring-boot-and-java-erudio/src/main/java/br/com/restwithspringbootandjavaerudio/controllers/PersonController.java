package br.com.restwithspringbootandjavaerudio.controllers;

import br.com.restwithspringbootandjavaerudio.DataTransfers.PersonDto;
import br.com.restwithspringbootandjavaerudio.services.PersonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import br.com.restwithspringbootandjavaerudio.Util.MediaType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/person/v1")
@Tag(name = "People", description = "Endpoints for managing people")//set o nome no swagger
public class PersonController {
    @Autowired
    private PersonService personService;

    //O @operariont é annotation de documentação
    @Operation(summary = "Find a person by ID",
            description = "Finding a person by ID",
            tags = "People",
            responses = {
                    @ApiResponse(description = "Sucess", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = PersonDto.class))),
                    @ApiResponse(description = "No content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not found", responseCode = "402", content = @Content),
                    @ApiResponse(description = "Internal server error", responseCode = "500", content = @Content),
            })
    @GetMapping(value = "/{id}",
            produces = {MediaType.aplicationJson, MediaType.aplicationXml, MediaType.aplicationYml})
    public PersonDto findByID(@PathVariable(value = "id") Long id) {
        return personService.findById(id);
    }

    @Operation(summary = "Find all People",
            description = "Finding all people",
            tags = "People",
            responses = {
                    @ApiResponse(description = "Sucess", responseCode = "200",
                            content = @Content(mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = PersonDto.class)))),
                    @ApiResponse(description = "Bad request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not found", responseCode = "402", content = @Content),
                    @ApiResponse(description = "Internal server error", responseCode = "500", content = @Content),
            })
    @GetMapping(produces = {MediaType.aplicationJson, MediaType.aplicationXml, MediaType.aplicationYml})
    public ResponseEntity<PagedModel<EntityModel<PersonDto>>> findAll(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "12") Integer size,
            @RequestParam(value = "direction", defaultValue = "asc") String asc
    ) {
        Sort.Direction direction = asc.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, "firstName"));

        return ResponseEntity.ok(personService.findALL(pageable));
    }


    @Operation(summary = "Create a Person",
            description = "Create a new Person",
            tags = "People",
            responses = {
                    @ApiResponse(description = "Created", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = PersonDto.class))),
                    @ApiResponse(description = "Bad request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Internal server error", responseCode = "500", content = @Content),
            })
    @PostMapping
            (consumes = {MediaType.aplicationJson, MediaType.aplicationXml, MediaType.aplicationYml},
                    produces = {MediaType.aplicationJson, MediaType.aplicationXml, MediaType.aplicationYml})
    public PersonDto createPerson(@RequestBody PersonDto person) {
        return personService.createPerson(person);
    }

    @Operation(summary = "Update a Person",
            description = "Update a Person",
            tags = "People",
            responses = {
                    @ApiResponse(description = "Update", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = PersonDto.class))),
                    @ApiResponse(description = "No content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not found", responseCode = "402", content = @Content),
                    @ApiResponse(description = "Internal server error", responseCode = "500", content = @Content),
            })
    @PutMapping(consumes = {MediaType.aplicationJson, MediaType.aplicationXml, MediaType.aplicationYml},
            produces = {MediaType.aplicationJson, MediaType.aplicationXml, MediaType.aplicationYml})
    public PersonDto updatePerson(
            @RequestBody PersonDto person
    ) {
        return personService.updatePerson(person);
    }

    @Operation(summary = "Delete a Person",
            description = "Delete a Person",
            tags = "People",
            responses = {
                    @ApiResponse(description = "No content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not found", responseCode = "402", content = @Content),
                    @ApiResponse(description = "Internal server error", responseCode = "500", content = @Content),
            })
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        personService.deletePerson(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Disable a specific person by ID",
            description = "Disable a specific person by ID",
            tags = "People",
            responses = {
                    @ApiResponse(description = "Sucess", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = PersonDto.class))),
                    @ApiResponse(description = "No content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not found", responseCode = "402", content = @Content),
                    @ApiResponse(description = "Internal server error", responseCode = "500", content = @Content),
            })
    @PatchMapping(value = "/{id}",
            produces = {MediaType.aplicationJson, MediaType.aplicationXml, MediaType.aplicationYml})
    public PersonDto disable(@PathVariable(value = "id") Long id) {
        return personService.disableById(id);
    }

}
