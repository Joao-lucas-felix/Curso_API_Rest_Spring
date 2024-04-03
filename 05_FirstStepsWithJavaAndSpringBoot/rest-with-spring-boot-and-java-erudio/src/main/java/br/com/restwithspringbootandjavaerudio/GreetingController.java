package br.com.restwithspringbootandjavaerudio;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicLong;

@RestController // combinação do @responsebody e @controller feita para criar aplicações restful
// Feita para retornar os dados na body da request http
public class GreetingController {
    private static final String frase = "Hello, %s";
    private final AtomicLong counter = new AtomicLong();

    @RequestMapping("/greeting") // mapeia uma requisição para um metodo
    public Greeting greeting(
            @RequestParam(value = "name", defaultValue = "World") //Parametro recebido na rquest
            // não é obrigatorio
            String name) {
        return new Greeting(counter.incrementAndGet(), String.format(frase, name));
    }
}


