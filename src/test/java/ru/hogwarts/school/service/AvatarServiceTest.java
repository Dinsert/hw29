package ru.hogwarts.school.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import ru.hogwarts.school.exception.AvatarNotFoundException;
import ru.hogwarts.school.exception.StudentNotFoundException;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.AvatarRepository;
import ru.hogwarts.school.repository.StudentRepository;

@ExtendWith(MockitoExtension.class)
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
class AvatarServiceTest {

    @Mock
    @NonFinal AvatarRepository avatarMock;
    @Mock
    @NonFinal StudentRepository studentMock;
    @InjectMocks
    @NonFinal AvatarServiceImpl out;

    String contentType = "image/jpeg";
    int contentLength = 42408;
    String path = "src/test/resources/test.jpg";
    long id = 1L;
    Avatar avatar = new Avatar();

    @Test
    void uploadAvatar() throws IOException {
        when(studentMock.findById(anyLong())).thenReturn(Optional.of(new Student(id, "Harry1", 15)));
        when(avatarMock.findByStudentId(anyLong())).thenReturn(Optional.of(avatar));
        when(avatarMock.save(any())).thenReturn(avatar);

        String actual = out.uploadAvatar(id, avatarFile());
        String expected = "Аватарка по идентификатору студента 1 была загружена";

        assertEquals(expected, actual);
    }

    @Test
    void shouldThrowStudentNotFoundExceptionAtUploadAvatar() {
        when(studentMock.findById(anyLong())).thenThrow(StudentNotFoundException.class);

        assertThrows(StudentNotFoundException.class, () -> out.uploadAvatar(id, avatarFile()));
    }

    @Test
    void downloadAvatarFromDB() throws IOException {
        avatar.setMediaType(contentType);
        avatar.setFileSize(contentLength);
        avatar.setData(getData());
        when(avatarMock.findByStudentId(anyLong())).thenReturn(Optional.of(avatar));

        ResponseEntity<byte[]> actual = out.downloadAvatarFromDB(id);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(contentType));
        headers.setContentLength(contentLength);

        assertEquals(HttpStatus.OK, actual.getStatusCode());
        assertEquals(headers, actual.getHeaders());
        assertArrayEquals(getData(), actual.getBody());
    }

    @Test
    void shouldThrowAvatarNotFoundExceptionAtDownloadAvatarFromDB() throws IOException {
        when(avatarMock.findByStudentId(anyLong())).thenThrow(AvatarNotFoundException.class);

        assertThrows(AvatarNotFoundException.class, () -> out.downloadAvatarFromDB(id));
    }

    @Test
    void downloadAvatarFromFile() throws IOException {
        avatar.setMediaType(contentType);
        avatar.setData(getData());
        avatar.setFilePath(path);
        when(avatarMock.findByStudentId(anyLong())).thenReturn(Optional.of(avatar));

        HttpServletResponse response = new MockHttpServletResponse();
        out.downloadAvatarFromFile(id, response);

        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    void shouldThrowAvatarNotFoundExceptionAtDownloadAvatarFromFile() throws IOException {
        when(avatarMock.findByStudentId(anyLong())).thenThrow(AvatarNotFoundException.class);

        assertThrows(AvatarNotFoundException.class, () -> out.downloadAvatarFromFile(id, new MockHttpServletResponse()));
    }

    private byte[] getData() throws IOException {
        return Files.readAllBytes(Path.of(path));
    }

    private MockMultipartFile avatarFile() throws IOException {
        return new MockMultipartFile("test", "test.jpg", contentType, getData());
    }
}