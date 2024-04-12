package br.com.service;

import br.com.exception.UnsuportMathOperationExeception;
import br.com.util.ConversorToDouble;
import org.jetbrains.annotations.NotNull;

public class Service {

    @NotNull
    public static Double sum(String n1, String n2) {
        return ConversorToDouble.convertToDouble(n1) + ConversorToDouble.convertToDouble(n2);
    }

    @NotNull
    public static Double sub(String n1, String n2) {
        return ConversorToDouble.convertToDouble(n1) - ConversorToDouble.convertToDouble(n2);
    }

    @NotNull
    public static Double mult(String n1, String n2) {
        return ConversorToDouble.convertToDouble(n1) * ConversorToDouble.convertToDouble(n2);
    }

    @NotNull
    public static Double div(String n1, String n2) {
        Double divisor = ConversorToDouble.convertToDouble(n2);
        if (divisor == 0) throw new UnsuportMathOperationExeception("Division by zero");
        return ConversorToDouble.convertToDouble(n1) / divisor;
    }

    @NotNull
    public static Double average(String n1, String n2) {
        return (ConversorToDouble.convertToDouble(n1) + ConversorToDouble.convertToDouble(n2)) / 2;
    }

    @NotNull
    public static Double sqrt(String n) {
        return Math.sqrt(ConversorToDouble.convertToDouble(n));
    }


}
