package ru.hogwarts.school.repository;

import java.util.Collection;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FacultyRepository extends JpaRepository<Faculty, Long> {

    Collection<Faculty> findByColor(String color);

    Optional<Faculty> findFirstByNameOrColorIgnoreCase(String name, String color);

    @Query("select students from faculties where id = :id")
    Collection<Student> getAllStudentsByFacultyId(long id);

}