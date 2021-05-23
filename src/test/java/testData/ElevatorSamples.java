package testData;

import domain.Elevator;

public class ElevatorSamples {
    public static Elevator getTestElevator(){
        Elevator elevator = new Elevator(15, 800, 1);
        elevator.getButtonsFloors().put(4, true);
        elevator.getButtonsFloors().put( 6, true);
        elevator.getButtonsFloors().put(9, true);
        return elevator;
    }
}