package org.writer;

import net.datafaker.Faker;
import org.writer.model.Months;
import org.writer.model.Person;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Пример использования CsvWriter.
 * Генерирует список объектов Person с помощью DataFaker и сохраняет их в CSV-файл.
 */

public class Main {
    public static void main(String[] args) {
        List<Person> people = generateFakePeople(5);
        Writable csvWriter = new CsvWriter();
        csvWriter.writeToFile(people, "people.csv");
    }

    private static List<Person> generateFakePeople(int count) {
        Faker faker = new Faker();
        Random random = new Random();
        List<Person> list = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            String firstName = faker.name().firstName();
            String lastName = faker.name().lastName();
            int day = random.nextInt(28) + 1;
            Months month = Months.values()[random.nextInt(Months.values().length)];
            int year = random.nextInt(25) + 1980;

            Person person = Person.builder()
                    .firstName(firstName)
                    .lastName(lastName)
                    .dayOfBirth(day)
                    .monthOfBirth(month)
                    .yearOfBirth(year)
                    .build();

            list.add(person);
        }

        return list;
    }
}