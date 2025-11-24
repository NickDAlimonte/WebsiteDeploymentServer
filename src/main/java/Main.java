public class Main {
    public static void main(String[] args) {
        DeploymentListener deployment = new DeploymentListener();
        ListenerThread deploymentThread = new ListenerThread(deployment);

        Thread j = new Thread(deploymentThread);
        j.start();


    }

}
