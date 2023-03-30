public class Event {
    double eventTime; // time that event is completed on
    String event;
    EventType eventType;

    int destinationFloor;

    int elevatorNum;

    public Event(String event, double eventTime, EventType eventType) {
        this.eventTime = eventTime;
        this.event = event;
        this.eventType = eventType;
    }
}
