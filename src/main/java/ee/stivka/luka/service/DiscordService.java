package ee.stivka.luka.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import ee.stivka.luka.model.DiscordRequest;
import reactor.core.publisher.Mono;

@Service
public class DiscordService {

    private final WebClient webClient;

    public DiscordService(WebClient.Builder webClientBuilder, @Value("${discord.alerts.webhook.url}") String discordWebhookUrl) {
        this.webClient = webClientBuilder.baseUrl(discordWebhookUrl).build();
    }

    private Mono<String> dispatchMessage(String message) {
        DiscordRequest discordRequest = new DiscordRequest(message);
        System.out.println("Sending out Discord alert webhook");
        return this.webClient.post()
                .body(Mono.just(discordRequest), DiscordRequest.class)
                .retrieve()
                .bodyToMono(String.class);
    }

    public void sendSweareWordAlert(String message) {
        System.out.println("Sweareword detected, sending alert to Discord");
        String discordMessage = "Someone has used a curseword in the chat: " + message;
        dispatchMessage(discordMessage).subscribe(
                response -> System.out.println("Response: " + response),
                error -> System.err.println("Error: " + error)
        );
    }

    public void notifyNewUser(String username) {
        System.out.println("New user has joined the platform, sending alert to Discord");
        String discordMessage = "New user has joined the platform: " + username;
        dispatchMessage(discordMessage).subscribe(
                response -> System.out.println("Response: " + response),
                error -> System.err.println("Error: " + error)
        );
    }
}
