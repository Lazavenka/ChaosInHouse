package service;

import domain.House;

public interface TaskProducer {
    void addTask(ElevatorController elevatorController, House house);
}
