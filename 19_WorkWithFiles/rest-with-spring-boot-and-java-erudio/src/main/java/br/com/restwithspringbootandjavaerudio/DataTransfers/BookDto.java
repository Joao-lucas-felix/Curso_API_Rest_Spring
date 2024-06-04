package br.com.restwithspringbootandjavaerudio.DataTransfers;

import br.com.restwithspringbootandjavaerudio.domain.Book;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.github.dozermapper.core.Mapping;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"id","author", "launchDate", "price","title"})
public class BookDto extends RepresentationModel<BookDto> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @Mapping("id")
    @JsonProperty("id")
    private Integer key;
    private String author;
    private Date launchDate;
    private Double price;
    private String title;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookDto bookDto = (BookDto) o;
        return Objects.equals(key, bookDto.key) && Objects.equals(author, bookDto.author) && Objects.equals(launchDate, bookDto.launchDate) && Objects.equals(price, bookDto.price) && Objects.equals(title, bookDto.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, author, launchDate, price, title);
    }
}
