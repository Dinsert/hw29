package ru.hogwarts.school;

import static org.assertj.core.api.Assertions.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import ru.hogwarts.school.controller.AvatarController;
import ru.hogwarts.school.controller.FacultyController;
import ru.hogwarts.school.controller.StudentController;
import ru.hogwarts.school.model.Faculty;

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
    private final Faculty expected = new Faculty(1L, "Gryffindor", "Red");

    @Test
    void contextLoads() throws Exception {
        assertThat(avatarController).isNotNull();
        assertThat(facultyController).isNotNull();
        assertThat(studentController).isNotNull();
    }

    @Test
    public void testPostFaculty() throws Exception {
        Faculty actual = new Faculty();
        actual.setName("Gryffindor");
        actual.setColor("Red");
        assertThat(restTemplate.postForObject("http://localhost:" + port + "/faculty", actual, Faculty.class))
                .isEqualTo(expected);
    }

    @Test
    public void testGetFaculty() throws Exception {
        assertThat(restTemplate.getForObject("http://localhost:" + port + "/faculty/1", Faculty.class))
                .isEqualTo(expected);
    }

    @Test
    public void testGetFacultyNotFound() throws Exception {
        assertThat(restTemplate.getForEntity("http://localhost:" + port + "/faculty/2", Faculty.class).getStatusCode())
                .isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void testPutFaculty() throws Exception {
        Faculty actual = new Faculty();
        actual.setName("testName");
        actual.setColor("testColor");
        ObjectMapper mapper = new ObjectMapper();
        String requestBody = mapper.writeValueAsString(actual);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> httpEntity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange("http://localhost:" + port + "/faculty/1", HttpMethod.PUT, httpEntity, String.class);
        String expectedResponseBody = "{id:1,name:testName,color:testColor}";
        JSONAssert.assertEquals(expectedResponseBody, responseEntity.getBody(), false);
    }

}