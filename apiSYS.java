import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

// Enum for API types (for extensibility)
enum ApiType {
    REST, SOAP, GRAPHQL
}

// User class representing different users (Admin, Employee, Customer)
class User {
    private String name;
    private String email;
    private AccountLevel accountLevel;

    public User(String name, String email, AccountLevel accountLevel) {
        this.name = name;
        this.email = email;
        this.accountLevel = accountLevel;
    }

    public AccountLevel getAccountLevel() {
        return accountLevel;
    }

    public String getName() {
        return name;
    }
}

// Enum for Account Levels
enum AccountLevel {
    ADMIN, EMPLOYEE, CUSTOMER;
}

// API Connection Manager class that handles API interactions
class APIConnectionManager {
    private static final Logger logger = Logger.getLogger(APIConnectionManager.class.getName());
    private HttpClient httpClient;
    private String apiUrl;
    private String apiKey;
    private ApiType apiType;

    public APIConnectionManager() {
        this.httpClient = HttpClient.newHttpClient();
    }

    public void configureApiConnection(String apiUrl, String apiKey, ApiType apiType) {
        this.apiUrl = apiUrl;
        this.apiKey = apiKey;
        this.apiType = apiType;
    }

    // Log API requests and responses
    private void logRequestAndResponse(String request, String response) {
        logger.info("API Request: " + request);
        logger.info("API Response: " + response);
    }

    // Send GET request to the API
    public String sendGetRequest() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .header("Authorization", "Bearer " + apiKey)
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        logRequestAndResponse(request.toString(), response.body());

        if (response.statusCode() != 200) {
            throw new IOException("Received non-OK response: " + response.statusCode());
        }

        return response.body();
    }

    // Send POST request to the API (For REST API)
    public String sendPostRequest(String jsonPayload) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonPayload, StandardCharsets.UTF_8))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        logRequestAndResponse(request.toString(), response.body());

        if (response.statusCode() != 200) {
            throw new IOException("Received non-OK response: " + response.statusCode());
        }

        return response.body();
    }

    // Method to handle SOAP requests (example)
    public String sendSoapRequest(String xmlPayload) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "text/xml")
                .POST(HttpRequest.BodyPublishers.ofString(xmlPayload, StandardCharsets.UTF_8))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        logRequestAndResponse(request.toString(), response.body());

        if (response.statusCode() != 200) {
            throw new IOException("Received non-OK response: " + response.statusCode());
        }

        return response.body();
    }

    // Method to handle GraphQL requests (example)
    public String sendGraphqlRequest(String graphqlQuery) throws IOException, InterruptedException {
        String jsonPayload = "{\"query\": \"" + graphqlQuery + "\"}";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonPayload, StandardCharsets.UTF_8))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        logRequestAndResponse(request.toString(), response.body());

        if (response.statusCode() != 200) {
            throw new IOException("Received non-OK response: " + response.statusCode());
        }

        return response.body();
    }
}

// Admin Class that can configure and interact with the API
class Admin extends User {

    public Admin(String name, String email) {
        super(name, email, AccountLevel.ADMIN);
    }

    public void configureApiConnection(APIConnectionManager apiManager, Scanner scanner) {
        System.out.println("Enter the API URL: ");
        String apiUrl = scanner.nextLine();

        System.out.println("Enter the API Key: ");
        String apiKey = scanner.nextLine();

        System.out.println("Select the API type (1: REST, 2: SOAP, 3: GraphQL): ");
        int apiTypeChoice = scanner.nextInt();
        ApiType apiType = ApiType.REST;
        switch (apiTypeChoice) {
            case 1:
                apiType = ApiType.REST;
                break;
            case 2:
                apiType = ApiType.SOAP;
                break;
            case 3:
                apiType = ApiType.GRAPHQL;
                break;
            default:
                System.out.println("Invalid choice, defaulting to REST.");
        }

        apiManager.configureApiConnection(apiUrl, apiKey, apiType);
    }

    public void interactWithApi(APIConnectionManager apiManager, Scanner scanner) throws IOException, InterruptedException {
        System.out.println("Select API action: ");
        System.out.println("1. Send GET Request");
        System.out.println("2. Send POST Request");
        int action = scanner.nextInt();
        scanner.nextLine(); // Consume newline character

        if (action == 1) {
            String response = apiManager.sendGetRequest();
            System.out.println("Response from GET request: " + response);
        } else if (action == 2) {
            System.out.println("Enter the POST data (JSON format): ");
            String data = scanner.nextLine();
            String response = apiManager.sendPostRequest(data);
            System.out.println("Response from POST request: " + response);
        } else {
            System.out.println("Invalid action.");
        }
    }
}

public class apiSYS {

    private static final Logger logger = Logger.getLogger(apiSYS.class.getName());

    public static void main(String[] args) {
        try {
            // Configure logger to log API requests and responses to a file
            FileHandler fileHandler = new FileHandler("api_requests.log", true);
            logger.addHandler(fileHandler);
            SimpleFormatter formatter = new SimpleFormatter();
            fileHandler.setFormatter(formatter);

            Scanner scanner = new Scanner(System.in);

            // Create Admin user
            Admin admin = new Admin("Admin User", "admin@company.com");
            admin.configureApiConnection(new APIConnectionManager(), scanner);

            // Admin interacts with the API
            admin.interactWithApi(new APIConnectionManager(), scanner);

            scanner.close();

        } catch (IOException | InterruptedException e) {
            logger.severe("Error occurred: " + e.getMessage());
            System.out.println("An error occurred: " + e.getMessage());
        }
    }
}