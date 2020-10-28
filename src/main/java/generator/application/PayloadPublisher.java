package generator.application;

import generator.application.message.ClientPayloadMessage;
import generator.application.message.ServerPayloadMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Random;
import java.util.UUID;

@Slf4j
@Service
public class PayloadPublisher {

    private final Random random = new Random();

    @Value("${data}")
    private String[] data;

    @Autowired
    private WebClient webClient;

    @Scheduled(cron = "*/2 * * * * *") //Every 2 seconds
    public void generatePayload() {
        webClient.post().uri("add")
                .bodyValue(ClientPayloadMessage.builder()
                        .requestId(UUID.randomUUID().toString())
                        .quantity(random.nextInt(100) + 1L)
                        .data(data[random.nextInt(data.length)])
                        .build())
                .retrieve().bodyToMono(ServerPayloadMessage.class)
                .subscribe(v -> System.out.println(v.toString()));
    }

}
