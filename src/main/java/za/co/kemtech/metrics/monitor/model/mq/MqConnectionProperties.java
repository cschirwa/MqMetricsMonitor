package za.co.kemtech.metrics.monitor.model.mq;

import lombok.Getter;
import lombok.Setter;
import za.co.kemtech.metrics.monitor.model.BiMetricThreshold;
import za.co.kemtech.metrics.monitor.model.Threshold;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class MqConnectionProperties {
        private String name;
        private String monitorFrequency;
        private String hostname;
        private int port;
        private String queueManager;
        private String channel;
        private String userName;
        private String password;
        private int transportType;
        private List<Threshold> thresholds = new ArrayList<>();
        private List<BiMetricThreshold> multiThreshold = new ArrayList<>();

        public List<Threshold> thresholdsForDestination(final String destinationName) {
            return thresholds.stream().filter(threshold -> threshold.getDestinationName().equals(destinationName))
                    .collect(Collectors.toList());
        }

        public List<BiMetricThreshold> multiThresholdForDestination(final String destinationName) {
            return multiThreshold.stream().filter(threshold -> (threshold.getFirst().getDestinationName().equals(destinationName) || threshold.getSecond().getDestinationName().equals(destinationName)))
                    .collect(Collectors.toList());
        }

    }
