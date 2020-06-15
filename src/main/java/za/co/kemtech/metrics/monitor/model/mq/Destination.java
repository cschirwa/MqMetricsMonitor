package za.co.kemtech.metrics.monitor.model.mq;

import lombok.Getter;
import lombok.NoArgsConstructor;
import za.co.kemtech.metrics.monitor.model.BiMetricThreshold;
import za.co.kemtech.metrics.monitor.model.Breach;
import za.co.kemtech.metrics.monitor.model.Threshold;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
@Getter
public class Destination {
    private String name;
    private Map<String, Object> properties;
    private List<Threshold> thresholds = new ArrayList<>();
    private List<BiMetricThreshold> multiThresholds = new ArrayList<>();
    private final List<Breach> breaches = new ArrayList<>();

    public Destination(final String name, final Map<String, Object> properties, final List<Threshold> thresholds, final List<BiMetricThreshold> multiThresholds) {
        this.name = name;
        this.properties = properties;
        this.thresholds = thresholds;
        this.multiThresholds = multiThresholds;
        checkThresholds();
    }

    private void checkThresholds() {
        thresholds.forEach(threshold -> {
            final Object value = properties.get(threshold.getMqPropertyName());
            if(value != null){
                final long numericValue = Long.parseLong(value.toString());
//                final boolean breach = threshold.getCondition().apply(numericValue, Long.valueOf(threshold.getValue()))
//                if(breach){
//                    breaches.add(new Breach(threshold, numericValue));
//                }
            }
        });
    }
}
