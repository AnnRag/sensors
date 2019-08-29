package postgresql;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import util.TimestampDesirializer;

@Entity(name = "SENSORS")
@Table(name = "SENSORS")
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
public class Sensor implements Serializable {
    @NotNull
    @Column(updatable = false)
    @Enumerated(EnumType.STRING)
    String object;

    @NotNull
    @Column(updatable = false)
    @Enumerated(EnumType.STRING)
    String sensor;

    @NotNull
    @Column(updatable = false)
    @JsonSerialize(using = TimestampDesirializer.class)
    Timestamp time;

    @NotNull
    @Column(updatable = false)
    Integer value;
}

