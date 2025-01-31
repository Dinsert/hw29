package ru.hogwarts.school.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

@ExtendWith(MockitoExtension.class)
class StudentControllerTest {

    @Mock
    private StudentService studentMock;
    @InjectMocks
    private StudentController out;
    private final long id = 1L;
    private final int age = 15;
    private final Faculty faculty = new Faculty(id, "Gryffindor", "Red");
    private final Student student = new Student(id, "Harry", age, faculty);
    private final String sucsessfullRemove = "Студент удалён";
    private final Collection<Student> students = new ArrayList<>(List.of(student));
    private final int min = 10;
    private final int max = 20;

    @Test
    void create() {
        when(studentMock.createStudent(any())).thenReturn(student);
        Student actual = out.create(student);
        Student expected = student;
        assertEquals(expected, actual);
    }

    @Test
    void find() {
        when(studentMock.findStudent(anyLong())).thenReturn(student);
        Student actual = out.find(id);
        Student expected = student;
        assertEquals(expected, actual);
    }

    @Test
    void shouldReturnHTTPStatusNotFoundAtFind() {
//        when(studentMock.findStudent(anyLong())).thenReturn(null);
//        HttpStatusCode actual = out.find(id).getStatusCode();
//        HttpStatusCode expected = HttpStatusCode.valueOf(HttpStatus.NOT_FOUND.value());
//        assertEquals(expected, actual);
    }

    @Test
    void edit() {
        when(studentMock.editStudent(any())).thenReturn(student);
        Student actual = out.edit(student);
        Student expected = student;
        assertEquals(expected, actual);
    }

    @Test
    void shouldReturnHTTPStatusNotFoundAtEdit() {
//        when(studentMock.editStudent(any())).thenReturn(null);
//        HttpStatusCode actual = out.edit(student).getStatusCode();
//        HttpStatusCode expected = HttpStatusCode.valueOf(HttpStatus.BAD_REQUEST.value());
//        assertEquals(expected, actual);
    }

    @Test
    void delete() {
        when(studentMock.deleteStudent(anyLong())).thenReturn(sucsessfullRemove);
        String actual = out.delete(anyLong());
        String expected = sucsessfullRemove;
        assertEquals(expected, actual);
    }

    @Test
    void getAListOfFacultiesBySpecifiedColor() {
        when(studentMock.getAListOfStudentsBySpecifiedAge(anyInt())).thenReturn(students);
        Collection<Student> actual = out.getAListOfStudentsBySpecifiedAge(age);
        Collection<Student> expected = students;
        assertEquals(expected, actual);
    }

    @Test
    void getAllStudentsBetweenTargetAge() {
        when(studentMock.getAllStudentsInASpecifiedAgeRange(anyInt(), anyInt())).thenReturn(students);
        Collection<Student> actual = out.getAllStudentsBetweenTargetAge(min, max);
        Collection<Student> expected = students;
        assertEquals(expected, actual);
    }

    @Test
    void getFaculty() {
        when(studentMock.getFacultyStudent(anyLong())).thenReturn(faculty);
        Faculty actual = out.getFaculty(id);
        Faculty expected = faculty;
        assertEquals(expected, actual);
    }
}