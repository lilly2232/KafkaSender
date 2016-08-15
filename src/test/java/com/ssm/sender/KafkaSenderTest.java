package com.ssm.sender;

import api.MessageSender;
import org.junit.Test;

import java.util.Properties;

/**
 * Created by byeongsukang on 2016. 8. 15..
 */
public class KafkaSenderTest {

    MessageSender messageSender;

    @Test
    public void senderTest() throws Exception {
        Properties properties = new Properties();
        messageSender = MessageSender.getInstance(properties);
        for(int i = 0 ;i<100;i++) {
            messageSender.send("test-topic", "test message");
        }
        Thread.sleep(3000);
        messageSender.close(true);

    }
}
