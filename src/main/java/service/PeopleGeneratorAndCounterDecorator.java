package service;

import domain.House;

import java.util.concurrent.atomic.AtomicInteger;

public class PeopleGeneratorAndCounterDecorator implements PeopleGenerator {

    private final PeopleGenerator peopleGenerator;
    private final AtomicInteger counter = new AtomicInteger(0);

    public PeopleGeneratorAndCounterDecorator(PeopleGenerator peopleGenerator) {
        this.peopleGenerator = peopleGenerator;
    }

    @Override
    public void generatePeople(House house) {
        peopleGenerator.generatePeople(house);
        counter.incrementAndGet();
    }

    public long getCounter() {
        return this.counter.get();
    }

    @Override
    public void run() {
        this.peopleGenerator.run();
    }
}
