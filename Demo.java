import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;

public class Demo {

    public static void main(String[] args) {
        Demo d = new Demo();
        d.start();
    }

    private PriorityQueue<Event> fel; // future event list
    private double clock;
    private int personNum;
    private List<Elevator> elevatorList;

    private double sumWaitTime;
    private double avgWaitTime;

    private double sumArrivalTime;
    private double avgArrivalTime;


    public int startFloor() {
        Random random = new Random();
        int r = random.nextInt(11);

        if (r > 0 && r < 6) {
            return r;
        } else {
            return 0;
        }
    }

    public int destFloor(int startFloor) {
        Random random = new Random();
        int r = random.nextInt(11);
        if (startFloor == 0) {
            while (r == startFloor || r > 5) {
                r = random.nextInt(12);
            }
        }
        while (r == startFloor) {
            r = random.nextInt(11);
        }

        if (r > 0 && r < 6) {
            return r;
        } else {
            return 0;
        }
    }
    public void start() {
        init();

        createEvents(); // generates new person and subsequent events and adds to future event list

        for (int i = 0; i < 1000; i++) {

            if( i < 20){ //debugging output
                int count = 0;
                System.out.println("----Iteration #" + i +"----");
                System.out.println("-- Current Clock Time: " + clock +" --");
                System.out.println("Current Future Event List: ");
                for( Event e : fel){
                    System.out.println("Event#" + count +": ");
                    System.out.println("--Event Description: "+ e.event);
                    System.out.println("--Event Time: " + e.eventTime);
                    System.out.println();
                    count++;
                }
                System.out.println("-------------------------");
                System.out.println();
            }

            if (!fel.isEmpty()) {
                Event curr = fel.poll(); //pop most recent event from priority queue
                clock = curr.eventTime; // update clock to popped event's event time

                if (curr.eventType == EventType.ELEVATORMOVING) {
                    elevatorList.get(curr.elevatorNum).moving = false;
                    elevatorList.get(curr.elevatorNum).elevatorNum = curr.destinationFloor;
                }

                if (curr.eventType == EventType.PERSONARRIVAL) { // if popped event was new person arrival, generate new person arrival
                    createEvents();
                }

                System.out.println("Clock " + clock + ": " + curr.event);
                System.out.println();
                
            }


            System.out.println();

        }
        avgArrivalTime = sumArrivalTime / personNum;
        System.out.println("Average new Person Arrival Time: " + avgArrivalTime );
        System.out.println("Average Wait Time to get on Elevator: " + avgWaitTime);
    }

    public void createEvents() { // generates new person and subsequent events and adds to future event list
        int startingFloor = startFloor();
        int destinationFloor = destFloor(startingFloor);
        Event genPerson = generatePersonEvent(startingFloor, clock, destinationFloor); //generate person arrival event add to future event list
        fel.add(genPerson);

        boolean eleOnFloor = elevatorOnFloor(startingFloor);
        int elevatorIdx = getClosestElevator(startingFloor);
        if (elevatorIdx != -1) {
            if (!eleOnFloor) { //if elevator is already on requested floor
                Event elevatorMove = generateElevatorMoveEvent(startingFloor, genPerson.eventTime, elevatorIdx);//generate move elevator to requested floor event add to future event list
                fel.add(elevatorMove);

                Event getOnElevator = generatePersonGetOnEvent(startingFloor, elevatorMove.eventTime,elevatorIdx);//generate get on elevator event add to future event list
                fel.add(getOnElevator);
                Event elevatorMove2 = generateElevatorMoveEvent(destinationFloor, getOnElevator.eventTime, elevatorIdx);//generate move elevator to requested floor event add to future event list
                fel.add(elevatorMove2);
                Event getOffElevator = generatePersonGetOffEvent(destinationFloor, elevatorMove2.eventTime, elevatorIdx); //generate get off elevator event add to future event list
                fel.add(getOffElevator);

                sumWaitTime += Math.abs(getOnElevator.eventTime - genPerson.eventTime);
                avgWaitTime = sumWaitTime / personNum;
            } else {//if elevator is not on requested floor
                Event getOnElevator = generatePersonGetOnEvent(startingFloor, genPerson.eventTime,elevatorIdx);
                fel.add(getOnElevator);
                Event elevatorMove = generateElevatorMoveEvent(destinationFloor, getOnElevator.eventTime, elevatorIdx);
                fel.add(elevatorMove);
                Event getOffElevator = generatePersonGetOffEvent(destinationFloor, elevatorMove.eventTime, elevatorIdx);
                fel.add(getOffElevator);

                sumWaitTime += Math.abs(getOnElevator.eventTime - genPerson.eventTime);
                avgWaitTime = sumWaitTime / personNum;
            }
        }
        personNum++;
    }

    public void init() {
        fel = new PriorityQueue<>(100, new EventComparator());

        clock = 0.0;
        personNum = 1;
        sumWaitTime = 0.0;
        avgWaitTime = 0.0;
        sumArrivalTime = 0.0;
        avgArrivalTime = 0.0;

        elevatorList = new ArrayList<>();
        elevatorList.add(new Elevator(1, 0, false));
        elevatorList.add(new Elevator(2, 0, false));
        elevatorList.add(new Elevator(3, 0, false));
        elevatorList.add(new Elevator(4, 0, false));


    }

    public double newPersonTime() {  // generates a number with exponential distribution
        double lambda = 0.1;
        double d = (-Math.log(1 - Math.random()) / (lambda));
        sumArrivalTime += d;
        return d;
    }

    public Event generatePersonEvent(int startingFloor, double startTime, int destinationFloor) {

        Event e = new Event("Person#" + personNum + " arrived on Floor#" + startingFloor + " and wants to get off on Floor#" + destinationFloor,
                startTime + newPersonTime(), //adds current clock time + exponential dist. number for arrival time
                EventType.PERSONARRIVAL);
        return e;
    }

    public Event generateElevatorMoveEvent(int floor, double startTime, int elevatorIdx) {
        Elevator temp = elevatorList.get(elevatorIdx);
        int time = Math.abs(floor - temp.floor) * 6;

        Event event = new Event("Elevator#" + (elevatorIdx + 1) + " arrived on Floor#" + floor,
                startTime + time,
                EventType.ELEVATORMOVING);

        event.elevatorNum = elevatorIdx;
        event.destinationFloor = floor;
        temp.moving = true;

        return event;
    }

    public Event generatePersonGetOnEvent(int floor, double startTime, int elevatorIdx) {
        return new Event("Person#" + personNum + " got on Elevator#" + (elevatorIdx + 1) + " on Floor#" + floor,
                startTime + 2,
                EventType.PERSONGETON);
    }

    public Event generatePersonGetOffEvent(int floor, double startTime, int elevatorIdx) {
        return new Event("Person#" + personNum + " got off Elevator#" + (elevatorIdx + 1) + " on Floor#" + floor,
                startTime + 2,
                EventType.PERSONGETOFF);
    }

    public boolean elevatorOnFloor(int floorNum) {
        for (Elevator e : elevatorList) {
            if (!e.moving && e.floor == floorNum) {
                return true;
            }
        }
        return false;
    }

    public int getClosestElevator(int floorNum) {
        int closest = -1;
        int dist = 6;
        for (int i = 0; i < elevatorList.size(); i++) {
            Elevator temp = elevatorList.get(i);
            if (!temp.moving) {
                int localDist = Math.abs(floorNum - temp.floor);
                if (localDist < dist) {
                    dist = localDist;
                    closest = i;
                }
            }
        }
        return closest;
    }
}
