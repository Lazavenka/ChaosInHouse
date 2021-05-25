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
public class TaskGenerator implements Runnable {
    private final House house;
    private final PeopleGenerator peopleGenerator;
    private static final long GENERATION_DELAY = 10_000; //mills

    public void distributeTasks() {
        List<Elevator> elevators = this.house.getElevators();

        final Optional<Elevator> optElevatorIdle = elevators.stream().filter(Elevator::isIdle).findFirst();
        final Optional<Elevator> optElevatorUp = elevators.stream().filter(elevator -> elevator.getDirection().equals(Direction.UP)).findFirst();
        final Optional<Elevator> optElevatorDown = elevators.stream().filter(elevator -> elevator.getDirection().equals(Direction.DOWN)).findFirst();

        if (optElevatorIdle.isPresent()) {
            final Elevator elevatorIdle = optElevatorIdle.get();
            Direction direction = findQueueDirectionWithMinDistanceTo(elevatorIdle);
            final List<MoveTask> moveTasks = getMoveTasksFromQueuesByDirection(direction, elevatorIdle); //anytask
            moveTasks.forEach(task -> elevatorIdle.getElevatorController().addTask(task, elevatorIdle));
        } else {
            if (optElevatorUp.isPresent()) {
                final Elevator elevatorUp = optElevatorUp.get();
                final List<MoveTask> moveTasks = getMoveTasksFromQueuesByDirection(Direction.UP, elevatorUp);
                moveTasks.forEach(moveTask -> elevatorUp.getElevatorController().addTask(moveTask, elevatorUp));
            }
            if (optElevatorDown.isPresent()) {
                final Elevator elevatorDown = optElevatorDown.get();
                final List<MoveTask> moveTasks = getMoveTasksFromQueuesByDirection(Direction.DOWN, elevatorDown);
                moveTasks.forEach(moveTask -> elevatorDown.getElevatorController().addTask(moveTask, elevatorDown));
            }
        }

    }

    private Direction findQueueDirectionWithMinDistanceTo(Elevator elevator) {
        final int[] floorNumbersButtonUpOn = getArrayFloorNumbers(Direction.UP);
        final int[] floorNumbersButtonDownOn = getArrayFloorNumbers(Direction.DOWN);
        if (floorNumbersButtonUpOn.length == 0) {
            return Direction.DOWN;
        }
        if (floorNumbersButtonDownOn.length == 0) {
            return Direction.UP;
        }
        final int currentElevatorFloor = elevator.getCurrentFloor();
        final int minToQueueUP = findMinDistance(currentElevatorFloor, floorNumbersButtonUpOn);
        final int minToQueueDown = findMinDistance(currentElevatorFloor, floorNumbersButtonDownOn);
        return minToQueueUP < minToQueueDown ? Direction.UP : Direction.DOWN;
    }

    private int findMinDistance(int currentFloor, int... floorNumbers) {
        if (floorNumbers.length == 0) {
            return 0;
        }
        int min = floorNumbers[0];
        for (int n : floorNumbers) {
            if ((Math.abs(n - currentFloor)) < (Math.abs(currentFloor - min)))
                min = n;
        }
        return min;
    }

    private List<MoveTask> getMoveTasksFromQueuesByDirection(Direction direction, Elevator elevator) {
        final int[] floorsButtonUpOn = getArrayFloorNumbers(direction);
        return getTasksByFloorsArray(direction, elevator, floorsButtonUpOn);
    }

    private int[] getArrayFloorNumbers(Direction direction) {
        return house.getFloors()
                .stream().filter(direction.equals(Direction.UP) ? Floor::isButtonUp : Floor::isButtonDown)
                .mapToInt(Floor::getFloorNumber)
                .toArray();
    }

    private List<MoveTask> getTasksByFloorsArray(Direction direction, Elevator elevator, int... floors) {
        final List<MoveTask> MoveTasks = new ArrayList<>(floors.length);
        for (int destinationFloor : floors) {
            final Floor floor = house.getFloorByNumber(destinationFloor);
            if (direction.equals(Direction.UP)) {
                floor.setButtonUp(false);
            } else {
                floor.setButtonDown(false);
            }
            MoveTasks.add(new MoveTask(floor, elevator, direction, false));
        }
        return MoveTasks;
    }

    @SneakyThrows
    @Override
    public void run() {
        while (true) {
            this.peopleGenerator.run();
            distributeTasks();
            //house.printHouseInfo();
            TimeUnit.MILLISECONDS.sleep(GENERATION_DELAY);
        }
    }
}
