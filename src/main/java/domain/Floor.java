package domain;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
@Getter
@Setter
public class Floor {
    private final int floorNumber;

    private final BlockingQueue<Person> personQueueUp = new LinkedBlockingQueue<>();
    private final BlockingQueue<Person> personQueueDown = new LinkedBlockingQueue<>();

    private boolean buttonUp;
    private boolean buttonDown;

    public Floor(int floorNumber) {
        this.floorNumber = floorNumber;
        this.buttonUp = false;
        this.buttonDown = false;

    }

    public void distribute(Person person) {

        final int destinationFloor = person.getDestinationFloor();
        if (this.floorNumber < destinationFloor){
            addPersonToQueueUp(person);
        }else {
            addPersonToQueueDown(person);
        }
    }
    @SneakyThrows
    private void addPersonToQueueUp(Person person){
        this.personQueueUp.put(person);
        setButtonUp(true);
    }
    @SneakyThrows
    private void addPersonToQueueDown(Person person){
        this.personQueueDown.put(person);
        setButtonDown(true);
    }

}
