package app;

import api.RestServer;

public class ApiMain {

    public static void main(String[] args) {
        ApplicationContext applicationContext = ApplicationContext.createDefault();
        RestServer server = new RestServer(applicationContext, 8080);
        server.start();
        System.out.println("API REST iniciada em http://localhost:8080");
    }
}
