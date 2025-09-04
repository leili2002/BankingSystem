package Handler.Http;

import Logic.Dto.AdminLoginResult;
import Handler.Dto.LoginFailedException;
import Logic.Dto.LoginRequest;
import Logic.Interface.IUserRepository;
import Repository.UserRepository;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import Logic.BankingService;

import java.io.IOException;
import java.io.InputStream;

public class AdminHandler implements Handler {

    private final Gson gson = new Gson();
    private final IUserRepository iUserRepository = new UserRepository();
    private final BankingService bankingService = new BankingService(iUserRepository);

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            exchange.sendResponseHeaders(405, -1); // Method Not Allowed
            return;
        }

        InputStream is = exchange.getRequestBody();
        String body = new String(is.readAllBytes());
        LoginRequest loginRequest = gson.fromJson(body, LoginRequest.class);

        if (loginRequest == null) {
            String error = "{\"error\":\"Invalid JSON format\"}";
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(400, error.getBytes().length);
            exchange.getResponseBody().write(error.getBytes());
            return;
        }
        try {
            AdminLoginResult result = bankingService.Admin_login(loginRequest.national_id, loginRequest.password);

            // Convert result to JSON and send success response
            String responseJson = gson.toJson(result);
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, responseJson.getBytes().length);
            exchange.getResponseBody().write(responseJson.getBytes());
            exchange.getResponseBody().close();

        } catch (LoginFailedException e) {
            // Convert error message to JSON and send 401
            String responseJson = gson.toJson("login failed: " + e.getMessage());
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(401, responseJson.getBytes().length);
            exchange.getResponseBody().write(responseJson.getBytes());
            exchange.getResponseBody().close();
        }
    }
}