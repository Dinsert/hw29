package ru.hogwarts.school;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import ru.hogwarts.school.controller.AvatarController;
import ru.hogwarts.school.controller.FacultyController;
import ru.hogwarts.school.controller.StudentController;

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

    @Test
    void contextLoads() throws Exception {
        Assertions.assertThat(avatarController).isNotNull();
        Assertions.assertThat(facultyController).isNotNull();
        Assertions.assertThat(studentController).isNotNull();
    }

}
