package service;

import domain.Elevator;
import domain.Floor;
import domain.House;

public interface ElevatorController {
    void addPersonToElevator(Elevator elevator, House house);
    void releasePassengers(Elevator elevator);
    void move(Elevator elevator);
}
