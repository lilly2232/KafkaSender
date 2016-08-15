package queue;


import java.util.HashMap;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by 1002718 on 2016. 8. 15..
 */
public class MemoryQueue {
    LinkedBlockingQueue<HashMap<String,String>> queue;


    public MemoryQueue(){
        queue=new LinkedBlockingQueue<HashMap<String,String>>();
    }

    public boolean enqueue(){
        return true;
    }
    public boolean dequeue(){

        return false;
    }

}
