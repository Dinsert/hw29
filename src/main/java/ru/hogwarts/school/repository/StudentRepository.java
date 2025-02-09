package ru.hogwarts.school.repository;

import java.util.Collection;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    Collection<Student> findByAge(int age);

    Collection<Student> findByAgeBetween(int min, int max);

    @Query("select faculty from students where id = :id")
    Optional<Faculty> getFacultyStudent(long id);

    @Query("select count(s) from students s")
    int getCountStudents();

    @Query("select avg(age) from students")
    double getAverageValueByAgeAllStudents();
}