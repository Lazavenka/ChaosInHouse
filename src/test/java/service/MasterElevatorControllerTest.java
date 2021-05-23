package service;

import domain.Elevator;
import domain.House;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import testData.ElevatorSamples;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
@Slf4j
class MasterElevatorControllerTest {

    private MasterElevatorController masterElevatorController;
    @Mock
    private House house;
    @BeforeEach
    void before(){
        MockitoAnnotations.initMocks(this);
        masterElevatorController = new MasterElevatorController(house);
    }

    @Test
    void generateTasksByElevatorButtons() {
        Elevator elevator = ElevatorSamples.getTestElevator();
        List<Task> tasks = masterElevatorController.generateTasksByElevatorButtons(elevator);
        log.info(tasks.toString());
    }


}