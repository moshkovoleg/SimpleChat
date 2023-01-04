//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package server;

public class MessagesSender implements Runnable {
    private String message;
    private String clientName;
    private Server server;

    public MessagesSender(String message, String clientName, Server server) {
        this.message = message;
        this.clientName = clientName;
        this.server = server;
    }

    public void run() {
        this.server.newMessageFromClient(this.message, this.clientName);
    }
}
