package br.com.restwithspringbootandjavaerudio.exception;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

public record ExceptionResponse(Date timestamp, String message, String details) implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    // Define como será o formato da minha execeção 1 timestamp uma menssagem de erro e os detalhes
}
