package ru.hogwarts.school;

import static org.assertj.core.api.Assertions.*;

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
import ru.hogwarts.school.controller.FacultyController;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class SchoolApplicationFacultyTests {

    @LocalServerPort
    private int port;
    @Autowired
    private FacultyController facultyController;
    @Autowired
    private TestRestTemplate restTemplate;

    private final long id2 = 2L;
    private final long id3 = 3L;
    private final String name2 = "Gryffindor2";
    private final String color2 = "Red2";
    private final String color7 = "Red7";

    private final Faculty faculty2 = new Faculty(id2, name2, color2);
    private final Faculty facultyEdit = new Faculty(id3, "testName", "testColor");

    private final String message = "{\"message\":\"";
    private final String status404 = "\",\"status\":404}";

    private final String getListByColor = "/get-list-by-color";
    private final String nameOrColor = "?nameOrColor=";
    private final String getStudents = "/get-students";
    private final String pathVariable7 = "/7";

    @Test
    void contextLoads() throws Exception {
        assertThat(facultyController).isNotNull();
    }

    @Test
    public void testPostFaculty() throws Exception {
        String name = "Gryffindor1";
        String color = "Red1";

        Faculty actual = new Faculty();
        actual.setName(name);
        actual.setColor(color);
        Faculty expected = new Faculty(1L, name, color);

        assertThat(restTemplate.postForObject(url(), actual, Faculty.class))
                .isEqualTo(expected);
    }

    @Test
    public void testGetFaculty() throws Exception {
        assertThat(restTemplate.getForObject(url("/2"), Faculty.class))
                .isEqualTo(faculty2);
    }

    @Test
    public void testGetFacultyNotFound() throws Exception {
        ResponseEntity<String> actual = restTemplate.getForEntity(url(pathVariable7), String.class);

        assertThatHttpStatusNotFound(actual);
        assertThatActualBodyEqualToMessage(actual);
    }

    @Test
    public void testPutFaculty() throws Exception {
        Faculty before = new Faculty(id3, "Gryffindor3", "Red3");
        Faculty actual = restTemplate.getForObject(url("/3"), Faculty.class);

        assertThat(actual).isEqualTo(before);
        assertThat(restTemplate.exchange(url(), HttpMethod.PUT, new HttpEntity<>(facultyEdit), Faculty.class).getBody())
                .isEqualTo(facultyEdit);
        assertThat(actual).isEqualTo(facultyEdit);
    }

    @Test
    public void testDeleteFaculty() throws Exception {
        int id = 6;

        assertThat(restTemplate.exchange(url("/" + id), HttpMethod.DELETE, null, String.class).getBody())
                .isEqualTo("Факультет по идентификатору " + id + " удалён");
    }

    @Test
    public void testDeleteFacultyNotFound() throws Exception {
        ResponseEntity<String> actual = restTemplate.exchange(url(pathVariable7), HttpMethod.DELETE, null, String.class);

        assertThatHttpStatusNotFound(actual);
        assertThatActualBodyEqualToMessage(actual);
    }

    @Test
    public void testGetAListOfFacultiesBySpecifiedColor() throws Exception {
        assertThat(restTemplate.getForObject(url(getListByColor + "/" + color2), Faculty[].class))
                .isEqualTo(new Faculty[]{faculty2, new Faculty(4L, name2, color2)});
    }

    @Test
    public void testGetAListOfFacultiesBySpecifiedColorNotFound() throws Exception {
        ResponseEntity<String> actual = restTemplate.getForEntity(url(getListByColor + "/" + color7), String.class);

        assertThatHttpStatusNotFound(actual);
        assertThat(actual.getBody()).isEqualTo(message + "Факультеты по цвету " + color7 + " не были найдены" + status404);
    }

    @Test
    public void testGetFacultyByNameOrColor() throws Exception {
        assertThat(restTemplate.getForObject(url(nameOrColor + name2), Faculty.class))
                .isEqualTo(faculty2);
        assertThat(restTemplate.getForObject(url(nameOrColor + color2), Faculty.class))
                .isEqualTo(faculty2);
    }

    @Test
    public void testGetFacultyByNameOrColorNotFound() throws Exception {
        String color = "Gryffindor7";

        assertThat(restTemplate.getForEntity(url(nameOrColor + color7), Faculty.class).getStatusCode())
                .isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(restTemplate.getForObject(url(nameOrColor + color), String.class))
                .isEqualTo(message + "Факультет по имени/цвету " + color + " не найден" + status404);
    }

    @Test
    public void testGetAllStudents() throws Exception {
        int age = 15;

        assertThat(restTemplate.getForObject(url(getStudents + "/5"), Student[].class))
                .isEqualTo(new Student[]{new Student(id2, "Harry2", age), new Student(id3, "Harry3", age)});
    }

    @Test
    public void testGetAllStudentsNotFound() throws Exception {
        ResponseEntity<String> actual = restTemplate.getForEntity(url(getStudents + pathVariable7), String.class);

        assertThatHttpStatusNotFound(actual);
        assertThat(actual.getBody()).isEqualTo(message + "Студенты по идентификатору факультета 7 не были найдены" + status404);
    }

    private String url() {
        return "http://localhost:" + port + "/faculty";
    }

    private String url(String path) {
        return "http://localhost:" + port + "/faculty" + path;
    }

    private void assertThatHttpStatusNotFound(ResponseEntity<String> actual) {
        assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    private void assertThatActualBodyEqualToMessage(ResponseEntity<String> actual) {
        assertThat(actual.getBody()).isEqualTo(message + "Факультет по идентификатору 7 не найден" + status404);
    }
}