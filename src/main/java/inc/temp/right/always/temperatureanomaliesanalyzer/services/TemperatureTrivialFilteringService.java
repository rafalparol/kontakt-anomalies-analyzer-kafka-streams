package inc.temp.right.always.temperatureanomaliesanalyzer.services;

import inc.temp.right.always.temperaturemodel.TemperatureMeasurement;
import lombok.extern.log4j.Log4j2;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
@Log4j2
public class TemperatureTrivialFilteringService implements AnomaliesDetectionService {
    @Value("${main.config.trivialThreshold}")
    private double threshold;
    public Function<KStream<String, TemperatureMeasurement>, KStream<String, TemperatureMeasurement>> process() {
        return (input -> input
            .filter((k, v) -> {
                log.info(String.format(String.format("Filtering. Thermometer: %s, temperature: %f.", v.getThermometerId(), v.getTemperature())));
                return (v.getTemperature() > threshold);
            })
            .mapValues((k, v) -> {
                log.info(String.format("Anomaly detected!. Thermometer: %s, temperature: %f.", v.getThermometerId(), v.getTemperature()));
                return v;
            }));
    }
}
