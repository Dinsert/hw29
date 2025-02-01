package ru.hogwarts.school;

import static org.assertj.core.api.Assertions.*;

import java.util.Collection;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import ru.hogwarts.school.controller.AvatarController;
import ru.hogwarts.school.controller.FacultyController;
import ru.hogwarts.school.controller.StudentController;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class SchoolApplicationTests {

    @LocalServerPort
    private int port;
    @Autowired
    private AvatarController avatarController;
    @Autowired
    private FacultyController facultyController;
    @Autowired
    private StudentController studentController;
    @Autowired
    private TestRestTemplate restTemplate;

                                      //-------------------Тестовые данные для FacultyController--------------------

    private final Faculty facultyEdit = new Faculty(3L, "testName", "testColor");
    private final String facultyNotFound = "{\"message\":\"Факультет по идентификатору 7 не найден\",\"status\":404}";
    private final Faculty faculty2 = new Faculty(2L, "Gryffindor2", "Red2");

                                      //-------------------Тестовые данные для StudentController---------------------

    private final Student studentEdit = new Student(4L, "testName", 99);
    private final String studentNotFound = "{\"message\":\"Студент по идентификатору 8 не был найден\",\"status\":404}";
    private final Student student5 = new Student(5L, "Harry5", 16);
    private final List<Student> students = List.of(student5, new Student(6L, "Harry6", 16));

    @Test
    void contextLoads() throws Exception {
        assertThat(avatarController).isNotNull();
        assertThat(facultyController).isNotNull();
        assertThat(studentController).isNotNull();
    }
                                        //-------------------------FACULTY------------------------------
    @Test
    public void testPostFaculty() throws Exception {
        Faculty actual = new Faculty();
        actual.setName("Gryffindor1");
        actual.setColor("Red1");
        Faculty expected = new Faculty(1L, "Gryffindor1", "Red1");
        assertThat(restTemplate.postForObject(urlFaculty(), actual, Faculty.class))
                .isEqualTo(expected);
    }

    @Test
    public void testGetFaculty() throws Exception {
        Faculty expected = faculty2;
        assertThat(restTemplate.getForObject(urlFaculty("/2"), Faculty.class))
                .isEqualTo(expected);
    }

    @Test
    public void testGetFacultyNotFound() throws Exception {
        assertThat(restTemplate.getForEntity(urlFaculty("/7"), Faculty.class).getStatusCode())
                .isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(restTemplate.getForObject(urlFaculty("/7"), String.class))
                .isEqualTo(facultyNotFound);
    }

    @Test
    public void testPutFaculty() throws Exception {
        Faculty before = new Faculty(3L, "Gryffindor3", "Red3");
        assertThat(restTemplate.getForObject(urlFaculty("/3"), Faculty.class))
                .isEqualTo(before);
        assertThat(restTemplate.exchange(urlFaculty(), HttpMethod.PUT, new HttpEntity<>(facultyEdit), Faculty.class).getBody())
                .isEqualTo(facultyEdit);
        assertThat(restTemplate.getForObject(urlFaculty("/3"), Faculty.class))
                .isEqualTo(facultyEdit);
    }

    @Test
    public void testDeleteFaculty() throws Exception {
        assertThat(restTemplate.exchange(urlFaculty("/6"), HttpMethod.DELETE, null, String.class).getBody())
                .isEqualTo("Факультет по идентификатору 6 удалён");
    }

    @Test
    public void testDeleteFacultyNotFound() throws Exception {
        assertThat(restTemplate.exchange(urlFaculty("/7"), HttpMethod.DELETE, null, Faculty.class).getStatusCode())
                .isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(restTemplate.exchange(urlFaculty("/7"), HttpMethod.DELETE, null, String.class).getBody())
                .isEqualTo(facultyNotFound);
    }

    @Test
    public void testGetAListOfFacultiesBySpecifiedColor() throws Exception {
        assertThat(restTemplate.exchange(urlFaculty("/get-list-by-color/Red2"), HttpMethod.GET, null, new ParameterizedTypeReference<Collection<Faculty>>() {
                               })
                               .getBody())
                .isEqualTo((List.of(faculty2, new Faculty(4L, "Gryffindor2", "Red2"))));
    }

    @Test
    public void testGetAListOfFacultiesBySpecifiedColorNotFound() throws Exception {
        assertThat(restTemplate.getForEntity(urlFaculty("/get-list-by-color/Red7"), String.class).getStatusCode())
                .isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(restTemplate.getForObject(urlFaculty("/get-list-by-color/Red7"), String.class))
                .isEqualTo("{\"message\":\"Факультеты по цвету Red7 не были найдены\",\"status\":404}");
    }

    @Test
    public void testGetFacultyByNameOrColor() throws Exception {
        assertThat(restTemplate.getForObject(urlFaculty("?nameOrColor=Gryffindor2"), Faculty.class))
                .isEqualTo(faculty2);
        assertThat(restTemplate.getForObject(urlFaculty("?nameOrColor=Red2"), Faculty.class))
                .isEqualTo(faculty2);
    }

    @Test
    public void testGetFacultyByNameOrColorNotFound() throws Exception {
        assertThat(restTemplate.getForEntity(urlFaculty("?nameOrColor=Red7"), Faculty.class).getStatusCode())
                .isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(restTemplate.getForObject(urlFaculty("?nameOrColor=Gryffindor7"), String.class))
                .isEqualTo("{\"message\":\"Факультет по имени/цвету Gryffindor7 не найден\",\"status\":404}");
    }

    @Test
    public void testGetAllStudents() throws Exception {
        assertThat(restTemplate.exchange(urlFaculty("/get-students/5"), HttpMethod.GET, null, new ParameterizedTypeReference<Collection<Student>>() {
        }).getBody())
                .isEqualTo(List.of(new Student(2L, "Harry2", 15), new Student(3L, "Harry3", 15)));
    }

    @Test
    public void testGetAllStudentsNotFound() throws Exception {
        assertThat(restTemplate.getForEntity(urlFaculty("/get-students/7"), String.class).getStatusCode())
                .isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(restTemplate.getForObject(urlFaculty("/get-students/7"), String.class))
                .isEqualTo("{\"message\":\"Студенты по идентификатору факультета 7 не были найдены\",\"status\":404}");

    }

                                             //-------------------------STUDENT------------------------------

    @Test
    public void testPostStudent() throws Exception {
        Student actual = new Student();
        actual.setName("Harry1");
        actual.setAge(15);
        Student expected = new Student(1L, "Harry1", 15);
        assertThat(restTemplate.postForObject(urlStudent(), actual, Student.class))
                .isEqualTo(expected);
    }

    @Test
    public void testGetStudent() throws Exception {
        assertThat(restTemplate.getForObject(urlStudent("/5"), Student.class))
                .isEqualTo(student5);
    }

    @Test
    public void testGetStudentNotFound() throws Exception {
        assertThat(restTemplate.getForEntity(urlStudent("/8"), Student.class).getStatusCode())
                .isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(restTemplate.getForObject(urlStudent("/8"), String.class))
                .isEqualTo(studentNotFound);
    }

    @Test
    public void testPutStudent() throws Exception {
        Student before = new Student(4L, "Harry4", 15);
        assertThat(restTemplate.getForObject(urlStudent("/4"), Student.class))
                .isEqualTo(before);
        assertThat(restTemplate.exchange(urlStudent(), HttpMethod.PUT, new HttpEntity<>(studentEdit), Student.class).getBody())
                .isEqualTo(studentEdit);
        assertThat(restTemplate.getForObject(urlStudent("/4"), Student.class))
                .isEqualTo(studentEdit);
    }

    @Test
    public void testDeleteStudent() throws Exception {
        assertThat(restTemplate.exchange(urlStudent("/7"), HttpMethod.DELETE, null, String.class).getBody())
                .isEqualTo("Студент по идентификатору 7 удалён");
    }

    @Test
    public void testDeleteStudentNotFound() throws Exception {
        assertThat(restTemplate.exchange(urlStudent("/8"), HttpMethod.DELETE, null, Student.class).getStatusCode())
                .isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(restTemplate.exchange(urlStudent("/8"), HttpMethod.DELETE, null, String.class).getBody())
                .isEqualTo(studentNotFound);
    }

    @Test
    public void testGetAListOfStudentsBySpecifiedAge() throws Exception {
        assertThat(restTemplate.exchange(urlStudent("/get-list-by-age/16"), HttpMethod.GET, null, new ParameterizedTypeReference<Collection<Student>>() {
        }).getBody())
                .isEqualTo(students);
    }

    @Test
    public void testGetAListOfStudentsBySpecifiedAgeNotFound() throws Exception {
        assertThat(restTemplate.getForEntity(urlStudent("/get-list-by-age/17"), String.class).getStatusCode())
                .isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(restTemplate.getForObject(urlStudent("/get-list-by-age/17"), String.class))
                .isEqualTo("{\"message\":\"Студенты по возрасту 17 лет не были найдены\",\"status\":404}");
    }

    @Test
    public void testGetAllStudentsBetweenTargetAge() throws Exception {
        assertThat(restTemplate.exchange(urlStudent("?min=16&max=17"), HttpMethod.GET, null, new ParameterizedTypeReference<Collection<Student>>() {
        }).getBody())
                .isEqualTo(students);
    }

    @Test
    public void testGetAllStudentsBetweenTargetAgeNotFound() throws Exception {
        assertThat(restTemplate.getForEntity(urlStudent("?min=17&max=18"), String.class).getStatusCode())
                .isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(restTemplate.getForObject(urlStudent("?min=17&max=18"), String.class))
                .isEqualTo("{\"message\":\"Студенты от 17 до 18 лет не были найдены\",\"status\":404}");
    }

    @Test
    public void testGetFacultyStudent() throws Exception {
        assertThat(restTemplate.getForObject(urlStudent("/get-faculty/2"), Faculty.class))
                .isEqualTo(new Faculty(5L, "Gryffindor5", "Red5"));
    }

    @Test
    public void testGetFacultyStudentNotFound() throws Exception {
        assertThat(restTemplate.getForEntity(urlStudent("/get-faculty/5"), Faculty.class).getStatusCode())
                .isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(restTemplate.getForObject(urlStudent("/get-faculty/5"), String.class))
                .isEqualTo("{\"message\":\"Факультет по идентификатору студента 5 не был найден\",\"status\":404}");
    }

                                                  //-------------------------AVATAR------------------------------



    private String urlFaculty() {
        return "http://localhost:" + port + "/faculty";
    }

    private String urlFaculty(String path) {
        return "http://localhost:" + port + "/faculty" + path;
    }

    private String urlStudent() {
        return "http://localhost:" + port + "/student";
    }

    private String urlStudent(String path) {
        return "http://localhost:" + port + "/student" + path;
    }

}