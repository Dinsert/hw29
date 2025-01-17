package ru.hogwarts.school.service;

import java.util.Collection;
import java.util.HashMap;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;

@Service
public class FacultyService {

    private final HashMap<Long, Faculty> faculties = new HashMap<>();
    private long lastId;

    public Faculty createFaculty(Faculty faculty) {
        faculty.setId(++lastId);
        faculties.put(faculty.getId(), faculty);
        return faculty;
    }

    public Faculty findFaculty(long id) {
        return faculties.get(id);
    }

    public Faculty editFaculty(Faculty faculty) {
        if (!faculties.containsKey(faculty.getId())) {
            return null;
        }
        faculties.put(faculty.getId(), faculty);
        return faculty;
    }

    public Faculty deleteFaculty(long id) {
        return faculties.remove(id);
    }

    public Collection<Faculty> getAListOfFacultiesBySpecifiedColor(String color) {
        return faculties.values()
                        .stream()
                        .filter(faculty -> faculty.getColor().equals(color))
                        .toList();
    }
}
