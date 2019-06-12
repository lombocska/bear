package io.lombocska.bear.utils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.awaitility.Awaitility;
import org.springframework.core.io.ClassPathResource;
import org.springframework.kafka.listener.MessageListenerContainer;

import java.io.IOException;
import java.nio.file.Files;
import java.util.concurrent.TimeUnit;

@Slf4j
@UtilityClass
public class TestUtils {

    public static byte[] loadJsonFromClasspath(String path) throws IOException {
        ClassPathResource classPathResource = new ClassPathResource(path);
        return Files.readAllBytes(classPathResource.getFile().toPath());
    }

    public static void waitForKafkaListenerPartitionAssignment(final MessageListenerContainer container) {
        while (container.getAssignedPartitions() == null || container.getAssignedPartitions().size() < 1) {
            log.info("Waiting for test Kafka consumer partition assignment...");

            Awaitility.await().atMost(4, TimeUnit.SECONDS)
                    .until(() -> container.getAssignedPartitions() != null && container.getAssignedPartitions().size() >= 1);
        }
    }

}
