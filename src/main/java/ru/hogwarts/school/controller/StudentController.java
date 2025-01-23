package ru.hogwarts.school.controller;

import java.util.Collection;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Student> find(@PathVariable long id) {
        Student student = studentService.findStudent(id);
        if (student == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(student);
    }

    @PutMapping
    public ResponseEntity<Student> edit(@RequestBody Student student) {
        Student foundStudent = studentService.editStudent(student);
        if (foundStudent == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(foundStudent);
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
}