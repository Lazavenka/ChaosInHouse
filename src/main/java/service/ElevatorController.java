package service;

import domain.Elevator;
import domain.Floor;
import domain.House;
import domain.Person;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;


@Slf4j
@AllArgsConstructor
public class ElevatorController {

    private final Elevator elevator;
    private final BlockingQueue<Task> tasks = new LinkedBlockingQueue<>();
    @SneakyThrows
    public void addTask(Task task){
        this.tasks.put(task);
    }
    @SneakyThrows
    public void completeTask(){
        this.tasks.take().run();
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
        return elevator.getDirection() == Elevator.DIRECTION_DOWN ? floor.getPersonQueueDown() : floor.getPersonQueueUp();
    }

    private boolean checkDirectionAndButton(Floor floor) {
        return (elevator.getDirection() == Elevator.DIRECTION_UP && floor.isButtonUp()) ||
                (elevator.getDirection() == Elevator.DIRECTION_DOWN && floor.isButtonDown());
    }

    private boolean checkFreeCapacity(Floor floor) {
        final Optional<Person> optionalPerson = peekPersonFromQueue(floor);
        final AtomicInteger personWeight;
        personWeight = optionalPerson.map(person -> new AtomicInteger(person.getWeight()))
                .orElseGet(() -> new AtomicInteger(0));
        return elevator.getFreeCapacity() >= personWeight.get();
    }

    private Optional<Person> peekPersonFromQueue(Floor floor) {
        return elevator.getDirection() == Elevator.DIRECTION_DOWN ?
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
        if (elevator.getCurrentFloor() == 0){
            elevator.setDirectionUp();
        }
    }

}
