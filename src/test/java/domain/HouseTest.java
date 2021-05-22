package domain;

import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

class HouseTest {

    private House house;


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
    void getElevatorByNumber() {
        house = House.ofFloorsAndElevators(10, 4);
        house.printHouseInfo();
        final int elevatorId = 2;
        final Elevator elevator = house.getElevatorByNumber(elevatorId);
        assertThat(elevator, is(notNullValue()));
        final int elevatorFromMethodId = elevator.getId();
        assertThat(elevatorId, is(equalTo(elevatorFromMethodId)));
    }
}