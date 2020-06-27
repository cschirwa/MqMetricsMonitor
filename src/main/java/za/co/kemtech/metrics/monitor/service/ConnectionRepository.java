package za.co.kemtech.metrics.monitor.service;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import za.co.kemtech.metrics.monitor.config.ConnectionData;
import za.co.kemtech.metrics.monitor.model.mq.Queue;
import za.co.kemtech.metrics.monitor.persistance.*;

import java.util.List;


@Slf4j
@Service
@Getter
public class ConnectionRepository {

    @Autowired
    private QueueManagerRepository queueManagerRepository;

    @Autowired
    private BreachRepository breachRepository;

    @Autowired
    private ThresholdRepository thresholdRepository;

    @Cacheable("connections")
    public ConnectionData findByHostAndQueueManager(final String host, final String queueManager) {
        return queueManagerRepository.findByHostnameAndQueueManagerName(host, queueManager).connectionData();
    }

    public void addConnectionData(final ConnectionData connectionData) {
        final QueueManager queueManager = queueManagerRepository.save(new QueueManager(connectionData));
        connectionData.getProperties().getThresholds().forEach(threshold -> thresholdRepository.save(new Threshold(queueManager, threshold)));

    }

    public List<QueueManager> findAll() {
        return queueManagerRepository.findAll();
    }

    public void recordBreach(final ConnectionData connectionData, final Queue queue) {
        if (queue.inBreach()) {
            queue.getBreaches().forEach(breach -> {
                final Threshold threshold = thresholdRepository.findByUuid(breach.getThreshold().getUuid());
                breachRepository.save(new Breach(queue.getName(), threshold, breach.getActualValue()));
                log.info("Recorded breach {}", queue.getBreaches());
            });
        }
    }
}


