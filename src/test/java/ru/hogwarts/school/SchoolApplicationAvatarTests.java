package ru.hogwarts.school;

import static org.assertj.core.api.Assertions.assertThat;

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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import ru.hogwarts.school.controller.AvatarController;

@ActiveProfiles("test")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class SchoolApplicationAvatarTests {

    @LocalServerPort
    @NonFinal int port;
    @Autowired
    @NonFinal AvatarController avatarController;
    @Autowired
    @NonFinal TestRestTemplate restTemplate;

    String fromDb = "/from-db";
    String fromFile = "/from-file";
    String pathVariable7 = "/7";
    String pathVariable5 = "/5";
    String path = "src/test/resources/test.jpg";

    @Test
    void contextLoads() throws Exception {
        assertThat(avatarController).isNotNull();
    }

    @Test
    public void testPostUploadAvatar() throws Exception {
        createAvatar();
    }

    @Test
    public void testDownloadAvatarFromDB() throws Exception {
        createAvatar();
        ResponseEntity<byte[]> actual = restTemplate.getForEntity(urlAvatar(fromDb + pathVariable7), byte[].class);
        downloadAvatar(actual);
    }

    @Test
    public void testDownloadAvatarFromDBNotFound() throws Exception {
        ResponseEntity<String> actual = restTemplate.getForEntity(urlAvatar(fromDb + pathVariable5), String.class);
        avatarNotFound(actual);
    }

    @Test
    public void testDownloadAvatarFromFile() throws Exception {
        createAvatar();
        ResponseEntity<byte[]> actual = restTemplate.getForEntity(urlAvatar(fromFile + pathVariable7), byte[].class);
        downloadAvatar(actual);
    }

    @Test
    public void testDownloadAvatarFromFileNotFound() throws Exception {
        ResponseEntity<String> actual = restTemplate.getForEntity(urlAvatar(fromFile + pathVariable5), String.class);
        avatarNotFound(actual);
    }

    private void createAvatar() {
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("avatarFile", new FileSystemResource(path));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(body, headers);

        assertThat(restTemplate.postForObject(urlAvatar(pathVariable7), httpEntity, String.class))
                .isEqualTo("Аватарка по идентификатору студента 7 была загружена");
    }

    private void downloadAvatar(ResponseEntity<byte[]> actual) throws IOException {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.parseMediaType("image/jpeg"));
        httpHeaders.setContentLength(42408);
        httpHeaders.setDate(new Date().getTime());

        assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(actual.getHeaders()).isEqualTo(httpHeaders);
        assertThat(actual.getBody()).isEqualTo(Files.readAllBytes(Path.of(path)));
    }

    private void avatarNotFound(ResponseEntity<String> actual) {
        assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(actual.getBody()).isEqualTo("{\"message\":\"Аватарка по идентификатору студента 5 не найдена\",\"status\":404}");
    }

    private String urlAvatar(String path) {
        return "http://localhost:" + port + "/avatar" + path;
    }
}