package ru.hogwarts.school.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@EqualsAndHashCode(of = "id")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "avatars")
public class Avatar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "filePath")
    private String filePath;
    @Column(name = "mediaType")
    private String mediaType;
    @Column(name = "fileSize")
    private long fileSize;
    @Column(name = "data")
    @Lob
    private byte[] data;
    @OneToOne
    @JoinColumn(name = "student_id")
    private Student student;
}