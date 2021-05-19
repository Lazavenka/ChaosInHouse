package service;

import domain.Elevator;
import domain.Floor;

public interface ElevatorController {
    void takePassenger(Elevator elevator, Floor floor);
    void releasePassenger(Elevator elevator);
    void move(Elevator elevator);
}
