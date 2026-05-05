package app;

import api.RestServer;

public class ApiMain {

    public static void main(String[] args) {
        ApplicationContext applicationContext = ApplicationContext.createDefault();
        int port = Integer.parseInt(System.getProperty("api.port", "8080"));
        RestServer server = new RestServer(applicationContext, port);
        server.start();
        System.out.println("API REST iniciada em http://localhost:" + port);
    }
}
