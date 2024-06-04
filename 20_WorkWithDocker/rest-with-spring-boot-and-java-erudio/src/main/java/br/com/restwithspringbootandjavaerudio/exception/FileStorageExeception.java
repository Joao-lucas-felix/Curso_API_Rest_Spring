package br.com.restwithspringbootandjavaerudio.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

//define que será uma execeção que retornara um status HTTP com o erro bad request
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class FileStorageExeception extends RuntimeException{
    public FileStorageExeception(String message) {
        super(message);
    }

    public FileStorageExeception(String message, Throwable cause) {
        super(message, cause);
    }

    @Serial
    private static final long serialVersionUID = 1L;
}
