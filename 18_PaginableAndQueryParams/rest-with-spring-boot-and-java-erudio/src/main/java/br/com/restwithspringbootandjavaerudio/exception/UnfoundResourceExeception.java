package br.com.restwithspringbootandjavaerudio.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

//define que será uma execeção que retornara um status HTTP com o erro bad request
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UnfoundResourceExeception extends RuntimeException{
    public UnfoundResourceExeception(String message) {
        super(message);
    }

    @Serial
    private static final long serialVersionUID = 1L;
}
