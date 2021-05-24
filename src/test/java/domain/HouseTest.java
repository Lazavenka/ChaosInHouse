package domain;

import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

class HouseTest {

    private House house;

    @Test
    void getFloorsNumber() {
        final int numberOfFloors = 10;
        house = House.ofFloors(numberOfFloors);

        final int generatedFloorsNumber = house.getFloorsNumber();

        assertThat(numberOfFloors, is(equalTo(generatedFloorsNumber)));
    }
    @Test

    void getFloorByNumber() {
        house = House.ofFloors(10);
        final int floorNumber = 5;
        System.out.println(floorNumber);
        final Floor floor = house.getFloorByNumber(floorNumber);
        assertThat(floor, is(notNullValue()));
        final int floorFromMethod = floor.getFloorNumber();

        assertThat(floorNumber, is(equalTo(floorFromMethod)));
    }
    @Test
    void getFloorByNumber_lastFloor() {
        final int floorNumber = 10;
        house = House.ofFloors(floorNumber);

        final Floor floor = house.getFloorByNumber(floorNumber);
        System.out.println(floor);
        assertThat(floor, is(notNullValue()));
        final int floorFromMethod = floor.getFloorNumber();

        assertThat(floorNumber, is(equalTo(floorFromMethod)));
    }
    @Test
    void getElevatorsNumber() {
        final int numberOfElevators = 1;
        house = House.ofFloorsAndElevators(10, numberOfElevators);
        final int generatedElevatorsNumber = house.getElevatorsNumber();

        assertThat(numberOfElevators, is(equalTo(generatedElevatorsNumber)));
    }
    @Test
    void getElevatorByNumber() {
        house = House.ofFloorsAndElevators(10, 4);
        house.printHouseInfo();
        final int elevatorId = 2;
        final Elevator elevator = house.getElevatorByNumber(elevatorId);
        assertThat(elevator, is(notNullValue()));
        final int elevatorFromMethodId = elevator.getId();
        assertThat(elevatorId, is(equalTo(elevatorFromMethodId)));
    }
    @Test
    void getElevatorByNumber_lastElevator() {
        final int numberOfElevators = 4;
        house = House.ofFloorsAndElevators(10, numberOfElevators);
        house.printHouseInfo();
        final int elevatorId = 4;
        final Elevator elevator = house.getElevatorByNumber(elevatorId);
        assertThat(elevator, is(notNullValue()));
        final int elevatorFromMethodId = elevator.getId();
        assertThat(elevatorId, is(equalTo(elevatorFromMethodId)));
    }



    @Test
    void testGetElevatorByNumber() {
    }


}