import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class PortListener {
    private final int port;
    private final ExecutorService executor =Executors.newFixedThreadPool(4);


    public PortListener(int port){
        this.port = port;
    }


    public void listener() {

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server listening on port: " + port);

            while (true) {
                Socket socket = serverSocket.accept();
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
}
