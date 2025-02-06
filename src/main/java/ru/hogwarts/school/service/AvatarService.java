package ru.hogwarts.school.service;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.model.Avatar;

public interface AvatarService {

    String uploadAvatar(long studentId, MultipartFile avatar) throws IOException;

    ResponseEntity<byte[]> downloadAvatarFromDB(long studentId);

    void downloadAvatarFromFile(long studentId, HttpServletResponse response) throws IOException;

    Collection<Avatar> getAllAvatars(int page, int size);
}