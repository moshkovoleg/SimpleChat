//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package server;

import exceptions.AuthFailException;
import filters.ChatFilter;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Server {
    private static volatile Server instance;
    private List<ClientHandler> clients;
    private List<ChatFilter> filters;
    private ServerSocket serverSocket = null;

    public void addFilter(ChatFilter filter) {
        this.filters.add(filter);
        System.out.println("Filter is added!");
    }

    public synchronized void addClient(ClientHandler clientHandler, String nick) throws AuthFailException {
        Iterator var3 = this.clients.iterator();

        ClientHandler client;
        do {
            if (!var3.hasNext()) {
                clientHandler.setNick(nick);
                this.clients.add(clientHandler);
                System.out.println(clientHandler.getClientName() + " is added to subscribers list!");
                return;
            }

            client = (ClientHandler)var3.next();
        } while(!client.getClientName().equals(nick));

        System.out.println("Client with nick " + nick + " is already exists!");
        throw new AuthFailException();
    }

    public Server(int serverPort, String DB_NAME) {
        System.out.println("Server init start.");
        this.clients = new LinkedList();
        this.filters = new ArrayList();

        try {
            this.serverSocket = new ServerSocket(serverPort);
            System.out.println("Server socket init OK.");
            System.out.println("Server DB init OK.");
            System.out.println("Server ready and waiting for clients...");
        } catch (IOException var4) {
            var4.printStackTrace();
        }

    }

    public void waitForClient() {
        Socket socket = null;

        try {
            while(true) {
                socket = this.serverSocket.accept();
                System.out.println("Client connected.");
                ClientHandler client = new ClientHandler(socket, this);
                (new Thread(client)).start();
            }
        } catch (IOException var10) {
            var10.printStackTrace();
        } finally {
            try {
                this.serverSocket.close();
                System.out.println("Server closed.");
                socket.close();
            } catch (IOException var9) {
                var9.printStackTrace();
            }

        }

    }

    public synchronized void newMessageFromClient(String message, String clientName) {
        Iterator var3;
        ChatFilter filter;
        for(var3 = this.filters.iterator(); var3.hasNext(); message = filter.filter(message)) {
            filter = (ChatFilter)var3.next();
        }

        var3 = this.clients.iterator();

        while(var3.hasNext()) {
            ClientHandler client = (ClientHandler)var3.next();

            try {
                client.getOut().writeUTF(clientName + ": " + message);
                client.getOut().flush();
            } catch (IOException var6) {
                var6.printStackTrace();
            }
        }

    }
}
