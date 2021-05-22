package domain;

import com.google.common.base.Preconditions;
import lombok.Getter;
import service.ElevatorController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Getter
public class House {
    private final List<Floor> floors;
    private final List<Elevator> elevators;
    private final List<ElevatorController> elevatorControllers;

    private House(int numberOfFloors) {
        this(numberOfFloors, 1);
    }

    private House(int numberOfFloors, int numberOfElevators) {
        this(numberOfFloors, numberOfElevators, 800);
    }

    private House(int numberOfFloors, int numberOfElevators, int elevatorLiftingCapacity) {
        this.floors = new ArrayList<>();
        this.elevators = new ArrayList<>();
        this.elevatorControllers = new ArrayList<>();
        IntStream.range(0, numberOfFloors).forEach(i -> floors.add(new Floor(i+1)));
        IntStream.range(0, numberOfElevators)
                .forEach(i -> elevators.add(new Elevator(numberOfFloors, elevatorLiftingCapacity, i+1)));
        IntStream.range(0, numberOfElevators)
                .forEach(i -> elevatorControllers.add(new ElevatorController(elevators.get(i))));
    }

    public static House ofFloors(int numberOfFloors) {
        Preconditions.checkArgument(numberOfFloors > 0, "Incorrect number of floors!");
        return new House(numberOfFloors);
    }

    public static House ofFloorsAndElevators(int numberOfFloors, int numberOfElevators) {
        Preconditions.checkArgument(numberOfFloors > 0, "Number of floors must be positive!");
        Preconditions.checkArgument(numberOfElevators > 0, "Number of elevators must be positive!");
        return new House(numberOfFloors, numberOfElevators);
    }

    public static House ofCustomizeAll(int numberOfFloors, int numberOfElevators, int elevatorLiftingCapacity) {
        Preconditions.checkArgument(numberOfFloors > 0, "Number of floors must be positive!");
        Preconditions.checkArgument(numberOfElevators > 0, "Number of elevators must be positive!");
        Preconditions.checkArgument(elevatorLiftingCapacity > 0, "Elevator lifting capacity must be positive!");
        return new House(numberOfFloors, numberOfElevators, elevatorLiftingCapacity);
    }

    public Floor getFloorByNumber(int floorNumber) {
        Preconditions.checkArgument(floorNumber > 0 && floorNumber < getFloorsNumber(),
                "Check args! Incorrect floor number");
        return floors.stream().filter(floor -> floor.getFloorNumber() == floorNumber)
                .findFirst()
                .orElseThrow();
    }

    public int getFloorsNumber() {
        return floors.size();
    }

    public Elevator getElevatorByNumber(int elevatorNumber) {
        Preconditions.checkArgument(elevatorNumber > 0 && elevatorNumber < getElevatorsNumber(),
                "Check args! Incorrect elevator number");
        return elevators.stream().filter(elevator -> elevator.getId() == elevatorNumber)
                .findFirst()
                .orElseThrow();
    }

    public int getElevatorsNumber() {
        return elevators.size();
    }



    public void printHouseInfo() {
        System.out.println(this);
        floors.forEach(System.out::println);
        elevators.forEach(System.out::println);
    }

    @Override
    public String toString() {
        return "House: " + floors.size() + " floors, " + elevators.size() + " elevators.";
    }
}
