package br.com.restwithspringbootandjavaerudio.exception.handler;

import br.com.restwithspringbootandjavaerudio.exception.ExceptionResponse;
import br.com.restwithspringbootandjavaerudio.exception.InvalidValuesExeception;
import br.com.restwithspringbootandjavaerudio.exception.UnfoundResourceExeception;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

@RestController
@ControllerAdvice// diz que sempre que lançar uma exceção se ngm fornece um tratamento especifico cai aq
public class CustomizedResponseEntityExceptionHandler
extends ResponseEntityExceptionHandler
{
    @ExceptionHandler(Exception.class) //Trata exeções geneircas.
    public final ResponseEntity<ExceptionResponse>
    handleAllException(Exception ex, WebRequest wr){
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                new Date(),
                ex.getMessage(),
                wr.getDescription(false));
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnfoundResourceExeception.class) //Trata a exceção criada por mim.
    public final ResponseEntity<ExceptionResponse>
    handleUnfoundResoucerExeception(Exception ex, WebRequest wr){
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                new Date(),
                ex.getMessage(),
                wr.getDescription(false)
        );
        return new ResponseEntity<>(exceptionResponse, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(InvalidValuesExeception.class)
    public final ResponseEntity<ExceptionResponse>
    handleInvalidValuesExeception(Exception ex, WebRequest wr){
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                new Date(),
                ex.getMessage(),
                wr.getDescription(false)
        );
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }


}
