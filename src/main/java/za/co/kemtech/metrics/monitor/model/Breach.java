package za.co.kemtech.metrics.monitor.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
@AllArgsConstructor
public class Breach {
    private final Threshold threshold;
    private final long actualValue;
}
