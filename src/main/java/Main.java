import Database.AccountCreator;
import Database.DBConnector;
import Server.AuthListener;
import Server.DeploymentListener;
import Server.ListenerThread;

public class Main {
    public static void main(String[] args) {
        DeploymentListener deployment = new DeploymentListener();
        AuthListener auth = new AuthListener();

        ListenerThread deploymentThread = new ListenerThread(deployment);
        ListenerThread authThread = new ListenerThread(auth);


        Thread authenticationThread = new Thread(authThread);
        Thread deployThread = new Thread(deploymentThread);
        deployThread.start();
        authenticationThread.start();

        AccountCreator.CreateAccount("N3i1c9k5", "Nick1995", "Nickolas.Dalimonte@gmail.com");


        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutting down");
            deployment.stop();
            auth.stop();
        }));


    }

}
