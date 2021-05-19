import domain.House;
import domain.Person;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Runner {
    public static void main(String[] args) {
        log.info("Hello");
        final House house = House.ofFloors(10);
        final int testFloorNumber = 5;
        Person person = new Person(testFloorNumber, house);
        log.info("Person "+person.toString());
        log.info("isButtonUp() before distribution - "+house.getFloorByNumber(testFloorNumber).isButtonUp());
        log.info("isButtonDown() before distribution - "+house.getFloorByNumber(testFloorNumber).isButtonDown());

        house.getFloorByNumber(testFloorNumber).distribute(person);

        log.info("isButtonUp() after distribution - "+house.getFloorByNumber(testFloorNumber).isButtonUp());
        log.info("isButtonDown() after distribution - "+house.getFloorByNumber(testFloorNumber).isButtonDown());


        log.info("QUEUES");
        log.info("QUEUE_UP:"+house.getFloorByNumber(testFloorNumber).getPersonQueueUp().toString());
        log.info("QUEUE_DOWN:"+house.getFloorByNumber(testFloorNumber).getPersonQueueDown().toString());

    }
}
