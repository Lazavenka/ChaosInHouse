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
        elevatorControllers.stream().map(ElevatorController::getElevator)
                .mapToInt(this::findMinDistanceToQueue).toArray();
    }

    private int findMinDistanceToQueue(Elevator elevator){
        final int currentFloor = elevator.getCurrentFloor();
        final int[] floorsButtonOn = house.getFloors()
                .stream().filter(floor -> !floor.isButtonUp() || !floor.isButtonDown())
                .mapToInt(Floor::getFloorNumber)
                .toArray();
        return  findMinDistance(floorsButtonOn, currentFloor);
    }

    private List<Task> generateTasksByElevatorButtons(Elevator elevator){
        final int currentFloor = elevator.getCurrentFloor();
        final int[] floorsButtonOn = elevator.getButtonsFloors()
                .entrySet().stream().filter(entry-> !entry.getValue())
                .mapToInt(Map.Entry::getKey)
                .toArray();
        return getTasksByFloorsArray(elevator, currentFloor,floorsButtonOn);
    }
    private List<Task> getTasksByFloorsArray(Elevator elevator, int currentFloor, int...floors){
        List<Task> tasks = new ArrayList<>(floors.length);
        int curr = currentFloor;
        for (int target: floors) {
            tasks.add(new Task(curr, target, elevator));
            curr = target;
        }
        return tasks;
    }

    private int findMinDistance(int[] floors, int current){
        int min = floors[0];
        for (int i: floors) {
            if ((Math.abs(i - current)) < (Math.abs(i - min))) {
                min = i;
            }
        }
        return min;
    }
}
