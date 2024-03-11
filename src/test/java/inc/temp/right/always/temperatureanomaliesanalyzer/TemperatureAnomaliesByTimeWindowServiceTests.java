package inc.temp.right.always.temperatureanomaliesanalyzer;

import inc.temp.right.always.temperatureanomaliesanalyzer.dto.MeasurementsTimeWindowAnalyzer;
import inc.temp.right.always.temperatureanomaliesanalyzer.services.TemperatureAnomaliesByTimeWindowService;
import inc.temp.right.always.temperaturemodel.TemperatureMeasurement;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class TemperatureAnomaliesByTimeWindowServiceTests {
    @Test
    public void addTemperatureMeasurement_ManyMeasurements_Test() {
        // Preparing spy

        TemperatureAnomaliesByTimeWindowService temperatureAnomaliesByTimeWindowService = spy(TemperatureAnomaliesByTimeWindowService.class);

        // Preparing input - agg1

        MeasurementsTimeWindowAnalyzer agg1 = new MeasurementsTimeWindowAnalyzer();

        List<TemperatureMeasurement> measurements1 = new ArrayList<>();

        TemperatureMeasurement anomaly1 = new TemperatureMeasurement("room-1", "thermometer-1", Timestamp.valueOf("2024-02-01 00:00:06").toInstant().toEpochMilli(), 35.0);
        TemperatureMeasurement anomaly2 = new TemperatureMeasurement("room-1", "thermometer-1", Timestamp.valueOf("2024-02-01 00:00:11").toInstant().toEpochMilli(), 35.0);

        measurements1.add(new TemperatureMeasurement("room-1", "thermometer-1", Timestamp.valueOf("2024-02-01 00:00:01").toInstant().toEpochMilli(), 20.0));
        measurements1.add(new TemperatureMeasurement("room-1", "thermometer-1", Timestamp.valueOf("2024-02-01 00:00:02").toInstant().toEpochMilli(), 20.0));
        measurements1.add(new TemperatureMeasurement("room-1", "thermometer-1", Timestamp.valueOf("2024-02-01 00:00:03").toInstant().toEpochMilli(), 20.0));
        measurements1.add(new TemperatureMeasurement("room-1", "thermometer-1", Timestamp.valueOf("2024-02-01 00:00:04").toInstant().toEpochMilli(), 20.0));
        measurements1.add(new TemperatureMeasurement("room-1", "thermometer-1", Timestamp.valueOf("2024-02-01 00:00:05").toInstant().toEpochMilli(), 20.0));
        measurements1.add(anomaly1);
        measurements1.add(new TemperatureMeasurement("room-1", "thermometer-1", Timestamp.valueOf("2024-02-01 00:00:07").toInstant().toEpochMilli(), 20.0));
        measurements1.add(new TemperatureMeasurement("room-1", "thermometer-1", Timestamp.valueOf("2024-02-01 00:00:08").toInstant().toEpochMilli(), 20.0));
        measurements1.add(new TemperatureMeasurement("room-1", "thermometer-1", Timestamp.valueOf("2024-02-01 00:00:09").toInstant().toEpochMilli(), 20.0));
        measurements1.add(new TemperatureMeasurement("room-1", "thermometer-1", Timestamp.valueOf("2024-02-01 00:00:10").toInstant().toEpochMilli(), 20.0));

        List<TemperatureMeasurement> anomalies1 = new ArrayList<>();
        anomalies1.add(anomaly1);

        agg1.setMeasurements(measurements1);
        agg1.setAnomalies(anomalies1);
        agg1.setAvg(215.0 / 10.0);
        agg1.setSum(215.0);
        agg1.setCount(10);

        // Preparing output - agg2

        MeasurementsTimeWindowAnalyzer agg2 = new MeasurementsTimeWindowAnalyzer();

        List<TemperatureMeasurement> measurements2 = new ArrayList<>();

        measurements2.add(new TemperatureMeasurement("room-1", "thermometer-1", Timestamp.valueOf("2024-02-01 00:00:01").toInstant().toEpochMilli(), 20.0));
        measurements2.add(new TemperatureMeasurement("room-1", "thermometer-1", Timestamp.valueOf("2024-02-01 00:00:02").toInstant().toEpochMilli(), 20.0));
        measurements2.add(new TemperatureMeasurement("room-1", "thermometer-1", Timestamp.valueOf("2024-02-01 00:00:03").toInstant().toEpochMilli(), 20.0));
        measurements2.add(new TemperatureMeasurement("room-1", "thermometer-1", Timestamp.valueOf("2024-02-01 00:00:04").toInstant().toEpochMilli(), 20.0));
        measurements2.add(new TemperatureMeasurement("room-1", "thermometer-1", Timestamp.valueOf("2024-02-01 00:00:05").toInstant().toEpochMilli(), 20.0));
        measurements2.add(anomaly1);
        measurements2.add(new TemperatureMeasurement("room-1", "thermometer-1", Timestamp.valueOf("2024-02-01 00:00:07").toInstant().toEpochMilli(), 20.0));
        measurements2.add(new TemperatureMeasurement("room-1", "thermometer-1", Timestamp.valueOf("2024-02-01 00:00:08").toInstant().toEpochMilli(), 20.0));
        measurements2.add(new TemperatureMeasurement("room-1", "thermometer-1", Timestamp.valueOf("2024-02-01 00:00:09").toInstant().toEpochMilli(), 20.0));
        measurements2.add(new TemperatureMeasurement("room-1", "thermometer-1", Timestamp.valueOf("2024-02-01 00:00:10").toInstant().toEpochMilli(), 20.0));
        measurements2.add(anomaly2);

        List<TemperatureMeasurement> anomalies2 = new ArrayList<>();
        anomalies2.add(anomaly1);
        anomalies2.add(anomaly2);

        agg2.setMeasurements(measurements2);
        agg2.setAnomalies(anomalies2);
        agg2.setAvg(250.0 / 11.0);
        agg2.setSum(250.0);
        agg2.setCount(11);

        // Intermediate result - agg

        MeasurementsTimeWindowAnalyzer agg3 = new MeasurementsTimeWindowAnalyzer();
        agg3.setMeasurements(agg2.getMeasurements());

        // Mocking spy

        doReturn(agg2).when(temperatureAnomaliesByTimeWindowService).calculateAnomalies(agg3, 5.0);

        // Running

        MeasurementsTimeWindowAnalyzer result = temperatureAnomaliesByTimeWindowService.addTemperatureMeasurement(anomaly2, agg1, 5.0);

        // Verifying and asserting

        verify(temperatureAnomaliesByTimeWindowService, times(1)).calculateAnomalies(agg3, 5.0);
        assertEquals(agg2, result, String.format("Expected result: %s and returned result: %s are not the same when analyzing anomalies.", agg2, result));
    }

    @Test
    public void addTemperatureMeasurement_NoMeasurements_Test() {
        // Preparing spy

        TemperatureAnomaliesByTimeWindowService temperatureAnomaliesByTimeWindowService = spy(TemperatureAnomaliesByTimeWindowService.class);

        // Preparing input - agg1

        MeasurementsTimeWindowAnalyzer agg1 = new MeasurementsTimeWindowAnalyzer();

        List<TemperatureMeasurement> measurements1 = new ArrayList<>();
        List<TemperatureMeasurement> anomalies1 = new ArrayList<>();

        agg1.setMeasurements(measurements1);
        agg1.setAnomalies(anomalies1);
        agg1.setAvg(0.0);
        agg1.setSum(0.0);
        agg1.setCount(0);

        // Preparing output - agg2

        MeasurementsTimeWindowAnalyzer agg2 = new MeasurementsTimeWindowAnalyzer();

        List<TemperatureMeasurement> measurements2 = new ArrayList<>();
        TemperatureMeasurement measurement = new TemperatureMeasurement("room-1", "thermometer-1", Timestamp.valueOf("2024-02-01 00:00:01").toInstant().toEpochMilli(), 20.0);
        measurements2.add(measurement);

        List<TemperatureMeasurement> anomalies2 = new ArrayList<>();

        agg2.setMeasurements(measurements2);
        agg2.setAnomalies(anomalies2);
        agg2.setAvg(20.0 / 1.0);
        agg2.setSum(20.0);
        agg2.setCount(1);

        // Intermediate result - agg

        MeasurementsTimeWindowAnalyzer agg3 = new MeasurementsTimeWindowAnalyzer();
        agg3.setMeasurements(agg2.getMeasurements());

        // Mocking spy

        doReturn(agg2).when(temperatureAnomaliesByTimeWindowService).calculateAnomalies(agg3, 5.0);

        // Running

        MeasurementsTimeWindowAnalyzer result = temperatureAnomaliesByTimeWindowService.addTemperatureMeasurement(measurement, agg1, 5.0);

        // Verifying and asserting

        verify(temperatureAnomaliesByTimeWindowService, times(1)).calculateAnomalies(agg3, 5.0);
        assertEquals(agg2, result, String.format("Expected result: %s and returned result: %s are not the same when analyzing anomalies.", agg2, result));
    }

    @Test
    public void calculateAnomalies_Test() {
        TemperatureAnomaliesByTimeWindowService temperatureAnomaliesByTimeWindowService = new TemperatureAnomaliesByTimeWindowService();

        // Preparing input - agg1

        MeasurementsTimeWindowAnalyzer agg1 = new MeasurementsTimeWindowAnalyzer();

        List<TemperatureMeasurement> measurements1 = new ArrayList<>();

        TemperatureMeasurement anomaly1 = new TemperatureMeasurement("room-1", "thermometer-1", Timestamp.valueOf("2024-02-01 00:00:06").toInstant().toEpochMilli(), 35.0);
        TemperatureMeasurement anomaly2 = new TemperatureMeasurement("room-1", "thermometer-1", Timestamp.valueOf("2024-02-01 00:00:11").toInstant().toEpochMilli(), 35.0);

        measurements1.add(new TemperatureMeasurement("room-1", "thermometer-1", Timestamp.valueOf("2024-02-01 00:00:01").toInstant().toEpochMilli(), 20.0));
        measurements1.add(new TemperatureMeasurement("room-1", "thermometer-1", Timestamp.valueOf("2024-02-01 00:00:02").toInstant().toEpochMilli(), 20.0));
        measurements1.add(new TemperatureMeasurement("room-1", "thermometer-1", Timestamp.valueOf("2024-02-01 00:00:03").toInstant().toEpochMilli(), 20.0));
        measurements1.add(new TemperatureMeasurement("room-1", "thermometer-1", Timestamp.valueOf("2024-02-01 00:00:04").toInstant().toEpochMilli(), 20.0));
        measurements1.add(new TemperatureMeasurement("room-1", "thermometer-1", Timestamp.valueOf("2024-02-01 00:00:05").toInstant().toEpochMilli(), 20.0));
        measurements1.add(anomaly1);
        measurements1.add(new TemperatureMeasurement("room-1", "thermometer-1", Timestamp.valueOf("2024-02-01 00:00:07").toInstant().toEpochMilli(), 20.0));
        measurements1.add(new TemperatureMeasurement("room-1", "thermometer-1", Timestamp.valueOf("2024-02-01 00:00:08").toInstant().toEpochMilli(), 20.0));
        measurements1.add(new TemperatureMeasurement("room-1", "thermometer-1", Timestamp.valueOf("2024-02-01 00:00:09").toInstant().toEpochMilli(), 20.0));
        measurements1.add(new TemperatureMeasurement("room-1", "thermometer-1", Timestamp.valueOf("2024-02-01 00:00:10").toInstant().toEpochMilli(), 20.0));
        measurements1.add(anomaly2);

        List<TemperatureMeasurement> anomalies1 = new ArrayList<>();
        anomalies1.add(anomaly1);

        agg1.setMeasurements(measurements1);
        agg1.setAnomalies(anomalies1);
        agg1.setAvg(215.0 / 10.0);
        agg1.setSum(215.0);
        agg1.setCount(10);

        // Preparing output - agg2

        MeasurementsTimeWindowAnalyzer agg2 = new MeasurementsTimeWindowAnalyzer();

        List<TemperatureMeasurement> measurements2 = new ArrayList<>();

        measurements2.add(new TemperatureMeasurement("room-1", "thermometer-1", Timestamp.valueOf("2024-02-01 00:00:01").toInstant().toEpochMilli(), 20.0));
        measurements2.add(new TemperatureMeasurement("room-1", "thermometer-1", Timestamp.valueOf("2024-02-01 00:00:02").toInstant().toEpochMilli(), 20.0));
        measurements2.add(new TemperatureMeasurement("room-1", "thermometer-1", Timestamp.valueOf("2024-02-01 00:00:03").toInstant().toEpochMilli(), 20.0));
        measurements2.add(new TemperatureMeasurement("room-1", "thermometer-1", Timestamp.valueOf("2024-02-01 00:00:04").toInstant().toEpochMilli(), 20.0));
        measurements2.add(new TemperatureMeasurement("room-1", "thermometer-1", Timestamp.valueOf("2024-02-01 00:00:05").toInstant().toEpochMilli(), 20.0));
        measurements2.add(anomaly1);
        measurements2.add(new TemperatureMeasurement("room-1", "thermometer-1", Timestamp.valueOf("2024-02-01 00:00:07").toInstant().toEpochMilli(), 20.0));
        measurements2.add(new TemperatureMeasurement("room-1", "thermometer-1", Timestamp.valueOf("2024-02-01 00:00:08").toInstant().toEpochMilli(), 20.0));
        measurements2.add(new TemperatureMeasurement("room-1", "thermometer-1", Timestamp.valueOf("2024-02-01 00:00:09").toInstant().toEpochMilli(), 20.0));
        measurements2.add(new TemperatureMeasurement("room-1", "thermometer-1", Timestamp.valueOf("2024-02-01 00:00:10").toInstant().toEpochMilli(), 20.0));
        measurements2.add(anomaly2);

        List<TemperatureMeasurement> anomalies2 = new ArrayList<>();
        anomalies2.add(anomaly1);
        anomalies2.add(anomaly2);

        agg2.setMeasurements(measurements2);
        agg2.setAnomalies(anomalies2);
        agg2.setAvg(250.0 / 11.0);
        agg2.setSum(250.0);
        agg2.setCount(11);

        // Running

        MeasurementsTimeWindowAnalyzer result = temperatureAnomaliesByTimeWindowService.calculateAnomalies(agg1, 5.0);

        // Verifying and asserting

        assertEquals(agg2, result, String.format("Expected result: %s and returned result: %s are not the same when analyzing anomalies.", agg2, result));
    }
}
