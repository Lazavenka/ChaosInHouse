package service;

import domain.Elevator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;

import java.util.concurrent.TimeUnit;

@AllArgsConstructor
public class Task implements Runnable{
    @Getter
    private final int destinationFloor;
    private final Elevator elevator;
    @Getter
    private final int direction;

    @Override
    public void run() {
        closeDoors(); //close doors
        move();
        openDoors(); //open open doors
    }
    @SneakyThrows
    private void move(){
        while(elevator.getCurrentFloor()!=destinationFloor){
            TimeUnit.MILLISECONDS.sleep(elevator.getElevatorMoveLag());
            elevator.move();
        }
    }

    @SneakyThrows
    private void closeDoors(){
        TimeUnit.MILLISECONDS.sleep(elevator.getDoorsOpenCloseLag());
        elevator.setOpenDoors(false);
    }
    @SneakyThrows
    private void openDoors(){
        TimeUnit.MILLISECONDS.sleep(elevator.getDoorsOpenCloseLag());
        elevator.setOpenDoors(true);
    }

}
