package service;

import domain.House;


public interface PeopleGenerator extends Runnable {
    void generatePeople(House house);
}
