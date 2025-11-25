package Server;

public class ListenerThread implements Runnable {

    PortListener listener;

    public ListenerThread(PortListener listener){
        this.listener = listener;
    }

    @Override
    public void run(){
        this.listener.listener();
    }
}
