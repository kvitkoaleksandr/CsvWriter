package org.writer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * Пример модели студента (не используется в текущей версии, но пригоден для расширения).
 */

@Data
@Builder
@AllArgsConstructor
public class Student {
    private String name;
    private List<String> score;
}