import java.util.Comparator;

public class EventComparator implements Comparator<Event> {
    public int compare(Event e1, Event e2) {
        if (e1.eventTime > e2.eventTime) {
            return 1;
        } else if (e1.eventTime < e2.eventTime) {
            return -1;
        }
        return 0;
    }
}
