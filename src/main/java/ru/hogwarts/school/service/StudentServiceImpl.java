package ru.hogwarts.school.service;

import java.util.Collection;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.exception.FacultyNotFoundException;
import ru.hogwarts.school.exception.StudentNotFoundException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
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
        return studentRepository
                .findById(id)
                .orElseThrow(() -> new StudentNotFoundException("Студент по идентификатору " + id + " не был найден"));
    }

    @Override
    public Student editStudent(Student student) {
        return studentRepository.save(student);
    }

    @Override
    public String deleteStudent(long id) {
        studentRepository.delete(findStudent(id));
        return "Студент по идентификатору " + id + " удалён";
    }

    @Override
    public Collection<Student> getAListOfStudentsBySpecifiedAge(int age) {
        Collection<Student> students = studentRepository.findByAge(age);
        if (students.isEmpty()) {
            throw new StudentNotFoundException("Студенты по возрасту " + age + " лет не были найдены");
        }
        return students;
    }

    @Override
    public Collection<Student> getAllStudentsInASpecifiedAgeRange(int min, int max) {
        Collection<Student> students = studentRepository.findByAgeBetween(min, max);
        if (students.isEmpty()) {
            throw new StudentNotFoundException("Студенты от " + min + " до " + max + " лет не были найдены");
        }
        return students;
    }

    @Override
    public Faculty getFacultyStudent(long id) {
        return studentRepository
                .getFacultyStudent(id)
                .orElseThrow(() -> new FacultyNotFoundException("Факультет по идентификатору студента " + id + " не был найден"));
    }

    @Override
    public int getQuantityAllStudents() {
        return studentRepository.getCountStudents();
    }

    @Override
    public double getAverageAgeAllStudents() {
        return studentRepository.getAverageValueByAgeAllStudents();
    }

    @Override
    public Collection<Student> getAllStudents(int page, int size) {
        PageRequest request = PageRequest.of(page - 1, size);
        return studentRepository.findAll(request).getContent();
    }
}