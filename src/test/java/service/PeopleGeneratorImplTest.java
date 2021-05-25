package service;

import domain.House;
import domain.Person;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Queue;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

class PeopleGeneratorImplTest {
    private final static int GENERATION_RATE = 10; //person per iteration

    private House house;
    private PeopleGenerator peopleGenerator;
    @BeforeEach
    void before(){
        house = House.ofFloorsAndElevators(10, 5);
        peopleGenerator = new PeopleGeneratorImpl(GENERATION_RATE, house);
    }
    @Test
    void generatePeople_checkPeopleNumber() {
        final int iterationsNumber = 100;
        final int personNumber = GENERATION_RATE * iterationsNumber;

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
        for (int i = 0; i < iterationsNumber; i++) {
            peopleGenerator.generatePeople(house);
        }
        return house;
    }
    private Queue<Person> getFirstFloorQueueDown(House house){
        return house.getFloorByNumber(1).getPersonQueueDown();
    }
    private Queue<Person> getLastFloorQueueUp(House house){
        return house.getFloorByNumber(house.getFloorsNumber()).getPersonQueueUp();
    }
}