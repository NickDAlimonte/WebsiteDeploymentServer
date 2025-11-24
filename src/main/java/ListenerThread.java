public class ListenerThread implements Runnable {

    DeploymentListener listener;

    public ListenerThread(DeploymentListener listener){
        this.listener = listener;
    }

    @Override
    public void run(){
        this.listener.listener();
        System.out.println("New Thread created");
    }
}
