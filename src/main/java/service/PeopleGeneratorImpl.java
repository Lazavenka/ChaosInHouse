package service;

import domain.House;
import domain.Person;
import lombok.AllArgsConstructor;

import java.util.concurrent.ThreadLocalRandom;

@AllArgsConstructor
public class PeopleGeneratorImpl implements PeopleGenerator {
    private final int personGenerationRate; //person per period

    @Override
    public void generatePeople(House house) {
        final ThreadLocalRandom random = ThreadLocalRandom.current();
        final int maxFloor = house.getFloorsNumber();
        for (int i = 0; i<personGenerationRate; i++) {
            final int floorToGeneratePerson = random.nextInt(1, maxFloor);
            Person person = new Person(floorToGeneratePerson, maxFloor);
            house.getFloorByNumber(floorToGeneratePerson).distributeByQueues(person);
        }
    }

    public int getPersonGenerationRate(){
        return personGenerationRate;
    }
}
