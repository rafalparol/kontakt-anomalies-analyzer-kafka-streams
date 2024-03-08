package inc.temp.right.always.temperatureanomaliesanalyzer.dto;

import inc.temp.right.always.temperaturemodel.TemperatureMeasurement;
import lombok.Data;
import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Log4j2
public class MeasurementsObservationsGroupAnalyzer {
    private List<TemperatureMeasurement> measurements = new ArrayList<>();
    private List<TemperatureMeasurement> anomalies = new ArrayList<>();

    @Override
    public String toString() {
        return "MeasurementsObservationsGroupAnalyzer{" +
            "measurements=[" + measurements.stream().map(tm -> Double.toString(tm.getTemperature())).collect(Collectors.joining(",")) + "]" +
            ", anomalies=[" + anomalies.stream().map(tm -> Double.toString(tm.getTemperature())).collect(Collectors.joining(",")) + "]" +
            '}';
    }
}
