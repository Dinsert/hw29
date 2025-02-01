package ru.hogwarts.school.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
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
import ru.hogwarts.school.repository.FacultyRepository;

@ExtendWith(MockitoExtension.class)
class FacultyServiceTest {

    @Mock
    private FacultyRepository facultyMock;
    private FacultyService out;
    private long id;
    private Faculty faculty;
    private String successfulRemove, color;
    private Collection<Student> students;
    private Collection<Faculty> faculties;


    @BeforeEach
    void setUp() {
        out = new FacultyServiceImpl(facultyMock);
        id = 1L;
        color = "Red";
        students = new ArrayList<>(List.of(new Student(id, "Harry", 15)));
        faculty = new Faculty(id, "Gryffindor", color);
        successfulRemove = "Факультет удалён";
        faculties = new ArrayList<>(List.of(faculty));
    }

    @Test
    void createFaculty() {
        when(facultyMock.save(faculty)).thenReturn(faculty);
        Faculty actual = out.createFaculty(faculty);
        Faculty expected = faculty;
        assertEquals(expected, actual);
    }

    @Test
    void findFaculty() {
        when(facultyMock.findById(id)).thenReturn(Optional.of(faculty));
        Faculty actual = out.findFaculty(id);
        Faculty expected = faculty;
        assertEquals(expected, actual);
    }

    @Test
    void shouldThrowRuntimeExceptionAtFindFaculty() {
        when(facultyMock.findById(id)).thenThrow(RuntimeException.class);
        assertThrows(RuntimeException.class, () -> out.findFaculty(id));
    }

    @Test
    void editFaculty() {
        when(facultyMock.save(faculty)).thenReturn(faculty);
        Faculty actual = out.editFaculty(faculty);
        Faculty expected = faculty;
        assertEquals(expected, actual);
    }

    @Test
    void deleteFaculty() {
        String actual = out.deleteFaculty(id);
        String expected = successfulRemove;
        assertEquals(expected, actual);
    }

    @Test
    void getAListOfFacultiesBySpecifiedColor() {
        when(facultyMock.findByColor(anyString())).thenReturn(faculties);
        Collection<Faculty> actual = out.getAListOfFacultiesBySpecifiedColor(color);
        Collection<Faculty> expected = new ArrayList<>(List.of(faculty));
        assertEquals(expected, actual);
    }

    @Test
    void shouldReturnEmptyListAtGetFacultiesByTargetColor() {
        when(facultyMock.findByColor(anyString())).thenReturn(Collections.emptyList());
        Collection<Faculty> actual = out.getAListOfFacultiesBySpecifiedColor(color);
        Collection<Faculty> expected = Collections.emptyList();
        assertEquals(expected, actual);
    }

    @Test
    void getFacultyByNameOrColor() {
//        when(facultyMock.findFirstByNameOrColorIgnoreCase(anyString(), anyString())).thenReturn(Optional.of(faculty));
        Faculty actual = out.getFacultyByNameOrColor(color);
        Faculty expected = faculty;
        assertEquals(expected, actual);
    }

    @Test
    void shouldThrowNoSuchElementExceptionAtGetFacultyByNameOrColor() {
        assertThrows(NoSuchElementException.class, () -> out.getFacultyByNameOrColor(color));
    }


    @Test
    void getAllStudents() {
        when(facultyMock.getAllStudentsByFacultyId(anyLong())).thenReturn(students);
        Collection<Student> actual = out.getAllStudents(id);
        Collection<Student> expected = students;
        assertEquals(expected, actual);
    }

    @Test
    void getAllStudentsIsEmpty() {
        when(facultyMock.getAllStudentsByFacultyId(anyLong())).thenReturn(Collections.emptyList());
        Collection<Student> actual = out.getAllStudents(id);
        Collection<Student> expected = Collections.emptyList();
        assertEquals(expected, actual);
    }
}