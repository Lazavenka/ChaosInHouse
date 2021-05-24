package service;

import domain.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;


@Slf4j
@Getter
public class ElevatorController{

    private final BlockingQueue<MoveTask> moveTasksUp = new PriorityBlockingQueue<>(10, new TaskComparator());
    private final BlockingQueue<MoveTask> moveTasksDown = new PriorityBlockingQueue<>(10, new TaskComparator().reversed());

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
    public void completeMoveTask(Elevator elevator) {
        final Direction elevatorDirection = elevator.getDirection();
        this.moveTasksUp.take().run(); //сделать выбор очереди по направлению движения
        if (moveTasksDown.isEmpty()) {
            elevator.setIdle(true);
        }
    }
    @SneakyThrows
    public void addPersonsToElevator(Floor floor, Elevator elevator) {
        boolean success;
        do {
            final Person person = getPersonQueue(floor, elevator).take();
            success = person.enterElevator(elevator);
            floor.checkQueues();
        } while (success);
        generateTasksByElevatorButtons(elevator).forEach(this::addTask);
    }
    private List<MoveTask> generateTasksByElevatorButtons(Elevator elevator){
        final int[] floorsButtonOn = elevator.getButtonsFloors()
                .entrySet().stream().filter(Map.Entry::getValue)
                .mapToInt(Map.Entry::getKey)
                .toArray();
        return getTasksByFloorsArray(elevator, floorsButtonOn);
    }
    private List<MoveTask> getTasksByFloorsArray(Elevator elevator, int...floors){
        final List<MoveTask> moveTasks = new ArrayList<>(floors.length);
        for (int destinationFloor: floors) {
            moveTasks.add(new MoveTask(destinationFloor, elevator, elevator.getDirection()));
        }
        return moveTasks;
    }
    // returns the queue corresponding to the direction of elevator
    private BlockingQueue<Person> getPersonQueue(Floor floor, Elevator elevator) {
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

    @SneakyThrows
    public void controlDirection(Elevator elevator) {
        if (elevator.getCurrentFloor() == elevator.getButtonsFloors().size()) {
            elevator.setDirectionDown();
        }
        if (elevator.getCurrentFloor() == 1) {
            elevator.setDirectionUp();
        }
    }


}
