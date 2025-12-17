package Database;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;

public class accountAuthenticator {

    public static int authenticateUser(String username, String password, OutputStream os){
        if(username == null || password == null){
            System.out.println("Error: Username and password are null.");
            return -1;
        }

        Argon2 argon2 = Argon2Factory.create(
                Argon2Factory.Argon2Types.ARGON2id);

        //Creating prepared statement strings
        String sqlUsername = "Select username, password_hash from users where username = ?;";
        String sqlEmail = "Select email, password_hash from users where email = ?;";



        //Establish Database Connector, run prepd statements
        try{
            Connection conn = DBConnector.getConnection();
            PreparedStatement stmt;

            if(username.contains("@") && username.contains(".com")){
                stmt = conn.prepareStatement(sqlEmail);
            }
            else{
                stmt = conn.prepareStatement(sqlUsername);
            }

            //format statements for user authentication && run query
            stmt.setString(1, username);
            stmt.executeQuery();

            ResultSet rs = stmt.getResultSet();

            String hashedPSW;
            if(rs.next()){
                System.out.println("Login Failed: User not found");
            }

            hashedPSW = rs.getString(2);

            if(argon2.verify(hashedPSW, password)){
                //random generation for token
                SecureRandom random = new SecureRandom();
                byte[] bytes = new byte[32];
                random.nextBytes(bytes);

                //create user session
                stmt = conn.prepareStatement("select user_id from users where username = ?");
                stmt.setString(1, username);
                rs = stmt.executeQuery();

                if(!rs.next()){
                    System.out.println("Login failed");
                    return -1;
                }
                int userID = rs.getInt("user_id");

                //generate token here
                String token = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);


                //prepare statement here
                stmt = conn.prepareStatement("insert into UserSessions (user_id, session_token) VALUES (?, ?)");
                stmt.setInt(1, userID);
                stmt.setString(2, token);
                stmt.executeUpdate();

                String cookieHeader = "Set-Cookie: session_token=" + token + "; HttpOnly" + "; Secure" + "; Path=/" + "; Domain=.nicksproject.ca" + "; Max-Age=43200" +"; SameSite=None";

                String loginSuccessful = "HTTP/1.1 303 See Other\r\n" +
                        "Location: https://Nicksproject.ca/UserAccount/UserProfile.php\r\n" +
                        cookieHeader+"\r\n" +
                        "\r\n";

                try{
                    os.write(loginSuccessful.getBytes(StandardCharsets.UTF_8));
                    System.out.println("Successfully sent session cookie");
                }catch(IOException e){
                    System.out.println("Failed to write to outputstream");
                }

                System.out.println("Sessions created successfully");
            }

        }catch(SQLException e){
            System.out.println("Error in authenticateUser");
        }

        return 0;
    }
}
