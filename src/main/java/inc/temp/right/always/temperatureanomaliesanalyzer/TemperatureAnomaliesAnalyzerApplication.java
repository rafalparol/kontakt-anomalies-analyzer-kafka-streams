package inc.temp.right.always.temperatureanomaliesanalyzer;

import inc.temp.right.always.temperatureanomaliesanalyzer.services.*;
import inc.temp.right.always.temperaturemodel.TemperatureMeasurement;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.function.Function;

@SpringBootApplication
public class TemperatureAnomaliesAnalyzerApplication {
	@Value("${main.config.algorithm}")
	private String algorithm;
	@Autowired
	// @Qualifier("TemperatureAnomaliesByObservationsGroupService")
	TemperatureAnomaliesByObservationsGroupService temperatureAnomaliesByObservationsGroupService;
	@Autowired
	// @Qualifier("TemperatureAnomaliesByTimeWindowService")
	TemperatureAnomaliesByTimeWindowService temperatureAnomaliesByTimeWindowService;

	@Bean
	public Function<KStream<String, TemperatureMeasurement>, KStream<String, TemperatureMeasurement>> process() {
		return (algorithm.equals("timeWindow")) ? temperatureAnomaliesByTimeWindowService.process() : temperatureAnomaliesByObservationsGroupService.process();
	}
	public static void main(String[] args) {
		SpringApplication.run(TemperatureAnomaliesAnalyzerApplication.class, args);
	}
}
