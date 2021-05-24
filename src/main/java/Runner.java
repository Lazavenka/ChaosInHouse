import domain.Elevator;
import domain.Floor;
import domain.House;
import domain.Person;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Runner {
    public static void main(String[] args) {
        log.info("Hello");
        final House house = House.ofFloors(10);
        final int testFloorNumber = 5;
        final int maxFloor = house.getFloorsNumber();
        Person person = new Person(testFloorNumber, maxFloor);
        Elevator elevator = house.getElevatorByNumber(1);
        log.info(elevator.getButtons());
        log.info("Person "+person);
        log.info("isButtonUp() before distribution - "+house.getFloorByNumber(testFloorNumber).isButtonUp());
        log.info("isButtonDown() before distribution - "+house.getFloorByNumber(testFloorNumber).isButtonDown());
        Floor floor = house.getFloorByNumber(testFloorNumber);
        person.getInLine(floor);

        log.info("isButtonUp() after distribution - "+house.getFloorByNumber(testFloorNumber).isButtonUp());
        log.info("isButtonDown() after distribution - "+house.getFloorByNumber(testFloorNumber).isButtonDown());


        log.info("QUEUES");
        log.info("QUEUE_UP:"+house.getFloorByNumber(testFloorNumber).getPersonQueueUp().toString());
        log.info("QUEUE_DOWN:"+house.getFloorByNumber(testFloorNumber).getPersonQueueDown().toString());


    }
}
