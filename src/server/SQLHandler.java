//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLHandler {
    private static Connection connection;
    private static PreparedStatement statement;

    public SQLHandler() {
    }

    public static void connect(String DB_URL) throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:" + DB_URL);
    }

    public static String getNick(String login, String password) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("select nick from \"users_table\" where login=\"" + login + "\" and password = \"" + password + "\"");

        String nick;
        for(nick = null; rs.next(); nick = rs.getString(1)) {
        }

        return nick;
    }
}
