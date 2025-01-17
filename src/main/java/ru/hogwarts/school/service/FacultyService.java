package ru.hogwarts.school.service;

import java.util.Collection;
import java.util.HashMap;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;

@Service
public class FacultyService {

    private final HashMap<Long, Faculty> facultyHashMap = new HashMap<>();
    private long lastId;

    public Faculty createFaculty(Faculty faculty) {
        faculty.setId(++lastId);
        facultyHashMap.put(faculty.getId(), faculty);
        return faculty;
    }

    public Faculty findFaculty(long id) {
        return facultyHashMap.get(id);
    }

    public Faculty editFaculty(Faculty faculty) {
        facultyHashMap.put(faculty.getId(), faculty);
        return faculty;
    }

    public Faculty deleteFaculty(long id) {
        return facultyHashMap.remove(id);
    }

    public Collection<Faculty> getAListOfFacultiesBySpecifiedColor(String color) {
        return facultyHashMap.values()
                             .stream()
                             .filter(faculty -> faculty.getColor().equals(color))
                             .toList();
    }
}
