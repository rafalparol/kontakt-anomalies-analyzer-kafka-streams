package inc.temp.right.always.temperatureanomaliesanalyzer.services.custom;

import inc.temp.right.always.temperaturemodel.TemperatureMeasurement;

import java.util.Comparator;

public class TimestampComparator implements Comparator<TemperatureMeasurement> {
    @Override
    public int compare(TemperatureMeasurement t1, TemperatureMeasurement t2) {
        return Long.compare(t1.getTimestamp(), t2.getTimestamp());
    }
}
