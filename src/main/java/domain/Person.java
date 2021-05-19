package domain;

import lombok.AllArgsConstructor;
import lombok.Generated;
import lombok.Getter;

import javax.print.attribute.standard.PresentationDirection;
import java.util.concurrent.ThreadLocalRandom;

@Generated
@Getter
@AllArgsConstructor
public class Person {
    private final int weight;
    private final String name;

    private final int destinationFloor;

    public Person(int spawnFloorNumber, House house){
        this.weight = ThreadLocalRandom.current().nextInt(20, 150);
        this.name = "RandomName";
        this.destinationFloor = nextRandomIntExceptOneNumber(1, house.getFloorsNumber(), spawnFloorNumber);
    }

    private int nextRandomIntExceptOneNumber(int origin, int bound, int except){
        int result = ThreadLocalRandom.current().nextInt(origin,bound);
        while (result == except){
            result = ThreadLocalRandom.current().nextInt(origin,bound);
        }
        return result;
    }

    @Override
    public String toString() {
        return  name + ", weight = " + weight + ", destinationFloor = " + destinationFloor;
    }
}
