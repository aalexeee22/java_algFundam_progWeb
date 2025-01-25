import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.*;
import java.net.InetSocketAddress;
import java.sql.*;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class UserManagementServer {
    private static final int PORT = 8080;
    private static final String DB_URL = "jdbc:mysql://localhost:3306/user_management";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "";
    private static final ConcurrentHashMap<String, String> sessions = new ConcurrentHashMap<>();
    private static Connection connection;

    public static void main(String[] args) throws IOException {
        try {
            connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            System.out.println("Connected to the database!");
        } catch (SQLException e) {
            System.err.println("Database connection error: " + e.getMessage());
            return;
        }

        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.createContext("/register", new RegisterHandler());
        server.createContext("/login", new LoginHandler());
        server.createContext("/logout", new LogoutHandler());
        server.createContext("/", exchange -> sendCORSResponse(exchange, "Server is running"));
        server.setExecutor(null);
        System.out.println("Server started on port " + PORT);
        server.start();
    }

    static class RegisterHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendCORSResponse(exchange, "");
                return;
            }

            if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                HashMap<String, String> requestData = parseRequestBody(exchange);
                String username = requestData.get("username");
                String password = requestData.get("password");

                if (username == null || password == null || username.isEmpty() || password.isEmpty()) {
                    sendCORSResponse(exchange, "Username and password are required.", 400);
                    return;
                }

                String hashedPassword = hashPassword(password);

                try (PreparedStatement stmt = connection.prepareStatement(
                        "INSERT INTO users (username, password) VALUES (?, ?)")) {
                    stmt.setString(1, username);
                    stmt.setString(2, hashedPassword);
                    stmt.executeUpdate();
                    sendCORSResponse(exchange, "Account created successfully!", 200);
                } catch (SQLException e) {
                    if (e.getMessage().contains("Duplicate entry")) {
                        sendCORSResponse(exchange, "Username already exists.", 400);
                    } else {
                        sendCORSResponse(exchange, "Error: " + e.getMessage(), 500);
                    }
                }
            } else {
                sendCORSResponse(exchange, "Method not allowed", 405);
            }
        }
    }

    static class LoginHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendCORSResponse(exchange, "");
                return;
            }

            if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                HashMap<String, String> requestData = parseRequestBody(exchange);
                String username = requestData.get("username");
                String password = requestData.get("password");

                if (username == null || password == null || username.isEmpty() || password.isEmpty()) {
                    sendCORSResponse(exchange, "Username and password are required.", 400);
                    return;
                }

                String hashedPassword = hashPassword(password);

                try (PreparedStatement stmt = connection.prepareStatement(
                        "SELECT * FROM users WHERE username = ? AND password = ?")) {
                    stmt.setString(1, username);
                    stmt.setString(2, hashedPassword);
                    ResultSet rs = stmt.executeQuery();

                    if (rs.next()) {
                        String token = generateToken(username);
                        sessions.put(token, username);
                        System.out.println("User logged in: " + username + " | Token: " + token);
                        sendCORSResponse(exchange, "{\"token\": \"" + token + "\"}", 200);
                    } else {
                        sendCORSResponse(exchange, "Invalid username or password.", 401);
                    }
                } catch (SQLException e) {
                    sendCORSResponse(exchange, "Error: " + e.getMessage(), 500);
                }
            } else {
                sendCORSResponse(exchange, "Method not allowed", 405);
            }
        }
    }

    static class LogoutHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendCORSResponse(exchange, "");
                return;
            }

            if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                HashMap<String, String> requestData = parseRequestBody(exchange);
                String token = requestData.get("token");

                if (token == null || !sessions.containsKey(token)) {
                    /*System.out.println("Invalid token or already logged out.");*/
                    sendCORSResponse(exchange, "User logged out.", 200);
                    return;
                }

                sessions.remove(token);
                System.out.println("Logout successful. Updated sessions: " + sessions);
                sendCORSResponse(exchange, "Logout successful.", 200);
            } else {
                sendCORSResponse(exchange, "Method not allowed", 405);
            }
        }
    }

    private static HashMap<String, String> parseRequestBody(HttpExchange exchange) throws IOException {
        InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), "utf-8");
        BufferedReader br = new BufferedReader(isr);
        StringBuilder body = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            body.append(line);
        }

        HashMap<String, String> data = new HashMap<>();
        String json = body.toString();
        json = json.replace("{", "").replace("}", "").replace("\"", "");
        for (String pair : json.split(",")) {
            String[] entry = pair.split(":");
            if (entry.length == 2) {
                data.put(entry[0].trim(), entry[1].trim());
            }
        }
        return data;
    }

    private static String hashPassword(String password) {
        return Integer.toHexString(password.hashCode());
    }

    private static String generateToken(String username) {
        return Integer.toHexString((username + System.currentTimeMillis()).hashCode());
    }

    private static void sendCORSResponse(HttpExchange exchange, String response) throws IOException {
        sendCORSResponse(exchange, response, 200);
    }

    private static void sendCORSResponse(HttpExchange exchange, String response, int statusCode) throws IOException {
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");
        exchange.sendResponseHeaders(statusCode, response.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}
