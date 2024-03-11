package inc.temp.right.always.temperatureanomaliesanalyzer.dto;

import inc.temp.right.always.temperaturemodel.TemperatureMeasurement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MeasurementsTimeWindowAnalyzer implements Serializable {
    private List<TemperatureMeasurement> measurements = new ArrayList<>();
    private double sum = 0.0;
    private int count = 0;
    private double avg = 0.0;
    private List<TemperatureMeasurement> anomalies = new ArrayList<>();

    @Override
    public String toString() {
        return "MeasurementsTimeWindowAnalyzer{" +
            "measurements=[" + measurements.stream().map(tm -> Double.toString(tm.getTemperature())).collect(Collectors.joining(",")) + "]" +
            ", sum=" + sum +
            ", count=" + count +
            ", avg=" + avg +
            ", anomalies=[" + anomalies.stream().map(tm -> Double.toString(tm.getTemperature())).collect(Collectors.joining(",")) + "]" +
            '}';
    }
}
