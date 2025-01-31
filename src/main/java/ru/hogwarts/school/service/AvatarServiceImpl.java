package ru.hogwarts.school.service;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.exception.AvatarNotFoundException;
import ru.hogwarts.school.exception.StudentNotFoundException;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.AvatarRepository;
import ru.hogwarts.school.repository.StudentRepository;

@Transactional
@Service
@RequiredArgsConstructor
public class AvatarServiceImpl implements AvatarService {

    @Value("${path.to.avatars.folder}")
    private String avatarDir;
    private final AvatarRepository avatarRepository;
    private final StudentRepository studentRepository;

    @Override
    public String uploadAvatar(long studentId, MultipartFile avatarFile) throws IOException {
        Student student = studentRepository
                .findById(studentId)
                .orElseThrow(() -> new StudentNotFoundException("Студент по идентификатору " + studentId + " не был найден"));
        Path filePath = Path.of(avatarDir, student + getExtensions(avatarFile.getOriginalFilename()));
        Files.createDirectories(filePath.getParent());
        Files.deleteIfExists(filePath);
        try (
                InputStream is = avatarFile.getInputStream();
                OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);
                BufferedInputStream bis = new BufferedInputStream(is, 1024);
                BufferedOutputStream bos = new BufferedOutputStream(os, 1024)
        ) {
            bis.transferTo(bos);
        }
        Avatar avatar = findAvatar(studentId);
        avatar.setStudent(student);
        avatar.setFilePath(filePath.toString());
        avatar.setFileSize(avatarFile.getSize());
        avatar.setMediaType(avatarFile.getContentType());
        avatar.setData(avatarFile.getBytes());
        avatarRepository.save(avatar);
        return "Аватарка по идентификатору студента " + studentId + " была загружена";
    }

    @Override
    public ResponseEntity<byte[]> downloadAvatarFromDB(long studentId) {
        Avatar avatar = avatarRepository
                .findByStudentId(studentId)
                .orElseThrow(() -> new AvatarNotFoundException("Аватарка по идентификатору студента " + studentId + " не найдена"));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(avatar.getMediaType()));
        headers.setContentLength(avatar.getFileSize());
        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(avatar.getData());
    }

    @Override
    public void downloadAvatarFromFile(long studentId, HttpServletResponse response) throws IOException {
        Avatar avatar = avatarRepository
                .findByStudentId(studentId)
                .orElseThrow(() -> new AvatarNotFoundException("Аватарка по идентификатору студента " + studentId + " не найдена"));
        Path path = Path.of(avatar.getFilePath());
        try (InputStream is = Files.newInputStream(path);
                OutputStream os = response.getOutputStream()) {
            response.setStatus(HttpStatus.OK.value());
            response.setContentType(avatar.getMediaType());
            response.setContentLength(avatar.getData().length);
            is.transferTo(os);
        }
    }

    private Avatar findAvatar(long studentId) {
        return avatarRepository.findByStudentId(studentId).orElseGet(Avatar::new);
    }

    private String getExtensions(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }
}