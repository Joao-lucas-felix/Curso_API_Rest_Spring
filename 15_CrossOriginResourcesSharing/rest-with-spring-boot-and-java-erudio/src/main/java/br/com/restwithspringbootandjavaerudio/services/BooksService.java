package br.com.restwithspringbootandjavaerudio.services;

import br.com.restwithspringbootandjavaerudio.DataTransfers.BookDto;

import br.com.restwithspringbootandjavaerudio.DataTransfers.BookDto;
import br.com.restwithspringbootandjavaerudio.Mappers.ObjectMapper;
import br.com.restwithspringbootandjavaerudio.controllers.BookController;
import br.com.restwithspringbootandjavaerudio.controllers.BookController;
import br.com.restwithspringbootandjavaerudio.domain.Book;
import br.com.restwithspringbootandjavaerudio.domain.Book;
import br.com.restwithspringbootandjavaerudio.exception.InvalidValuesExeception;
import br.com.restwithspringbootandjavaerudio.exception.UnfoundResourceExeception;
import br.com.restwithspringbootandjavaerudio.repositories.BooksRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.logging.Logger;

@Service
public class BooksService {

    private final Logger logger = Logger.getLogger(BooksService.class.getName());

    @Autowired
    private BooksRepository repository;

    public BookDto findById(Integer id){
       if (id == null) throw new InvalidValuesExeception("Invalid Value for a search");
       logger.info("finding a book with this id: "+id);
       BookDto dto = ObjectMapper.parseObject( repository.findById(id)
               .orElseThrow(()-> new UnfoundResourceExeception("Dont exits a book with this id ")
               ), BookDto.class);

       dto.add(linkTo(methodOn(BookController.class).findById(dto.getKey())).withSelfRel());
       return dto;
    }


    public List<BookDto> findAll() {
        logger.info("finding all books");
        List<BookDto> bookDtos = ObjectMapper.parseList(repository.findAll(), BookDto.class);
        bookDtos
                .forEach(
                        dto -> dto.add(linkTo(methodOn(BookController.class).findById(dto.getKey())).withSelfRel())
                );
        return bookDtos;
    }

    public BookDto createBook(BookDto dto){
        if (dto == null || dto.getLaunchDate() == null || dto.getPrice() == null)
            throw new InvalidValuesExeception("Can't create this book with the null params");

        logger.info("creating a new book");
        Book book = ObjectMapper.parseObject(dto, Book.class);
        BookDto bookDto = ObjectMapper.parseObject(repository.save(book), BookDto.class);
        bookDto.add(linkTo(methodOn(BookController.class).findById(bookDto.getKey())).withSelfRel());
        return bookDto;
    }


    public BookDto updateBook(BookDto p) {
        if (p == null ||
                p.getKey() == null ||
                p.getAuthor() == null ||
                p.getTitle() == null ||
                p.getLaunchDate() == null ||
                p.getPrice() == null)
            throw new InvalidValuesExeception("Values can not be null");

        logger.info("updating a book");
        Book book = ObjectMapper.parseObject(p,Book.class);
        Book bookToUpdate = repository.findById(book.getId())
                .orElseThrow(
                        () -> new UnfoundResourceExeception("Unfound Resource with this ID")
                );
        bookToUpdate.setAuthor(book.getAuthor());
        bookToUpdate.setTitle(book.getTitle());
        bookToUpdate.setLaunchDate(book.getLaunchDate());
        bookToUpdate.setPrice(book.getPrice());
        BookDto dto = ObjectMapper.parseObject(repository.save(bookToUpdate), BookDto.class);
        dto.add(linkTo(methodOn(BookController.class).findById(dto.getKey())).withSelfRel());
        return dto;
    }

    public void deleteBook(Integer id) {
        repository.delete(ObjectMapper.parseObject(this.findById(id),Book.class));
    }
}
