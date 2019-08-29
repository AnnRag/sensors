package responses;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import util.TimestampDesirializer;

import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
public class SensorValuesForTime {
    String sensor;
    @JsonSerialize(using = TimestampDesirializer.class)
    Timestamp timeFrom;
    Timestamp timeTo;
    List<Integer> values;
}
