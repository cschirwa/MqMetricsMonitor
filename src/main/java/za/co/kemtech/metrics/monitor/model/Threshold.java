package za.co.kemtech.metrics.monitor.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private Long value;
    private TimeUnit timeUnit;

}
