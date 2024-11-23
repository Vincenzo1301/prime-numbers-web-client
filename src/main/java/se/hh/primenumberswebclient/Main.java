package se.hh.primenumberswebclient;

import static java.lang.String.valueOf;
import static org.springframework.http.MediaType.APPLICATION_JSON;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Scanner;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;
import se.hh.primenumberswebclient.data.PrimeNumberDto;

@SpringBootApplication
public class Main implements CommandLineRunner {

  @Value("${server.url}")
  private String serverUrl;

  private final ObjectMapper objectMapper;
  private final RestClient client;

  public Main(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
    this.client = RestClient.create();
  }

  public static void main(String[] args) {
    SpringApplication.run(Main.class, args);
  }

  @Override
  public void run(String... args) {
    Scanner scanner = new Scanner(System.in);

    System.out.println("Prime Number CLI - Enter numbers to check or 'exit()' to quit.");

    while (true) {
      System.out.print("Enter a number to check if it is prime: ");
      String input = scanner.nextLine();

      if (input.equalsIgnoreCase("exit()")) {
        System.out.println("Goodbye!");
        System.exit(0);
      }

      try {
        int number = Integer.parseInt(input);

        String getUri = serverUrl + "/api/v1/prime?number=" + number;
        String result = client.get().uri(getUri).retrieve().body(String.class);

        JsonNode jsonNode = objectMapper.readTree(result);

        String checkResponse = jsonNode.get("isPrime").asText();
        if (checkResponse.isEmpty()) {
          updateServer(number);
        } else {
          System.out.println("Number " + number + " is " + checkResponse);
        }
      } catch (JsonProcessingException e) {
        System.out.println("Error parsing JSON response.");
        throw new RuntimeException(e);
      }
    }
  }

  private void updateServer(int number) {
    boolean isPrime = isPrime(number);
    PrimeNumberDto body = new PrimeNumberDto(number, valueOf(isPrime));

    String bodyJson;
    try {
      bodyJson = objectMapper.writeValueAsString(body);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }

    String storeUri = serverUrl + "/api/v1/prime";
    ResponseEntity<Void> response =
        client
            .post()
            .uri(storeUri)
            .contentType(APPLICATION_JSON)
            .body(bodyJson)
            .retrieve()
            .toBodilessEntity();

    if (response.getStatusCode().is2xxSuccessful()) {
      System.out.println("Number is stored successfully. You can try it again.");
    } else {
      System.out.println("Something went wrong while storing the number. Please try it again.");
    }
  }

  private boolean isPrime(int n) {
    if (n <= 1) return false;
    for (int i = 2; i < n; i++) if (n % i == 0) return false;
    return true;
  }
}
