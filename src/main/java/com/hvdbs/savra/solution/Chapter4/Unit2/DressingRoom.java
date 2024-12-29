package com.hvdbs.savra.solution.Chapter4.Unit2;

import com.hvdbs.savra.solution.Chapter4.Unit2.enums.DressingRoomState;
import com.hvdbs.savra.solution.Chapter4.Unit2.enums.Sex;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DressingRoom {
    private DressingRoomState state;
    private final List<Person> persons;
    private final Random rand = new Random();
    private final Object mutex = new Object();
    private final int dressingRoomSize;

    public DressingRoom(int dressingRoomSize) {
        this.state = DressingRoomState.EMPTY;
        this.persons = new ArrayList<>(dressingRoomSize);
        this.dressingRoomSize = dressingRoomSize;
    }

    public void tryEnter(Person person) throws InterruptedException {
        synchronized (mutex) {
            if (persons.size() < dressingRoomSize) {
                Sex sex = person.sex();
                System.out.printf("-----В раздевалку пытается зайти %s (Имя %s). Раздевалка в данный момент %s.%n",
                        sex.getTitle(), person.name(), state.getTitle());

                if (state == DressingRoomState.EMPTY
                        || (sex == Sex.FEMALE && state == DressingRoomState.OCCUPIED_BY_WOMAN ||
                        sex == Sex.MALE && state == DressingRoomState.OCCUPIED_BY_MAN)) {
                    addPerson(person);

                    System.out.printf("-----%s в раздевалке. Раздевалка в данный момент %s. Количество человек в раздевалке: %d%n", person.name(),
                            state == DressingRoomState.OCCUPIED_BY_MAN ? "мужская" : state == DressingRoomState.OCCUPIED_BY_WOMAN ? "женская" : "пустая",
                            persons.size());

                    mutex.wait(rand.nextInt(3000));
                    exit();
                } else {
                    System.out.printf("Раздевалка для людей другого пола. %s уходит%n", person.name());
                }
            } else {
                System.out.printf("В раздевалке нет места, %s уходит%n", person.name());
            }
        }
    }

    private void exit() {
        Person p = persons.remove(rand.nextInt(persons.size()));

        System.out.printf("%s выходит. Имя: %s%n", p.sex().getTitle(), p.name());

        if (persons.isEmpty()) {
            state = DressingRoomState.EMPTY;
            System.out.println("Раздевалка теперь пустая");
        }
    }

    private void addPerson(Person person) {
        if (state == DressingRoomState.EMPTY) {
            if (person.sex() == Sex.MALE) {
                state = DressingRoomState.OCCUPIED_BY_MAN;
            } else {
                state = DressingRoomState.OCCUPIED_BY_WOMAN;
            }
        }

        persons.add(person);
    }
}
