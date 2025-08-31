package Handler.Http;

import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;

public interface Handler {
    void handle(HttpExchange exchange) throws IOException;
}
