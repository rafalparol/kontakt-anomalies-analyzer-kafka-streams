package inc.temp.right.always.temperaturemodel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TemperatureMeasurement {
    private String roomId;
    private String thermometerId;
    private long timestamp;
    private double temperature;
}
