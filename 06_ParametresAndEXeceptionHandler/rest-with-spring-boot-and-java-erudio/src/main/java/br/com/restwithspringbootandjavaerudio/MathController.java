package br.com.restwithspringbootandjavaerudio;

import br.com.exception.UnsuportMathOperationExeception;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.atomic.AtomicLong;

@RestController
public class MathController {
    private final AtomicLong counter = new AtomicLong();

    @GetMapping(value = "/sum/{numberOne}/{numberTwo}")
    public Double sum(
            @PathVariable(value = "numberOne") String numberOne, //Recupera os dados do Path
            @PathVariable(value = "numberTwo") String numberTwo
    ){
        if(!isNumeric(numberOne) || !isNumeric(numberTwo)){
            throw  new UnsuportMathOperationExeception("Please set a numeric value");
        }

        return convertToDouble(numberOne) + convertToDouble(numberTwo);
    }

    @GetMapping(path = "/sub/{numberOne}/{numberTwo}")
    public Double sub(
             @PathVariable(value = "numberOne") String numberOne,
             @PathVariable(value = "numberTwo") String numberTwo
    ){
        return convertToDouble(numberOne) - convertToDouble(numberTwo);
    }
    @GetMapping(path = "/mult/{numberOne}/{numberTwo}")
    public Double mult(
            @PathVariable(value = "numberOne") String numberOne,
            @PathVariable(value = "numberTwo") String numberTwo
    ){
        return convertToDouble(numberOne) * convertToDouble(numberTwo);
    }
    @GetMapping(path = "/div/{numberOne}/{numberTwo}")
    public Double div(
            @PathVariable(value = "numberOne") String numberOne,
            @PathVariable(value = "numberTwo") String numberTwo
    )
    {
        Double divisor = convertToDouble(numberTwo);
        if(divisor == 0 ) throw new UnsuportMathOperationExeception("Divison by Zero");
        return convertToDouble(numberOne)/divisor;
    }
    @GetMapping(path = "/average/{numberOne}/{numberTwo}")
    public Double average(
            @PathVariable(value = "numberOne") String numberOne,
            @PathVariable(value = "numberTwo") String numberTwo
    ){
        return (convertToDouble(numberOne)+convertToDouble(numberTwo))/2;
    }
    @GetMapping(path = "/sqrt/{number}")
    public Double sqrt(
            @PathVariable(value = "number") String number
    ){
        return Math.sqrt(convertToDouble(number));
    }




    private Double convertToDouble(String strNumber) {
        if (strNumber == null) throw  new UnsuportMathOperationExeception("Please set a numeric value");
        String numberFormated = strNumber.replaceAll(",", ".");
        if (isNumeric(numberFormated)) return Double.parseDouble(numberFormated);
        throw  new UnsuportMathOperationExeception("Please set a numeric value");
    }

    private boolean isNumeric(String strNumber) {
        if (strNumber == null) return false;
        String numberFormated = strNumber.replaceAll(",", ".");
        return numberFormated.matches("[-+]?\\d*.?\\d+");
    }

}


