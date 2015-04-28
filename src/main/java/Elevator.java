import java.util.*;

/**
 * Objects of this class represent individual elevators
 */
public class Elevator implements Runnable{

    private boolean operating;
    private int id;
    private ElevatorState elevatorState;
    private int currentFloor;

    // Set of floors the elevator will make a stop or pass by while moving
    private NavigableSet<Integer> floorStops;

    // This map is required to serve requests that require an elevator
    // to move in both UP and DOWN direction.
    // e.g. request comes from floor 8 to go to floor 2. But the
    // elevator is at floor 0 currently. In this case, an elevator
    // move UP towards floor 8. Picks person(s) and starts moving
    // DOWN towards floor 2.
    // ElevatorStat stores UP or DOWN motion.
    public Map<ElevatorState, NavigableSet<Integer>> floorStopsMap;

    public Elevator(int id){
        this.id = id;
        setOperating(true);
    }

    public int getId() {
        return id;
    }

    public ElevatorState getElevatorState() {
        return elevatorState;
    }

    public int getCurrentFloor() {
        return currentFloor;
    }

    public void setElevatorState(ElevatorState elevatorState) {
        this.elevatorState = elevatorState;
    }

    public boolean isOperating(){
        return this.operating;
    }

    public void setOperating(boolean state){
        this.operating = state;

        if(!state){
            setElevatorState(ElevatorState.MAINTAINANCE);
            this.floorStops.clear();
        } else {
            setElevatorState(ElevatorState.STATIONARY);
            this.floorStopsMap = new LinkedHashMap<ElevatorState, NavigableSet<Integer>>();

            // To let controller know that this elevator is ready to serve
            ElevatorController.updateElevatorLists(this);
        }

        setCurrentFloor(0);
    }

    public void setCurrentFloor(int currentFloor) {
        this.currentFloor = currentFloor;
    }

    /**
     * Move the elevator UP or DOWn
     */
    public void move(){
        synchronized (ElevatorController.getInstance()){ // Synchronized over the ElevatorController singleton.
            Iterator<ElevatorState> iter = floorStopsMap.keySet().iterator();

            while(iter.hasNext()){
                elevatorState = iter.next();

                // Get the floors that elevator will pass in the requested direction
                floorStops = floorStopsMap.get(elevatorState);
                iter.remove();
                Integer currFlr = null;
                Integer nextFlr = null;

                // Start moving the elevator
                while (!floorStops.isEmpty()) {

                    if (elevatorState.equals(ElevatorState.UP)) {
                        currFlr = floorStops.pollFirst();
                        nextFlr = floorStops.higher(currFlr);

                    } else if (elevatorState.equals(ElevatorState.DOWN)) {
                        currFlr = floorStops.pollLast();
                        nextFlr = floorStops.lower(currFlr);
                    } else {
                        return;
                    }

                    setCurrentFloor(currFlr);

                    if (nextFlr != null) {
                        // This helps us in picking up any request that might come
                        // while we are on the way.
                        generateIntermediateFloors(currFlr, nextFlr);
                    } else {
                        setElevatorState(ElevatorState.STATIONARY);
                        ElevatorController.updateElevatorLists(this);
                    }

                    System.out.println("Elevator ID " + this.id + " | Current floor - " + getCurrentFloor() + " | next move - " + getElevatorState());

                    try {
                        Thread.sleep(1000); // Let people get off the elevator :P
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            try {
                // Wait till ElevatorController has scanned the state of all elevators.
                // This helps us to serve any intermediate requests that might come
                // while elevators are on their respective paths.
                ElevatorController.getInstance().wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }


    /**
     * This method helps to generate list of floors that the elevator will
     * either stop or pass by when in motion.
     * @param initial
     * @param target
     */
    private void generateIntermediateFloors(int initial, int target){

        if(initial==target){
            return;
        }

        if(Math.abs(initial-target) == 1){
            return;
        }

        int n = 1;
        if(target-initial<0){
            // This means with are moving DOWN
            n = -1;
        }

        while(initial!=target){
            initial += n;
            if(!floorStops.contains(initial)) {
                floorStops.add(initial);
            }
        }
    }

    @Override
    public void run() {
        while(true){
            if(isOperating()){
                move();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                break;
            }
        }
    }
}
