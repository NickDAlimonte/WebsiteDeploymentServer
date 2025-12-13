package Server;
//Listens on port 24002 for the Username and password or Username & email field to determine whether to create an account or login.

//Bad logic for determining login/creation conditions (maybe use a different listener & port for creation/login???)

//requires native access to run from JAR
//java --enable-native-access=ALL-UNNAMED -jar .\WebsiteDeploymentServer.jar
import java.io.*;
import java.net.Socket;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

import Database.AccountCreator;
import Database.accountAuthenticator;
import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;

public class AuthListener extends PortListener{

    public AuthListener() {
        super(24002);
    }


    @Override
    public void handleClient(Socket socket) throws IOException {

        OutputStream os = socket.getOutputStream();
        InputStream is = socket.getInputStream();
        BufferedReader br =  new BufferedReader(new InputStreamReader(is));
        int contentLength = 0;
        String contentLine;
        System.out.println("Message Received on Port: "+socket.getLocalPort());



        //Scanning post request for content length
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

        //reading from POST request. Stopping exactly at the end

        char[] bodyChars = new char[contentLength];
        br.read(bodyChars, 0, contentLength);

        String body = new String(bodyChars);

        String[] pairs = body.split("&");
        HashMap<String, String> accountInfo = new HashMap<>();



        //creating hashmap from KV in the input forms
        for(String pair : pairs){
            if(!pair.contains("=")){continue;}

            String[] keyVal = pair.split("=", 2);

            String key = URLDecoder.decode(keyVal[0], StandardCharsets.UTF_8);
            String value = URLDecoder.decode(keyVal[1], StandardCharsets.UTF_8);

            accountInfo.put(key, value);
        }



        //Check whether the account is intended for creation or login
        if(accountInfo.containsKey("email") && accountInfo.containsKey("uName")){

            //hashing password
            try {
                String hashedPSW = hashPassword(accountInfo.get("psw"));
                accountInfo.remove("psw");

                accountInfo.put("hashedPSW", hashedPSW);
            } catch(Exception e){
                System.out.println("Argon 2 hashing failed");
            }

            AccountCreator.CreateAccount(accountInfo.get("uName"), accountInfo.get("hashedPSW"), accountInfo.get("email"));

        }
        else{
            //Authenticating the user by username
                accountAuthenticator.authenticateUser(accountInfo.get("uName"), accountInfo.get("psw"), os);

        }

        socket.close();
    }

    //hashing the password
    public static String hashPassword(String rawPassword){
        Argon2 argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id);

        try{
            return argon2.hash(3, 65536, 1, rawPassword.toCharArray());
        }finally{
            argon2.wipeArray(rawPassword.toCharArray());
        }
    }

}
