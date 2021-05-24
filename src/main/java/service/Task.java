package service;

import domain.Direction;
import domain.Elevator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;


import java.util.Objects;
import java.util.concurrent.TimeUnit;
@Slf4j
@AllArgsConstructor
public class Task implements Runnable, Comparable<Task> {
    @Getter
    private final int destinationFloor;
    private final Elevator elevator;
    @Getter
    private final Direction direction;

    @Override
    public void run() {
        if(elevator.getCurrentFloor() != destinationFloor) {
            log.info("Elevator " + elevator + " closing doors.");
            closeDoors();
            log.info("Elevator " + elevator + " start moving.");
            move();
            log.info("Elevator " + elevator + " arrived.");
        }
        openDoors();
        log.info("Elevator " + elevator + " opens doors.");
    }
    @SneakyThrows
    private void move(){
        while(elevator.getCurrentFloor()!=destinationFloor){
            TimeUnit.MILLISECONDS.sleep(elevator.getElevatorMoveLag());
            elevator.move();
            log.info("Elevator " + elevator + " on " + elevator.getCurrentFloor() + " floor.");
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



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return destinationFloor == task.destinationFloor;
    }

    @Override
    public int hashCode() {
        return Objects.hash(destinationFloor);
    }

    @Override
    public String toString() {
        return "Task{" +
                "destinationFloor=" + destinationFloor +
                ", direction=" + direction +
                '}';
    }

    @Override
    public int compareTo(Task o) {
        return this.destinationFloor-o.getDestinationFloor();
    }

}
