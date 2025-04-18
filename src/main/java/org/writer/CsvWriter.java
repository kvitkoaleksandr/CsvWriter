package org.writer;

import org.writer.annotations.CsvField;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

/**
 * Реализация интерфейса Writable для записи объектов в CSV.
 */
public class CsvWriter implements Writable {

    @Override
    public void writeToFile(List<?> data, String fileName) {
        if (data == null || data.isEmpty()) {
            System.out.println("No data to write.");
            return;
        }

        Class<?> clazz = data.get(0).getClass();
        List<Field> annotatedFields = getAnnotatedFieldsSorted(clazz);

        if (annotatedFields.isEmpty()) {
            throw new RuntimeException("Класс " + clazz.getName()
                    + " не содержит ни одного поля с аннотацией @CsvField");
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            String header = buildHeader(annotatedFields);
            writer.write(header);
            writer.newLine();

            for (Object obj : data) {
                String row = buildRow(obj, annotatedFields);
                writer.write(row);
                writer.newLine();
            }

            System.out.println("CSV файл успешно создан: " + fileName);
        } catch (IOException | IllegalAccessException e) {
            throw new RuntimeException("Ошибка при записи CSV: " + e.getMessage(), e);
        }
    }

    private List<Field> getAnnotatedFieldsSorted(Class<?> clazz) {
        List<Field> fields = new ArrayList<>();
        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(CsvField.class)) {
                field.setAccessible(true);
                fields.add(field);
            }
        }
        fields.sort(Comparator.comparingInt(f -> f.getAnnotation(CsvField.class).order()));
        return fields;
    }

    private String buildHeader(List<Field> fields) {
        List<String> headers = new ArrayList<>();
        for (Field field : fields) {
            headers.add(field.getAnnotation(CsvField.class).name());
        }
        return String.join(",", headers);
    }

    private String buildRow(Object obj, List<Field> fields) throws IllegalAccessException {
        List<String> values = new ArrayList<>();
        for (Field field : fields) {
            Object value = field.get(obj);
            values.add(value != null ? value.toString() : "");
        }
        return String.join(",", values);
    }
}