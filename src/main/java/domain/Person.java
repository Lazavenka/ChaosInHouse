package domain;

import lombok.AllArgsConstructor;
import lombok.Generated;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;
import java.util.concurrent.ThreadLocalRandom;

@Generated
@Getter
@AllArgsConstructor
public class Person {
    private final int weight;
    private final String name;
    private final LocalTime spawnTime;
    @Setter
    private LocalTime enterElevatorTime;
    private final int destinationFloor;

    public Person(int spawnFloorNumber, int maxFloor){
        this.weight = ThreadLocalRandom.current().nextInt(20, 150);
        this.name = "RandomName";
        this.destinationFloor = nextRandomIntExceptOneNumber(maxFloor, spawnFloorNumber);
        this.spawnTime = LocalTime.now();
    }

    private int nextRandomIntExceptOneNumber(int bound, int except){
        int result = ThreadLocalRandom.current().nextInt(1,bound);
        while (result == except){
            result = ThreadLocalRandom.current().nextInt(1,bound);
        }
        return result;
    }

    @Override
    public String toString() {
        return  name + ", weight = " + weight + ", destinationFloor = " + destinationFloor;
    }
}
