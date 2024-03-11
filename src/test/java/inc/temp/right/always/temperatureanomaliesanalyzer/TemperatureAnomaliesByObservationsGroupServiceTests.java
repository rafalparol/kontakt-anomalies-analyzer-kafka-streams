package inc.temp.right.always.temperatureanomaliesanalyzer;

import inc.temp.right.always.temperatureanomaliesanalyzer.dto.MeasurementsObservationsGroupAnalyzer;
import inc.temp.right.always.temperatureanomaliesanalyzer.services.TemperatureAnomaliesByObservationsGroupService;
import inc.temp.right.always.temperaturemodel.TemperatureMeasurement;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class TemperatureAnomaliesByObservationsGroupServiceTests {
    @Test
    public void addTemperatureMeasurement_ManyMeasurements_Test() {
        // Preparing spy

        TemperatureAnomaliesByObservationsGroupService temperatureAnomaliesByObservationsGroupService = spy(TemperatureAnomaliesByObservationsGroupService.class);

        // Preparing input - agg1

        MeasurementsObservationsGroupAnalyzer agg1 = new MeasurementsObservationsGroupAnalyzer();

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

        // Preparing output - agg2

        MeasurementsObservationsGroupAnalyzer agg2 = new MeasurementsObservationsGroupAnalyzer();

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

        // Intermediate result - agg

        MeasurementsObservationsGroupAnalyzer agg3 = new MeasurementsObservationsGroupAnalyzer();
        agg3.setMeasurements(agg2.getMeasurements());

        // Mocking spy

        doReturn(agg2).when(temperatureAnomaliesByObservationsGroupService).calculateAnomalies(agg3, 10, 5.0);

        // Running

        MeasurementsObservationsGroupAnalyzer result = temperatureAnomaliesByObservationsGroupService.addTemperatureMeasurement(anomaly2, agg1, 10, 5.0);

        // Verifying and asserting

        verify(temperatureAnomaliesByObservationsGroupService, times(1)).calculateAnomalies(agg3, 10, 5.0);
        assertEquals(agg2, result, String.format("Expected result: %s and returned result: %s are not the same when analyzing anomalies.", agg2, result));
    }

    @Test
    public void addTemperatureMeasurement_NoMeasurements_Test() {
        // Preparing spy

        TemperatureAnomaliesByObservationsGroupService temperatureAnomaliesByObservationsGroupService = spy(TemperatureAnomaliesByObservationsGroupService.class);

        // Preparing input - agg1

        MeasurementsObservationsGroupAnalyzer agg1 = new MeasurementsObservationsGroupAnalyzer();

        List<TemperatureMeasurement> measurements1 = new ArrayList<>();
        List<TemperatureMeasurement> anomalies1 = new ArrayList<>();

        agg1.setMeasurements(measurements1);
        agg1.setAnomalies(anomalies1);

        // Preparing output - agg2

        MeasurementsObservationsGroupAnalyzer agg2 = new MeasurementsObservationsGroupAnalyzer();

        List<TemperatureMeasurement> measurements2 = new ArrayList<>();
        TemperatureMeasurement measurement = new TemperatureMeasurement("room-1", "thermometer-1", Timestamp.valueOf("2024-02-01 00:00:01").toInstant().toEpochMilli(), 20.0);
        measurements2.add(measurement);

        List<TemperatureMeasurement> anomalies2 = new ArrayList<>();

        agg2.setMeasurements(measurements2);
        agg2.setAnomalies(anomalies2);

        // Intermediate result - agg

        MeasurementsObservationsGroupAnalyzer agg3 = new MeasurementsObservationsGroupAnalyzer();
        agg3.setMeasurements(agg2.getMeasurements());

        // Mocking spy

        doReturn(agg2).when(temperatureAnomaliesByObservationsGroupService).calculateAnomalies(agg3, 10, 5.0);

        // Running

        MeasurementsObservationsGroupAnalyzer result = temperatureAnomaliesByObservationsGroupService.addTemperatureMeasurement(measurement, agg1, 10, 5.0);

        // Verifying and asserting

        verify(temperatureAnomaliesByObservationsGroupService, never()).calculateAnomalies(any(MeasurementsObservationsGroupAnalyzer.class), anyInt(),anyDouble());
        assertEquals(agg2, result, String.format("Expected result: %s and returned result: %s are not the same when analyzing anomalies.", agg2, result));
    }

    @Test
    public void calculateAnomalies_Test() {
        TemperatureAnomaliesByObservationsGroupService temperatureAnomaliesByObservationsGroupService = spy(TemperatureAnomaliesByObservationsGroupService.class);

        // Preparing input - agg1

        MeasurementsObservationsGroupAnalyzer agg1 = new MeasurementsObservationsGroupAnalyzer();

        List<TemperatureMeasurement> measurements1 = new ArrayList<>();

        TemperatureMeasurement anomaly1 = new TemperatureMeasurement("room-1", "thermometer-1", Timestamp.valueOf("2024-02-01 00:00:10").toInstant().toEpochMilli(), 35.0);
        TemperatureMeasurement anomaly2 = new TemperatureMeasurement("room-1", "thermometer-1", Timestamp.valueOf("2024-02-01 00:00:11").toInstant().toEpochMilli(), 36.0);

        measurements1.add(new TemperatureMeasurement("room-1", "thermometer-1", Timestamp.valueOf("2024-02-01 00:00:01").toInstant().toEpochMilli(), 20.0));
        measurements1.add(new TemperatureMeasurement("room-1", "thermometer-1", Timestamp.valueOf("2024-02-01 00:00:02").toInstant().toEpochMilli(), 20.0));
        measurements1.add(new TemperatureMeasurement("room-1", "thermometer-1", Timestamp.valueOf("2024-02-01 00:00:03").toInstant().toEpochMilli(), 20.0));
        measurements1.add(new TemperatureMeasurement("room-1", "thermometer-1", Timestamp.valueOf("2024-02-01 00:00:04").toInstant().toEpochMilli(), 20.0));
        measurements1.add(new TemperatureMeasurement("room-1", "thermometer-1", Timestamp.valueOf("2024-02-01 00:00:05").toInstant().toEpochMilli(), 20.0));
        measurements1.add(new TemperatureMeasurement("room-1", "thermometer-1", Timestamp.valueOf("2024-02-01 00:00:06").toInstant().toEpochMilli(), 20.0));
        measurements1.add(new TemperatureMeasurement("room-1", "thermometer-1", Timestamp.valueOf("2024-02-01 00:00:07").toInstant().toEpochMilli(), 20.0));
        measurements1.add(new TemperatureMeasurement("room-1", "thermometer-1", Timestamp.valueOf("2024-02-01 00:00:08").toInstant().toEpochMilli(), 20.0));
        measurements1.add(new TemperatureMeasurement("room-1", "thermometer-1", Timestamp.valueOf("2024-02-01 00:00:09").toInstant().toEpochMilli(), 20.0));
        measurements1.add(anomaly1);
        measurements1.add(anomaly2);

        List<TemperatureMeasurement> anomalies1 = new ArrayList<>();
        anomalies1.add(anomaly1);

        agg1.setMeasurements(measurements1);
        agg1.setAnomalies(anomalies1);

        // Preparing output - agg2

        MeasurementsObservationsGroupAnalyzer agg2 = new MeasurementsObservationsGroupAnalyzer();

        List<TemperatureMeasurement> measurements2 = new ArrayList<>();

        measurements2.add(new TemperatureMeasurement("room-1", "thermometer-1", Timestamp.valueOf("2024-02-01 00:00:01").toInstant().toEpochMilli(), 20.0));
        measurements2.add(new TemperatureMeasurement("room-1", "thermometer-1", Timestamp.valueOf("2024-02-01 00:00:02").toInstant().toEpochMilli(), 20.0));
        measurements2.add(new TemperatureMeasurement("room-1", "thermometer-1", Timestamp.valueOf("2024-02-01 00:00:03").toInstant().toEpochMilli(), 20.0));
        measurements2.add(new TemperatureMeasurement("room-1", "thermometer-1", Timestamp.valueOf("2024-02-01 00:00:04").toInstant().toEpochMilli(), 20.0));
        measurements2.add(new TemperatureMeasurement("room-1", "thermometer-1", Timestamp.valueOf("2024-02-01 00:00:05").toInstant().toEpochMilli(), 20.0));
        measurements2.add(new TemperatureMeasurement("room-1", "thermometer-1", Timestamp.valueOf("2024-02-01 00:00:06").toInstant().toEpochMilli(), 20.0));
        measurements2.add(new TemperatureMeasurement("room-1", "thermometer-1", Timestamp.valueOf("2024-02-01 00:00:07").toInstant().toEpochMilli(), 20.0));
        measurements2.add(new TemperatureMeasurement("room-1", "thermometer-1", Timestamp.valueOf("2024-02-01 00:00:08").toInstant().toEpochMilli(), 20.0));
        measurements2.add(new TemperatureMeasurement("room-1", "thermometer-1", Timestamp.valueOf("2024-02-01 00:00:09").toInstant().toEpochMilli(), 20.0));
        measurements2.add(anomaly1);
        measurements2.add(anomaly2);

        List<TemperatureMeasurement> anomalies2 = new ArrayList<>();
        anomalies2.add(anomaly1);
        anomalies2.add(anomaly2);

        agg2.setMeasurements(measurements2);
        agg2.setAnomalies(anomalies2);

        // Running

        ArrayList<TemperatureMeasurement> tempList1 = new ArrayList<>();
        tempList1.add(new TemperatureMeasurement("room-1", "thermometer-1", Timestamp.valueOf("2024-02-01 00:00:01").toInstant().toEpochMilli(), 20.0));
        tempList1.add(new TemperatureMeasurement("room-1", "thermometer-1", Timestamp.valueOf("2024-02-01 00:00:02").toInstant().toEpochMilli(), 20.0));
        tempList1.add(new TemperatureMeasurement("room-1", "thermometer-1", Timestamp.valueOf("2024-02-01 00:00:03").toInstant().toEpochMilli(), 20.0));
        tempList1.add(new TemperatureMeasurement("room-1", "thermometer-1", Timestamp.valueOf("2024-02-01 00:00:04").toInstant().toEpochMilli(), 20.0));
        tempList1.add(new TemperatureMeasurement("room-1", "thermometer-1", Timestamp.valueOf("2024-02-01 00:00:05").toInstant().toEpochMilli(), 20.0));
        tempList1.add(new TemperatureMeasurement("room-1", "thermometer-1", Timestamp.valueOf("2024-02-01 00:00:06").toInstant().toEpochMilli(), 20.0));
        tempList1.add(new TemperatureMeasurement("room-1", "thermometer-1", Timestamp.valueOf("2024-02-01 00:00:07").toInstant().toEpochMilli(), 20.0));
        tempList1.add(new TemperatureMeasurement("room-1", "thermometer-1", Timestamp.valueOf("2024-02-01 00:00:08").toInstant().toEpochMilli(), 20.0));
        tempList1.add(new TemperatureMeasurement("room-1", "thermometer-1", Timestamp.valueOf("2024-02-01 00:00:09").toInstant().toEpochMilli(), 20.0));
        tempList1.add(anomaly1);

        ArrayList<TemperatureMeasurement> tempList2 = new ArrayList<>();
        tempList2.add(new TemperatureMeasurement("room-1", "thermometer-1", Timestamp.valueOf("2024-02-01 00:00:02").toInstant().toEpochMilli(), 20.0));
        tempList2.add(new TemperatureMeasurement("room-1", "thermometer-1", Timestamp.valueOf("2024-02-01 00:00:03").toInstant().toEpochMilli(), 20.0));
        tempList2.add(new TemperatureMeasurement("room-1", "thermometer-1", Timestamp.valueOf("2024-02-01 00:00:04").toInstant().toEpochMilli(), 20.0));
        tempList2.add(new TemperatureMeasurement("room-1", "thermometer-1", Timestamp.valueOf("2024-02-01 00:00:05").toInstant().toEpochMilli(), 20.0));
        tempList2.add(new TemperatureMeasurement("room-1", "thermometer-1", Timestamp.valueOf("2024-02-01 00:00:06").toInstant().toEpochMilli(), 20.0));
        tempList2.add(new TemperatureMeasurement("room-1", "thermometer-1", Timestamp.valueOf("2024-02-01 00:00:07").toInstant().toEpochMilli(), 20.0));
        tempList2.add(new TemperatureMeasurement("room-1", "thermometer-1", Timestamp.valueOf("2024-02-01 00:00:08").toInstant().toEpochMilli(), 20.0));
        tempList2.add(new TemperatureMeasurement("room-1", "thermometer-1", Timestamp.valueOf("2024-02-01 00:00:09").toInstant().toEpochMilli(), 20.0));
        tempList2.add(anomaly1);
        tempList2.add(anomaly2);

        doReturn(Optional.of(anomaly1)).when(temperatureAnomaliesByObservationsGroupService).calculateAnomalyForGroup(tempList1, 5.0);
        doReturn(Optional.of(anomaly2)).when(temperatureAnomaliesByObservationsGroupService).calculateAnomalyForGroup(tempList2, 5.0);

        MeasurementsObservationsGroupAnalyzer result = temperatureAnomaliesByObservationsGroupService.calculateAnomalies(agg1, 10, 5.0);

        // Verifying and asserting

        verify(temperatureAnomaliesByObservationsGroupService, times(1)).calculateAnomalyForGroup(tempList1, 5.0);
        verify(temperatureAnomaliesByObservationsGroupService, times(1)).calculateAnomalyForGroup(tempList2, 5.0);

        assertEquals(agg2, result, String.format("Expected result: %s and returned result: %s are not the same when analyzing anomalies.", agg2, result));
    }

    @Test
    public void calculateAnomalyForGroup_Test() {
        TemperatureAnomaliesByObservationsGroupService temperatureAnomaliesByObservationsGroupService = new TemperatureAnomaliesByObservationsGroupService();

        // Preparing input - agg1

        List<TemperatureMeasurement> measurements1 = new ArrayList<>();

        TemperatureMeasurement anomaly1 = new TemperatureMeasurement("room-1", "thermometer-1", Timestamp.valueOf("2024-02-01 00:00:10").toInstant().toEpochMilli(), 35.0);

        measurements1.add(new TemperatureMeasurement("room-1", "thermometer-1", Timestamp.valueOf("2024-02-01 00:00:01").toInstant().toEpochMilli(), 20.0));
        measurements1.add(new TemperatureMeasurement("room-1", "thermometer-1", Timestamp.valueOf("2024-02-01 00:00:02").toInstant().toEpochMilli(), 20.0));
        measurements1.add(new TemperatureMeasurement("room-1", "thermometer-1", Timestamp.valueOf("2024-02-01 00:00:03").toInstant().toEpochMilli(), 20.0));
        measurements1.add(new TemperatureMeasurement("room-1", "thermometer-1", Timestamp.valueOf("2024-02-01 00:00:04").toInstant().toEpochMilli(), 20.0));
        measurements1.add(new TemperatureMeasurement("room-1", "thermometer-1", Timestamp.valueOf("2024-02-01 00:00:05").toInstant().toEpochMilli(), 20.0));
        measurements1.add(new TemperatureMeasurement("room-1", "thermometer-1", Timestamp.valueOf("2024-02-01 00:00:06").toInstant().toEpochMilli(), 20.0));
        measurements1.add(new TemperatureMeasurement("room-1", "thermometer-1", Timestamp.valueOf("2024-02-01 00:00:07").toInstant().toEpochMilli(), 20.0));
        measurements1.add(new TemperatureMeasurement("room-1", "thermometer-1", Timestamp.valueOf("2024-02-01 00:00:08").toInstant().toEpochMilli(), 20.0));
        measurements1.add(new TemperatureMeasurement("room-1", "thermometer-1", Timestamp.valueOf("2024-02-01 00:00:09").toInstant().toEpochMilli(), 20.0));
        measurements1.add(anomaly1);

        // Running

        Optional<TemperatureMeasurement> expectedResult = Optional.of(anomaly1);
        Optional<TemperatureMeasurement> result = temperatureAnomaliesByObservationsGroupService.calculateAnomalyForGroup(measurements1, 5.0);

        // Verifying and asserting

        assertEquals(expectedResult, result, String.format("Expected result: %s and returned result: %s are not the same when analyzing anomalies.", expectedResult, result));
    }
}
