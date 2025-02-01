package ru.hogwarts.school.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
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
import ru.hogwarts.school.service.FacultyService;

@ExtendWith(MockitoExtension.class)
class FacultyControllerTest {

    @Mock
    private FacultyService facultyMock;
    @InjectMocks
    private FacultyController out;
    private final long id = 1L;
    private final String color = "Red";
    private final String name = "Gryffindor";
    private final Faculty faculty = new Faculty(id, name, color);
    private final String sucsessfullRemove = "Факультет удалён";
    private final Collection<Faculty> faculties = new ArrayList<>(List.of(faculty));
    private final Collection<Student> students = new ArrayList<>(List.of(new Student(id, "Harry", 15)));

    @Test
    void create() {
        when(facultyMock.createFaculty(any())).thenReturn(faculty);
        Faculty actual = out.create(faculty);
        Faculty expected = faculty;
        assertEquals(expected, actual);
    }

    @Test
    void find() {
        when(facultyMock.findFaculty(anyLong())).thenReturn(faculty);
        Faculty actual = out.find(id);
        Faculty expected = faculty;
        assertEquals(expected, actual);
    }

    @Test
    void shouldReturnHTTPStatusNotFoundAtFind() {
//        when(facultyMock.findFaculty(anyLong())).thenReturn(null);
//        HttpStatusCode actual = out.find(id);
//        HttpStatusCode expected = HttpStatusCode.valueOf(HttpStatus.NOT_FOUND.value());
//        assertEquals(expected, actual);
    }

    @Test
    void edit() {
        when(facultyMock.editFaculty(any())).thenReturn(faculty);
        Faculty actual = out.edit(faculty);
        Faculty expected = faculty;
        assertEquals(expected, actual);
    }

    @Test
    void shouldReturnHTTPStatusNotFoundAtEdit() {
//        when(facultyMock.editFaculty(any())).thenReturn(null);
//        HttpStatusCode actual = out.edit(faculty).getStatusCode();
//        HttpStatusCode expected = HttpStatusCode.valueOf(HttpStatus.BAD_REQUEST.value());
//        assertEquals(expected, actual);
    }

    @Test
    void delete() {
        when(facultyMock.deleteFaculty(anyLong())).thenReturn(sucsessfullRemove);
        String actual = out.delete(anyLong());
        String expected = sucsessfullRemove;
        assertEquals(expected, actual);
    }

    @Test
    void getAListOfFacultiesBySpecifiedColor() {
        when(facultyMock.getAListOfFacultiesBySpecifiedColor(anyString())).thenReturn(faculties);
        Collection<Faculty> actual = out.getAListOfFacultiesBySpecifiedColor(color);
        Collection<Faculty> expected = faculties;
        assertEquals(expected, actual);
    }

    @Test
    void getFacultyByNameOrColor() {
        when(facultyMock.getFacultyByNameOrColor(anyString())).thenReturn(faculty);
        Faculty actual = out.getFacultyByNameOrColor(name);
        Faculty expected = faculty;
        assertEquals(expected, actual);
    }

    @Test
    void getAllStudents() {
        when(facultyMock.getAllStudents(anyLong())).thenReturn(students);
        Collection<Student> actual = out.getAllStudents(id);
        Collection<Student> expected = students;
        assertEquals(expected, actual);
    }
}