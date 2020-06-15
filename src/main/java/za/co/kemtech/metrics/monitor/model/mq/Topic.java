package za.co.kemtech.metrics.monitor.model.mq;

import lombok.Getter;
import lombok.NoArgsConstructor;
import za.co.kemtech.metrics.monitor.model.BiMetricThreshold;
import za.co.kemtech.metrics.monitor.model.Threshold;

import java.util.List;
import java.util.Map;

@Getter
@NoArgsConstructor
public class Topic extends Destination implements Comparable<Topic> {
    public Topic(final String name, final Map<String, Object> properties, final List<Threshold> thresholds, final List<BiMetricThreshold> multiThreshold) {
        super(name, properties, thresholds, multiThreshold);
    }

    @Override
    public int compareTo(final Topic o) {
        if (null != o.getBreaches()) {
            return 1;
        }
        return 0;
    }
}

