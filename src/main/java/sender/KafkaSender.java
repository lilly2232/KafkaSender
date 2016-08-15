package sender;

import org.apache.kafka.clients.producer.*;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import property.KafkaProperties;
import queue.MemoryQueue;

import java.util.Enumeration;
import java.util.Properties;


public class KafkaSender implements Runnable {
    private final Logger logger = LoggerFactory.getLogger(KafkaSender.class);
    private MemoryQueue queue;
    private Producer<String, String> kafkaProducer;
    private final static String settingPrefix = "async.kafka.";

    public KafkaSender(MemoryQueue queue, Properties runningProperties) throws Exception {
        this.queue = queue;

        KafkaProperties kafkaProperties = new KafkaProperties(runningProperties);
        Properties properties = kafkaProperties.getAll(settingPrefix);
        Properties producerProperties = new Properties();

        Enumeration names = properties.propertyNames();
        while (names.hasMoreElements()) {
            String key = names.nextElement().toString();
            String asyncKey = key.replace(settingPrefix, "");
            String asyncValue = properties.getProperty(key);
            logger.info("Kafka Async Properties. Key : " + asyncKey + ", Value : " + asyncValue);
            producerProperties.setProperty(asyncKey, asyncValue);
        }

        kafkaProducer = new KafkaProducer<String, String>(producerProperties);

    }

    private void kafkaSend(final String data) {
        try {
            JSONObject jsonObj = new JSONObject(data);
            String topic = "";
            String message = "";
            if (jsonObj.has("topic")) {
                topic = jsonObj.get("topic").toString();
            }
            if (jsonObj.has("message")) {
                message = jsonObj.get("message").toString();
            }

            if (!topic.equals("")) {
                kafkaProducer.send(new ProducerRecord<String, String>(topic, message), new Callback() {
                    public void onCompletion(RecordMetadata metadata, Exception e) {
                        if (e != null) {
                            queue.enqueue(data);
                            logger.error("Send Error in Callback : ", e.getMessage());
                        }
                    }
                });
                logger.debug("sent successfully: [{}]", data);
            }
        } catch (Throwable t) {
            logger.error("Wrong log. Ignore: " + data, new Exception());
        }
    }

    public void close() {
        if (null != kafkaProducer) {
            kafkaProducer.close();
            kafkaProducer = null;
        }
        logger.info("KafkaProducer is closed");
    }

    public void run() {
        while (queue.size() != 0) {
            kafkaSend(queue.dequeue());
        }
    }
}
