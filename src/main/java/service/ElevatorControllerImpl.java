package service;

import domain.Elevator;
import domain.Floor;
import domain.Person;
import lombok.SneakyThrows;

import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class ElevatorControllerImpl implements ElevatorController {


    @SneakyThrows
    @Override
    public void takePassenger(Elevator elevator, Floor floor) {
        final int direction = elevator.getDirection();
        if(checkDirection(elevator, floor) && checkCapacity(elevator, floor)){
            final Person person = getQueue(direction, floor).take();
            elevator.getPersonDestinationMap().put(new AtomicInteger(person.getDestinationFloor()), person);
        }
    }

    private BlockingQueue<Person> getQueue(int direction, Floor floor){
        return direction > 0 ? floor.getPersonQueueUp() : floor.getPersonQueueDown();
    }
    private boolean checkDirection(Elevator elevator, Floor floor) {
        return (elevator.getDirection() > 0 && floor.isButtonUp()) ||
                (elevator.getDirection() < 0 && floor.isButtonDown());
    }

    private boolean checkCapacity(Elevator elevator, Floor floor) {
        return elevator.getFreeCapacity() > Objects.requireNonNull(getQueue(elevator.getDirection(), floor).peek()).getWeight();
    }

    @Override
    public void releasePassenger(Elevator elevator) {
//        elevator.getPersonDestinationMap().keySet()
//                .forEach(i -> {
//                    i.get() == elevator.getCurrentFloor() ? elevator.getPersonDestinationMap().;
//                });
    }

    @Override
    public void move(Elevator elevator) {

    }


}
