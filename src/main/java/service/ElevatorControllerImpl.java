package service;

import domain.Elevator;
import domain.Floor;
import domain.House;
import domain.Person;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class ElevatorControllerImpl implements ElevatorController {

    @Override
    public void addPersonToElevator(Elevator elevator, House house) {
        final int elevatorDirection = elevator.getDirection();
        final int currentFloorNumber = elevator.getCurrentFloor();
        final Floor floor = house.getFloorByNumber(currentFloorNumber);
        //заменить на 1 норм метод
        if(checkDirection(elevatorDirection, floor)
                && checkFreeCapacity(elevator, floor)){
            final Optional<Person> optionalPerson = getOptionalPersonFromQueue(elevatorDirection, floor);
            optionalPerson.ifPresentOrElse(elevator::addPerson, () -> log.debug("Optional person is empty! IMPOSSIBLE!"));
            floor.checkQueues();
        }
    }

    @SneakyThrows
    private Optional<Person> getOptionalPersonFromQueue(int direction, Floor floor){
        if (direction == Elevator.DIRECTION_UP){
            return Optional.of(floor.getPersonQueueUp().take());
        }
        if (direction == Elevator.DIRECTION_DOWN){
            return Optional.of(floor.getPersonQueueDown().take());
        }
        return Optional.empty();
    }

    private boolean checkDirection(int direction, Floor floor) {
        return (direction == Elevator.DIRECTION_UP && floor.isButtonUp()) ||
                (direction == Elevator.DIRECTION_DOWN && floor.isButtonDown());
    }

    private boolean checkFreeCapacity(Elevator elevator, Floor floor) {
        final int direction = elevator.getDirection();
        final Optional<Person> optionalPerson = peekPersonFromQueue(direction, floor);
        final AtomicInteger personWeight;
        personWeight = optionalPerson.map(person -> new AtomicInteger(person.getWeight()))
                .orElseGet(() -> new AtomicInteger(0));
        return elevator.getFreeCapacity() >= personWeight.get();
    }

    private Optional<Person> peekPersonFromQueue(int direction, Floor floor){
        if (direction == Elevator.DIRECTION_UP){
            return Optional.ofNullable(floor.getPersonQueueUp().peek());
        }
        if (direction == Elevator.DIRECTION_DOWN){
            return Optional.ofNullable(floor.getPersonQueueDown().peek());
        }
        return Optional.empty();
    }
    @Override
    public void releasePassengers(Elevator elevator) {
        final List<Person> personList = elevator.getPersonList();
        for (Person person : personList) {
            if (person.getDestinationFloor() == elevator.getCurrentFloor()) {
                elevator.removePerson(person);
                log.info("Person "+person+" exit from elevator");
            }

        }
    }

    @Override
    public void move(Elevator elevator) {

    }


}
