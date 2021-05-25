import domain.Elevator;
import domain.Floor;
import domain.House;
import domain.Person;
import lombok.extern.slf4j.Slf4j;
import service.PeopleGenerator;
import service.PeopleGeneratorImpl;
import service.TaskGenerator;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Slf4j
public class Runner {
    public static void main(String[] args) {
        log.info("Hello");
        final House house = House.ofCustomizeAll(15, 2, 1200);
        final int generationRate = 5;
        PeopleGenerator peopleGenerator = new PeopleGeneratorImpl(generationRate, house);
        TaskGenerator taskGenerator = new TaskGenerator(house, peopleGenerator);
        List<Elevator> elevators = house.getElevators();
        final List<Thread> elevatorsThreads = elevators.stream()
                .map(Thread::new)
                .collect(Collectors.toList());
        elevatorsThreads.forEach(Thread::start);
        new Thread(taskGenerator).start();
    }
}
