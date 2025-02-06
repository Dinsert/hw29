package ru.hogwarts.school;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import ru.hogwarts.school.controller.AvatarController;
import ru.hogwarts.school.controller.FacultyController;
import ru.hogwarts.school.controller.StudentController;
import ru.hogwarts.school.exception.FacultyNotFoundException;
import ru.hogwarts.school.exception.StudentNotFoundException;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.AvatarRepository;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.AvatarServiceImpl;
import ru.hogwarts.school.service.FacultyServiceImpl;
import ru.hogwarts.school.service.StudentServiceImpl;

@WebMvcTest
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class SchoolApplicationWithMockTest {

    @Autowired
    @NonFinal MockMvc mockMvc;
    @MockBean
    @NonFinal AvatarRepository avatarMock;
    @MockBean
    @NonFinal FacultyRepository facultyMock;
    @MockBean
    @NonFinal StudentRepository studentMock;
    @SpyBean
    @NonFinal AvatarServiceImpl avatarService;
    @SpyBean
    @NonFinal FacultyServiceImpl facultyService;
    @SpyBean
    @NonFinal StudentServiceImpl studentService;
    @InjectMocks
    @NonFinal AvatarController avatarController;
    @InjectMocks
    @NonFinal FacultyController facultyController;
    @InjectMocks
    @NonFinal StudentController studentController;

    //-----------------------------------------------------Тестовые данные FACULTY----------------------------------------------------------------

    long id = 1L;
    String nameFaculty = "Gryffindor1";
    String color = "Red1";
    Faculty faculty = new Faculty(id, nameFaculty, color);
    String anyJSON = "{\"anyKey\":\"anyValue\"}";
    String expectedFaculty = "{\"id\":" + id + ",\"name\":\"" + nameFaculty + "\",\"color\":\"" + color + "\"}";
    String urlFaculty = "/faculty";
    String facultyNotFound = "{\"message\":\"Факультет по идентификатору " + id + " не найден\",\"status\":404}";
    String facultyNotFoundByNameOrColor = "Факультет по имени/цвету " + nameFaculty + " не найден";
    String facultyNotFoundById = "Факультет по идентификатору студента " + id + " не был найден";

    //-----------------------------------------------------Тестовые данные STUDENT----------------------------------------------------------------

    String urlStudent = "/student";
    String nameStudent = "Harry1";
    int age = 15;
    Student student = new Student(id, nameStudent, age);
    String expectedStudent = "{\"id\":" + id + ",\"name\":\"" + nameStudent + "\",\"age\":" + age + "}";
    String studentNotFound = "{\"message\":\"Студент по идентификатору " + id + " не был найден\",\"status\":404}";
    String partDelete = "по идентификатору " + id + " удалён";

    //-----------------------------------------------------Тестовые данные AVATAR----------------------------------------------------------------

    String urlAvatar = "/avatar";
    Avatar avatar = new Avatar();
    MockMultipartFile avatarFile = getMockMultipartFile();
    String path = "src/test/resources/test.jpg";

    //------------------------------------------------------------------FACULTY-------------------------------------------------------------------

    @Test
    public void createFacultyTest() throws Exception {
        when(facultyMock.save(any())).thenReturn(faculty);
        mockMvc.perform(post(urlFaculty)
                                .content(anyJSON)
                                .contentType(MediaType.APPLICATION_JSON))
               .andExpect(content().json(expectedFaculty));
    }

    @Test
    public void findFacultyTest() throws Exception {
        when(facultyMock.findById(anyLong())).thenReturn(Optional.of(faculty));
        mockMvc.perform(get(urlFaculty + "/" + id))
               .andExpect(content().json(expectedFaculty));
    }

    @Test
    public void findFacultyNotFoundTest() throws Exception {
        when(facultyMock.findById(anyLong())).thenThrow(getFacultyNotFoundException());
        mockMvc.perform(get(urlFaculty + "/" + id))
               .andExpect(content().json(facultyNotFound));
    }

    @Test
    public void editFacultyTest() throws Exception {
        when(facultyMock.save(any())).thenReturn(faculty);
        mockMvc.perform(put(urlFaculty)
                                .content(anyJSON)
                                .contentType(MediaType.APPLICATION_JSON))
               .andExpect(content().json(expectedFaculty));
    }

    @Test
    public void deleteFacultyTest() throws Exception {
        when(facultyMock.findById(anyLong())).thenReturn(Optional.of(faculty));
        doNothing().when(facultyMock).delete(any());
        mockMvc.perform(delete(urlFaculty + "/" + id))
               .andExpect(content().string("Факультет " + partDelete));
    }

    @Test
    public void deleteFacultyNotFoundTest() throws Exception {
        when(facultyMock.findById(anyLong())).thenThrow(getFacultyNotFoundException());
        mockMvc.perform(delete(urlFaculty + "/" + id))
               .andExpect(content().json(facultyNotFound));

    }

    @Test
    public void getAListOfFacultiesBySpecifiedColorTest() throws Exception {
        when(facultyMock.findByColor(anyString())).thenReturn(Collections.singleton(faculty));
        mockMvc.perform(get(urlFaculty + "/get-list-by-color/" + color))
               .andExpect(content().json("[" + expectedFaculty + "]"));
    }

    @Test
    public void getAListOfFacultiesBySpecifiedColorNotFoundTest() throws Exception {
        when(facultyMock.findByColor(anyString())).thenReturn(Collections.emptyList());
        mockMvc.perform(get(urlFaculty + "/get-list-by-color/" + color))
               .andExpect(content().json("{\"message\":\"Факультеты по цвету " + color + " не были найдены\",\"status\":404}"));
    }

    @Test
    public void getFacultyByNameOrColorTest() throws Exception {
        when(facultyMock.findFirstByNameIgnoreCaseOrColorIgnoreCase(anyString(), anyString())).thenReturn(Optional.of(faculty));
        mockMvc.perform(get(urlFaculty + "?nameOrColor=" + nameFaculty))
               .andExpect(content().json(expectedFaculty));
    }

    @Test
    public void getFacultyByNameOrColorNotFoundTest() throws Exception {
        when(facultyMock.findFirstByNameIgnoreCaseOrColorIgnoreCase(anyString(), anyString()))
                .thenThrow(new FacultyNotFoundException(facultyNotFoundByNameOrColor));
        mockMvc.perform(get(urlFaculty + "?nameOrColor=" + nameFaculty))
               .andExpect(content().json("{\"message\":\"" + facultyNotFoundByNameOrColor + "\",\"status\":404}"));
    }

    @Test
    public void getAllStudentsTest() throws Exception {
        when(facultyMock.getAllStudentsByFacultyId(anyLong())).thenReturn(Collections.singleton(student));
        mockMvc.perform(get(urlFaculty + "/get-students/" + id))
               .andExpect(getJson());
    }

    @Test
    public void getAllStudentsNotFoundTest() throws Exception {
        when(facultyMock.getAllStudentsByFacultyId(anyLong())).thenReturn(Collections.emptyList());
        mockMvc.perform(get(urlFaculty + "/get-students/" + id))
               .andExpect(content().json("{\"message\":\"Студенты по идентификатору факультета " + id + " не были найдены\",\"status\":404}"));
    }

    //------------------------------------------------------------------STUDENT-------------------------------------------------------------------

    @Test
    public void createStudentTest() throws Exception {
        when(studentMock.save(any())).thenReturn(student);
        mockMvc.perform(post(urlStudent).content(anyJSON)
                                        .contentType(MediaType.APPLICATION_JSON))
               .andExpect(content().json(expectedStudent));
    }

    @Test
    public void findStudentTest() throws Exception {
        when(studentMock.findById(anyLong())).thenReturn(Optional.of(student));
        mockMvc.perform(get(urlStudent + "/" + id))
               .andExpect(content().json(expectedStudent));
    }

    @Test
    public void findStudentNotFoundTest() throws Exception {
        when(studentMock.findById(anyLong())).thenThrow(getStudentNotFoundException());
        mockMvc.perform(get(urlStudent + "/" + id))
               .andExpect(content().json(studentNotFound));
    }

    @Test
    public void editStudentTest() throws Exception {
        when(studentMock.save(any())).thenReturn(student);
        mockMvc.perform(put(urlStudent).content(anyJSON)
                                       .contentType(MediaType.APPLICATION_JSON))
               .andExpect(content().json(expectedStudent));
    }

    @Test
    public void deleteStudentTest() throws Exception {
        when(studentMock.findById(anyLong())).thenReturn(Optional.of(student));
        doNothing().when(studentMock).delete(any());
        mockMvc.perform(delete(urlStudent + "/" + id))
               .andExpect(content().string("Студент " + partDelete));
    }

    @Test
    public void deleteStudentNotFoundTest() throws Exception {
        when(studentMock.findById(anyLong())).thenThrow(getStudentNotFoundException());
        mockMvc.perform(delete(urlStudent + "/" + id))
               .andExpect(content().json(studentNotFound));
    }

    @Test
    public void getAListOfStudentsBySpecifiedAgeTest() throws Exception {
        when(studentMock.findByAge(anyInt())).thenReturn(Collections.singleton(student));
        mockMvc.perform(get(urlStudent + "/get-list-by-age/" + age))
               .andExpect(getJson());
    }

    @Test
    public void getAListOfStudentsBySpecifiedAgeNotFoundTest() throws Exception {
        when(studentMock.findByAge(anyInt())).thenReturn(Collections.emptyList());
        mockMvc.perform(get(urlStudent + "/get-list-by-age/" + age))
               .andExpect(content().json("{\"message\":\"Студенты по возрасту " + age + " лет не были найдены\",\"status\":404}"));
    }

    @Test
    public void getAllStudentsBetweenTargetAgeTest() throws Exception {
        when(studentMock.findByAgeBetween(anyInt(), anyInt())).thenReturn(Collections.singleton(student));
        mockMvc.perform(get(urlStudent + "?min=" + age + "&max=" + age))
               .andExpect(getJson());
    }

    @Test
    public void getAllStudentsBetweenTargetAgeNotFoundTest() throws Exception {
        when(studentMock.findByAgeBetween(anyInt(), anyInt())).thenReturn(Collections.emptyList());
        mockMvc.perform(get(urlStudent + "?min=" + age + "&max=" + age))
               .andExpect(content().json("{\"message\":\"Студенты от 15 до 15 лет не были найдены\",\"status\":404}"));
    }

    @Test
    public void getFacultyTest() throws Exception {
        when(studentMock.getFacultyStudent(anyLong())).thenReturn(Optional.of(faculty));
        mockMvc.perform(get(urlStudent + "/get-faculty/" + id))
               .andExpect(content().json(expectedFaculty));
    }

    @Test
    public void getFacultyNotFoundTest() throws Exception {
        when(studentMock.getFacultyStudent(anyLong())).thenThrow(new FacultyNotFoundException(facultyNotFoundById));
        mockMvc.perform(get(urlStudent + "/get-faculty/" + id))
               .andExpect(content().json("{\"message\":\"" + facultyNotFoundById + "\",\"status\":404}"));
    }

    //------------------------------------------------------------------AVATAR-------------------------------------------------------------------

    @Test
    public void uploadAvatarTest() throws Exception {
        when(studentMock.findById(anyLong())).thenReturn(Optional.of(student));
        when(avatarMock.findByStudentId(anyLong())).thenReturn(Optional.of(avatar));
        when(avatarMock.save(any())).thenReturn(avatar);
        mockMvc.perform(multipart(urlAvatar + "/" + id)
                                .file(avatarFile))
               .andExpect(content().string("Аватарка по идентификатору студента " + id + " была загружена"));
    }

    @Test
    public void downloadAvatarFromDBTest() throws Exception {
        setAvatarAndReturnMock();
        mockMvc.perform(get(urlAvatar + "/from-db/" + id))
               .andExpect(status().isOk())
               .andExpect(content().bytes(avatarFile.getBytes()))
               .andExpect(content().contentType(avatarFile.getContentType() + ";charset=UTF-8"))
               .andExpect(header().longValue("Content-Length", avatar.getFileSize()));
    }

    @Test
    public void downloadAvatarFromFileTest() throws Exception {
        avatar.setFilePath(path);
        setAvatarAndReturnMock();
        mockMvc.perform(get(urlAvatar + "/from-file/" + id))
               .andExpect(status().isOk())
               .andExpect(content().bytes(avatarFile.getBytes()))
               .andExpect(content().contentType(avatarFile.getContentType() + ";charset=UTF-8"))
               .andExpect(header().longValue("Content-Length", avatar.getFileSize()));
    }

    private FacultyNotFoundException getFacultyNotFoundException() {
        return new FacultyNotFoundException("Факультет по идентификатору " + id + " не найден");
    }

    private StudentNotFoundException getStudentNotFoundException() {
        return new StudentNotFoundException("Студент по идентификатору " + id + " не был найден");
    }

    private ResultMatcher getJson() {
        return content().json("[" + expectedStudent + "]");
    }

    private MockMultipartFile getMockMultipartFile() {
        MockMultipartFile avatarFile;
        try {
            avatarFile = new MockMultipartFile("avatarFile", "test.jpg", "image/jpeg",
                                               Files.readAllBytes(Path.of(path)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return avatarFile;
    }

    private void setAvatarAndReturnMock() throws IOException {
        avatar.setMediaType(avatarFile.getContentType());
        avatar.setFileSize(avatarFile.getSize());
        avatar.setData(avatarFile.getBytes());
        when(avatarMock.findByStudentId(anyLong())).thenReturn(Optional.of(avatar));
    }
}