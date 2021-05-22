package service;

import domain.House;

public interface ElevatorController {
    void addPersonToElevator(House house);
    void releasePassengers();
    void controlDirection(House house);
}
