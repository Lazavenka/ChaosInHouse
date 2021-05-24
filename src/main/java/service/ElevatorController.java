package service;

import domain.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;


@Slf4j
@AllArgsConstructor
@Getter
public class ElevatorController {

    private final Elevator elevator;
    private final BlockingQueue<Task> tasksMoveUp = new PriorityBlockingQueue<>(10, new TaskComparator());
    private final BlockingQueue<Task> tasksMoveDown = new PriorityBlockingQueue<>(10, new TaskComparator().reversed());
    @SneakyThrows
    public void addTask(Task task){
        if (task.getDirection().equals(Direction.UP)) {
            this.tasksMoveUp.put(task);
        } else {
            this.tasksMoveDown.put(task);
        }
        elevator.setIdle(false);
    }
    @SneakyThrows
    public void completeTask(){
        this.tasksMoveUp.take().run(); //сделать выбор очереди по направлению движения
        if (tasksMoveUp.isEmpty()){
            elevator.setIdle(true);
        }

    }
    public int getCurrentFloorNumber(){
        return this.elevator.getCurrentFloor();
    }
    @SneakyThrows
    public void addPersonToElevator(House house) {
        final int currentFloorNumber = elevator.getCurrentFloor();
        final Floor floor = house.getFloorByNumber(currentFloorNumber);
        while (checkDirectionAndButton(floor) && checkFreeCapacity(floor)) { //rename methods
            final Person person = getPersonQueue(floor).take();
            elevator.addPerson(person);
            floor.checkQueues();
        }
    }
    // returns the queue corresponding to the direction of elevator
    private BlockingQueue<Person> getPersonQueue(Floor floor) {
        return elevator.getDirection() == Direction.DOWN ? floor.getPersonQueueDown() : floor.getPersonQueueUp();
    }

    private boolean checkDirectionAndButton(Floor floor) {
        return (elevator.getDirection() == Direction.UP && floor.isButtonUp()) ||
                (elevator.getDirection() == Direction.UP && floor.isButtonDown());
    }

    private boolean checkFreeCapacity(Floor floor) {
        final Optional<Person> optionalPerson = peekPersonFromQueue(floor);
        final AtomicInteger personWeight;
        personWeight = optionalPerson.map(person -> new AtomicInteger(person.getWeight()))
                .orElseGet(() -> new AtomicInteger(0));
        return elevator.getFreeCapacity() >= personWeight.get();
    }

    private Optional<Person> peekPersonFromQueue(Floor floor) {
        return elevator.getDirection() == Direction.DOWN ?
                Optional.ofNullable(floor.getPersonQueueDown().peek()) :
                Optional.ofNullable(floor.getPersonQueueUp().peek());
    }

    public void releasePassengers() {
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
    public void controlDirection(House house) {
        if (elevator.getCurrentFloor() == house.getFloorsNumber()){
            elevator.setDirectionDown();
        }
        if (elevator.getCurrentFloor() == 1){
            elevator.setDirectionUp();
        }
    }

}
