package Handler.Http;

import Logic.AccountData;
import Logic.AccountService;
import Logic.Interface.IAccountRepository;
import Repository.AccountRepository;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class CreatAccountsHandler implements Handler {
    private final Gson gson = new Gson();
    private final IAccountRepository iAccountRepository = new AccountRepository();
    private final AccountService accountService = new AccountService(iAccountRepository);

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            sendResponse(exchange, 405, "{\"error\":\"Method Not Allowed\"}");
            return;
        }

        // 1️⃣ Read JSON body
        InputStream is = exchange.getRequestBody();
        String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);

        AccountData newAccount;
        try {
            newAccount = gson.fromJson(body, AccountData.class);
        } catch (Exception e) {
            sendResponse(exchange, 400, "{\"error\":\"Invalid JSON format\"}");
            return;
        }

        // 2️⃣ Insert account into DB
        Exception e = accountService.addAccount(newAccount);

        if (e == null) {
            // Success → send JSON with success message
            String responseJson = gson.toJson(new ResponseMessage("account added  successful"));
            sendResponse(exchange, 200, responseJson);
        } else {
            // Error → send JSON with error
            String responseJson = gson.toJson(new ResponseMessage("Error: " + e.getMessage()));
            sendResponse(exchange, 400, responseJson);
        }
    }

    private void sendResponse(HttpExchange exchange, int statusCode, String responseText) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        byte[] bytes = responseText.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(statusCode, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }

    // Inner class for response JSON
    private static class ResponseMessage {
        private final String message;
        public ResponseMessage(String message) {
            this.message = message;
        }
    }
}