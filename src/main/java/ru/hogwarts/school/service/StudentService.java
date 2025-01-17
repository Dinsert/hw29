package ru.hogwarts.school.service;

import java.util.Collection;
import java.util.HashMap;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Student;

@Service
public class StudentService {

    private final HashMap<Long, Student> students = new HashMap<>();
    private long lastId;

    public Student createStudent(Student student) {
        student.setId(++lastId);
        students.put(student.getId(), student);
        return student;
    }

    public Student findStudent(long id) {
        return students.get(id);
    }

    public Student editStudent(Student student) {
        if (!students.containsKey(student.getId())) {
            return null;
        }
        students.put(student.getId(), student);
        return student;
    }

    public Student deleteStudent(long id) {
        return students.remove(id);
    }

    public Collection<Student> getAListOfStudentsBySpecifiedAge(int age) {
        return students.values()
                       .stream()
                       .filter(student -> student.getAge() == age)
                       .toList();
    }

}
