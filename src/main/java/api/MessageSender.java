package api;


import queue.MemoryQueue;

/**
 * Created by 1002718 on 2016. 8. 15..
 */
public class MessageSender {
    private static MessageSender instance = null;

    private MessageSender(){

        //큐 생성
        MemoryQueue queue = new MemoryQueue();
        //센더 생성

    }

    public static synchronized MessageSender getInstance() throws Exception {
        if (null == instance) {
            instance = new MessageSender();
        }
        return instance;
    }

    public boolean send(String topic, String message){




        //메모리큐에 인큐한다

        //리턴한다.

        return true;
    }
}
