package domain;


import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import service.ElevatorController;

import java.time.Duration;
import java.time.LocalTime;
import java.util.*;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

@Slf4j
@Getter
public class Elevator implements Runnable{

    private final int elevatorMoveLag = 1_200; //mills
    private final int doorsOpenCloseLag = 1_000; //mills

    private final int id;
    private Direction direction;
    @Setter
    private boolean idle;
    @Setter
    private boolean isOpenDoors;
    private final AtomicInteger currentFloor;

    private final int liftingCapacity;

    private final List<Person> personList = new ArrayList<>();
    private final Map<Integer, Boolean> buttonsFloors;

    private final ElevatorController elevatorController;

    public Elevator(int numberOfFloors, int liftingCapacity, int id, ElevatorController elevatorController) {
        this.direction = Direction.UP;
        this.currentFloor = new AtomicInteger(1);
        this.buttonsFloors = new HashMap<>(numberOfFloors);
        IntStream.range(0, numberOfFloors).forEach(i -> this.buttonsFloors.put(i + 1, false)); //номера этажей начиная с 1
        this.liftingCapacity = liftingCapacity;
        this.idle = true;
        this.id = id;
        this.elevatorController = elevatorController;
    }

    public void setDirectionUp() {
        this.direction = Direction.UP;
    }

    public void setDirectionDown() {
        this.direction = Direction.DOWN;
    }
    public void reverseDirection(){
        if (this.direction.equals(Direction.UP)) {
            setDirectionDown();
        } else {
            setDirectionUp();
        }
    }
    public void move() {
        int delta = this.direction.equals(Direction.UP) ? 1 : -1;
        this.currentFloor.addAndGet(delta);
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
        this.buttonsFloors.put(person.getDestinationFloor(), false);
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
    public void run() {
        Floor destinationFloor;
        while (true){ //Поправить на правильное условие процесса
            destinationFloor = this.elevatorController.completeMoveTask(this);
            this.elevatorController.dropPassengers(this);
            this.elevatorController.addPersonsToElevator(destinationFloor,this);
        }
    }
    @Override
    public String toString() {
        return "Elevator " + id +
                ", direction " + direction +
                ", currentFloor " + currentFloor +
                ", liftingCapacity " + liftingCapacity;
    }

}
