package za.co.kemtech.metrics.monitor.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import za.co.kemtech.metrics.monitor.model.Condition;

import java.util.concurrent.TimeUnit;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class Threshold {
    private String uuid;
    private String name;
    private String destinationName;
    private String mqPropertyName;
    private Condition condition;
    private TimeUnit timeUnit;
    private long value;

    public boolean isTime() {
        return timeUnit != null;
    }
}

