package za.co.kemtech.metrics.monitor.model.mq;

import lombok.Getter;

import javax.jms.Topic;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Getter
public class QueueManager {

    private final String hostName;
    private final String queueManagerName;
    private final List<Queue> queues;
    private final List<Topic> topics;
    private final Map<String, Object> properties;

    public QueueManager(final String hostName, final String queueManagerName, final List<Queue> queues, final List<Topic> topics, final Map<String, Object> properties) {
        this.hostName = hostName;
        this.queueManagerName = queueManagerName;
        this.queues = queues;
        this.topics = topics;
        this.properties = properties;
        Optional.ofNullable(this.queues).ifPresent(list -> Collections.sort(queues));
    }

}
