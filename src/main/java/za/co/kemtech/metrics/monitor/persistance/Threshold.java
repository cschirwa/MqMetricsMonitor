package za.co.kemtech.metrics.monitor.persistance;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import za.co.kemtech.metrics.monitor.model.Condition;

import javax.persistence.*;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static javax.persistence.GenerationType.SEQUENCE;

@NoArgsConstructor
@Entity
@Getter
@Setter
public class Threshold {

    public static final String SEQUENCE_NAME = "mm_threshold_seq";

    @Id
    @GeneratedValue(generator = SEQUENCE_NAME, strategy = SEQUENCE)
    @SequenceGenerator(name = SEQUENCE_NAME, sequenceName = SEQUENCE_NAME, initialValue = 1, allocationSize = 1)
    @JsonIgnore
    private Long id;

    private final String uuid = UUID.randomUUID().toString();
    private String name;
    private String destinationName;
    private String mqPropertyName;

    @ManyToOne
    private QueueManager queueManager;

    @Enumerated(EnumType.STRING)
    private Condition condition;

    @Enumerated(EnumType.STRING)
    private TimeUnit timeUnit;

    private long value;

    public boolean isTime() {
        return timeUnit != null;
    }

    public Threshold(final QueueManager queueManager, final Threshold domain) {
        this.name = domain.getName();
        this.destinationName = domain.getDestinationName();
        this.mqPropertyName = domain.getMqPropertyName();
        this.condition = domain.getCondition();
        this.timeUnit = domain.getTimeUnit();
        this.value = domain.getValue();
        this.queueManager = queueManager;
    }
}

