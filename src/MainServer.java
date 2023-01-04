
//import filters.JavaOnlyFilter;
import server.Server;

public class MainServer {
    public static final int SERVER_PORT = 9933;
    public static final String DB_NAME = "chat.db";

    public MainServer() {
    }

    public static void main(String[] args) {
        Server server = new Server(9933, "chat.db");
        (new Thread(() -> {
            server.waitForClient();
        })).start();
//        server.addFilter(new JavaOnlyFilter());
    }
}