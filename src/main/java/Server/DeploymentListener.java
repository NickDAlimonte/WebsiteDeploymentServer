package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

public class DeploymentListener extends PortListener {


    public DeploymentListener(){
        super(24001);
    }

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


    public void handleClient(Socket socket) throws IOException {

        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        OutputStream os = socket.getOutputStream();

        String deploymentKey = firstLine;


        try{
            if (deploymentKey == null || !deploymentKey.startsWith("GET /?key=")){
                os.write(responseFailed.getBytes());
                return;
            }

            if (deploymentKey.toLowerCase().contains("key=")) {
                if (deploymentKey.split("key=")[1].split(" HTTP")[0].equals(deployKey)) {
                    deployServer();
                    os.write(responseSuccessful.getBytes());
                    System.out.println("Deployment successful");
                }
            }
            else {
                os.write(responseFailed.getBytes());
                System.out.println("Deployment Failed");
            }
        }catch(Exception e){
            System.out.println("Error: "+e.getMessage());
        }
    }



    public static void deployServer(){
        ProcessBuilder processBuilder = new ProcessBuilder("schtasks", "/run", "/tn", "WebsiteDeployment");
        try {
            processBuilder.start();
            System.out.println("\nstarted");
        }catch (Exception e){
            System.out.println("\nError starting deployment");
        }

    }

}
