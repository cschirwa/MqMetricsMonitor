package za.co.kemtech.metrics.monitor.model.mq;

import lombok.Getter;
import lombok.NoArgsConstructor;
import za.co.kemtech.metrics.monitor.model.BiMetricThreshold;
import za.co.kemtech.metrics.monitor.model.Threshold;

import java.util.List;
import java.util.Map;

@Getter
@NoArgsConstructor
public class Queue extends Destination implements Comparable<Queue> {

    public Queue(final String name, final Map<String, Object> properties, final List<Threshold> thresholds, final List<BiMetricThreshold> multiThreshold) {
        super(name, properties, thresholds, multiThreshold);
    }

    public int getQueueDepth() {
        return Integer.parseInt(getProperties().get("MQIA_CURRENT_Q_DEPTH").toString());
    }

    public String getLastPutDateTime() {
        final String lastPutTime = getProperties().get("MQCACF_LAST_PUT_TIME").toString();
        final String lastPutDate = getProperties().get("MQCACF_LAST_PUT_DATE").toString();
        return String.format("%s %s", lastPutDate, lastPutTime);
    }

    public boolean inBreach() {
        return !getBreaches().isEmpty();
    }

    @Override
    public int compareTo(final Queue o) {
        return Integer.valueOf(o.getQueueDepth()).compareTo(getQueueDepth());
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final Queue other = (Queue) obj;
        if (getName() == null) {
            if (other.getName() != null)
                return false;
        } else if (!getName().equals(other.getName()))
            return false;
        return true;
    }


}
