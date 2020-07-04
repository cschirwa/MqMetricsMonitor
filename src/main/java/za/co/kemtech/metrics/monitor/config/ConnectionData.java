package za.co.kemtech.metrics.monitor.config;

import com.ibm.mq.MQC;
import com.ibm.mq.MQException;
import com.ibm.mq.MQQueueManager;
import com.ibm.mq.headers.MQDataException;
import com.ibm.mq.headers.pcf.PCFMessageAgent;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import za.co.kemtech.metrics.monitor.model.mq.MqConnectionProperties;

import java.util.Hashtable;

@Slf4j
@Getter
@Setter
@AllArgsConstructor
public class ConnectionData {

    private final MqConnectionProperties properties;
    private MQQueueManager queueManager;

    public ConnectionData(MqConnectionProperties properties) {
        this.properties = properties;
    }
    public PCFMessageAgent createAgent() throws MQException, MQDataException {
        if(queueManager==null || !queueManager.isConnected()){
            log.info("Created connection to {}", properties.getQueueManager());
            final Hashtable<String, Object> mqProps = new Hashtable<>();
            mqProps.put("hostname", properties.getHostname());
            mqProps.put("port", properties.getPort());
            mqProps.put("channel", properties.getChannel());
            mqProps.put("userID", properties.getUserName());
            mqProps.put("password", properties.getPassword());
            mqProps.put(MQC.TRANSPORT_PROPERTY, MQC.TRANSPORT_MQSERIES_CLIENT);
            queueManager = new MQQueueManager(properties.getQueueManager(), mqProps);

        }
        return new PCFMessageAgent(queueManager);
    }
}
