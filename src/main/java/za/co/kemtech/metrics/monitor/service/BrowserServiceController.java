package za.co.kemtech.metrics.monitor.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import za.co.kemtech.metrics.monitor.model.mq.Queue;
import za.co.kemtech.metrics.monitor.model.mq.QueueManager;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
public class BrowserServiceController {

    @Autowired
    private BrowserService browserService;

    @RequestMapping("/hosts/{host}/queue-managers/{queueManager}/queues/{queueName}")
    public Queue fetchQueueStats(@PathVariable("host") final String host, @PathVariable("queueManager") final String queueManager, @PathVariable("queueName") final String queueName) {
        return browserService.fetchQueueStats(host, queueManager, queueName);
    }

    @RequestMapping("/hosts/{host}/queue-managers/{queueManager}")
    public QueueManager queueManagerStatus(@PathVariable("host") final String host, @PathVariable("queueManager") final String queueManager) {
        return browserService.fetchQueueManagerStatus(host, queueManager);
    }

    @RequestMapping("/hosts/{host}/queue-managers/{queueManager}/queues")
    public QueueManager fetchQueues(@PathVariable("host") final String host, @PathVariable("queueManager") final String queueManager) {
        return browserService.fetchQueues(host, queueManager);
    }

    @RequestMapping("/hosts/{host}/queue-managers/{queueManager}/topics")
    public QueueManager fetchTopics(@PathVariable("host") final String host, @PathVariable("queueManager") final String queueManager) {
        return browserService.fetchTopics(host, queueManager);
    }

    @RequestMapping("/hosts")
    public List<QueueManager> fetchQueueManagers() {
        return browserService.queueManagers();
    }
}
