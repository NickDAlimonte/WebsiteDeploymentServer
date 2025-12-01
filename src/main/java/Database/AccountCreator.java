package Database;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class AccountCreator {

    public static void CreateAccount(String username, String password, String email){

        String sql = "INSERT INTO users (username, password_hash, email) VALUES (?, ?, ?) ";


        try{
            Connection conn = DBConnector.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, email);
            stmt.executeUpdate();
            System.out.println("User created successfully");

        }catch(SQLException e){
            e.printStackTrace();
        }
    }

}
