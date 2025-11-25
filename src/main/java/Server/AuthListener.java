package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

public class AuthListener extends PortListener{

    public AuthListener() {
        super(24002);
    }


    @Override
    public void handleClient(Socket socket) throws IOException {
        System.out.println("Message Received on Port: "+socket.getLocalPort());

        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        OutputStream os = socket.getOutputStream();




    }
}
