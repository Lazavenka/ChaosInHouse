package service;

import domain.Elevator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;

import java.util.Comparator;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@AllArgsConstructor
public class Task implements Runnable, Comparable<Task> {
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
