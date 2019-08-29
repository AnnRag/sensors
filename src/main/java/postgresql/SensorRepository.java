package postgresql;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import responses.AvgValuesForObject;

import java.sql.Timestamp;
import java.util.List;

public interface SensorRepository extends JpaRepository<Sensor, Integer> {

    @Query("SELECT s FROM SENSORS s WHERE s.time > ?1 and s.time < ?2 and s.sensor=?3")
    List<Sensor> getSensorsForTimeInterval(Timestamp timeFrom, Timestamp timeTo, String sensor);

    @Query("SELECT ss FROM (select ss, row_number() over(partition by ss.sensor order by time desc) as rn from SENSOR ss where ss.object=?1) t where t.rn = 1")
    List<Sensor> getSensorsForObject(String object);

    @Query("SELECT AVG(ss.value), ss.object FROM (select ss, row_number() over(partition by ss.sensor order by time desc) as rn from SENSOR ss) t where t.rn = 1 GROUP BY ss.object")
    List<AvgValuesForObject> getAvgValues();
}
