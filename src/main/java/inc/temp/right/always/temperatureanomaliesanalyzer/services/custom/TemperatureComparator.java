package inc.temp.right.always.temperatureanomaliesanalyzer.services.custom;

import inc.temp.right.always.temperaturemodel.TemperatureMeasurement;

import java.util.Comparator;

public class TemperatureComparator implements Comparator<TemperatureMeasurement> {
    @Override
    public int compare(TemperatureMeasurement t1, TemperatureMeasurement t2) {
        return Double.compare(t1.getTemperature(), t2.getTemperature());
    }
}
