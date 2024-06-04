package br.com.restwithspringbootandjavaerudio.controllers;

import br.com.restwithspringbootandjavaerudio.DataTransfers.BookDto;
import br.com.restwithspringbootandjavaerudio.Util.MediaType;
import br.com.restwithspringbootandjavaerudio.services.BooksService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/api/books/v1")
@Tag(name = "Books", description = "Endpoints for managing Books")
public class BookController {
    @Autowired
    private BooksService service;

    @Operation(summary = "Find a book by ID",
            description = "Receives the id of a book, and searches in the database for a register, " +
                    "if the search was be successful " +
                    "returns a Json Xml or Yaml object who contains the fields: Author, price, title, id, and the launch date",
            tags = "Books",
            responses = {
                    @ApiResponse(description = "Sucess", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = BookDto.class))),
                    @ApiResponse(description = "No content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not found", responseCode = "402", content = @Content),
                    @ApiResponse(description = "Internal server error", responseCode = "500", content = @Content),
            })
    @GetMapping(value = "/{id}",
            produces = {MediaType.aplicationJson, MediaType.aplicationYml   , MediaType.aplicationXml})
    public BookDto findById(@PathVariable Integer id){
        return service.findById(id);
    }

    @Operation(summary = "Find all books",
            description = "Searches in database for all books registers and returns in a array. The return format can be in xml json or yaml",
            tags = "Books",
            responses = {
                    @ApiResponse(description = "Sucess", responseCode = "200",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = BookDto.class)))),
                    @ApiResponse(description = "Bad request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not found", responseCode = "402", content = @Content),
                    @ApiResponse(description = "Internal server error", responseCode = "500", content = @Content),
            })
    @GetMapping(
            produces = {MediaType.aplicationJson, MediaType.aplicationYml   , MediaType.aplicationXml})
    public ResponseEntity<PagedModel<EntityModel<BookDto>>> findAll(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value =  "size", defaultValue = "10") Integer size,
            @RequestParam(value = "direction", defaultValue = "asc") String direction
    ){
        Sort.Direction dir = direction.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(dir, "author"));
        return ResponseEntity.ok(service.findAll(pageable));
    }


    @Operation(summary = "Create a book",
            description = "Receives a json, xml or yaml object with the fields: Author, title, price, launch date. Persist this book in the database and returns the persisted object",
            tags = "Books",
            responses = {
                    @ApiResponse(description = "Sucess", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = BookDto.class))),
                    @ApiResponse(description = "Bad request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not found", responseCode = "402", content = @Content),
                    @ApiResponse(description = "Internal server error", responseCode = "500", content = @Content),
            })
    @PostMapping( consumes = {MediaType.aplicationJson, MediaType.aplicationYml   , MediaType.aplicationXml},
            produces = {MediaType.aplicationJson, MediaType.aplicationYml   , MediaType.aplicationXml} )
    public BookDto createBook(@RequestBody BookDto dto){
        return  service.createBook(dto);
    }

    @Operation(summary = "Update a book",
            description = "Receives a json, xml or yaml object with the fields: ID, Author, title, price, launch date. Searches for a object with this ID and Update the register with the received object",
            tags = "Books",
            responses = {
                    @ApiResponse(description = "Sucess", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = BookDto.class))),
                    @ApiResponse(description = "Bad request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not found", responseCode = "402", content = @Content),
                    @ApiResponse(description = "Internal server error", responseCode = "500", content = @Content),
            })
    @PutMapping( consumes = {MediaType.aplicationJson, MediaType.aplicationYml   , MediaType.aplicationXml},
            produces = {MediaType.aplicationJson, MediaType.aplicationYml   , MediaType.aplicationXml} )
    public BookDto updateBook(@RequestBody BookDto dto){
        return  service.updateBook(dto);
    }



    @Operation(summary = "Delete a book",
            description = "Receives a value who represents the book id, searches in the database for this register, if successful remove the register of database.",
            tags = "Books",
            responses = {
                    @ApiResponse(description = "No content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not found", responseCode = "402", content = @Content),
                    @ApiResponse(description = "Internal server error", responseCode = "500", content = @Content),
            })
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable Integer id)
    {
        service.deleteBook(id);
        return ResponseEntity.noContent().build();
    }

}
