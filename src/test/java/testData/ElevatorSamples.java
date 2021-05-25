package testData;

import domain.Elevator;
import domain.House;
import service.ElevatorController;

public class ElevatorSamples {
    public static Elevator getTestElevator(House house){
        Elevator elevator = new Elevator(15, 800, 1, new ElevatorController(house));
        elevator.getButtonsFloors().put(4, true);
        elevator.getButtonsFloors().put( 6, true);
        elevator.getButtonsFloors().put(9, true);
        return elevator;
    }
}
