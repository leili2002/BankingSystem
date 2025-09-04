//package Handler.Http;
//
//import Handler.Dto.LoginFailedException;
//import Handler.Dto.LoginServerOutput;
//import Logic.AccountService;
//import Logic.BankingService;
//import Logic.Dto.LoginResult;
//import com.google.gson.Gson;
//import com.sun.net.httpserver.HttpExchange;
//
//import java.io.IOException;
//
//public class ExistAccountHandler implements Handler{
//    private final Gson gson = new Gson();
//    private final AccountService accountService = new AccountService();
//    @Override
//    public void handle(HttpExchange exchange) throws IOException {
//        if (!"Get".equalsIgnoreCase(exchange.getRequestMethod())) {
//            exchange.sendResponseHeaders(405, -1); // Method Not Allowed
//        }
//        try {
//            LoginResult result = accountService.GetUserAccounts(loginRequest.national_id,loginRequest.password);
//            LoginServerOutput ServerOutput = new LoginServerOutput(result.getName(), result.getAccessToken());
//
//            // Convert result to JSON and send success response
//            String responseJson = gson.toJson(ServerOutput);
//            exchange.getResponseHeaders().set("Content-Type", "application/json");
//            exchange.sendResponseHeaders(200, responseJson.getBytes().length);
//            exchange.getResponseBody().write(responseJson.getBytes());
//            exchange.getResponseBody().close();
//
//        } catch (LoginFailedException e) {
//            // Convert error message to JSON and send 401
//            String responseJson = gson.toJson("login failed: " + e.getMessage());
//            exchange.getResponseHeaders().set("Content-Type", "application/json");
//            exchange.sendResponseHeaders(401, responseJson.getBytes().length);
//            exchange.getResponseBody().write(responseJson.getBytes());
//            exchange.getResponseBody().close();
//        }
//    }
//}
//    }
//}
