package unititests;

import br.com.restwithspringbootandjavaerudio.DataTransfers.BookDto;
import br.com.restwithspringbootandjavaerudio.DataTransfers.PersonDto;
import br.com.restwithspringbootandjavaerudio.domain.Book;
import br.com.restwithspringbootandjavaerudio.domain.Person;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MockBook {
    public Book mockEntity() {
        return mockEntity(0);
    }

    public BookDto mockVO() {
        return mockDto(0);
    }

    public List<Book> mockEntityList() {
        List<Book> books = new ArrayList<Book>();
        for (int i = 0; i < 14; i++) {
            books.add(mockEntity(i));
        }
        return books;
    }

    public List<BookDto> mockDtoList() {
        List<BookDto> books = new ArrayList<>();
        for (int i = 0; i < 14; i++) {
            books.add(mockDto(i));
        }
        return books;
    }


    public Book mockEntity(Integer number){
        Book book = new Book();
        book.setAuthor("Author Test" + number);
        book.setTitle("Title Test" + number);
        book.setPrice(99.99);
        book.setId(number);
        book.setLaunchDate(new Date());
        return book;
    }
    public BookDto mockDto(Integer number){
        BookDto book = new BookDto();
        book.setAuthor("Author Test" + number);
        book.setTitle("Title Test" + number);
        book.setPrice(99.99);
        book.setKey(number);
        book.setLaunchDate(new Date());
        return book;
    }
}
