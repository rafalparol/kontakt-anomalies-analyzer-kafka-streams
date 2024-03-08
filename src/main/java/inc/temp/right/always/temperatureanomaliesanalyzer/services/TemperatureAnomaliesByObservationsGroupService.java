package inc.temp.right.always.temperatureanomaliesanalyzer.services;

import inc.temp.right.always.temperatureanomaliesanalyzer.dto.MeasurementsObservationsGroupAnalyzer;
import inc.temp.right.always.temperatureanomaliesanalyzer.services.custom.TemperatureComparator;
import inc.temp.right.always.temperatureanomaliesanalyzer.services.custom.TimestampComparator;
import inc.temp.right.always.temperaturemodel.TemperatureMeasurement;
import lombok.extern.log4j.Log4j2;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.TimeWindows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.support.serializer.JsonSerde;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@Log4j2
public class TemperatureAnomaliesByObservationsGroupService implements AnomaliesDetectionService {
    @Value("${main.config.diffThreshold}")
    private double diffThreshold;
    @Value("${main.config.timeWindowSize}")
    private int timeWindowSize;
    @Value("${main.config.observationsGroupSize}")
    private int observationsGroupSize;
    @Value("${main.config.materializedTable}")
    private String materializedTable;

    public Function<KStream<String, TemperatureMeasurement>, KStream<String, TemperatureMeasurement>> process() {
        return input -> input
            .groupByKey(Grouped.with(Serdes.String(), new JsonSerde<TemperatureMeasurement>()))
            .windowedBy(TimeWindows.of(Duration.ofSeconds(timeWindowSize)))
            .aggregate(
                MeasurementsObservationsGroupAnalyzer::new,
                (k, v, aggregatedV) -> addTemperatureMeasurement(v, aggregatedV, observationsGroupSize, diffThreshold),
                Materialized.as(materializedTable).with(Serdes.String(), new JsonSerde<>(MeasurementsObservationsGroupAnalyzer.class))
            )
            .toStream()
            .flatMapValues(MeasurementsObservationsGroupAnalyzer::getAnomalies)
            .map((k, v) -> new KeyValue<>(k.key(), v));
    }

    public MeasurementsObservationsGroupAnalyzer addTemperatureMeasurement(TemperatureMeasurement temperatureMeasurement, MeasurementsObservationsGroupAnalyzer agg, int observationsGroupSize, double diffThreshold) {
        MeasurementsObservationsGroupAnalyzer measurementsObservationsGroupAnalyzer = new MeasurementsObservationsGroupAnalyzer();
        measurementsObservationsGroupAnalyzer.setMeasurements(agg.getMeasurements());

        measurementsObservationsGroupAnalyzer.getMeasurements().add(temperatureMeasurement);
        log.info(String.format("Added element: %s", temperatureMeasurement.toString()));

        // We need to have at least N measurements.
        if (measurementsObservationsGroupAnalyzer.getMeasurements().size() >= observationsGroupSize) measurementsObservationsGroupAnalyzer = this.calculateAnomalies(measurementsObservationsGroupAnalyzer, observationsGroupSize, diffThreshold);
        log.info(String.format("Calculated anomalies: %s", measurementsObservationsGroupAnalyzer.toString()));
        return measurementsObservationsGroupAnalyzer;
    }

    public MeasurementsObservationsGroupAnalyzer calculateAnomalies(MeasurementsObservationsGroupAnalyzer agg, int observationsGroupSize, double diffThreshold) {
        // We sort measurements by time.
        agg.setMeasurements(agg.getMeasurements().stream().sorted(new TimestampComparator()).collect(Collectors.toList()));
        // We take all possible N consecutive groups of measurements.
        agg.setAnomalies(IntStream.range(0, agg.getMeasurements().size() - observationsGroupSize + 1)
            .boxed()
            // We check if there is any anomaly in the current group.
            .flatMap(i -> calculateAnomalyForGroup(new ArrayList<>(agg.getMeasurements().subList(i, i + observationsGroupSize)), diffThreshold).stream())
            // We remove duplicates.
            .distinct()
            .collect(Collectors.toList()));

        return agg;
    }

    public Optional<TemperatureMeasurement> calculateAnomalyForGroup(List<TemperatureMeasurement> group, double diffThreshold) {
        // We find the measurement with the highest temperature.
        TemperatureMeasurement highest = group.stream().max(new TemperatureComparator()).get();

        // We remove it from the group.
        group.remove(highest);

        // We calculate the statistics.
        double sum = group.stream().mapToDouble(TemperatureMeasurement::getTemperature).sum();
        int count = group.size();
        double avg = sum / ((double)count);

        // We check if the highest temperature is greater enough than the average.
        return (highest.getTemperature() > avg + diffThreshold) ? Optional.of(highest) : Optional.empty();
    }
}
