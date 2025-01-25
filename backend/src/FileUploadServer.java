import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class FileUploadServer {
    private static final int PORT = 8081;
    private static final String UPLOAD_DIR = "uploads";
    private static final String RESULT_DIR = "results";

    public static void main(String[] args) throws IOException {
        // Creăm directoarele pentru upload și rezultate, dacă nu există
        new File(UPLOAD_DIR).mkdirs();
        new File(RESULT_DIR).mkdirs();

        // Configurăm serverul HTTP cu endpoint-uri
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.createContext("/upload-problema1", new FileUploadHandler("D://Alexandra2//master_bdts_ifr//anul1//semestrul1//Programare_web_java//proiect//backend//src//Problema1.java", "lee1.txt"));
        server.createContext("/upload-problema2", new FileUploadHandler("D://Alexandra2//master_bdts_ifr//anul1//semestrul1//Programare_web_java//proiect//backend//src//Problema2.java", "statisticiordine.txt"));
        server.createContext("/upload-problema3", new FileUploadHandler("D://Alexandra2//master_bdts_ifr//anul1//semestrul1//Programare_web_java//proiect//backend//src//Problema3.java", "interclasari.txt"));
        server.createContext("/download", new FileDownloadHandler());
        server.setExecutor(null); // Default executor
        System.out.println("Server started on port " + PORT);
        server.start();
    }

    static class FileUploadHandler implements HttpHandler {
        private final String className;
        private final String resultFileName;

        public FileUploadHandler(String className, String resultFileName) {
            this.className = className;
            this.resultFileName = resultFileName;
        }

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            // se permit cererile CORS
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "POST, OPTIONS");
            exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");

            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(204, -1); // No Content
                return;
            }

            if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                // se proceseaza fisierul primit si se salveaza
                File uploadedFile = new File(UPLOAD_DIR + File.separator + resultFileName);
                extractFileContent(exchange.getRequestBody(), uploadedFile);

                System.out.println("File uploaded successfully: " + uploadedFile.getAbsolutePath());

                // se ruleaza codul specific problemei
                runProblemSolver(className, resultFileName);

                String response = "File processed successfully. You can download the result.";
                exchange.sendResponseHeaders(200, response.getBytes().length);
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(response.getBytes());
                }
            } else {
                String response = "Method not allowed";
                exchange.sendResponseHeaders(405, response.getBytes().length);
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(response.getBytes());
                }
            }
        }

        private void extractFileContent(InputStream inputStream, File uploadedFile) throws IOException {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
                 BufferedWriter writer = new BufferedWriter(new FileWriter(uploadedFile))) {

                String line;
                boolean isFileContent = false;

                while ((line = reader.readLine()) != null) {
                    // se detecteaza inceputul fisierului
                    if (line.contains("Content-Disposition") && line.contains("filename=")) {
                        isFileContent = true; // se incepe citirea
                        continue;
                    }

                    // se ignora linia Content-Type
                    if (isFileContent && line.contains("Content-Type")) {
                        continue;
                    }

                    //se ignora liniile goale care separă antetul de conținut
                    if (isFileContent && line.isEmpty()) {
                        continue;
                    }

                    // se opreste la sfarsitul continutului fisierului
                    if (isFileContent && line.startsWith("------")) {
                        break;
                    }

                    // se scrie continutul fisierului
                    if (isFileContent) {
                        writer.write(line);
                        writer.newLine();
                    }
                }
            }
        }

        private void runProblemSolver(String className, String resultFileName) {
            try {
                ProcessBuilder processBuilder = new ProcessBuilder("java", "-cp", ".", className)
                        .redirectInput(new File(UPLOAD_DIR + File.separator + resultFileName))
                        .redirectOutput(new File(RESULT_DIR + File.separator + resultFileName))
                        .redirectError(new File(RESULT_DIR + File.separator + "error.log"));

                Process process = processBuilder.start();
                int exitCode = process.waitFor();

                if (exitCode != 0) {
                    System.err.println("Process for " + className + " exited with code: " + exitCode);

                    File errorFile = new File(RESULT_DIR + File.separator + "error.log");
                    if (errorFile.exists()) {
                        try (BufferedReader br = new BufferedReader(new FileReader(errorFile))) {
                            String line;
                            while ((line = br.readLine()) != null) {
                                System.err.println("Error: " + line);
                            }
                        }
                    }
                } else {
                    System.out.println("Process for " + className + " completed successfully.");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    static class FileDownloadHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");

            String query = exchange.getRequestURI().getQuery();
            String fileName = query.split("=")[1];

            File file = new File(RESULT_DIR + File.separator + fileName);
            if (file.exists()) {
                exchange.getResponseHeaders().add("Content-Type", "application/octet-stream");
                exchange.getResponseHeaders().add("Content-Disposition", "attachment; filename=" + fileName);
                exchange.sendResponseHeaders(200, file.length());

                try (OutputStream os = exchange.getResponseBody(); FileInputStream fis = new FileInputStream(file)) {
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = fis.read(buffer)) != -1) {
                        os.write(buffer, 0, bytesRead);
                    }
                }
            } else {
                String response = "File not found";
                exchange.sendResponseHeaders(404, response.getBytes().length);
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(response.getBytes());
                }
            }
        }
    }
}
