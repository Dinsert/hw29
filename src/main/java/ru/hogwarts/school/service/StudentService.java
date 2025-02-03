package ru.hogwarts.school.service;

import java.util.Collection;

public interface StudentService {

    Student createStudent(Student student);

    Student findStudent(long id);

    Student editStudent(Student student);

    String deleteStudent(long id);

    Collection<Student> getAListOfStudentsBySpecifiedAge(int age);

    Collection<Student> getAllStudentsInASpecifiedAgeRange(int min, int max);

    Faculty getFacultyStudent(long id);
}
