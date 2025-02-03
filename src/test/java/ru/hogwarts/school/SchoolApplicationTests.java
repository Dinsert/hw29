package ru.hogwarts.school;

import static org.assertj.core.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import ru.hogwarts.school.controller.AvatarController;
import ru.hogwarts.school.controller.FacultyController;
import ru.hogwarts.school.controller.StudentController;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

@ActiveProfiles("test")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class SchoolApplicationTests {

    @LocalServerPort
    @NonFinal int port;
    @Autowired
    @NonFinal AvatarController avatarController;
    @Autowired
    @NonFinal FacultyController facultyController;
    @Autowired
    @NonFinal StudentController studentController;
    @Autowired
    @NonFinal TestRestTemplate restTemplate;

    //-------------------Тестовые данные для FacultyController--------------------

    Faculty facultyEdit = new Faculty(3L, "testName", "testColor");
    String facultyNotFound = "{\"message\":\"Факультет по идентификатору 7 не найден\",\"status\":404}";
    Faculty faculty2 = new Faculty(2L, "Gryffindor2", "Red2");

    //-------------------Тестовые данные для StudentController---------------------

    Student studentEdit = new Student(4L, "testName", 99);
    String studentNotFound = "{\"message\":\"Студент по идентификатору 9 не был найден\",\"status\":404}";
    Student student5 = new Student(5L, "Harry5", 16);
    Student[] students = {student5, new Student(6L, "Harry6", 16)};

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
        assertThat(restTemplate.getForObject(urlFaculty("/2"), Faculty.class))
                .isEqualTo(faculty2);
    }

    @Test
    public void testGetFacultyNotFound() throws Exception {
        ResponseEntity<String> actual = restTemplate.getForEntity(urlFaculty("/7"), String.class);
        assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(actual.getBody()).isEqualTo(facultyNotFound);
    }

    @Test
    public void testPutFaculty() throws Exception {
        Faculty before = new Faculty(3L, "Gryffindor3", "Red3");
        Faculty actual = restTemplate.getForObject(urlFaculty("/3"), Faculty.class);
        assertThat(actual).isEqualTo(before);
        assertThat(restTemplate.exchange(urlFaculty(), HttpMethod.PUT, new HttpEntity<>(facultyEdit), Faculty.class).getBody())
                .isEqualTo(facultyEdit);
        assertThat(actual).isEqualTo(facultyEdit);
    }

    @Test
    public void testDeleteFaculty() throws Exception {
        assertThat(restTemplate.exchange(urlFaculty("/6"), HttpMethod.DELETE, null, String.class).getBody())
                .isEqualTo("Факультет по идентификатору 6 удалён");
    }

    @Test
    public void testDeleteFacultyNotFound() throws Exception {
        ResponseEntity<String> actual = restTemplate.exchange(urlFaculty("/7"), HttpMethod.DELETE, null, String.class);
        assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(actual.getBody()).isEqualTo(facultyNotFound);
    }

    @Test
    public void testGetAListOfFacultiesBySpecifiedColor() throws Exception {
        assertThat(restTemplate.getForObject(urlFaculty("/get-list-by-color/Red2"), Faculty[].class))
                .isEqualTo(new Faculty[]{faculty2, new Faculty(4L, "Gryffindor2", "Red2")});
    }

    @Test
    public void testGetAListOfFacultiesBySpecifiedColorNotFound() throws Exception {
        ResponseEntity<String> actual = restTemplate.getForEntity(urlFaculty("/get-list-by-color/Red7"), String.class);
        assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(actual.getBody()).isEqualTo("{\"message\":\"Факультеты по цвету Red7 не были найдены\",\"status\":404}");
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
        assertThat(restTemplate.getForObject(urlFaculty("/get-students/5"), Student[].class))
                .isEqualTo(new Student[]{new Student(2L, "Harry2", 15), new Student(3L, "Harry3", 15)});
    }

    @Test
    public void testGetAllStudentsNotFound() throws Exception {
        ResponseEntity<String> actual = restTemplate.getForEntity(urlFaculty("/get-students/7"), String.class);
        assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(actual.getBody()).isEqualTo("{\"message\":\"Студенты по идентификатору факультета 7 не были найдены\",\"status\":404}");
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
        ResponseEntity<String> actual = restTemplate.getForEntity(urlStudent("/9"), String.class);
        assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(actual.getBody()).isEqualTo(studentNotFound);
    }

    @Test
    public void testPutStudent() throws Exception {
        Student before = new Student(4L, "Harry4", 15);
        Student actual = restTemplate.getForObject(urlStudent("/4"), Student.class);
        assertThat(actual).isEqualTo(before);
        assertThat(restTemplate.exchange(urlStudent(), HttpMethod.PUT, new HttpEntity<>(studentEdit), Student.class).getBody())
                .isEqualTo(studentEdit);
        assertThat(actual).isEqualTo(studentEdit);
    }

    @Test
    public void testDeleteStudent() throws Exception {
        assertThat(restTemplate.exchange(urlStudent("/8"), HttpMethod.DELETE, null, String.class).getBody())
                .isEqualTo("Студент по идентификатору 8 удалён");
    }

    @Test
    public void testDeleteStudentNotFound() throws Exception {
        ResponseEntity<String> actual = restTemplate.exchange(urlStudent("/9"), HttpMethod.DELETE, null, String.class);
        assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(actual.getBody()).isEqualTo(studentNotFound);
    }

    @Test
    public void testGetAListOfStudentsBySpecifiedAge() throws Exception {
        assertThat(restTemplate.getForObject(urlStudent("/get-list-by-age/16"), Student[].class))
                .isEqualTo(students);
    }

    @Test
    public void testGetAListOfStudentsBySpecifiedAgeNotFound() throws Exception {
        ResponseEntity<String> actual = restTemplate.getForEntity(urlStudent("/get-list-by-age/17"), String.class);
        assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(actual.getBody()).isEqualTo("{\"message\":\"Студенты по возрасту 17 лет не были найдены\",\"status\":404}");
    }

    @Test
    public void testGetAllStudentsBetweenTargetAge() throws Exception {
        assertThat(restTemplate.getForObject(urlStudent("?min=16&max=17"), Student[].class))
                .isEqualTo(students);
    }

    @Test
    public void testGetAllStudentsBetweenTargetAgeNotFound() throws Exception {
        ResponseEntity<String> actual = restTemplate.getForEntity(urlStudent("?min=17&max=18"), String.class);
        assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(actual.getBody()).isEqualTo("{\"message\":\"Студенты от 17 до 18 лет не были найдены\",\"status\":404}");
    }

    @Test
    public void testGetFacultyStudent() throws Exception {
        assertThat(restTemplate.getForObject(urlStudent("/get-faculty/2"), Faculty.class))
                .isEqualTo(new Faculty(5L, "Gryffindor5", "Red5"));
    }

    @Test
    public void testGetFacultyStudentNotFound() throws Exception {
        ResponseEntity<String> actual = restTemplate.getForEntity(urlStudent("/get-faculty/5"), String.class);
        assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(actual.getBody()).isEqualTo("{\"message\":\"Факультет по идентификатору студента 5 не был найден\",\"status\":404}");
    }

    //-------------------------AVATAR------------------------------

    @Test
    public void testPostUploadAvatar() throws Exception {
        createAvatar();
    }

    @Test
    public void testDownloadAvatarFromDB() throws Exception {
        createAvatar();
        ResponseEntity<byte[]> actual = restTemplate.getForEntity(urlAvatar("/from-db/7"), byte[].class);
        downloadAvatar(actual);
    }

    @Test
    public void testDownloadAvatarFromDBNotFound() throws Exception {
        ResponseEntity<String> actual = restTemplate.getForEntity(urlAvatar("/from-db/5"), String.class);
        avatarNotFound(actual);
    }

    @Test
    public void testDownloadAvatarFromFile() throws Exception {
        createAvatar();
        ResponseEntity<byte[]> actual = restTemplate.getForEntity(urlAvatar("/from-file/7"), byte[].class);
        downloadAvatar(actual);
    }

    @Test
    public void testDownloadAvatarFromFileNotFound() throws Exception {
        ResponseEntity<String> actual = restTemplate.getForEntity(urlAvatar("/from-file/5"), String.class);
        avatarNotFound(actual);
    }

    private void createAvatar() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("avatarFile", new FileSystemResource("src/test/resources/test.jpg"));
        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(body, headers);
        assertThat(restTemplate.postForObject(urlAvatar("/7"), httpEntity, String.class))
                .isEqualTo("Аватарка по идентификатору студента 7 была загружена");
    }

    private void downloadAvatar(ResponseEntity<byte[]> actual) throws IOException {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.parseMediaType("image/jpeg"));
        httpHeaders.setContentLength(42408);
        httpHeaders.setDate(new Date().getTime());
        assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(actual.getHeaders()).isEqualTo(httpHeaders);
        assertThat(actual.getBody()).isEqualTo(Files.readAllBytes(Path.of("src/test/resources/test.jpg")));
    }

    private void avatarNotFound(ResponseEntity<String> actual) {
        assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(actual.getBody()).isEqualTo("{\"message\":\"Аватарка по идентификатору студента 5 не найдена\",\"status\":404}");
    }

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

    private String urlAvatar(String path) {
        return "http://localhost:" + port + "/avatar" + path;
    }

}