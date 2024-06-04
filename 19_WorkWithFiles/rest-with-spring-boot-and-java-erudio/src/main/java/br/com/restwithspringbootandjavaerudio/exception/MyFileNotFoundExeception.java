package br.com.restwithspringbootandjavaerudio.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

//define que será uma execeção que retornara um status HTTP com o erro bad request
@ResponseStatus(HttpStatus.NOT_FOUND)
public class MyFileNotFoundExeception extends RuntimeException{
    public MyFileNotFoundExeception(String message) {
        super(message);
    }

    public MyFileNotFoundExeception(String message, Throwable cause) {
        super(message, cause);
    }

    @Serial
    private static final long serialVersionUID = 1L;
}
