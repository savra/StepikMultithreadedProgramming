package com.hvdbs.savra.solution.Chapter4.Unit2;

/*
Существует раздевалка в спортзале. Если в раздевалке находится женщина, то туда может зайти только женщина.
Если в раздевалке мужчина - то только мужчина. Если раздевалка пустая, то туда может зайти любой желающий.

На двери есть индикатор, который может принимать 3 состояния:
1) свободно
2) раздевалка занята женщинами
3) раздевалка занята мужчинами

Напишите программу, в которой мужчины и женщины смогут воспользоваться этой раздевалкой.
* */

import com.hvdbs.savra.solution.Chapter4.Unit2.enums.Sex;

import java.util.Random;
import java.util.concurrent.*;
import java.util.stream.IntStream;

public class Task1 {
    public static void main(String[] args) {
        int personCount = 10;
        int dressingRoomSize = 5;
        DressingRoom dressingRoom = new DressingRoom(dressingRoomSize);
        Random rand = new Random();

        System.out.printf("Раздевалка рассчитана на %d. Пришло %d человек %n", dressingRoomSize, personCount);

        CompletableFuture<?>[] completableFutures = IntStream.range(0, personCount)
                .mapToObj(i -> CompletableFuture.runAsync(() -> {
                    Sex sex = rand.nextInt(2) == 0 ? Sex.MALE : Sex.FEMALE;
                    Person person = new Person(sex, "Имя" + i);
                    try {
                        System.out.printf("%s пришел к раздевалке%n", person.getName());
                        dressingRoom.enter(person);
                        TimeUnit.MILLISECONDS.sleep(rand.nextInt(300));
                        dressingRoom.exit();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                })).toArray(CompletableFuture[]::new);

        CompletableFuture.allOf(completableFutures).join();
    }
}