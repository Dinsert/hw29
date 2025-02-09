package ru.hogwarts.school.repository;

import java.util.Collection;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

@Repository
public interface FacultyRepository extends JpaRepository<Faculty, Long> {

    Collection<Faculty> findByColor(String color);

    Optional<Faculty> findFirstByNameIgnoreCaseOrColorIgnoreCase(String name, String color);

    @Query("select students from faculties where id = :id")
    Collection<Student> getAllStudentsByFacultyId(long id);

}