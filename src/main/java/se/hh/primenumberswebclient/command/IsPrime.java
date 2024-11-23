package se.hh.primenumberswebclient.command;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.shell.command.annotation.Command;
import se.hh.primenumberswebclient.client.PrimeNumberClient;
import se.hh.primenumberswebclient.data.PrimeNumberDto;

@Command
public class IsPrime {

  @Value("${server.url}")
  private String serverUrl;

  private final PrimeNumberClient primeNumberClient = new PrimeNumberClient();

  @Command(command = "is-prime", description = "Check if a number is prime.")
  public void checkPrime(int number) {
    try {
      JsonNode response = primeNumberClient.checkPrime(serverUrl, number);
      String isPrimeResponse = response.get("isPrime").asText();

      if (isPrimeResponse.isEmpty()) {
        updateServer(number);
      } else {
        String isPrime = isPrimeResponse.equals("true") ? "✅ Prime 🎉" : "❌ Not Prime 😢";
        System.out.println("ℹ️ Number " + number + ": " + isPrime);
      }
    } catch (Exception e) {
      System.out.println("⚠️ Error: " + e.getMessage());
    }
  }

  private void updateServer(int number) {
    boolean isPrime = isPrime(number);
    PrimeNumberDto body = new PrimeNumberDto(number, String.valueOf(isPrime));

    ResponseEntity<Void> response = primeNumberClient.storePrime(serverUrl, body);

    if (response.getStatusCode().is2xxSuccessful()) {
      System.out.println("✅ Number stored successfully! You can try it again.");
    } else {
      System.out.println("❌ Something went wrong while storing the number. Please try again.");
    }
  }

  private boolean isPrime(int n) {
    if (n <= 1) return false;
    for (int i = 2; i < n; i++) if (n % i == 0) return false;
    return true;
  }
}
