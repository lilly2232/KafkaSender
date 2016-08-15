package queue;


import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by 1002718 on 2016. 8. 15..
 */
public class MemoryQueue {
    LinkedBlockingQueue<String> queue;


    public MemoryQueue(){
        queue=new LinkedBlockingQueue<String>();
    }

    public boolean enqueue(String data){
        return queue.add(data);
    }
    public String dequeue(){
        return queue.remove();
    }

    public int size(){
        return queue.size();
    }

    public void close(){

    }
}
