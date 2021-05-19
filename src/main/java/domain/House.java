package domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class House {
    private final List<Floor> floors = new ArrayList<>();
    private final List<Elevator> elevators = new ArrayList<>();

    private House(int numberOfFloors){
        IntStream.range(1,numberOfFloors).forEach(i -> floors.add(new Floor(i)));
    }

    public static House ofFloors(int numberOfFloors){
        return new House(numberOfFloors);
    }

    public Floor getFloorByNumber(int floorNumber){
        return floors.get(floorNumber);
    }
    public int getFloorsNumber(){
        return floors.size();
    }
}
