package Handler.Http;

import Logic.Interface.IUserRepository;
import Repository.UserRepository;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import Logic.BankingService;
import Logic.UserData;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class SignupHandler implements Handler {

    private final Gson gson = new Gson();
    private final IUserRepository iUserRepository = new UserRepository();
    private final BankingService bankingService = new BankingService(iUserRepository);

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            exchange.sendResponseHeaders(405, -1);
            return;
        }

        InputStream is = exchange.getRequestBody();
        String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);
        UserData newUser = gson.fromJson(body, UserData.class);

        String responseText;
        int statusCode;

        try {
            int result = bankingService.register(newUser);
            if (result == 1) {
                responseText = "{\"message\":\"Sign-up successful\"}";
                statusCode = 200;
            } else {
                responseText = "{\"error\":\"Sign-up failed\"}";
                statusCode = 400;
            }
        } catch (Exception e) {
            responseText = "{\"error\":\"" + e.getMessage() + "\"}";
            statusCode = 400;
        }

        exchange.getResponseHeaders().set("Content-Type", "application/json");
        byte[] responseBytes = responseText.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(statusCode, responseBytes.length);
        exchange.getResponseBody().write(responseBytes);
        exchange.getResponseBody().close();
    }
}
