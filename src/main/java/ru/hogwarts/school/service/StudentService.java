package ru.hogwarts.school.service;

import java.util.Collection;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

public interface StudentService {

    Student createStudent(Student student);

    Student findStudent(long id);

    Student editStudent(Student student);

    String deleteStudent(long id);

    Collection<Student> getAListOfStudentsBySpecifiedAge(int age);

    Collection<Student> getAllStudentsInASpecifiedAgeRange(int min, int max);

    Faculty getFacultyStudent(long id);

    int getQuantityAllStudents();

    double getAverageAgeAllStudents();

    Collection<Student> getAllStudents(int page, int size);
}
