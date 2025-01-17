package ru.hogwarts.school.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Faculty {

    private long id;
    private String name;
    private String color;

}
