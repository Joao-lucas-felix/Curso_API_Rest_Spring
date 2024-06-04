package integrationtests.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.github.dozermapper.core.builder.xml.JAXBModelParser;
import com.sun.tools.xjc.api.JAXBModel;
import jakarta.xml.bind.JAXB;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.*;
import org.glassfish.jaxb.core.annotation.XmlLocation;
import org.glassfish.jaxb.runtime.v2.runtime.JAXBContextImpl;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.Serial;
import java.io.Serializable;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
@XmlRootElement(name = "PersonDto")

public class PersonDto  implements Serializable{
    @Serial
    private static final long serialVersionUID = 1L;
    private Long id;
    private String firstName;
    private String lastName;
    private String address;
    private String gender;
    private Boolean enabled;

}

