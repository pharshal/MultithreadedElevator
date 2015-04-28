----------------------
What is this?
----------------------
This is a simulator for 'Elevator System'. A system of 16 elevators
is simulated in this code. Every request for an elevator on any 
floor is served with the optimal choice of elevator out of available
16 elevators.

----------------------
Compile
----------------------

To compile run, mvn compile

----------------------
Run
----------------------

to execute run, mvn exec:java

At any given point, press either 1 or 2 to interact with the program.
If you wish to exit, press Control+C

-------------------------
Unit Tests
-------------------------

To run unit test run, mvn test

This execution has been purposely slowed down by introducing Thread.sleep() call
whenever an elevator passes any floor. So when running unit tests, please be
patient even if there is no output on the console for a brief moment. Please
wait till you get the command prompt back.

-------------------------
Design Decisions
-------------------------
We have 3 Major componets in this system :-

1. Elevator - This class simulates an elevator and implements a basic
functionality for the movement of the elevator. In this setup we have
total 16 instances of this class with each representing and individual
elevator. The number 16 is an arbitary choice here and is not a design
contraint. We may choose to have as many number of elevators as we 
like. There is no restriction on the max number of elevators from the
design of the system.

2. ElevatorController - This is core component of our system. This class
is designed in such a way that it decouples the elevators from the 
user requests to use elevators. Advantage of such design is, both 
elevators and user requests (ElevatorRequest) can function independanty
without having to block each other. This also opens up scope for scaling 
this on a distributed system with extracted interface of ElevatorController
class.
	
3. ElevatorRequest - The objects of this class represent the user request
to use the elevator system. 

The ElevatorController dispatches ElevatorRequests based on nearest available
elevators. Elevators are not selected just by simple first-come, first-served.
Every elevator has 3 states; UP, DOWN and STATIONARY. Every time elevator
changes the state ElevatorController is notified. This helps in scenarios
where ElevatorRequest(s) coming later point in time but falling on the the path of
original ElevatorRequest can be served without any blocking.
e.g. 
  a. All elevators are at floor 0.   
  b. ElevatorRequest comes from floor 8 to go down to floor 2. Let's call it
     'req1'
  c. Since all elevators are at 0, the first one is selected 
     and it starts moving towards floor 8 from floor 0.
  d. While it's passing floor 4, and there comes an ElevatorRequest
     for floor 5 to floor 7. This request doesn't have to wait for
     orignal request 'req1' to complete. This request can be served
     immediately while elevator is moving upwards towards floor 8 
     but have not passed floor 5.
  e. Similarly where 'req1' is moving from floor 8 to floor 2, any 
     request towards DOWN direction from the floors that the elevator
     has not passed already will be served. 

Overall the flow looks like, 

ElevatorRequest(s) -> ElevatorController -> Elevator(s)

-------------------------
Issues
-------------------------

1. If you see console screen stuck at something like,
<snip>
Enter choice (number): 
 1. Elevator status
 2. Request elevator
Elevator ID 14 | Current floor - 0 | next move - STATIONARY
Elevator ID 14 | Current floor - 0 | next move - UP
Elevator ID 14 | Current floor - 1 | next move - UP
Elevator ID 14 | Current floor - 2 | next move - STATIONARY
_
</snip>
Just press 1 for Elevator status or 2 for making another elevator request.
TODO - Need to work on a better user interface.

2. Design issues -
   A. While searching for potential elevators, ElevatorController
      looks only for elevators that are moving in the same direction as
      that of request or are in stationary. i.e. If request comes to 
      move UP, then ElevatorController will search through only those
      elevators who are moving up or stationary. 
      This limitation sometimes makes ElevatorController to skip more
      optimal elevators. This can be improved if ElevatorController
      takes into accout the target floor of each moving elevator 
      and not just the general direction. 
   B. If elevator is moving in an opposite direction to the requested 
      direction, it is possible for an elevator to go pass the original
      requested floor. This happens when the second request comes who's 
      target floor is beyond requested floor of the original request. 
      This results in a delayed service for the original request. This 
      can be fixed if elevators are able to hand over their respective
      requests to each other. 
	

-------------------------
Unit Tests
-------------------------
ElevatorControllerTest.java  
ElevatorRequestTest.java


