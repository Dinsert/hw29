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
import ru.hogwarts.school.service.StudentService;

@RequestMapping("/student")
@RestController
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @PostMapping
    public Student create(@RequestBody Student student) {
        return studentService.createStudent(student);
    }

    @GetMapping("{id}")
    public Student find(@PathVariable long id) {
        return studentService.findStudent(id);
    }

    @PutMapping
    public Student edit(@RequestBody Student student) {
        return studentService.editStudent(student);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable long id) {
        return studentService.deleteStudent(id);
    }

    @GetMapping("/get-list-by-age/{age}")
    public Collection<Student> getAListOfStudentsBySpecifiedAge(@PathVariable int age) {
        return studentService.getAListOfStudentsBySpecifiedAge(age);
    }

    @GetMapping
    public Collection<Student> getAllStudentsBetweenTargetAge(@RequestParam int min, @RequestParam int max) {
        return studentService.getAllStudentsInASpecifiedAgeRange(min, max);
    }

    @GetMapping("/get-faculty/{id}")
    public Faculty getFaculty(@PathVariable long id) {
        return studentService.getFacultyStudent(id);
    }

    @GetMapping("/get-quantity-all-students")
    public int getQuantityAllStudents() {
        return studentService.getQuantityAllStudents();
    }

    @GetMapping("/get-average-age")
    public double getAverageAgeAllStudents() {
        return studentService.getAverageAgeAllStudents();
    }

    @GetMapping("/get-all")
    public Collection<Student> getAllStudents(@RequestParam int page, @RequestParam int size) {
        return studentService.getAllStudents(page, size);
    }
}