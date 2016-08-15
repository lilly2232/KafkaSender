package api;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import queue.MemoryQueue;
import sender.KafkaSender;
import tool.Tools;

import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by 1002718 on 2016. 8. 15..
 */
public class MessageSender {
    private final Logger logger = LoggerFactory.getLogger(MessageSender.class);
    private static MessageSender instance = null;
    private ScheduledExecutorService senderExecutorService;
    private MemoryQueue queue;
    private KafkaSender kafkaSender;

    private MessageSender(Properties properties) throws Exception {
        queue = new MemoryQueue();
        try {
            kafkaSender = new KafkaSender(queue, properties);
        } catch (Exception e) {
            logger.error("cannot create Sender: ", e);
            throw e;
        }

        senderExecutorService = Executors.newSingleThreadScheduledExecutor();
        senderExecutorService.scheduleWithFixedDelay(kafkaSender, 100, 10, TimeUnit.MILLISECONDS);
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                logger.info("Sender is closing");
                senderExecutorService.shutdown();
                logger.info("Sender is closed");
            }
        });

    }

    public static synchronized MessageSender getInstance(Properties properties) throws Exception {
        if (null == instance) {
            instance = new MessageSender(properties);
        }
        return instance;
    }

    public boolean send(String topic, String message) {
        if (queue.enqueue(Tools.stringToJson(topic, message)))
            return true;
        return false;
    }

    public void close(boolean waitSendAll) {
        if (waitSendAll) {
            logger.info("Current queue size is [{}]. Waiting until sending all data to Kafka...", queue.size());
            try {
                while (queue.size() > 0) {
                    Thread.sleep(1000);
                    logger.info("Current queue size is [{}]", queue.size());
                }
                logger.info("Queue is empty");
            } catch (InterruptedException e) {
                logger.error("closing error", e);
            }
        }

        logger.info("Logger is closing...");
        if (null != senderExecutorService && !senderExecutorService.isShutdown()) {
            logger.info("Shutdown log sender executor");
            senderExecutorService.shutdown();
        }

        if (null != queue) {
            queue.close();
        }

        if (null != kafkaSender) {
            kafkaSender.close();
        }
        instance = null;
        logger.info("Logger closed.");
    }
}
