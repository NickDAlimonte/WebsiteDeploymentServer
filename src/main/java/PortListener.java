import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class PortListener {
    public static void listenForDeployment() {
        int port = 24001;

        String responseSuccessful =
                "HTTP/1.1 200 OK\r\n" +
                        "Content-Type: text/plain\r\n" +
                        "Connection: close\r\n" +
                        "Content-Length: 20\r\n" +
                        "\r\n" +
                        "Deployment Triggered";

        String responseFailed =
                "HTTP/1.1 200 OK\r\n" +
                        "Content-Type: text/plain\r\n" +
                        "Connection: close\r\n" +
                        "Content-Length: 17\r\n" +
                        "\r\n" +
                        "Deployment Failed";

        String deployKey = System.getenv("DEPLOY_KEY");


        try (ServerSocket deploymentSocket = new ServerSocket(port)) {
            System.out.println("Server listening on port " + port);

            while (true) {
                String key = "";
                try(Socket socket = deploymentSocket.accept()) {
                    BufferedReader keyReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    String keyReceiver = keyReader.readLine();

                    try{key = keyReceiver.split("key=")[1];
                    key = key.split(" ")[0];}
                    catch (Exception e){
                        System.out.println("No deployment key found");
                    }


                    System.out.println(socket.getInetAddress().getHostAddress());
                    System.out.println("Received deployment request");

                    OutputStream os = socket.getOutputStream();

                    if (key.equals(deployKey)) {
                        deployServer();
                        System.out.println("Deployment Successful");
                        os.write(responseSuccessful.getBytes());
                        os.flush();
                    }
                    else{
                        System.out.println("Server received deployment request with invalid deployment key");
                        os.write(responseFailed.getBytes());
                        os.flush();
                    }

                }catch (IOException e) {
                    System.out.println("Error handling client: " + e.getMessage());
                }
            }
        }catch (IOException e) {
            System.out.println("Error listening on port " + port);
        }

    }

    public static void deployServer(){
        ProcessBuilder processBuilder = new ProcessBuilder("schtasks", "/run", "/tn", "WebsiteDeployment");
        try {
            processBuilder.start();
            System.out.println("Deployment started");
        }catch (Exception e){
            System.out.println("Error starting server");
        }

    }
}
