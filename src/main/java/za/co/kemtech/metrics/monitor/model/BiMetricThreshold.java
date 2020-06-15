package za.co.kemtech.metrics.monitor.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BiMetricThreshold {
    private String name;
    private Threshold first;
    private Threshold second;
    private Operand operand;

}
