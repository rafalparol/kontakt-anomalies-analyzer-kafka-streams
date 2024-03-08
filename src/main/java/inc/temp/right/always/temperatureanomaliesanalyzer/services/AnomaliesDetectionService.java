package inc.temp.right.always.temperatureanomaliesanalyzer.services;

import inc.temp.right.always.temperaturemodel.TemperatureMeasurement;
import org.apache.kafka.streams.kstream.KStream;

import java.util.function.Function;

public interface AnomaliesDetectionService {
    public Function<KStream<String, TemperatureMeasurement>, KStream<String, TemperatureMeasurement>> process();
}
