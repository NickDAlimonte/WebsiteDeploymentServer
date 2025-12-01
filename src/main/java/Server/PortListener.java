package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class PortListener {
    private final int port;
    String firstLine;

    private final ExecutorService executor =Executors.newFixedThreadPool(4);


    public PortListener(int port){
        this.port = port;
    }


    public void listener() {



        try {
            this.serverSocket = new ServerSocket(port);
            System.out.println("Server listening on port: " + port);

            while (running) {
                Socket socket = serverSocket.accept();

//                BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//                firstLine = br.readLine();
//                if(firstLine.toLowerCase().contains("favicon")){
//                    System.out.println("Ignoring favicon request");
//                    socket.close();
//                }


                System.out.println("Accepted connection from: " + socket.getInetAddress().getHostName());
                executor.submit(() -> {

                    try{
                        handleClient(socket);
                    }catch(Exception e){
                        System.out.println("Error handling client connection: " + e.getMessage());
                    }

                });
            }
        }catch (IOException e) {
            System.out.println("Error listening on port " + port);
        }

    }

    public abstract void handleClient(Socket socket) throws IOException;

    private volatile boolean running = true;
    private ServerSocket serverSocket;


    public void stop(){
        running = false;

        try{
            if(this.serverSocket != null && !this.serverSocket.isClosed()){
                this.serverSocket.close();
            }
        }catch(IOException ignored){}

        executor.shutdown();
    }
}
