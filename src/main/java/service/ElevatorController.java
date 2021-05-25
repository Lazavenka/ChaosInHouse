package service;

import domain.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;


@Slf4j
@Getter
public class ElevatorController{

    private final BlockingQueue<MoveTask> moveTasksUp = new PriorityBlockingQueue<>(50, new TaskComparator());
    private final BlockingQueue<MoveTask> moveTasksDown = new PriorityBlockingQueue<>(50, new TaskComparator().reversed());

    @SneakyThrows
    private void addTask(MoveTask moveTask) {
        final Elevator elevator = moveTask.getElevator();
        if (moveTask.getDirection().equals(Direction.UP)) {
            this.moveTasksUp.put(moveTask);
        } else {
            this.moveTasksDown.put(moveTask);
        }
        elevator.setIdle(false);
    }

    @SneakyThrows
    public Floor completeMoveTask(Elevator elevator) {
        final Direction elevatorDirection = elevator.getDirection();
        final MoveTask moveTask = getTaskQueue(elevatorDirection).take();
        final Floor destinationFloor = moveTask.getDestinationFloor();
        reverseDirection(elevator, moveTask);
        final boolean forcedReverse = moveTask.isNeedReverse();
        moveTask.run();

        reverseDirection(elevator, forcedReverse);
        if (moveTasksDown.isEmpty() && moveTasksUp.isEmpty()) {
            elevator.setIdle(true);
        }
        return destinationFloor;
    }

    @SneakyThrows
    private BlockingQueue<MoveTask> getTaskQueue(Direction direction){
        return direction.equals(Direction.UP) ? moveTasksUp : moveTasksDown;
    }
    @SneakyThrows
    public void addPersonsToElevator(Floor floor, Elevator elevator) {
        boolean success;
        Queue<Person> personQueue = getPersonQueue(floor, elevator);
        if (!personQueue.isEmpty()) {
            do {
                final Person person = getPersonQueue(floor, elevator).poll();
                success = Objects.requireNonNull(person).enterElevator(elevator);
                floor.checkQueues();
            } while (success && !personQueue.isEmpty());
        }
    }

    // returns the queue corresponding to the direction of elevator
    private Queue<Person> getPersonQueue(Floor floor, Elevator elevator) {
        return elevator.getDirection() == Direction.UP ? floor.getPersonQueueUp() : floor.getPersonQueueDown();
    }


    public void dropPassengers(Elevator elevator) {
        final List<Person> personList = elevator.getPersonList();
        for (Person person : personList) {
            if (person.getDestinationFloor() == elevator.getCurrentFloor()) {
                elevator.removePerson(person);
                log.info("Person " + person + " exit from elevator");
            }
        }
        elevator.switchOffButton(elevator.getCurrentFloor());
    }

    private void reverseDirection(Elevator elevator, boolean forced) {
        if (elevator.getCurrentFloor() == elevator.getButtonsFloors().size()) {
            elevator.setDirectionDown();
        }
        if (elevator.getCurrentFloor() == 1) {
            elevator.setDirectionUp();
        }
        if (forced){
            elevator.reverseDirection();
        }
    }
    //костыль
    private void reverseDirection(Elevator elevator, MoveTask task){
        final Direction elevatorDirection = elevator.getDirection();
        final Direction taskDirection = task.getDirection();
        final boolean matchDirection = elevatorDirection.equals(taskDirection);
        final boolean floorMismatchWithSameDirection;
        if (elevatorDirection.equals(Direction.UP)){
            floorMismatchWithSameDirection = elevator.getCurrentFloor() > task.getDestinationFloor().getFloorNumber();
        }else {
            floorMismatchWithSameDirection = elevator.getCurrentFloor() < task.getDestinationFloor().getFloorNumber();
        }
        if (matchDirection && floorMismatchWithSameDirection){
            elevator.reverseDirection();
            task.setNeedReverse(true);
        }
    }


}
