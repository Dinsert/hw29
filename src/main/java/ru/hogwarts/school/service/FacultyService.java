package ru.hogwarts.school.service;

import java.util.Collection;

public interface FacultyService {

    Faculty createFaculty(Faculty faculty);

    Faculty findFaculty(long id);

    Faculty editFaculty(Faculty faculty);

    String deleteFaculty(long id);

    Collection<Faculty> getAListOfFacultiesBySpecifiedColor(String color);

    Collection<Student> getAllStudents(long id);

    Faculty getFacultyByNameOrColor(String nameOrColor);
}
