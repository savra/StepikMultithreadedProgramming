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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.concurrent.*;
import java.util.stream.IntStream;

public class Task1 {
    public static void main(String[] args) {
        int personCount = 10;
        int dressingRoomSize = 5;
        DressingRoom dressingRoom = new DressingRoom(dressingRoomSize);
        Random rand = new Random();

        System.out.printf("Раздевалка рассчитана на %d. Пришло %d человек %n%n", dressingRoomSize, personCount);

        CompletableFuture<?>[] completableFutures = IntStream.range(0, personCount)
                .mapToObj(i -> CompletableFuture.runAsync(() -> {
                    try {
                        TimeUnit.MILLISECONDS.sleep(rand.nextInt(200));
                        Sex sex = rand.nextInt(2) == 0 ? Sex.MALE : Sex.FEMALE;
                        Person person = new Person(sex, "Имя" + i);

                        System.out.printf("%s. %s (%s) пришел к раздевалке%n",
                                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")),
                                person.name(),
                                person.sex().getTitle());
                        dressingRoom.tryEnter(person);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                })).toArray(CompletableFuture[]::new);

        CompletableFuture.allOf(completableFutures).join();
    }
}