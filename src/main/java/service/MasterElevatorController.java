package service;

import domain.Elevator;
import domain.Floor;
import domain.House;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
public class MasterElevatorController {
    private final House house;

    public void generateTask(){
        List<ElevatorController> elevatorControllers = house.getElevatorControllers();
        //elevatorControllers.stream().map(ElevatorController::getElevator)
        //        .mapToInt(this::findMinDistanceToQueue).toArray(); //дописать
    }
    //Едет на максимально высокий этаж из списка этажей если лифт находится внизу и надо забрать людей
    private Task getSpecifiedTask(Elevator elevator, int direction){

        return  new Task(1, elevator, elevator.getDirection()); //дописать
    }
    private List<Task> getMoveTasksFromQueuesUp(Elevator elevator){
        final int[] floorsButtonUpOn = house.getFloors()
                .stream().filter(elevator.getDirection() == 1 ? Floor::isButtonUp : Floor::isButtonDown)
                .mapToInt(Floor::getFloorNumber)
                .toArray();
        return getTasksByFloorsArray(elevator, floorsButtonUpOn);
    } //Floor::isButtonUp
    private int[] getArrayFloorNumbers(int direction){
        return house.getFloors()
                .stream().filter(direction == 1 ? Floor::isButtonUp : Floor::isButtonDown)
                .mapToInt(Floor::getFloorNumber)
                .toArray();
    }
    private List<Task> getMoveTasksFromQueuesDown(Elevator elevator){
        final int[] floorsButtonUpOn = house.getFloors()
                .stream().filter(Floor::isButtonDown)
                .mapToInt(Floor::getFloorNumber)
                .toArray();
        return getTasksByFloorsArray(elevator, floorsButtonUpOn);
    }
    private int findMinDistanceToQueue(Elevator elevator){
        final int currentFloor = elevator.getCurrentFloor();
        final int[] floorsButtonOn = house.getFloors()
                .stream().filter(floor -> !floor.isButtonUp() || !floor.isButtonDown())
                .mapToInt(Floor::getFloorNumber)
                .toArray();
        return  findMinTargetFloor(floorsButtonOn, currentFloor);
    }

    public List<Task> generateTasksByElevatorButtons(Elevator elevator){
        final int[] floorsButtonOn = elevator.getButtonsFloors()
                .entrySet().stream().filter(Map.Entry::getValue)
                .mapToInt(Map.Entry::getKey)
                .toArray();
        return getTasksByFloorsArray(elevator, floorsButtonOn);
    }
    private List<Task> getTasksByFloorsArray(Elevator elevator, int...floors){
        final List<Task> tasks = new ArrayList<>(floors.length);
        for (int destinationFloor: floors) {
            tasks.add(new Task(destinationFloor, elevator, elevator.getDirection()));
       }
        return tasks;
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
}
