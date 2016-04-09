package socala.app.models;

import java.util.concurrent.TimeUnit;

public class EventDuration {
    public static final String FIFTEEN_MINUTES = "15 Minutes";
    public static final String THIRTY_MINUTES = "30 Minutes";
    public static final String FORTY_FIVE_MINUTES = "45 Minutes";
    public static final String ONE_HOUR = "1 hour";
    public static final String TWO_HOURS = "2 hours";

    public static long getMilliseconds(String duration) {
        switch (duration) {
            case FIFTEEN_MINUTES:
                return TimeUnit.MILLISECONDS.convert(15, TimeUnit.MINUTES);
            case THIRTY_MINUTES:
                return TimeUnit.MILLISECONDS.convert(30, TimeUnit.MINUTES);
            case FORTY_FIVE_MINUTES:
                return TimeUnit.MILLISECONDS.convert(45, TimeUnit.MINUTES);
            case ONE_HOUR:
                return TimeUnit.MILLISECONDS.convert(1, TimeUnit.HOURS);
            case TWO_HOURS:
                return TimeUnit.MILLISECONDS.convert(2, TimeUnit.HOURS);
        }

        throw new UnsupportedOperationException("Cannot convert duration to milliseconds");
    }
}
