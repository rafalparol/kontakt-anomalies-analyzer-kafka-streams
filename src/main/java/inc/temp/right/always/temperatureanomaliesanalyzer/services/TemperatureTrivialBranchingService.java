package inc.temp.right.always.temperatureanomaliesanalyzer.services;

import inc.temp.right.always.temperaturemodel.TemperatureMeasurement;
import lombok.extern.log4j.Log4j2;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.function.Function;

@Service
@Log4j2
public class TemperatureTrivialBranchingService implements MeasurementsGroupingService {
    @Value("${main.config.trivialThreshold}")
    private double threshold;
    public Function<KStream<String, TemperatureMeasurement>, KStream<String, TemperatureMeasurement>[]> process() {
        return (input -> {
            final Map<String, KStream<String, TemperatureMeasurement>> stringKStreamMap =
                input
                    .split()
                    .branch((k, v) -> v.getTemperature() <= threshold)
                    .branch((k, v) -> v.getTemperature() > threshold)
                    .noDefaultBranch();

            return stringKStreamMap.values().toArray(new KStream[0]);
        });
    }
}
