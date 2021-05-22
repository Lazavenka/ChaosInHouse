package service;

import domain.Elevator;
import domain.Floor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;

import java.util.concurrent.TimeUnit;

@AllArgsConstructor
public class Task implements Runnable{
    @Getter
    private final Floor currentFloor;
    @Getter
    private final Floor destinationFloor;
    private final Elevator elevator;
    @Getter
    private final int direction;
    @SneakyThrows
    @Override
    public void run() {
        closeOpenDoors(); //close doors
        for (int i = 0; i < getNumberOfFloorsToMove(); i++){
            TimeUnit.MILLISECONDS.sleep(movementLagInMills());
            elevator.move();
        }
        closeOpenDoors(); //open open doors
    }
    private int movementLagInMills(){
        return (int) (currentFloor.getFloorHeight() / elevator.getElevatorSpeed() * 1000);
    }
    private int getNumberOfFloorsToMove(){
        return Math.abs(destinationFloor.getFloorNumber() - currentFloor.getFloorNumber());
    }
    @SneakyThrows
    private void closeOpenDoors(){
        TimeUnit.MILLISECONDS.sleep(elevator.getDoorsOpenCloseLag());
    }
}
