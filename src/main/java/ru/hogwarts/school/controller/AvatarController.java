package ru.hogwarts.school.controller;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.service.AvatarService;

@RestController
@RequestMapping("/avatar")
@RequiredArgsConstructor
public class AvatarController {

    private final AvatarService avatarService;

    @PostMapping(value = "/{studentId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String uploadAvatar(@PathVariable long studentId, @RequestParam MultipartFile avatarFile) throws IOException {
        return avatarService.uploadAvatar(studentId, avatarFile);
    }

    @GetMapping("/from-db/{studentId}")
    public ResponseEntity<byte[]> downloadAvatarFromDB(@PathVariable long studentId) {
        return avatarService.downloadAvatarFromDB(studentId);
    }

    @GetMapping("/from-file/{studentId}")
    public void downloadAvatarFromFile(@PathVariable long studentId, HttpServletResponse response) throws IOException {
        avatarService.downloadAvatarFromFile(studentId, response);
    }
}