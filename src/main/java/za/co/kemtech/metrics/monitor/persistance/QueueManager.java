package za.co.kemtech.metrics.monitor.persistance;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import za.co.kemtech.metrics.monitor.config.ConnectionData;
import za.co.kemtech.metrics.monitor.model.mq.MqConnectionProperties;

import javax.persistence.*;

import java.util.List;
import java.util.stream.Collectors;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class QueueManager {

    public final String SEQUENCE_NAME = "mm_queuemanager_seq";
    @Id
    @GeneratedValue(generator = SEQUENCE_NAME, strategy = SEQUENCE)
    @SequenceGenerator(name = SEQUENCE_NAME, sequenceName = SEQUENCE_NAME, initialValue = 1, allocationSize = 1)
    @JsonIgnore
    private Long id;

    private String name;
    private String queueManagerName;
    private String monitorFrequency;
    private String hostname;
    private int port;
    private String channel;
    private String userName;
    private String password;
    private int transportType;

    public QueueManager(final ConnectionData connectionData) {
        this.name = connectionData.getProperties().getName();
        this.queueManagerName = connectionData.getProperties().getQueueManager();
        this.monitorFrequency = connectionData.getProperties().getMonitorFrequency();
        this.hostname = connectionData.getProperties().getHostname();
        this.port = connectionData.getProperties().getPort();
        this.channel = connectionData.getProperties().getChannel();
        this.userName = connectionData.getProperties().getUserName();
        this.password = connectionData.getProperties().getPassword();
        this.transportType = connectionData.getProperties().getTransportType();
    }

    public ConnectionData connectionData() {
        final MqConnectionProperties properties = new MqConnectionProperties();
        properties.setChannel(channel);
        properties.setHostname(hostname);
        properties.setMonitorFrequency(monitorFrequency);
        properties.setName(name);
        properties.setPassword(password);
        properties.setPort(port);
        properties.setQueueManager(queueManagerName);
        properties.setTransportType(transportType);
        properties.setUserName(userName);
        final List<za.co.kemtech.metrics.monitor.model.Threshold> list = thresholds.stream().map(threshold -> new za.co.kemtech.metrics.monitor.model.Threshold(threshold.getUuid(), threshold
                .getName(), threshold
                .getDestinationName(), threshold
                .getMqPropertyName(), threshold.getCondition(), threshold
                .getTimeUnit(), threshold.getValue())).collect(Collectors.toList());
        properties.setThresholds(list);
        return new ConnectionData(properties);

    }

    @OneToMany(mappedBy = "queueManager", fetch = FetchType.EAGER)
    private List<Threshold> thresholds;

    @OneToMany(mappedBy = "queueManager")
    private List<BiMetricThreshold> biMetricThreshold;

}
