import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import postgresql.Sensor;
import postgresql.SensorRepository;
import responses.AvgValuesForObject;
import responses.SensorValuesForTime;

import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@RestController
public class Rest {

    @Autowired
    SensorRepository sensorRepository;

    @CrossOrigin
    @PostMapping("/save")
    public ResponseEntity saveValues(
            @RequestBody Sensor sensor,
            HttpServletResponse response
    ) throws JsonProcessingException{
        sensorRepository.save(sensor);
        response.setHeader("Access-Control-Allow-Origin", "*");
        String res = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT).writeValueAsString(sensor);
        return ResponseEntity.ok(String.format("%s\n", res));
    }

    @CrossOrigin
    @GetMapping("/getValuesForTime")
    public ResponseEntity getValuesForTime(
        String sensor,
        Timestamp timeFrom,
        Timestamp timeTo
    ) throws JsonProcessingException {
        List<Sensor> sensors = sensorRepository.getSensorsForTimeInterval(timeFrom, timeTo, sensor);
        SensorValuesForTime sensorValuesForTime = new SensorValuesForTime();
        sensorValuesForTime.setSensor(sensor);
        sensorValuesForTime.setTimeFrom(timeFrom);
        sensorValuesForTime.setTimeTo(timeTo);
        sensorValuesForTime.setValues(new ArrayList<>());
        sensors.forEach(sensor1 -> {
            sensorValuesForTime.getValues().add(sensor1.getValue());
        });
        String res = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT).writeValueAsString(sensorValuesForTime);
        return ResponseEntity.ok(String.format("%s\n", res));
    }

    @CrossOrigin
    @GetMapping("/getValuesForObject")
    public ResponseEntity getValuesForObject(
            String object
    ) throws JsonProcessingException {
        List<Sensor> sensors = sensorRepository.getSensorsForObject(object);
        String res = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT).writeValueAsString(sensors);
        return ResponseEntity.ok(String.format("%s\n", res));
    }

    @CrossOrigin
    @GetMapping("/getAvgValues")
    public ResponseEntity getAvgValues(
    ) throws JsonProcessingException {
        List<AvgValuesForObject> valuesForObjects = sensorRepository.getAvgValues();
        String res = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT).writeValueAsString(valuesForObjects);
        return ResponseEntity.ok(String.format("%s\n", res));
    }
}
