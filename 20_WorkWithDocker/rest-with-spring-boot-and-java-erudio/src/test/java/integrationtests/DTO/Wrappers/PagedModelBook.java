package integrationtests.DTO.Wrappers;


import integrationtests.DTO.BookDto;
import integrationtests.DTO.PersonDto;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.List;

@XmlRootElement
public class PagedModelBook {

    @XmlElement(name = "content")
    private List<BookDto> content;

    public PagedModelBook() {}

    public List<BookDto> getContent() {
        return content;
    }

    public void setContent(List<BookDto> content) {
        this.content = content;
    }
}
