package domain;


import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
public class Elevator {

    private static final int DIRECTION_UP = 1;
    private static final int DIRECTION_NONE = 0;
    private static final int DIRECTION_DOWN = -1;

    private static final float ELEVATOR_SPEED = 1.8f; //meter per second
    private static final int DOORS_OPEN_SPEED = 3_000; //milliseconds

    private int direction;
    private final AtomicInteger currentFloor;

    private final int liftingCapacity;

    private final Map<AtomicInteger, Person> personDestinationMap = new HashMap<>();

    public Elevator(int liftingCapacity) {
        this.direction = DIRECTION_NONE;
        this.currentFloor = new AtomicInteger(1);
        this.liftingCapacity = liftingCapacity;
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
    public int getFreeCapacity(){
        final int totalWeight = personDestinationMap.values()
                .stream().mapToInt(Person::getWeight).sum();
        return liftingCapacity - totalWeight;
    }
}
