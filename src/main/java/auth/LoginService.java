package auth;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.Pengguna;

public class LoginService extends Login {
    private Connection connection;
    private Pengguna pengguna;  

    public LoginService(Connection connection, String username, String password) {
        super(username, password);  
        this.connection = connection;
    }

    @Override
    public boolean Authenticate() {
        String query = "SELECT id, username, email, password FROM pengguna WHERE username = ? AND password = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, getUsername());
            statement.setString(2, getPassword());
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int id_user = resultSet.getInt("id");
                String username = resultSet.getString("username");
                String email = resultSet.getString("email");
                String password = resultSet.getString("password");
                pengguna = new Pengguna(username, email, password);
                pengguna.setIdPengguna(id_user);

                SessionManager.setCurrentUser(pengguna);  

                Authentication.setLoggedInUserId(id_user);
                Authentication.setLoggedInUsername(getUsername());

                return true;  
            }
        } catch (SQLException e) {
            e.printStackTrace();  
        }
        return false;  
    }

    public int getUserID() {
        return Authentication.getLoggedInUserId();
    }

    public Pengguna getPengguna() {
        return pengguna; 
    }
}
