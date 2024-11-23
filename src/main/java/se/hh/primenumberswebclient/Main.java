package se.hh.primenumberswebclient;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.Scanner;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import se.hh.primenumberswebclient.client.PrimeNumberClient;
import se.hh.primenumberswebclient.data.PrimeNumberDto;

@SpringBootApplication
public class Main implements CommandLineRunner {

  @Value("${server.url}")
  private String serverUrl;

  private final PrimeNumberClient primeNumberClient;

  public Main(PrimeNumberClient primeNumberClient) {
    this.primeNumberClient = primeNumberClient;
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

        JsonNode response = primeNumberClient.checkPrime(serverUrl, number);
        String isPrimeResponse = response.get("isPrime").asText();

        if (isPrimeResponse.isEmpty()) {
          updateServer(number);
        } else {
          String isPrime = isPrimeResponse.equals("true") ? "prime" : "not prime";
          System.out.println("Number " + number + " is " + isPrime);
        }
      } catch (Exception e) {
        System.out.println("Error: " + e.getMessage());
      }
    }
  }

  private void updateServer(int number) {
    boolean isPrime = isPrime(number);
    PrimeNumberDto body = new PrimeNumberDto(number, String.valueOf(isPrime));

    ResponseEntity<Void> response = primeNumberClient.storePrime(serverUrl, body);

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