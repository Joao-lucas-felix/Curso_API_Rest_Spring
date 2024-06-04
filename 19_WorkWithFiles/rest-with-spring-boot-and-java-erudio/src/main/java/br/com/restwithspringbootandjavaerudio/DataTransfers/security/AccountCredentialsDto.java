package br.com.restwithspringbootandjavaerudio.DataTransfers.security;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class AccountCredentialsDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String userName;
    private String password;
}
