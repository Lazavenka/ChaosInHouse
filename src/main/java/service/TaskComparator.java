package service;

import java.util.Comparator;

public class TaskComparator implements Comparator<MoveTask> {
    @Override
    public int compare(MoveTask o1, MoveTask o2) {
        return o1.getDestinationFloor() - o2.getDestinationFloor();
    }

}
