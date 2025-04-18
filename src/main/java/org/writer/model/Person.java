package org.writer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.writer.annotations.CsvField;

/**
 * Модель человека, данные которого можно экспортировать в CSV-файл.
 */

@Data
@Builder
@AllArgsConstructor
public class Person {
    @CsvField(name = "First Name", order = 1)
    private String firstName;

    @CsvField(name = "Last Name", order = 2)
    private String lastName;

    @CsvField(name = "Day", order = 3)
    private int dayOfBirth;

    @CsvField(name = "Month", order = 4)
    private Months monthOfBirth;

    @CsvField(name = "Year", order = 5)
    private int yearOfBirth;
}