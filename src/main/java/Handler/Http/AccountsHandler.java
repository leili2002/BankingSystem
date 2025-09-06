package Handler.Http;

import Handler.Dto.AccountServerOutput;
import Logic.AccountData;
import Logic.AccountService;
import Logic.Interface.IAccountRepository;
import Repository.AccountRepository;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import io.jsonwebtoken.Claims;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;


public class AccountsHandler implements Handler {
    private final IAccountRepository iAccountRepository = new AccountRepository();
    private final AccountService accountService = new AccountService(iAccountRepository);


    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (!"Get".equalsIgnoreCase(exchange.getRequestMethod())) {
            exchange.sendResponseHeaders(405, -1); // Method Not Allowed
            return;
        }
        try {
            Claims claims = (Claims) exchange.getAttribute("claims");
            int userId = Integer.parseInt(claims.getSubject());
            List<AccountData> accounts = accountService.GetUserAccounts(userId);
            List<AccountServerOutput> outputList = accounts.stream()
                    .map(acc -> new AccountServerOutput(
                            acc.getName(),
                            acc.getLastName(),
                            acc.getType(),
                            acc.getBalance()
                    ))
                    .toList();

            if (accounts.isEmpty()) {
                String error = "{\"error\":\"No accounts found\"}";
                exchange.getResponseHeaders().set("Content-Type", "application/json");
                exchange.sendResponseHeaders(404, error.getBytes().length);
                exchange.getResponseBody().write(error.getBytes());
                exchange.getResponseBody().close();
                return;
            }

            String jsonResponse = new Gson().toJson(outputList);
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, jsonResponse.getBytes(StandardCharsets.UTF_8).length);
            exchange.getResponseBody().write(jsonResponse.getBytes());
            exchange.getResponseBody().close();

        } catch (Exception e) {
            String error = "{\"error\":\"Server error: " + e.getMessage() + "\"}";
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(500, error.getBytes().length);
            exchange.getResponseBody().write(error.getBytes());
            exchange.getResponseBody().close();
        }
    }
}