package com.hvdbs.savra.solution.Chapter4.Unit2;

import com.hvdbs.savra.solution.Chapter4.Unit2.enums.DressingRoomState;
import com.hvdbs.savra.solution.Chapter4.Unit2.enums.Sex;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class DressingRoom {
    private final Object lock = new Object();
    private final Semaphore semaphore;
    private DressingRoomState state;
    private final BlockingQueue<Person> persons = new LinkedBlockingQueue<>();

    public DressingRoom(int dressingRoomSize) {
        this.state = DressingRoomState.EMPTY;
        this.semaphore = new Semaphore(dressingRoomSize);
    }

    public void enter(Person person) throws InterruptedException {
        if (semaphore.tryAcquire(100, TimeUnit.MILLISECONDS)) {
            synchronized (lock) {
                Sex sex = person.getSex();
                System.out.printf("В раздевалку пытается зайти %s (Имя %s). Раздевалка в данный момент %s.%n",
                        sex.getTitle(), person.getName(), state.getTitle());

                if (state == DressingRoomState.EMPTY
                        || (sex == Sex.FEMALE && state == DressingRoomState.OCCUPIED_BY_WOMAN ||
                        sex == Sex.MALE && state == DressingRoomState.OCCUPIED_BY_MAN)) {
                    addPerson(person);

                    System.out.printf("%s в раздевалке. Раздевалка в данный момент %s. Количество человек в раздевалке: %d%n", person.getName(),
                            state == DressingRoomState.OCCUPIED_BY_MAN ? "мужская" : state == DressingRoomState.OCCUPIED_BY_WOMAN ? "женская" : "пустая",
                            persons.size());

                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    System.out.println("Раздевалка для людей другого пола");
                }
            }
        } else {
            System.out.printf("В раздевалке нет места, %s уходит%n", person.getName());
        }
    }

    public void exit() {
        synchronized (lock) {
            Person p = persons.poll();

            if (p != null) {
                System.out.printf("%s выходит. Имя: %s%n", p.getSex().getTitle(), p.getName());
                semaphore.release();

                if (persons.isEmpty()) {
                    state = DressingRoomState.EMPTY;
                    System.out.println("Раздевалка теперь пустая");
                } else {
                    System.out.printf("Раздевалка %s%n", state.getTitle());
                }
            }
        }
    }


    private void addPerson(Person person) {
        if (state == DressingRoomState.EMPTY) {
            if (person.getSex() == Sex.MALE) {
                state = DressingRoomState.OCCUPIED_BY_MAN;
            } else {
                state = DressingRoomState.OCCUPIED_BY_WOMAN;
            }
        }

        persons.add(person);
    }
}
