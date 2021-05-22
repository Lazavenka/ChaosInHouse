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
    private final int currentFloor;
    @Getter
    private final int destinationFloor;
    private final Elevator elevator;

    @Override
    public void run() {
        closeOpenDoors(); //close doors
        move();
        changeElevatorDirectionIfNeed();
        closeOpenDoors(); //open open doors
    }
    @SneakyThrows
    private void move(){
        final int countFloorsToMove = getNumberOfFloorsToMove();
        for (int i = 0; i < countFloorsToMove; i++){
            TimeUnit.MILLISECONDS.sleep(elevator.getElevatorMoveLag());
            elevator.move();
        }
    }
    private void changeElevatorDirectionIfNeed(){
        if(currentFloor < destinationFloor){
            elevator.setDirectionUp();
        }else {
            elevator.setDirectionDown();
        }
    }

    private int getNumberOfFloorsToMove(){
        return Math.abs(destinationFloor - currentFloor);
    }
    @SneakyThrows
    private void closeOpenDoors(){
        TimeUnit.MILLISECONDS.sleep(elevator.getDoorsOpenCloseLag());
    }

}
