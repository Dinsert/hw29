package ru.hogwarts.school.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentMock;
    private StudentService out;
    private Faculty faculty;
    private long id;
    private int age;
    private Student student;
    private String successfulRemove;
    private Collection<Student> students;
    private int min;
    private int max;

    @BeforeEach
    void setUp() {
        out = new StudentServiceImpl(studentMock);
        faculty = new Faculty(id, "Gryffindor", "Red");
        id = 1L;
        age = 15;
        student = new Student(id, "Harry", age, faculty);
        successfulRemove = "Студент удалён";
        students = new ArrayList<>(List.of(student));
        min = 10;
        max = 20;
    }

    @Test
    void createStudent() {
        when(studentMock.save(student)).thenReturn(student);
        Student actual = out.createStudent(student);
        Student expected = student;
        assertEquals(expected, actual);
    }

    @Test
    void findStudent() {
        when(studentMock.findById(id)).thenReturn(Optional.of(student));
        Student actual = out.findStudent(id);
        Student expected = student;
        assertEquals(expected, actual);
    }

    @Test
    void shouldThrowRuntimeExceptionAtFindStudent() {
        when(studentMock.findById(id)).thenThrow(RuntimeException.class);
        assertThrows(RuntimeException.class, () -> out.findStudent(id));
    }

    @Test
    void editStudent() {
        when(studentMock.save(student)).thenReturn(student);
        Student actual = out.editStudent(student);
        Student expected = student;
        assertEquals(expected, actual);
    }

    @Test
    void deleteStudent() {
        String actual = out.deleteStudent(id);
        String expected = successfulRemove;
        assertEquals(expected, actual);
    }

    @Test
    void getAListOfFacultiesBySpecifiedAge() {
        when(studentMock.findByAge(anyInt())).thenReturn(students);
        Collection<Student> actual = out.getAListOfStudentsBySpecifiedAge(age);
        Collection<Student> expected = new ArrayList<>(List.of(student));
        assertEquals(expected, actual);
    }

    @Test
    void shouldReturnEmptyListAtGetFacultiesByTargetAge() {
        when(studentMock.findByAge(anyInt())).thenReturn(Collections.emptyList());
        Collection<Student> actual = out.getAListOfStudentsBySpecifiedAge(age);
        Collection<Student> expected = Collections.emptyList();
        assertEquals(expected, actual);
    }

    @Test
    void getAllStudentsInASpecifiedAgeRange() {
        when(studentMock.findByAgeBetween(anyInt(), anyInt())).thenReturn(students);
        Collection<Student> actual = out.getAllStudentsInASpecifiedAgeRange(min, max);
        Collection<Student> expected = students;
        assertEquals(expected, actual);
    }

    @Test
    void getAllStudentsInASpecifiedAgeRangeIsEmpty() {
        when(studentMock.findByAgeBetween(anyInt(), anyInt())).thenReturn(Collections.emptyList());
        Collection<Student> actual = out.getAllStudentsInASpecifiedAgeRange(min, max);
        Collection<Student> expected = Collections.emptyList();
        assertEquals(expected, actual);
    }

    @Test
    void getFacultyStudent() {
        when(studentMock.getFacultyStudent(anyLong())).thenReturn(Optional.of(faculty));
        Faculty actual = out.getFacultyStudent(id);
        Faculty expected = faculty;
        assertEquals(expected, actual);
    }

    @Test
    void shouldThrowNoSuchElementExceptionAtGetFacultyStudent() {
        assertThrows(NoSuchElementException.class, () -> out.getFacultyStudent(anyLong()));
    }
}