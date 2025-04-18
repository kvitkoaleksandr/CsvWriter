package org.writer;

import org.junit.jupiter.api.*;
import org.writer.model.Months;
import org.writer.model.Person;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CsvWriterTest {

    private CsvWriter writer;
    private String fileName;

    @BeforeEach
    void setUp() {
        writer = new CsvWriter();
        fileName = "test_people.csv";
    }

    @AfterEach
    void cleanup() throws IOException {
        Files.deleteIfExists(Path.of(fileName));
    }

    @Test
    void writeToFileWithValidDataTest() throws IOException {
        Person person = createValidPerson();
        writer.writeToFile(List.of(person), fileName);

        List<String> lines = Files.readAllLines(Path.of(fileName));

        assertEquals(2, lines.size(), "CSV должен содержать заголовок и одну строку данных");
        assertTrue(lines.get(0).contains("First Name"));
        assertTrue(lines.get(1).contains("Alice"));
    }

    @Test
    void writeToFileWithEmptyListTest() {
        assertDoesNotThrow(() -> writer.writeToFile(List.of(), fileName));
        assertFalse(Files.exists(Path.of(fileName)), "Файл не должен быть создан");
    }

    @Test
    void writeToFileWithNoAnnotatedFieldsTest() {
        Object objectWithoutAnnotations = new Object();

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                writer.writeToFile(List.of(objectWithoutAnnotations), fileName));

        assertTrue(
                ex.getMessage().contains("не содержит ни одного поля с аннотацией @CsvField"),
                "Ожидается сообщение о том, что поля не найдены"
        );
    }

    @Test
    void columnOrderRespectedTest() throws IOException {
        Person person = createValidPerson();
        writer.writeToFile(List.of(person), fileName);

        String header = Files.readAllLines(Path.of(fileName)).get(0);
        assertEquals("First Name,Last Name,Day,Month,Year", header);
    }

    @Test
    void writeToFileWithNullFieldsTest() throws IOException {
        Person personWithNulls = createPersonWithNullFields();
        writer.writeToFile(List.of(personWithNulls), fileName);

        List<String> lines = Files.readAllLines(Path.of(fileName));
        assertEquals(2, lines.size(), "Ожидается 2 строки: заголовок и строка с данными");

        String dataLine = lines.get(1);
        String[] columns = dataLine.split(",");

        assertEquals("", columns[0], "Поле firstName должно быть пустым");
        assertEquals("Nullman", columns[1], "Поле lastName должно быть 'Nullman'");
        assertEquals("5", columns[2], "Поле dayOfBirth должно быть '5'");
        assertEquals("", columns[3], "Поле monthOfBirth должно быть пустым");
        assertEquals("2000", columns[4], "Поле yearOfBirth должно быть '2000'");
    }

    private Person createValidPerson() {
        return Person.builder()
                .firstName("Alice")
                .lastName("Smith")
                .dayOfBirth(10)
                .monthOfBirth(Months.JANUARY)
                .yearOfBirth(1990)
                .build();
    }

    private Person createPersonWithNullFields() {
        return Person.builder()
                .firstName(null)
                .lastName("Nullman")
                .dayOfBirth(5)
                .monthOfBirth(null)
                .yearOfBirth(2000)
                .build();
    }
}