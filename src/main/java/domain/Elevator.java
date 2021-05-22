package domain;


import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import java.util.concurrent.atomic.AtomicInteger;
@Slf4j
@Getter
public class Elevator {

    public static final int DIRECTION_UP = 1;
    private static final int IDLE = 0;
    public static final int DIRECTION_DOWN = -1;

    private static final float ELEVATOR_SPEED = 1.8f; //meter per second
    private static final int DOORS_OPEN_SPEED = 3_000; //milliseconds

    private final int id;
    private int direction;
    private final AtomicInteger currentFloor;

    private final int liftingCapacity;

    private final List<Person> personList = new ArrayList<>();

    public Elevator(int liftingCapacity, int number) {
        this.direction = IDLE;
        this.currentFloor = new AtomicInteger(1);
        this.liftingCapacity = liftingCapacity;
        this.id = number;
    }

    public void setDirectionUp() {
        this.direction = DIRECTION_UP;
    }

    public void setDirectionDown() {
        this.direction = DIRECTION_DOWN;
    }

    public void moveUp() {
        this.currentFloor.incrementAndGet();
    }

    public void moveDown() {
        this.currentFloor.decrementAndGet();
    }
    public int getCurrentFloor(){
        return currentFloor.get();
    }
    public void addPerson(Person person){
            final long elapsedSeconds = Duration.between(LocalTime.now(), person.getSpawnTime()).getSeconds();
            person.setEnterElevatorTime(LocalTime.now());
            log.info("Person " + person + " wait elevator in queue " + elapsedSeconds + " sec.");
    }
    public void removePerson(Person person){
        final long elapsedSeconds = Duration.between(LocalTime.now(), person.getEnterElevatorTime()).getSeconds();
        this.getPersonList().remove(person);
        log.info("Person "+person +" move in elevator " + elapsedSeconds +" sec.");
    }

    public int getFreeCapacity(){
        final int totalWeight = personList.stream().mapToInt(Person::getWeight).sum();
        return liftingCapacity - totalWeight;
    }

    @Override
    public String toString() {
        return "Elevator " + id +
                ", direction " + direction +
                ", currentFloor " + currentFloor +
                ", liftingCapacity " + liftingCapacity;
    }
}
