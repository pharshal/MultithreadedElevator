

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by harshal on 19/4/15.
 */
public class ElevatorRequestTest {

    private ElevatorController elevatorController;
    private Thread elevatorControllerThread;



    @Before
    public void setUp() throws Exception {

       // Executor executor = Executors.newSingleThreadExecutor();
       // executor.execute(elevatorController);
        elevatorController = ElevatorController.getInstance();
        elevatorControllerThread = new Thread(elevatorController);
        elevatorControllerThread.start();

      //  elevator = new Elevator(0);
      //  elevatorThread = new Thread(elevator);
      //  elevatorThread.start();


    }

    @After
    public void tearDown() throws Exception {
        if(!elevatorController.isStopController()) {
            elevatorController.setStopController(true);
        }

      //  if (elevator.isOperating()){
      //      elevator.setOperating(false);
      //  }
    }

    @Test
    public void testSubmitRequest1() throws Exception {
        ElevatorRequest elevatorRequest = new ElevatorRequest(0, 2);
        Elevator elevator = elevatorRequest.submitRequest();
        Thread.sleep(5000);
        assertEquals(2, elevator.getCurrentFloor());
    }

    @Test
    public void testSubmitRequest2() throws Exception {
        ElevatorRequest elevatorRequest = new ElevatorRequest(0, 1);
        Elevator elevator = elevatorRequest.submitRequest();
        Thread.sleep(3000);
        ElevatorRequest elevatorRequest1 = new ElevatorRequest(3, 5);
        elevator = elevatorRequest1.submitRequest();
        Thread.sleep(10000);
        assertEquals(5, elevator.getCurrentFloor());
    }

    @Test
    public void testSubmitRequest3() throws Exception {
        ElevatorRequest elevatorRequest = new ElevatorRequest(0, 4);
        Elevator elevator = elevatorRequest.submitRequest();
        Thread.sleep(5000);
        ElevatorRequest elevatorRequest1 = new ElevatorRequest(0, 1);

        elevator = elevatorRequest1.submitRequest();
        Thread.sleep(3000);
        assertEquals(1, elevator.getCurrentFloor());
    }

    @Test
    public void testSubmitRequest4() throws Exception {
        ElevatorRequest elevatorRequest = new ElevatorRequest(0, 2);
        Elevator elevator = elevatorRequest.submitRequest();
        Thread.sleep(6000);
        ElevatorRequest elevatorRequest1 = new ElevatorRequest(2, 0);
        elevator = elevatorRequest1.submitRequest();
        Thread.sleep(6000);
        assertEquals(0, elevator.getCurrentFloor());
    }

    @Test
    public void testSubmitRequest5() throws Exception {
        ElevatorRequest elevatorRequest = new ElevatorRequest(3, 1);
        Elevator elevator = elevatorRequest.submitRequest();
        Thread.sleep(10000);
        assertEquals(1, elevator.getCurrentFloor());
    }
}