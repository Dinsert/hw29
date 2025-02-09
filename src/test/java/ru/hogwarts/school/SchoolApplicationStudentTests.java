package ru.hogwarts.school;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import ru.hogwarts.school.controller.StudentController;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class SchoolApplicationStudentTests {

    @LocalServerPort
    private int port;
    @Autowired
    private StudentController studentController;
    @Autowired
    private TestRestTemplate restTemplate;

    private final int age15 = 15;
    private final int age16 = 16;

    private final Student studentEdit = new Student(4L, "testName", 99);
    private final Student student5 = new Student(5L, "Harry5", age16);
    private final Student[] students = {student5, new Student(6L, "Harry6", age16)};

    private final String message = "{\"message\":\"";
    private final String status404 = "\",\"status\":404}";
    private final String studentNotFound = message + "Студент по идентификатору 9 не был найден" + status404;

    private final String getListByAge = "/get-list-by-age";
    private final String getFaculty = "/get-faculty";
    private final String pathVariable9 = "/9";

    @Test
    void contextLoads() throws Exception {
        assertThat(studentController).isNotNull();
    }

    @Test
    public void testPostStudent() throws Exception {
        String name = "Harry1";

        Student actual = new Student();
        actual.setName(name);
        actual.setAge(age15);
        Student expected = new Student(1L, name, age15);

        assertThat(restTemplate.postForObject(url(), actual, Student.class))
                .isEqualTo(expected);
    }

    @Test
    public void testGetStudent() throws Exception {
        assertThat(restTemplate.getForObject(url("/5"), Student.class))
                .isEqualTo(student5);
    }

    @Test
    public void testGetStudentNotFound() throws Exception {
        ResponseEntity<String> actual = restTemplate.getForEntity(url(pathVariable9), String.class);

        assertThatHttpStatusNotFound(actual);
        assertThat(actual.getBody()).isEqualTo(studentNotFound);
    }

    @Test
    public void testPutStudent() throws Exception {
        Student before = new Student(4L, "Harry4", age15);
        Student actual = restTemplate.getForObject(url("/4"), Student.class);

        assertThat(actual).isEqualTo(before);
        assertThat(restTemplate.exchange(url(), HttpMethod.PUT, new HttpEntity<>(studentEdit), Student.class).getBody())
                .isEqualTo(studentEdit);
        assertThat(actual).isEqualTo(studentEdit);
    }

    @Test
    public void testDeleteStudent() throws Exception {
        assertThat(restTemplate.exchange(url("/8"), HttpMethod.DELETE, null, String.class).getBody())
                .isEqualTo("Студент по идентификатору 8 удалён");
    }

    @Test
    public void testDeleteStudentNotFound() throws Exception {
        ResponseEntity<String> actual = restTemplate.exchange(url(pathVariable9), HttpMethod.DELETE, null, String.class);

        assertThatHttpStatusNotFound(actual);
        assertThat(actual.getBody()).isEqualTo(studentNotFound);
    }

    @Test
    public void testGetAListOfStudentsBySpecifiedAge() throws Exception {
        assertThat(restTemplate.getForObject(url(getListByAge + "/" + age16), Student[].class))
                .isEqualTo(students);
    }

    @Test
    public void testGetAListOfStudentsBySpecifiedAgeNotFound() throws Exception {
        ResponseEntity<String> actual = restTemplate.getForEntity(url(getListByAge + "/17"), String.class);

        assertThatHttpStatusNotFound(actual);
        assertThat(actual.getBody()).isEqualTo(message + "Студенты по возрасту 17 лет не были найдены" + status404);
    }

    @Test
    public void testGetAllStudentsBetweenTargetAge() throws Exception {
        assertThat(restTemplate.getForObject(url("?min=" + age16 + "&max=17"), Student[].class))
                .isEqualTo(students);
    }

    @Test
    public void testGetAllStudentsBetweenTargetAgeNotFound() throws Exception {
        ResponseEntity<String> actual = restTemplate.getForEntity(url("?min=17&max=18"), String.class);

        assertThatHttpStatusNotFound(actual);
        assertThat(actual.getBody()).isEqualTo(message + "Студенты от 17 до 18 лет не были найдены" + status404);
    }

    @Test
    public void testGetFacultyStudent() throws Exception {
        assertThat(restTemplate.getForObject(url(getFaculty + "/2"), Faculty.class))
                .isEqualTo(new Faculty(5L, "Gryffindor5", "Red5"));
    }

    @Test
    public void testGetFacultyStudentNotFound() throws Exception {
        ResponseEntity<String> actual = restTemplate.getForEntity(url(getFaculty + "/5"), String.class);

        assertThatHttpStatusNotFound(actual);
        assertThat(actual.getBody()).isEqualTo(message + "Факультет по идентификатору студента 5 не был найден" + status404);
    }

    private String url() {
        return "http://localhost:" + port + "/student";
    }

    private String url(String path) {
        return "http://localhost:" + port + "/student" + path;
    }

    private void assertThatHttpStatusNotFound(ResponseEntity<String> actual) {
        assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}