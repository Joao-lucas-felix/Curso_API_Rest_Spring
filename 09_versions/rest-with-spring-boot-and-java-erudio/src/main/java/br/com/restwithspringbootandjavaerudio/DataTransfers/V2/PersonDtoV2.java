package br.com.restwithspringbootandjavaerudio.DataTransfers.V2;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;


@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class PersonDtoV2 implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private Long id;
    private String firstName;
    private String lastName;
    private Date birthDay;
    private String address;
    private String gender;
}

