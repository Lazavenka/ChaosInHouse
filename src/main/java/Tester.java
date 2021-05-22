import domain.House;
import lombok.extern.slf4j.Slf4j;
import service.ElevatorController;
import service.ElevatorControllerImpl;
import service.PeopleGenerator;
import service.PeopleGeneratorImpl;


@Slf4j
public class Tester {
    public static void main(String[] args) {
        House house = House.ofFloorsAndElevators(5, 2);
        house.printHouseInfo();
        PeopleGenerator peopleGenerator = new PeopleGeneratorImpl(8);
        peopleGenerator.generatePeople(house);
        System.out.println("---------1st generation----------");
        house.printHouseInfo();
        peopleGenerator.generatePeople(house);
        System.out.println("---------2nd generation----------");
        house.printHouseInfo();

    }
}
