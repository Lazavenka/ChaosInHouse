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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
@Slf4j
class moveTaskGeneratorTest {

    private TaskGenerator taskGenerator;
    @Mock
    private House house;
    @Mock
    private PeopleGenerator peopleGenerator;
    @BeforeEach
    void before(){
        MockitoAnnotations.initMocks(this);
        taskGenerator = new TaskGenerator(house, peopleGenerator);
    }

    @Test
    void generateTasks() {

    }


}