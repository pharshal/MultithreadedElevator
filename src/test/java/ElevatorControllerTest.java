import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by harshal on 18/4/15.
 */
public class ElevatorControllerTest {

    private ElevatorController elevatorController;
    private Thread t;

    @Before
    public void setUp() throws Exception {
        elevatorController = ElevatorController.getInstance();
        t = new Thread(elevatorController);
        t.start();
    }

    @After
    public void tearDown() throws Exception {
        if(!elevatorController.isStopController()) {
            elevatorController.setStopController(true);
        }
    }

    @Test
    public void testControllerThread(){
        assertEquals(false, elevatorController.isStopController());
    }

    @Test
    public void setElevatorControllerStop() throws Exception{
        elevatorController.setStopController(true);
        assertEquals(true, elevatorController.isStopController());
    }

}