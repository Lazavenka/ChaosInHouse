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
@AllArgsConstructor
public class ElevatorController {

    private final BlockingQueue<MoveTask> moveTasksUp = new PriorityBlockingQueue<>(50, new TaskComparator());
    private final BlockingQueue<MoveTask> moveTasksDown = new PriorityBlockingQueue<>(50, new TaskComparator().reversed());
    private final House house;

    @SneakyThrows
    public void addTask(MoveTask moveTask, Elevator elevator) {
        final BlockingQueue<MoveTask> taskQueueByDirection = getTaskQueue(moveTask.getDirection());
        if (!taskQueueByDirection.contains(moveTask)) {
            taskQueueByDirection.put(moveTask);
//            log.debug("Add task " + moveTask);
            elevator.setIdle(false);
        }

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

    private BlockingQueue<MoveTask> getTaskQueue(Direction direction) {
        return direction.equals(Direction.UP) ? moveTasksUp : moveTasksDown;
    }

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
        generateTasksByElevatorButtons(elevator).forEach(task -> addTask(task, elevator));
    }
    private List<MoveTask> generateTasksByElevatorButtons(Elevator elevator){
        final int[] floorsButtonOn = elevator.getButtonsFloors()
                .entrySet().stream().filter(Map.Entry::getValue)
                .mapToInt(Map.Entry::getKey)
                .toArray();
        return getTasksByFloorsArray(elevator, floorsButtonOn);
    }
    private List<MoveTask> getTasksByFloorsArray(Elevator elevator, int...floors){
        final List<MoveTask> MoveTasks = new ArrayList<>(floors.length);
        for (int destinationFloor: floors) {
            MoveTasks.add(new MoveTask(house.getFloorByNumber(destinationFloor), elevator, elevator.getDirection(), false));
        }
        return MoveTasks;
    }
    // returns the queue corresponding to the direction of elevator
    private Queue<Person> getPersonQueue(Floor floor, Elevator elevator) {
        return elevator.getDirection() == Direction.UP ? floor.getPersonQueueUp() : floor.getPersonQueueDown();
    }


    public void dropPassengers(Elevator elevator) {
        final List<Person> personList = elevator.getPersonList();
        for (Person person : personList) {
            person.exitElevator(elevator);
            log.info("Person " + person + " exit from elevator");
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
        if (forced) {
            elevator.reverseDirection();
        }
    }

    //костыль
    private void reverseDirection(Elevator elevator, MoveTask task) {
        final Direction elevatorDirection = elevator.getDirection();
        final Direction taskDirection = task.getDirection();
        final boolean matchDirection = elevatorDirection.equals(taskDirection);
        final boolean floorMismatchWithSameDirection;
        if (elevatorDirection.equals(Direction.UP)) {
            floorMismatchWithSameDirection = elevator.getCurrentFloor() > task.getDestinationFloor().getFloorNumber();
        } else {
            floorMismatchWithSameDirection = elevator.getCurrentFloor() < task.getDestinationFloor().getFloorNumber();
        }
        if (matchDirection && floorMismatchWithSameDirection) {
            elevator.reverseDirection();
            task.setNeedReverse(true);
        }
    }

    @Override
    public String toString() {
        return "ElevatorController{" +
                "moveTasksUp=" + moveTasksUp.size() +
                ", moveTasksDown=" + moveTasksDown.size() +
                '}';
    }
}
