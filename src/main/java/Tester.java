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

        House house = House.ofFloorsAndElevators(5, 2);
        house.printHouseInfo();
        PeopleGenerator peopleGenerator = new PeopleGeneratorImpl(8);
        for (int i = 0; i < 100; i++) {
            peopleGenerator.generatePeople(house);
        }
//        peopleGenerator.generatePeople(house);
//        System.out.println("---------1st generation----------");
        house.printHouseInfo();
//        peopleGenerator.generatePeople(house);
//        System.out.println("---------2nd generation----------");
//        house.printHouseInfo();
        /*
        PriorityBlockingQueue<Task> priorityTasks = new PriorityBlockingQueue<>(5, new TaskComparator());
        Elevator elevator = new Elevator(29, 800, 1, new ElevatorController());
        Task[] generated = IntStream.range(3, 6).mapToObj(i -> new Task(i*2, elevator, elevator.getDirection())).toArray((Task[]::new));
        priorityTasks.add(new Task(1, elevator,elevator.getDirection()));
        priorityTasks.addAll(Arrays.asList(generated.clone()));

        System.out.println(priorityTasks);

        final Task task = new Task(7, elevator, elevator.getDirection());
        System.out.println("---------------");
        priorityTasks.add(task);



        System.out.println(priorityTasks);
        System.out.println(elevator);
        System.out.println("tasks.take");
        priorityTasks.take().run();
        System.out.println(priorityTasks);
        System.out.println(elevator);
        System.out.println("second tasks.take");
        priorityTasks.take().run();
        System.out.println(priorityTasks);
        System.out.println("3rd tasks.take");
        priorityTasks.take().run();
        System.out.println(priorityTasks);
        System.out.println("4th tasks.take");
        priorityTasks.take().run();
        System.out.println(priorityTasks);
        System.out.println("5th tasks.take");
        priorityTasks.take().run();
        System.out.println(priorityTasks);
        System.out.println(elevator);
        */
    }
}
