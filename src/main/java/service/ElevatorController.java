package service;

import domain.House;

public interface ElevatorController {
    void addTask(Task task);
    void completeTask();
    int getCurrentFloorNumber();
    void addPersonToElevator(House house);
    void releasePassengers();
    void controlDirection(House house);
}
