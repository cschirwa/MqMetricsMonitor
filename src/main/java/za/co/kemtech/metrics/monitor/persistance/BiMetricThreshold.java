package za.co.kemtech.metrics.monitor.persistance;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Entity;
import org.springframework.data.annotation.Id;
import za.co.kemtech.metrics.monitor.model.Operand;
import za.co.kemtech.metrics.monitor.model.Threshold;

import javax.persistence.*;
import java.util.UUID;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity
@Getter
@Setter
public class BiMetricThreshold {

    public static final String SEQUENCE_NAME = "mm_bm_thresh_seq";

    @Id
    @GeneratedValue(generator = SEQUENCE_NAME, strategy = SEQUENCE)
    @SequenceGenerator(name = SEQUENCE_NAME, sequenceName = SEQUENCE_NAME, initialValue = 1, allocationSize = 1)
    @JsonIgnore
    private Long id;

    private final String uuid = UUID.randomUUID().toString();

    private String name;

    @ManyToOne
    private QueueManager queueManager;

    @OneToOne
    private Threshold first;

    @Enumerated(EnumType.STRING)
    private Operand operand;

    @OneToOne
    private Threshold second;
}

