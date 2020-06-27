package za.co.kemtech.metrics.monitor.persistance;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import za.co.kemtech.metrics.monitor.model.Threshold;

import javax.persistence.*;

import java.util.UUID;

import static javax.persistence.GenerationType.SEQUENCE;

@NoArgsConstructor
@Getter
@Entity
public class Breach {

    public static final String SEQUENCE_NAME = "mm_breach_seq";
    @Id
    @GeneratedValue(generator = SEQUENCE_NAME, strategy = SEQUENCE)
    @SequenceGenerator(name = SEQUENCE_NAME, sequenceName = SEQUENCE_NAME, initialValue = 1, allocationSize = 1)
    @JsonIgnore
    private Long id;

    private final String uuid = UUID.randomUUID().toString();

    private String destinationName;
    @ManyToOne
    private Threshold threshold;
    private long actualValue;

    public Breach(final String destinationName, final Threshold threshold, final long actualValue) {
        this.threshold = threshold;
        this.actualValue = actualValue;
        this.destinationName = destinationName;
    }

}

