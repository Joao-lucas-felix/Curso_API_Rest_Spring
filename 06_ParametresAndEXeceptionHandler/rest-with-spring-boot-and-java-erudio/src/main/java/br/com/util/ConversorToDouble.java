package br.com.util;

import br.com.exception.UnsuportMathOperationExeception;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class ConversorToDouble {

    @NotNull
    @Contract("null -> fail")
    public static Double convertToDouble(String strNumber) {
        if (strNumber == null) throw  new UnsuportMathOperationExeception("Please set a numeric value");
        String numberFormated = strNumber.replaceAll(",", ".");
        if (isNumeric(numberFormated)) return Double.parseDouble(numberFormated);
        throw  new UnsuportMathOperationExeception("Please set a numeric value");
    }
    private static boolean isNumeric(String strNumber) {
        if (strNumber == null) return false;
        String numberFormated = strNumber.replaceAll(",", ".");
        return numberFormated.matches("[-+]?\\d*.?\\d+");
    }
}
