//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package client;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ClientWindow extends JFrame {
    JPanel authPanel;
    private JTextField clientMsgElement;
    private JTextArea serverMsgElement;
    final String serverHost;
    final int serverPort;
    Socket socket;
    DataInputStream in;
    DataOutputStream out;

    public ClientWindow(String host, int port) {
        this.serverHost = host;
        this.serverPort = port;
        this.initConnection();
        this.initGUI();
        this.initServerListener();
    }

    private void initConnection() {
        try {
            this.socket = new Socket(this.serverHost, this.serverPort);
            this.in = new DataInputStream(this.socket.getInputStream());
            this.out = new DataOutputStream(this.socket.getOutputStream());
        } catch (IOException var2) {
            var2.printStackTrace();
        }

    }

    private void initGUI() {
        this.setBounds(600, 300, 500, 500);
        this.setTitle("Chat Client");
        this.authPanel = new JPanel(new GridLayout(2, 3));
        final JTextField loginField = new JTextField();
        loginField.setToolTipText("Enter your Login here");
        this.authPanel.add(new JLabel("Login: "));
        this.authPanel.add(loginField);
        JButton signInButton = new JButton("Sign in");
        this.authPanel.add(signInButton);
        final JTextField passwordField = new JTextField();
        passwordField.setToolTipText("Enter your Password here");
        this.authPanel.add(new JLabel("Password: "));
        this.authPanel.add(passwordField);
        this.add(this.authPanel, "North");
        signInButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    ClientWindow.this.sendAuthCommand(loginField.getText(), passwordField.getText());
                } catch (IOException var3) {
                    var3.printStackTrace();
                }

            }
        });
        this.serverMsgElement = new JTextArea();
        this.serverMsgElement.setEditable(false);
        this.serverMsgElement.setLineWrap(true);
        JScrollPane scrollPane = new JScrollPane(this.serverMsgElement);
        this.add(scrollPane, "Center");
        JPanel bottomPanel = new JPanel(new BorderLayout());
        this.add(bottomPanel, "South");
        JButton sendButton = new JButton("SEND");
        bottomPanel.add(sendButton, "East");
        this.clientMsgElement = new JTextField();
        bottomPanel.add(this.clientMsgElement, "Center");
        sendButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String message = ClientWindow.this.clientMsgElement.getText();

                try {
                    ClientWindow.this.sendMessage(message);
                } catch (IOException var4) {
                    var4.printStackTrace();
                }

                ClientWindow.this.clientMsgElement.grabFocus();
            }
        });
        this.clientMsgElement.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String message = ClientWindow.this.clientMsgElement.getText();

                try {
                    ClientWindow.this.sendMessage(message);
                } catch (IOException var4) {
                    var4.printStackTrace();
                }

            }
        });
        this.addWindowListener(new WindowAdapter() {
            public void windowClosed(WindowEvent e) {
                super.windowClosing(e);

                try {
                    ClientWindow.this.out.writeUTF("end");
                    ClientWindow.this.out.flush();
                    ClientWindow.this.socket.close();
                    ClientWindow.this.out.close();
                    ClientWindow.this.in.close();
                } catch (IOException var3) {
                }

            }
        });
        this.setVisible(true);
    }

    private void sendAuthCommand(String login, String password) throws IOException {
        String command = "auth___" + login + "___" + password;
        this.out.writeUTF(command);
        this.out.flush();
    }

    private void initServerListener() {
        (new Thread(new Runnable() {
            public void run() {
                while(true) {
                    try {
                        String message = ClientWindow.this.in.readUTF();
                        if (!message.equalsIgnoreCase("end session")) {
                            if (message.equalsIgnoreCase("signIn_success")) {
                                JOptionPane.showMessageDialog((Component)null, "SingIn ok!");
                                ClientWindow.this.authPanel.setVisible(false);
                                continue;
                            }

                            if (message.equalsIgnoreCase("signIn_fail")) {
                                JOptionPane.showMessageDialog((Component)null, "SingIn fail! Try one more time");
                                continue;
                            }

                            ClientWindow.this.serverMsgElement.append(message + "\n");
                            continue;
                        }
                    } catch (Exception var2) {
                    }

                    return;
                }
            }
        })).start();
    }

    public void sendMessage(String message) throws IOException {
        if (!message.trim().isEmpty()) {
            this.out.writeUTF(message);
            this.out.flush();
            this.clientMsgElement.setText("");
        }

    }
}
