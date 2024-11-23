package se.hh.primenumberswebclient.client;

import static org.springframework.http.MediaType.APPLICATION_JSON;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import se.hh.primenumberswebclient.data.PrimeNumberDto;

@Component
public class PrimeNumberClient {

  private final RestClient client;
  private final ObjectMapper objectMapper;

  public PrimeNumberClient() {
    this.client = RestClient.create();
    this.objectMapper = new ObjectMapper();
  }

  /**
   * Sends a GET request to check if the number is prime.
   *
   * @param serverUrl the server URL
   * @param number the number to check
   * @return a JSON response from the server
   */
  public JsonNode checkPrime(String serverUrl, int number) {
    String getUri = serverUrl + "/api/v1/prime?number=" + number;
    try {
      String response = client.get().uri(getUri).retrieve().body(String.class);
      return objectMapper.readTree(response);
    } catch (Exception e) {
      throw new RuntimeException("Failed to fetch prime status from server: " + e.getMessage(), e);
    }
  }

  /**
   * Sends a POST request to store the prime number on the server.
   *
   * @param serverUrl the server URL
   * @param body the body containing the number and its prime status
   * @return the HTTP response from the server
   */
  public ResponseEntity<Void> storePrime(String serverUrl, PrimeNumberDto body) {
    try {
      String bodyJson = objectMapper.writeValueAsString(body);
      String storeUri = serverUrl + "/api/v1/prime";

      return client
          .post()
          .uri(storeUri)
          .contentType(APPLICATION_JSON)
          .body(bodyJson)
          .retrieve()
          .toBodilessEntity();
    } catch (JsonProcessingException e) {
      throw new RuntimeException("Error serializing PrimeNumberDto: " + e.getMessage(), e);
    }
  }
}
