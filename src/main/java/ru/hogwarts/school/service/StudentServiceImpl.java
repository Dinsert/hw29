package ru.hogwarts.school.service;

import java.util.Collection;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.repository.StudentRepository;

@RequiredArgsConstructor
@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;

    @Override
    public Student createStudent(Student student) {
        return studentRepository.save(student);
    }

    @Override
    public Student findStudent(long id) {
        return studentRepository.findById(id).orElseThrow();
    }

    @Override
    public Student editStudent(Student student) {
        return studentRepository.save(student);
    }

    @Override
    public String deleteStudent(long id) {
        studentRepository.deleteById(id);
        return "Студент удалён";
    }

    @Override
    public Collection<Student> getAListOfStudentsBySpecifiedAge(int age) {
        return studentRepository.findByAge(age);
    }

    @Override
    public Collection<Student> getAllStudentsInASpecifiedAgeRange(int min, int max) {
        return studentRepository.findByAgeBetween(min, max);
    }

    @Override
    public Faculty getFacultyStudent(long id) {
        return studentRepository.getFacultyStudent(id).orElseThrow();
    }
}