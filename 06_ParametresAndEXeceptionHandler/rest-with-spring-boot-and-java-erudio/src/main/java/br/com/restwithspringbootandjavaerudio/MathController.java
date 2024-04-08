package br.com.restwithspringbootandjavaerudio;

import org.springframework.web.bind.annotation.*;

import java.util.concurrent.atomic.AtomicLong;

@RestController
public class MathController {
    private final AtomicLong counter = new AtomicLong();

    @GetMapping(value = "/sum/{numberOne}/{numberTwo}")
    public Double sum(
            @PathVariable(value = "numberOne") String numberOne, //Recupera os dados do Path
            @PathVariable(value = "numberTwo") String numberTwo
    ) throws Exception {
        if(!isNumeric(numberOne) || !isNumeric(numberTwo)){
            throw new Exception();
        }

        return convertToDouble(numberOne) + convertToDouble(numberTwo);
    }

    private Double convertToDouble(String strNumber) {
        if (strNumber == null) throw new RuntimeException();
        String numberFormated = strNumber.replaceAll(",", ".");
        if (isNumeric(numberFormated)) return Double.parseDouble(numberFormated);
        throw new RuntimeException();
    }

    private boolean isNumeric(String strNumber) {
        if (strNumber == null) return false;
        String numberFormated = strNumber.replaceAll(",", ".");
        return numberFormated.matches("[-+]?\\d*.?\\d+");
    }

}


