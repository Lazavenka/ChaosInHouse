package service;

import domain.House;
import domain.Person;
import org.junit.jupiter.api.Test;

import java.util.concurrent.BlockingQueue;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class PeopleGeneratorImplTest {
    final int generationRate = 10; //person per iteration
    final PeopleGenerator peopleGenerator = new PeopleGeneratorImpl(generationRate);
    @Test
    void generatePeople_checkPeopleNumber() {
        final int iterationsNumber = 100;
        final int personNumber = generationRate * iterationsNumber;

        final House house = getHouseWithGeneratedPeople(iterationsNumber);

        final int generatedPersonNumberInHouse = house.getPersonCount();

        assertThat(personNumber, is(equalTo(generatedPersonNumberInHouse)));
    }
    @Test
    void generatePeople_checkQueues() {
        final int iterationsNumber = 1000;
        final House house = getHouseWithGeneratedPeople(iterationsNumber);
        house.printHouseInfo();
        assertThat(getFirstFloorQueueDown(house).isEmpty(), is(true));
        assertThat(getLastFloorQueueUp(house).isEmpty(), is(true));

    }
    private House getHouseWithGeneratedPeople(int iterationsNumber){
        final House house = House.ofFloorsAndElevators(10, 5);
        for (int i = 0; i < iterationsNumber; i++) {
            peopleGenerator.generatePeople(house);
        }
        return house;
    }
    private BlockingQueue<Person> getFirstFloorQueueDown(House house){
        return house.getFloorByNumber(1).getPersonQueueDown();
    }
    private BlockingQueue<Person> getLastFloorQueueUp(House house){
        return house.getFloorByNumber(house.getFloorsNumber()).getPersonQueueUp();
    }
}