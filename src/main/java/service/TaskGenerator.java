package service;

import domain.Direction;
import domain.Elevator;
import domain.Floor;
import domain.House;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@AllArgsConstructor
public class TaskGenerator implements Runnable{
    private final House house;
    private final PeopleGenerator peopleGenerator;
    private static final long GENERATION_DELAY = 10_000; //mills

    public void generateTasks(){
        //List<ElevatorController> elevatorControllers = house.getElevatorControllers();
        //elevatorControllers.stream().map(ElevatorController::getElevator)
        //        .mapToInt(this::findMinDistanceToQueue).toArray(); //дописать
    }
    //Едет на максимально высокий этаж из списка этажей если лифт находится внизу и надо забрать людей
    private MoveTask getSpecifiedTask(int destinationFloor, Elevator elevator){

        return new MoveTask(destinationFloor, elevator, elevator.getDirection()); //дописать
    }
    private List<MoveTask> getMoveTasksFromQueuesUp(Elevator elevator){
        final int[] floorsButtonUpOn = house.getFloors()
                .stream().filter(elevator.getDirection().equals(Direction.UP) ? Floor::isButtonUp : Floor::isButtonDown)
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
    private int findMinDistanceToQueue(Elevator elevator){
        final int currentFloor = elevator.getCurrentFloor();
        final int[] floorsButtonOn = house.getFloors()
                .stream().filter(floor -> floor.isButtonUp() || floor.isButtonDown())
                .mapToInt(Floor::getFloorNumber)
                .toArray();
        return  findMinTargetFloor(floorsButtonOn, currentFloor);
    }

    public List<MoveTask> generateTasksByElevatorButtons(Elevator elevator){
        final int[] floorsButtonOn = elevator.getButtonsFloors()
                .entrySet().stream().filter(Map.Entry::getValue)
                .mapToInt(Map.Entry::getKey)
                .toArray();
        return getTasksByFloorsArray(elevator, floorsButtonOn);
    }
    private List<MoveTask> getTasksByFloorsArray(Elevator elevator, int...floors){
        final List<MoveTask> MoveTasks = new ArrayList<>(floors.length);
        for (int destinationFloor: floors) {
            MoveTasks.add(new MoveTask(destinationFloor, elevator, elevator.getDirection()));
       }
        return MoveTasks;
    }

    private int findMinTargetFloor(int[] floors, int current){
        int min = floors[0];
        for (int i: floors) {
            if ((Math.abs(i - current)) < (Math.abs(i - min))) {
                min = i;
            }
        }
        return min;

    }
    private int findMaxTargetFloor(int[] floors, int current){
        int max = floors[0];
        for (int i: floors) {
            if ((Math.abs(i - current)) > (Math.abs(i - max))) {
                max = i;
            }
        }
        return max;
    }

    @SneakyThrows
    @Override
    public void run() {
        while (true) {
            this.peopleGenerator.generatePeople(this.house);
            generateTasks();
            TimeUnit.MILLISECONDS.sleep(GENERATION_DELAY);
        }
    }
}
