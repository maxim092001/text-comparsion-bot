package dbrequest;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;


/***
 * id - User id in telegram
 * first_text - First text added by user
 * second_text - Second text added by user
 * status - {
 *     0 - first login
 *     1 - added first text
 *     2 - added second text
 * }
 */


public class Request {
    private final String URL = "url";
    private final String DRIVER = "driver";
    private final String NAME = "name";
    private final String PASS = "pass";
    private final Logger log = Logger.getLogger(Request.class.getName());
    private Connection connection = null;

    public void connect() {
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            log.log(Level.SEVERE, "Driver not found\n" + e.getMessage());
            return;
        }

        try {
            connection = DriverManager.getConnection(URL, NAME, PASS);
        } catch (SQLException e) {
            log.log(Level.SEVERE, "Connection failed\n" + e.getMessage());
            return;
        }

        if (connection != null) {
            log.log(Level.INFO, "Connected");
        } else {
            log.log(Level.SEVERE, "Failed to make connection");
        }
    }

    public ResultSet getDataById(long id) {
        try {
            String sql = "SELECT * FROM text_comparison WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, id);
            return preparedStatement.executeQuery();
        } catch (SQLException e) {
            log.log(Level.WARNING, "Failed select with " + id + "\n" + e.getMessage());
            return null;
        }
    }

    public int getUserStatus(long id) {
        try {
            ResultSet res = getDataById(id);
            res.next();
            return res.getInt("status");
        } catch (SQLException e) {
            log.log(Level.INFO, "User was not registered");
            return -1;
        }
    }

    public boolean setText(String text, long id) {
        try {
            int status = getUserStatus(id);
            String textColumn = (status == 0 ? "first_text" : "second_text");

            String sql = "UPDATE text_comparison SET " + textColumn + " = ?, status = ? WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, text);
            preparedStatement.setInt(2, (status == 0 ? 1 : 2));
            preparedStatement.setLong(3, id);
            preparedStatement.executeUpdate();

            log.log(Level.INFO, "Added text into " + textColumn);
            log.log(Level.INFO, "New status: " + status);
            return true;
        } catch (SQLException e) {
            log.log(Level.WARNING, "Failed to add text " + e.getMessage());
            return false;
        }
    }

    public String getText(long id, boolean textNumber) {
        try {
            ResultSet res = getDataById(id);
            res.next();
            return res.getString((textNumber ? "second_text" : "first_text"));
        } catch (SQLException e) {
            log.log(Level.INFO, "User was not registered");
            return null;
        }
    }

    private boolean checkUserInDataBase(long id) {
        try {
            String sql = "SELECT id FROM text_comparison WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, id);
            return preparedStatement.executeQuery() != null;
        } catch (SQLException e) {
            log.log(Level.INFO, "User was not registered");
            return false;
        }
    }

    public void eraseTexts(long id) {
        try {
            String sql = "UPDATE text_comparison SET first_text = ?, second_text = ?, status = ? WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, null);
            preparedStatement.setString(2, null);
            preparedStatement.setInt(3, 0);
            preparedStatement.setLong(4, id);
            preparedStatement.executeUpdate();

            log.log(Level.INFO, "Texts erased for used with id: " + id);
            log.log(Level.INFO, "New status: " + getUserStatus(id));
        } catch (SQLException e) {
            log.log(Level.SEVERE, "Failed erase texts " + e.getMessage());
        }
    }

    public boolean addNewUser(long id) {

        if (!checkUserInDataBase(id)) {
            log.log(Level.INFO, "User was already added");
            return false;
        }

        try {
            String sql = "INSERT INTO text_comparison (id, first_text, second_text, status) Values(?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setLong(1, id);
            preparedStatement.setString(2, null);
            preparedStatement.setString(3, null);
            preparedStatement.setInt(4, 0);
            preparedStatement.executeUpdate();

            log.log(Level.INFO, "New user added");
            return true;
        } catch (SQLException e) {
            log.log(Level.SEVERE, "Failed to add user " + id + e.getMessage());
        }
        return false;
    }
}
