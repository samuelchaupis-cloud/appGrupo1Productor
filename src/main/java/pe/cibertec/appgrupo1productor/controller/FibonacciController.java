package pe.cibertec.appgrupo1productor.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pe.cibertec.appgrupo1productor.config.RabbitMQConfig;

import java.util.Arrays;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/fibonacci")
@AllArgsConstructor
public class FibonacciController {

    private RabbitTemplate rabbitTemplate;

    @GetMapping("/send")
    public String sendNumbers(@RequestParam("numbers") String numbers) {
        String numerosLimpios = Arrays.stream(numbers.split(";"))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.joining(";"));

        log.info("Enviando numeros A RabbitMQ: {}", numerosLimpios);
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE, RabbitMQConfig.ROUTING_KEY, numerosLimpios);
        return "Lista enviada a RabbitMQ correctamente.";
    }
}
