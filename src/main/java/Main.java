import Server.AuthListener;
import Server.DeploymentListener;
import Server.ListenerThread;

public class Main {
    public static void main(String[] args) {
        DeploymentListener deployment = new DeploymentListener();
        AuthListener auth = new AuthListener();

        ListenerThread deploymentThread = new ListenerThread(deployment);
        ListenerThread authThread = new ListenerThread(auth);


        //Thread creation and deployment
        Thread authenticationThread = new Thread(authThread);
        Thread deployThread = new Thread(deploymentThread);
        deployThread.start();
        authenticationThread.start();


        //Graceful shutdown
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutting down");
            deployment.stop();
            auth.stop();
        }));




    }

}
