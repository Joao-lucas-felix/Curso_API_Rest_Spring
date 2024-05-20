package br.com.restwithspringbootandjavaerudio.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidValuesExeception extends RuntimeException{
    public InvalidValuesExeception(String message){super(message);}
    @Serial
    private static final long serialVersionUID = 1L;

}
