package za.co.kemtech.metrics.monitor.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import za.co.kemtech.metrics.monitor.model.mq.MqConnectionProperties;

import java.util.List;

@Getter
@Setter
@ConfigurationProperties("browser")
public class MessageBrowserProperties {

    private String metricsCollectionFrequency;
    private List<MqConnectionProperties> monitored;

}
