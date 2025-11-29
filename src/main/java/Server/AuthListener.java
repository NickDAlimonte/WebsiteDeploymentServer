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
        OutputStream os = socket.getOutputStream();
        InputStream is = socket.getInputStream();
        BufferedReader br =  new BufferedReader(new InputStreamReader(is));
        int contentLength = 0;
        String contentLine;
        String response =
                "HTTP/1.1 303 See Other\r\n" +
                        "Location: https://Nicksproject.ca\r\n" +
                        "Content-Length: 0\r\n" +
                        "Connection: close\r\n" +
                        "\r\n";
        System.out.println("Message Received on Port: "+socket.getLocalPort());

        while((contentLine = br.readLine()) != null && !contentLine.isEmpty()){

            if(contentLine.toLowerCase().startsWith("content-length:")){
                String header = contentLine.trim().split(":")[1].trim();
                if(header.chars().allMatch(Character::isDigit)){
                    contentLength = Integer.parseInt(header);
                }
                else{
                    int digitLocation = 0;
                    for(int i = 0; i < header.length(); i++){
                        if (!Character.isDigit(header.charAt(i))){
                            digitLocation = i;
                            break;
                        }
                    }

                    contentLength = Integer.parseInt(header.substring(0,digitLocation));
                }
            }
        }

        char[] bodyChars = new char[contentLength];
        br.read(bodyChars, 0, contentLength);

        String body = new String(bodyChars);
        System.out.println(body);

        os.write(response.getBytes(StandardCharsets.UTF_8));


    }

}
