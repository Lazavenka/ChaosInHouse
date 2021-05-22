package domain;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
@Getter
@Setter
public class Floor {
    private static final float DEFAULT_FLOOR_HEIGHT = 3.2f;

    private final int floorNumber;
    private final float floorHeight;
    private final BlockingQueue<Person> personQueueUp = new LinkedBlockingQueue<>();
    private final BlockingQueue<Person> personQueueDown = new LinkedBlockingQueue<>();

    private boolean buttonUp;
    private boolean buttonDown;

    public Floor(int floorNumber) {
        this(floorNumber, DEFAULT_FLOOR_HEIGHT);
    }
    public Floor(int floorNumber, float floorHeight){
        this.floorNumber = floorNumber;
        this.buttonUp = false;
        this.buttonDown = false;
        this.floorHeight = floorHeight;
    }

    public void distributeByQueues(Person person) {
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

    public void checkQueues(){
        if (personQueueUp.isEmpty()){
            setButtonUp(false);
        }
        if (personQueueDown.isEmpty()){
            setButtonDown(false);
        }
    }

    @Override
    public String toString() {
        return "Floor number " + floorNumber +
                ", floorHeight " + floorHeight +
                ", buttonUp " + buttonUp +
                ", buttonDown " + buttonDown +
                ", peopleUp " + personQueueUp.size() +
                ", peopleUp " + personQueueDown.size();
    }
}
