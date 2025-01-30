package ru.hogwarts.school.service;

import java.util.Collection;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;

@Service
@RequiredArgsConstructor
public class FacultyServiceImpl implements FacultyService {

    private final FacultyRepository facultyRepository;

    @Override
    public Faculty createFaculty(Faculty faculty) {
        return facultyRepository.save(faculty);
    }

    @Override
    public Faculty findFaculty(long id) {
        return facultyRepository.findById(id).orElse(null);
    }

    @Override
    public Faculty editFaculty(Faculty faculty) {
        return facultyRepository.save(faculty);
    }

    @Override
    public String deleteFaculty(long id) {
        facultyRepository.deleteById(id);
        return "Факультет удалён";
    }

    @Override
    public Collection<Faculty> getAListOfFacultiesBySpecifiedColor(String color) {
        return facultyRepository.findByColor(color);
    }

    @Override
    public Faculty getFacultyByNameOrColor(String nameOrColor) {
        return facultyRepository.findFirstByNameOrColorIgnoreCase(nameOrColor, nameOrColor).orElseThrow();
    }

    @Override
    public Collection<Student> getAllStudents(long id) {
        return facultyRepository.getAllStudentsByFacultyId(id);
    }

}