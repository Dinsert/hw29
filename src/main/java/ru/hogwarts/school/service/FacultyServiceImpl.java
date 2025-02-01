package ru.hogwarts.school.service;

import java.util.Collection;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.exception.FacultyNotFoundException;
import ru.hogwarts.school.exception.StudentNotFoundException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;

@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class FacultyServiceImpl implements FacultyService {

    FacultyRepository facultyRepository;

    @Override
    public Faculty createFaculty(Faculty faculty) {
        return facultyRepository.save(faculty);
    }

    @Override
    public Faculty findFaculty(long id) {
        return facultyRepository
                .findById(id)
                .orElseThrow(() -> new FacultyNotFoundException("Факультет по идентификатору " + id + " не найден"));
    }

    @Override
    public Faculty editFaculty(Faculty faculty) {
        return facultyRepository.save(faculty);
    }

    @Override
    public String deleteFaculty(long id) {
        facultyRepository.delete(findFaculty(id));
        return "Факультет по идентификатору " + id + " удалён";
    }

    @Override
    public Collection<Faculty> getAListOfFacultiesBySpecifiedColor(String color) {
        Collection<Faculty> faculties = facultyRepository.findByColor(color);
        if (faculties.isEmpty()) {
            throw new FacultyNotFoundException("Факультеты по цвету " + color + " не были найдены");
        }
        return faculties;
    }

    @Override
    public Faculty getFacultyByNameOrColor(String nameOrColor) {
        return facultyRepository
                .findFirstByNameIgnoreCaseOrColorIgnoreCase(nameOrColor, nameOrColor)
                .orElseThrow(() -> new FacultyNotFoundException("Факультет по имени/цвету " + nameOrColor + " не найден"));
    }

    @Override
    public Collection<Student> getAllStudents(long id) {
        Collection<Student> students = facultyRepository.getAllStudentsByFacultyId(id);
        if (students.isEmpty()) {
            throw new StudentNotFoundException("Студенты по идентификатору факультета " + id + " не были найдены");
        }
        return students;
    }

}