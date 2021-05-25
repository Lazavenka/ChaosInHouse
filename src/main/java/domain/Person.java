package domain;

import lombok.*;

import java.time.LocalTime;
import java.util.concurrent.BlockingQueue;
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
    private final int spawnFloor;
    private final int destinationFloor;
    private final Direction direction;
    public Person(int spawnFloorNumber, int maxFloor, String id){
        this.weight = ThreadLocalRandom.current().nextInt(20, 150);
        this.name = id;
        this.spawnFloor = spawnFloorNumber;
        this.destinationFloor = generateDestinationFloor(maxFloor, spawnFloorNumber);
        this.spawnTime = LocalTime.now();
        this.direction = this.spawnFloor < this.destinationFloor ? Direction.UP : Direction.DOWN;
    }

    private int generateDestinationFloor(int maxFloor, int except){
        int result = ThreadLocalRandom.current().nextInt(1,maxFloor+1);
        while (result == except){
            result = ThreadLocalRandom.current().nextInt(1,maxFloor+1);
        }
        return result;
    }
    public boolean enterElevator(Elevator elevator){
        //заменить на класс проверяльщик
        if ((this.spawnFloor == elevator.getCurrentFloor() || this.direction.equals(elevator.getDirection()))
                && (elevator.getFreeCapacity() >=this.weight) && elevator.isOpenDoors()){
            elevator.addPerson(this);
            return true;
        } else {
            return false;
        }
    }
    public void exitElevator(Elevator elevator){
        if (this.destinationFloor == elevator.getCurrentFloor() && elevator.isOpenDoors())
        elevator.removePerson(this);
    }

    @SneakyThrows
    public void getInLine(Floor floor){
        if (this.direction.equals(Direction.UP)){
            floor.getPersonQueueUp().offer(this);
            floor.setButtonUp(true);
        } else {
            floor.getPersonQueueDown().offer(this);
            floor.setButtonDown(true);
        }
    }

    @Override
    public String toString() {
        return  name + ", weight = " + weight +", spawn = " + spawnFloor +", dest = " + destinationFloor;
    }
}
