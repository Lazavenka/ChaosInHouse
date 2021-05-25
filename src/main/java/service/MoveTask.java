package service;

import domain.Direction;
import domain.Elevator;
import domain.Floor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;


import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Slf4j
@AllArgsConstructor
@Getter
public class MoveTask implements Runnable, Comparable<MoveTask> {
    private final Floor destinationFloor;
    private final Elevator elevator;
    private final Direction direction;
    @Setter
    private boolean needReverse;
    @Override
    public void run() {
        if (elevator.getCurrentFloor() != destinationFloor.getFloorNumber()) {
            log.info(elevator + " closing doors.");
            closeDoors();
            log.info(elevator + " start moving.");
            move();
            log.info(elevator + " arrived.");
        }
        openDoors();
        log.info(elevator + " opens doors.");
    }

    @SneakyThrows
    private void move() {
        while (elevator.getCurrentFloor() != destinationFloor.getFloorNumber()) {
            TimeUnit.MILLISECONDS.sleep(elevator.getElevatorMoveLag());
            elevator.move();
            log.info(elevator + " on " + elevator.getCurrentFloor() + " floor.");
        }
    }

    @SneakyThrows
    private void closeDoors() {
        if (elevator.isOpenDoors()) {
            TimeUnit.MILLISECONDS.sleep(elevator.getDoorsOpenCloseLag());
            elevator.setOpenDoors(false);
        }
    }

    @SneakyThrows
    private void openDoors() {
        if(!elevator.isOpenDoors()) {
            TimeUnit.MILLISECONDS.sleep(elevator.getDoorsOpenCloseLag());
            elevator.setOpenDoors(true);
        }
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MoveTask moveTask = (MoveTask) o;
        return destinationFloor == moveTask.destinationFloor;
    }

    @Override
    public int hashCode() {
        return Objects.hash(destinationFloor);
    }

    @Override
    public String toString() {
        return "Task{" +
                "destinationFloor=" + destinationFloor.getFloorNumber() +
                ", direction=" + direction +
                '}';
    }

    @Override
    public int compareTo(MoveTask o) {
        return this.destinationFloor.getFloorNumber() - o.getDestinationFloor().getFloorNumber();
    }

}
