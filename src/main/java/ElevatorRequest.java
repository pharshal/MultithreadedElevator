
/**
 * Represents a request for an user to use the elevator
 */
public class ElevatorRequest {
    private int requestFloor;
    private int targetFloor;

    public ElevatorRequest(int requestFloor, int targetFloor){
        this.requestFloor = requestFloor;
        this.targetFloor = targetFloor;
    }

    public int getRequestFloor() {
        return requestFloor;
    }

    public int getTargetFloor() {
        return targetFloor;
    }

    /**
     * Submit the request to the ElevatorController to select the
     * optimal elevator for this request
     * @return
     */
    public Elevator submitRequest(){
        return ElevatorController.getInstance().selectElevator(this);
    }
}
