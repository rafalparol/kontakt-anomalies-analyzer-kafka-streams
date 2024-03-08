package inc.temp.right.always.temperatureanomaliesanalyzer.services;

import inc.temp.right.always.temperatureanomaliesanalyzer.dto.MeasurementsTimeWindowAnalyzer;
import inc.temp.right.always.temperaturemodel.TemperatureMeasurement;
import lombok.extern.log4j.Log4j2;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.support.serializer.JsonSerde;
import org.springframework.stereotype.Service;
import java.time.Duration;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Log4j2
public class TemperatureAnomaliesByTimeWindowService implements AnomaliesDetectionService {
    @Value("${main.config.diffThreshold}")
    private double diffThreshold;
    @Value("${main.config.timeWindowSize}")
    private int timeWindowSize;
    @Value("${main.config.materializedTable}")
    private String materializedTable;

    public Function<KStream<String, TemperatureMeasurement>, KStream<String, TemperatureMeasurement>> process() {
        return input -> input
            .groupByKey(Grouped.with(Serdes.String(), new JsonSerde<TemperatureMeasurement>()))
            .windowedBy(TimeWindows.of(Duration.ofSeconds(timeWindowSize)))
            .aggregate(
                MeasurementsTimeWindowAnalyzer::new,
                (k, v, aggregatedV) -> addTemperatureMeasurement(v, aggregatedV, diffThreshold),
                Materialized.as(materializedTable).with(Serdes.String(), new JsonSerde<>(MeasurementsTimeWindowAnalyzer.class))
            )
            .toStream()
            .flatMapValues(MeasurementsTimeWindowAnalyzer::getAnomalies)
            .map((k, v) -> new KeyValue<>(k.key(), v));
    }

    public MeasurementsTimeWindowAnalyzer addTemperatureMeasurement(TemperatureMeasurement temperatureMeasurement, MeasurementsTimeWindowAnalyzer agg, double diffThreshold) {
        MeasurementsTimeWindowAnalyzer measurementsIntervalAnalyzer = new MeasurementsTimeWindowAnalyzer();
        measurementsIntervalAnalyzer.setMeasurements(agg.getMeasurements());

        measurementsIntervalAnalyzer.getMeasurements().add(temperatureMeasurement);
        log.info(String.format("Added element: %s", temperatureMeasurement.toString()));

        // We need to have any measurements.
        if (!measurementsIntervalAnalyzer.getMeasurements().isEmpty()) measurementsIntervalAnalyzer = this.calculateAnomalies(measurementsIntervalAnalyzer, diffThreshold);
        log.info(String.format("Calculated anomalies: %s", measurementsIntervalAnalyzer.toString()));
        return measurementsIntervalAnalyzer;
    }

    public MeasurementsTimeWindowAnalyzer calculateAnomalies(MeasurementsTimeWindowAnalyzer agg, double diffThreshold) {
        double sum = agg.getMeasurements().stream().mapToDouble(TemperatureMeasurement::getTemperature).sum();
        int count = agg.getMeasurements().size();
        double avg = sum / ((double)count);

        List<TemperatureMeasurement> anomalies = agg.getMeasurements().stream().filter(m -> m.getTemperature() > avg + diffThreshold).collect(Collectors.toList());

        agg.setSum(sum);
        agg.setCount(count);
        agg.setAvg(avg);
        agg.setAnomalies(anomalies);

        return agg;
    }
}
