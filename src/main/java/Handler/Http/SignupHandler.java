package Handler.Http;

import Logic.Interface.IUserRepository;
import Repository.UserRepository;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import Logic.BankingService;
import Logic.UserData;

import java.io.IOException;
import java.io.InputStream;

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
        String body = new String(is.readAllBytes());
        UserData newUser = gson.fromJson(body, UserData.class);

        int result = bankingService.register(newUser);
        String responseText = (result == 1) ? "Sign-up successful" : "Sign-up failed";
        if (result != 1) {
            String error = "{\"error\":\"Invalid JSON format\"}";
            exchange.getResponseHeaders().set("Content-Type", "text/plain");
            exchange.sendResponseHeaders(400, responseText.getBytes().length);
            exchange.getResponseBody().write(responseText.getBytes());
            exchange.getResponseBody().close();
        } else {
            exchange.getResponseHeaders().set("Content-Type", "text/plain");
            exchange.sendResponseHeaders(400, responseText.getBytes().length);
            exchange.getResponseBody().write(responseText.getBytes());
            exchange.getResponseBody().close();
        }
    }
}
