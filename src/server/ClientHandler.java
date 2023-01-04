//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package server;

import exceptions.AuthFailException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;

public class ClientHandler implements Runnable {
    private Socket socket;
    private Server server;
    private DataOutputStream out;
    private DataInputStream in;
    private static int clientsCount = 0;
    private String clientName;

    public ClientHandler(Socket socket, Server server) {
        try {
            this.socket = socket;
            this.server = server;
            this.out = new DataOutputStream(socket.getOutputStream());
            this.in = new DataInputStream(socket.getInputStream());
            ++clientsCount;
            this.clientName = "client" + clientsCount;
            System.out.println("Client \"" + this.clientName + "\" ready!");
        } catch (IOException var4) {
        }

    }

    public void run() {
        this.waitForAuth();
        this.waitForMessage();
    }

    private void waitForMessage() {
        while(true) {
            String message = null;

            try {
                message = this.in.readUTF();
            } catch (IOException var3) {
                var3.printStackTrace();
            }

            System.out.println(this.clientName + ": " + message);
            (new Thread(new MessagesSender(message, this.clientName, this.server))).start();
        }
    }

    private void waitForAuth() {
        while(true) {
            String message = null;

            try {
                message = this.in.readUTF();
            } catch (IOException var3) {
                var3.printStackTrace();
            }

            try {
                if (!this.isAuthOk(message)) {
                    this.out.writeUTF("signIn_fail");
                    continue;
                }

                this.out.writeUTF("signIn_success");
            } catch (IOException var4) {
                var4.printStackTrace();
                continue;
            }

            System.out.println(this.clientName + " auth ok and is ready for chat!");
            return;
        }
    }

    private boolean isAuthOk(String message) {
        System.out.println(this.clientName + "[NO AUTH]: " + message);
        if (message != null) {
            String[] parsedMessage = message.split("___");
            if (parsedMessage.length == 3) {
                try {
                    this.processAuthMessage(parsedMessage);
                    return true;
                } catch (AuthFailException var4) {
                    return false;
                }
            }
        }

        return false;
    }

    private void processAuthMessage(String[] parsedMessage) throws AuthFailException {
        if (parsedMessage[0].equals("auth")) {
            System.out.println("Auth message from " + this.clientName);
            String login = parsedMessage[1];
            String password = parsedMessage[2];
            String nick = null;

            try {
                nick = SQLHandler.getNick(login, password);
            } catch (SQLException var6) {
                var6.printStackTrace();
                throw new AuthFailException();
            }

            if (nick != null) {
                this.server.addClient(this, nick);
            } else {
                throw new AuthFailException();
            }
        }
    }

    public DataOutputStream getOut() {
        return this.out;
    }

    public String getClientName() {
        return this.clientName;
    }

    public void setNick(String nick) {
        this.clientName = nick;
    }
}
