package domain;

import lombok.Getter;
import lombok.Setter;

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

    public Floor(int floorNumber){
        this.floorNumber = floorNumber;
        this.buttonUp = false;
        this.buttonDown = false;
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
                ", buttonUp " + buttonUp +
                ", buttonDown " + buttonDown +
                ", peopleUp " + personQueueUp.size() +
                ", peopleDown " + personQueueDown.size();
    }
}
