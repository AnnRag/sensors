package responses;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import util.DoubleDesirializer;

@Getter
@Setter
@EqualsAndHashCode
public class AvgValuesForObject {

    String object;
    @JsonSerialize(using = DoubleDesirializer.class)
    Double avgValue;
}
