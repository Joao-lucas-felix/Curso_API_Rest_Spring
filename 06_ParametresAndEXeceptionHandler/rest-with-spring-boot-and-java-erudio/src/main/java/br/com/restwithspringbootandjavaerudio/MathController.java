package br.com.restwithspringbootandjavaerudio;

import br.com.service.Service;
import br.com.util.ConversorToDouble;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicLong;

@RestController
public class MathController {
    private final AtomicLong counter = new AtomicLong();
    @GetMapping(value = "/sum/{numberOne}/{numberTwo}")
    public Double sum(
            @PathVariable(value = "numberOne") String numberOne, //Recupera os dados do Path
            @PathVariable(value = "numberTwo") String numberTwo
    ) {
       return Service.sum(numberOne, numberTwo);
    }

    @GetMapping(path = "/sub/{numberOne}/{numberTwo}")
    public Double sub(
            @PathVariable(value = "numberOne") String numberOne,
            @PathVariable(value = "numberTwo") String numberTwo
    ) {
        return Service.sub(numberOne,numberTwo);
    }

    @GetMapping(path = "/mult/{numberOne}/{numberTwo}")
    public Double mult(
            @PathVariable(value = "numberOne") String numberOne,
            @PathVariable(value = "numberTwo") String numberTwo
    ) {
        return Service.mult(numberOne, numberTwo);
    }

    @GetMapping(path = "/div/{numberOne}/{numberTwo}")
    public Double div(
            @PathVariable(value = "numberOne") String numberOne,
            @PathVariable(value = "numberTwo") String numberTwo
    ) {
        return Service.div(numberOne,numberTwo);
    }

    @GetMapping(path = "/average/{numberOne}/{numberTwo}")
    public Double average(
            @PathVariable(value = "numberOne") String numberOne,
            @PathVariable(value = "numberTwo") String numberTwo
    ) {
        return Service.average(numberOne, numberTwo);
    }

    @GetMapping(path = "/sqrt/{number}")
    public Double sqrt(
            @PathVariable(value = "number") String number
    ) {
        return Service.sqrt(number);
    }


}


