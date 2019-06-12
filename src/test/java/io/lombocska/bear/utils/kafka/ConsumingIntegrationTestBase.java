package io.lombocska.bear.utils.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.After;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;

import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertNotNull;

/**
 * Base test class  with autowired dependencies using {@link ConsumingIntegrationTestHelper} to create consumers and receive kafka messages.
 */
@Slf4j
public class ConsumingIntegrationTestBase {

    @Autowired
    protected KafkaProperties kafkaProperties;

    @Autowired
    protected ObjectMapper objectMapper;

    protected ConsumingIntegrationTestHelper testHelper;

    @Before
    public final void initialize(){
        testHelper = new ConsumingIntegrationTestHelper(kafkaProperties);
    }

    @After
    public void tearDown(){
        testHelper.tearDown();
    }

    protected void setUpConsumer(String topicToListen, String dlqTopicName) {
        testHelper.setUpConsumer(topicToListen, dlqTopicName);
    }

    protected LinkedBlockingQueue<ConsumerRecord<String, byte[]>> getTopicRecords(final String topic) {
        return testHelper.getTopicRecords(topic);
    }

    protected ConsumerRecord<String, byte[]> pollRecord(final String topic, long timeout, TimeUnit timeUnit) throws InterruptedException {
        return getTopicRecords(topic).poll(timeout, timeUnit);
    }

    protected ConsumerRecord<String, byte[]> pollRecord(final String topic) throws InterruptedException {
        return pollRecord(topic, defaultTimeout(), defaultTimeoutTimeUnit());
    }

    protected <T> T pollValue(Class<T> valueType, final String topic, long timeout, TimeUnit timeUnit) throws InterruptedException, IOException {
        ConsumerRecord<String, byte[]> consumerRecord = getTopicRecords(topic).poll(timeout, timeUnit);
        assertNotNull(String.format("No record received on topic: %s", topic), consumerRecord);
        return objectMapper.readValue(consumerRecord.value(), valueType);
    }

    protected <T> T pollValue(Class<T> valueType, final String topic) throws IOException, InterruptedException {
        return pollValue(valueType, topic, defaultTimeout(), defaultTimeoutTimeUnit());
    }

    protected long defaultTimeout() {
        return 1000L;
    }

    protected TimeUnit defaultTimeoutTimeUnit() {
        return TimeUnit.SECONDS;
    }
}
