package property;

import java.util.Enumeration;
import java.util.Properties;

/**
 * Created by 1002718 on 2016. 8. 15..
 */
public class KafkaProperties {

    private Properties defaultProperties = null;
    private Properties currentDefaultProperties = null;
    private Properties runningProperties = null;

    public KafkaProperties(Properties properties) {
        defaultProperties = new Properties();
        currentDefaultProperties = new Properties();
        runningProperties = new Properties();


        defaultProperties.setProperty("async.kafka.bootstrap.servers", "localhost:9092");
        defaultProperties.setProperty("async.kafka.acks", "all");
        defaultProperties.setProperty("async.kafka.retries", "0");
        defaultProperties.setProperty("async.kafka.batch.size", "16384");
        defaultProperties.setProperty("async.kafka.linger.ms", "0");
        defaultProperties.setProperty("async.kafka.buffer.memory", "33554432");
        defaultProperties.setProperty("async.kafka.key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        defaultProperties.setProperty("async.kafka.value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        defaultProperties.setProperty("async.kafka.client.id", "");

        runningProperties = properties;
        currentDefaultProperties = defaultProperties;
    }


    public Properties getAll(String prefix) {
        Properties properties = new Properties();
        String prefixLowerCase = prefix.toLowerCase();
        Enumeration names =currentDefaultProperties.propertyNames();
        while(names.hasMoreElements()){
            String key=names.nextElement().toString();

            if (key.startsWith(prefixLowerCase)) {
                properties.setProperty(key, currentDefaultProperties.getProperty(key));
            }
        }

        names =runningProperties.propertyNames();
        while(names.hasMoreElements()){
            String key=names.nextElement().toString();
            if (key.startsWith(prefixLowerCase)) {
                properties.setProperty(key, runningProperties.getProperty(key));
            }
        }
        return properties;
    }
}
