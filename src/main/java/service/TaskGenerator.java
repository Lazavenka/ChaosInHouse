package service;

import domain.Direction;
import domain.Elevator;
import domain.Floor;
import domain.House;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
@Slf4j
@AllArgsConstructor
public class TaskGenerator implements Runnable{
    private final House house;
    private final PeopleGenerator peopleGenerator;
    private static final long GENERATION_DELAY = 10_000; //mills

    public void distributeTasks(){
        List<Elevator> elevators = this.house.getElevators();

        final Optional<Elevator> optElevatorIdle = elevators.stream().filter(Elevator::isIdle).findFirst();
        final Optional<Elevator> optElevatorUp = elevators.stream().filter(elevator -> elevator.getDirection().equals(Direction.UP)).findFirst();
        final Optional<Elevator> optElevatorDown = elevators.stream().filter(elevator -> elevator.getDirection().equals(Direction.DOWN)).findFirst();

        if(optElevatorIdle.isPresent()){
            final Elevator elevatorIdle = optElevatorIdle.get();
            final List<MoveTask> moveTasks = getMoveTasksFromQueuesByElevatorDirection(elevatorIdle); //anytask
            moveTasks.forEach(task -> elevatorIdle.getElevatorController().addTask(task, elevatorIdle));
        }else {
            if (optElevatorUp.isPresent()){
                final Elevator elevatorUp = optElevatorUp.get();
                final List<MoveTask> moveTasks = getMoveTasksFromQueuesUp(elevatorUp);
                moveTasks.forEach(moveTask -> elevatorUp.getElevatorController().addTask(moveTask, elevatorUp));
            }
            if (optElevatorDown.isPresent()){
                final Elevator elevatorDown= optElevatorDown.get();
                final List<MoveTask> moveTasks = getMoveTasksFromQueuesDown(elevatorDown);
                moveTasks.forEach(moveTask -> elevatorDown.getElevatorController().addTask(moveTask, elevatorDown));
            }
        }

    }

    private List<MoveTask> getMoveTasksFromQueuesByElevatorDirection(Elevator elevator){
        final int[] floorsButtonUpOn = house.getFloors()
                .stream().filter(elevator.getDirection().equals(Direction.UP) ? Floor::isButtonUp : Floor::isButtonDown)
                .mapToInt(Floor::getFloorNumber)
                .toArray();
        return getTasksByFloorsArray(elevator, floorsButtonUpOn);
    }

    private List<MoveTask> getMoveTasksFromQueuesUp(Elevator elevator){
        final int[] floorsButtonUpOn = house.getFloors()
                .stream().filter(Floor::isButtonUp)
                .mapToInt(Floor::getFloorNumber)
                .toArray();
        return getTasksByFloorsArray(elevator, floorsButtonUpOn);
    } //Floor::isButtonUp
    private int[] getArrayFloorNumbers(Direction direction){
        return house.getFloors()
                .stream().filter(direction.equals(Direction.UP) ? Floor::isButtonUp : Floor::isButtonDown)
                .mapToInt(Floor::getFloorNumber)
                .toArray();
    }
    private List<MoveTask> getMoveTasksFromQueuesDown(Elevator elevator){
        final int[] floorsButtonUpOn = house.getFloors()
                .stream().filter(Floor::isButtonDown)
                .mapToInt(Floor::getFloorNumber)
                .toArray();
        return getTasksByFloorsArray(elevator, floorsButtonUpOn);
    }

    private List<MoveTask> getTasksByFloorsArray(Elevator elevator, int...floors){
        final List<MoveTask> MoveTasks = new ArrayList<>(floors.length);
        for (int destinationFloor: floors) {
            MoveTasks.add(new MoveTask(house.getFloorByNumber(destinationFloor), elevator, elevator.getDirection(), false));
       }
        return MoveTasks;
    }

    @SneakyThrows
    @Override
    public void run() {
        while(true) {
            this.peopleGenerator.run();
            distributeTasks();
            house.printHouseInfo();
            TimeUnit.MILLISECONDS.sleep(GENERATION_DELAY);
        }
    }
}
