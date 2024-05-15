package br.com.restwithspringbootandjavaerudio.DataTransfers;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;


@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"id","address", "first-name", "lastName", "gender"})
public class PersonDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private Long id;
    @JsonProperty("first-name")
    private String firstName;
    private String lastName;
    private String address;
    @JsonIgnore
    private String gender;
}

