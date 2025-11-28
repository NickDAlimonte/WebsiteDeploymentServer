package Server;
//Listens on port 24002 for the Username and password or Username & email field to determine whether to create an account or login.

//Bad logic for determining login/creation conditions (maybe use a different listener & port for creation/login???)
//Potentially doesn't read the entire message if it's longer than 1024 bytes. Not sure if this is a problem?

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class AuthListener extends PortListener{

    public AuthListener() {
        super(24002);
    }
    String password = "Password";
    String username = "Username";


    @Override
    public void handleClient(Socket socket) throws IOException {
        System.out.println("Message Received on Port: "+socket.getLocalPort());

        InputStream is = socket.getInputStream();

        byte[] b = new byte[1024];

        String message = new String(b, 0, is.read(b), StandardCharsets.UTF_8);

        if(message.contains("uName") && message.contains("email")){
            //create account
            System.out.println("Account created");

        }

        else if(message.contains(username) && message.contains(password)){
            System.out.println("Logged in");
        }


    }

}
