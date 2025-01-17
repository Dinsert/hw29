package ru.hogwarts.school.service;

import java.util.Collection;
import java.util.HashMap;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Student;

@Service
public class StudentService {

    private final HashMap<Long, Student> studentHashMap = new HashMap<>();
    private long lastId;

    public Student createStudent(Student student) {
        student.setId(++lastId);
        studentHashMap.put(student.getId(), student);
        return student;
    }

    public Student findStudent(long id) {
        return studentHashMap.get(id);
    }

    public Student editStudent(Student student) {
        studentHashMap.put(student.getId(), student);
        return student;
    }

    public Student deleteStudent(long id) {
        return studentHashMap.remove(id);
    }

    public Collection<Student> getAListOfStudentsBySpecifiedAge(int age) {
        return studentHashMap.values()
                             .stream()
                             .filter(student -> student.getAge() == age)
                             .toList();
    }

}
