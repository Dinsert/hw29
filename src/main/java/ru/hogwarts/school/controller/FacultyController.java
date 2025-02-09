package ru.hogwarts.school.controller;

import java.util.Collection;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.FacultyService;

@RequestMapping("/faculty")
@RestController
@RequiredArgsConstructor
public class FacultyController {

    private final FacultyService facultyService;

    @PostMapping
    public Faculty create(@RequestBody Faculty faculty) {
        return facultyService.createFaculty(faculty);
    }

    @GetMapping("{id}")
    public Faculty find(@PathVariable long id) {
        return facultyService.findFaculty(id);
    }

    @PutMapping
    public Faculty edit(@RequestBody Faculty faculty) {
        return facultyService.editFaculty(faculty);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable long id) {
        return facultyService.deleteFaculty(id);
    }

    @GetMapping("/get-list-by-color/{color}")
    public Collection<Faculty> getAListOfFacultiesBySpecifiedColor(@PathVariable String color) {
        return facultyService.getAListOfFacultiesBySpecifiedColor(color);
    }

    @GetMapping
    public Faculty getFacultyByNameOrColor(@RequestParam String nameOrColor) {
        return facultyService.getFacultyByNameOrColor(nameOrColor);
    }

    @GetMapping("/get-students/{id}")
    public Collection<Student> getAllStudents(@PathVariable long id) {
        return facultyService.getAllStudents(id);
    }
}