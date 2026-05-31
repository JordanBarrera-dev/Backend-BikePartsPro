package backend.BikePartsPro.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Map;

@Service
public class EmailService {

    @Value("${google.script.url}")
    private String googleScriptUrl;

    private final HttpClient httpClient = HttpClient.newBuilder()
            .followRedirects(HttpClient.Redirect.ALWAYS)
            .connectTimeout(Duration.ofSeconds(15))
            .build();

    private final ObjectMapper objectMapper = new ObjectMapper();

    public void enviarCodigoVerificacion(String destinatario, String codigo) {
        try {
            String payload = objectMapper.writeValueAsString(Map.of(
                    "to", destinatario,
                    "subject", "Código de verificación - BikePartsPro",
                    "body", "Tu código de verificación es: " + codigo + "\nEste código expira en 2 minutos."
            ));

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(googleScriptUrl))
                    .timeout(Duration.ofSeconds(30))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(payload))
                    .build();

            httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            System.err.println("Error enviando email via Google Script: " + e.getMessage());
        }
    }
}
