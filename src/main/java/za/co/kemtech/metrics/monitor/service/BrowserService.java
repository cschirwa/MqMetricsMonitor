package za.co.kemtech.metrics.monitor.service;

import org.springframework.beans.factory.annotation.Autowired;
import za.co.kemtech.metrics.monitor.config.ConnectionData;
import za.co.kemtech.metrics.monitor.model.mq.Queue;
import za.co.kemtech.metrics.monitor.model.mq.QueueManager;
import za.co.kemtech.metrics.monitor.model.mq.Topic;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class BrowserService {

    @Autowired
    private ConnectionRepository connectionRepository;

    @Autowired
    private MeterRegistry registry;

    public Queue fetchQueueStats(final String host, final String queueManager, final String queueName) {
        final ConnectionData connectionData = connectionRepository.findByHostAndQueueManager(host, queueManager);
        final PCFMessage request = new PCFMessage(MQConstants.MQCMD_INQUIRE_Q_STATUS);
        request.addParameter(MQConstants.MQCA_Q_NAME, queueName);
        request.addParameter(MQConstants.MQIA_Q_TYPE, MQConstants.MQQT_LOCAL);
        final Queue queue = new Queue(queueName, executeCommand(request, connectionData), connectionData.getProperties().thresholdsForDestination(queueName), connectionData.getProperties()
                .multiThresholdForDestination(queueName));
        registry.gauge(String.format("metrics.monitor.mq.%s.%s.MQIA_CURRENT_Q_DEPTH", queueManager, queueName), queue.getQueueDepth());
        connectionRepository.recordBreach(connectionData, queue);
        return queue;
    }

    public Topic fetchTopicStats(final String host, final String queueManager, final String topicName) {
        final ConnectionData connectionData = connectionRepository.findByHostAndQueueManager(host, queueManager);
        final PCFMessage request = new PCFMessage(MQConstants.MQCMD_INQUIRE_TOPIC);
        request.addParameter(MQConstants.MQCA_TOPIC_NAME, topicName);
        return new Topic(topicName, executeCommand(request, connectionData), connectionData.getProperties().thresholdsForDestination(topicName), connectionData.getProperties().getMultiThreshold());
    }

    public Queue fetchTopicStatus(final ConnectionData connectionData, final String topicName, final String topicString) {
        final PCFMessage request = new PCFMessage(MQConstants.MQCMD_INQUIRE_TOPIC_STATUS);
        request.addParameter(MQConstants.MQCA_TOPIC_STRING, topicString);
        return new Queue(topicName, executeCommand(request, connectionData), connectionData.getProperties().getThresholds(), connectionData.getProperties().getMultiThreshold());
    }

    public QueueManager fetchQueues(final String host, final String queueManager) {
        final ConnectionData connectionData = connectionRepository.findByHostAndQueueManager(host, queueManager);
        final PCFMessage request = new PCFMessage(MQConstants.MQCMD_INQUIRE_Q_NAMES);
        request.addParameter(MQConstants.MQCA_Q_NAME, "*");
        final Pattern systemQueues = Pattern.compile("SYSTEM\\..*||AMQ\\..*");
        final Map<String, Object> executeCommand = executeCommand(request, connectionData);
        final String[] queNames = (String[]) executeCommand.get("MQCACF_Q_NAMES");
        return new QueueManager(host, queueManager, Arrays.asList(queNames).stream().filter(queueName -> !systemQueues.matcher(queueName).matches()).map(queueName -> fetchQueueStats(host,
                queueManager, queueName.trim()))
                .sorted()
                .collect(Collectors
                        .toList()), null, executeCommand);
    }

    public QueueManager fetchTopics(final String host, final String queueManager) {
        final ConnectionData connectionData = connectionRepository.findByHostAndQueueManager(host, queueManager);
        final PCFMessage request = new PCFMessage(MQConstants.MQCMD_INQUIRE_TOPIC_NAMES);
        request.addParameter(MQConstants.MQCA_TOPIC_NAME, "*");
        final Pattern systemQueues = Pattern.compile("SYSTEM\\..*||AMQ\\..*");
        final Map<String, Object> executeCommand = executeCommand(request, connectionData);
        final String[] topicNames = (String[]) executeCommand.get("MQCACF_TOPIC_NAMES");
        final List<Topic> topics = Arrays.asList(topicNames).stream().filter(topicName -> !systemQueues.matcher(topicName).matches()).map(topicName -> fetchTopicStats(host,
                queueManager, topicName.trim()))
                .sorted()
                .collect(Collectors
                        .toList());
        return new QueueManager(host, queueManager, null, topics, executeCommand);
    }

    public QueueManager fetchQueueManagerStatus(final String host, final String queueManager) {
        final ConnectionData connectionData = connectionRepository.findByHostAndQueueManager(host, queueManager);
        final PCFMessage request = new PCFMessage(MQConstants.MQCMD_INQUIRE_Q_MGR);
        return new QueueManager(host, queueManager, null, null, executeCommand(request, connectionData));
    }

    public List<QueueManager> queueManagers() {
        return connectionRepository.findAll().stream().map(qm -> new QueueManager(qm.getHostname(), qm.getQueueManagerName(), null, null, null)).collect(Collectors.toList());
    }

    private Map<String, Object> executeCommand(final PCFMessage request, final ConnectionData connectionData) {
        try {
            final PCFMessageAgent pcfMessageAgent = connectionData.createAgent();
            final PCFMessage[] pcfMessages = pcfMessageAgent.send(request);
            final Map<String, Object> properties = new HashMap<>();
            for (final PCFMessage pcfMessage : pcfMessages) {
                final Enumeration<PCFParameter> parameters = pcfMessage.getParameters();
                while (parameters.hasMoreElements()) {
                    final PCFParameter nextElement = parameters.nextElement();
                    final Object parameterValue = pcfMessage.getParameterValue(nextElement.getParameter());
                    if (nextElement.getParameterName() != null) {
                        properties.put(nextElement.getParameterName(), parameterValue);
                    }
                }
            }
            pcfMessageAgent.disconnect();
            return properties;
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }


