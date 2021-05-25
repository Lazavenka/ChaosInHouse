import com.google.common.primitives.Ints;
import domain.Elevator;
import domain.House;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import service.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.stream.IntStream;


@Slf4j
public class Tester {
    @SneakyThrows
    public static void main(String[] args) {
        int currentFloor = 10;
        int[] floorsUp = new int[]{2, 5, 7, 8};
        int[] floorsDown = new int[]{3, 5, 6};

        int ans = findMinDistance(currentFloor, floorsUp);
        log.info(""+ans);
    }
    private static int findMinDistance(int currentFloor, int... floorNumbers){
        int min = floorNumbers[0];
        for(int n : floorNumbers)
        {
            if ((Math.abs(n - currentFloor)) < (Math.abs(currentFloor - min)))
                min = n;
        }
        return min;
    }
}
