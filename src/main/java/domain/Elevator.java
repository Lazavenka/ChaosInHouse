package domain;


import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.time.LocalTime;
import java.util.*;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

@Slf4j
@Getter
public class Elevator {

    public static final int DIRECTION_UP = 1;
    public static final int DIRECTION_DOWN = -1;

    private final int elevatorMoveLag = 4_200; //mills
    private final int doorsOpenCloseLag = 3_000; //mills

    private final int id;
    private int direction;
    private final AtomicInteger currentFloor;

    private final int liftingCapacity;

    private final List<Person> personList = new ArrayList<>();
    private final Map<Integer, Boolean> buttonsFloors;

    public Elevator(int numberOfFloors, int liftingCapacity, int id) {
        this.direction = DIRECTION_UP;
        this.currentFloor = new AtomicInteger(1);
        this.buttonsFloors = new HashMap<>(numberOfFloors);
        IntStream.range(0, numberOfFloors).forEach(i -> this.buttonsFloors.put(i+1, false)); //номера этажей начиная с 1
        this.liftingCapacity = liftingCapacity;
        this.id = id;
    }

    public void setDirectionUp() {
        this.direction = DIRECTION_UP;
    }

    public void setDirectionDown() {
        this.direction = DIRECTION_DOWN;
    }

    public void move() {
        this.currentFloor.addAndGet(direction);
    }

    public int getCurrentFloor() {
        return currentFloor.get();
    }

    public void addPerson(Person person) {
        final long elapsedSeconds = Duration.between(LocalTime.now(), person.getSpawnTime()).getSeconds();
        person.setEnterElevatorTime(LocalTime.now());
        this.buttonsFloors.put(person.getDestinationFloor(), true);
        log.info("Person " + person + " wait elevator in queue " + elapsedSeconds + " sec.");
    }

    public void removePerson(Person person) {
        final long elapsedSeconds = Duration.between(LocalTime.now(), person.getEnterElevatorTime()).getSeconds();
        this.personList.remove(person);

        log.info("Person " + person + " move in elevator " + elapsedSeconds + " sec.");
    }

    public Map<Integer, Boolean> getButtonsFloors() {
        return buttonsFloors;
    }

    public void switchOffButton(int floorNumber) {
        this.buttonsFloors.put(floorNumber, false);
    }

    public int getFreeCapacity() {
        final int totalWeight = personList.stream().mapToInt(Person::getWeight).sum();
        return liftingCapacity - totalWeight;
    }

    public String getButtons() {
        return buttonsFloors.toString();
    }

    @Override
    public String toString() {
        String direction = this.direction == DIRECTION_UP ? "UP" : "DOWN";
        return "Elevator " + id +
                ", direction " + direction +
                ", currentFloor " + currentFloor +
                ", liftingCapacity " + liftingCapacity;
    }
}
