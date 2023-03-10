package com.example.onlinelibrary.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "book")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_book")
    private Long idBook;

    @Column(name = "name", nullable = false, unique = true, length = 50)
    private String name;

    @Column(name = "publish_date", nullable = false, length = 50)
    private LocalDate publishDate;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinTable(name = "book_author",
            joinColumns = {
                    @JoinColumn(name = "id_book", referencedColumnName = "id_book",
                            nullable = false, updatable = false)},
            inverseJoinColumns = {
                    @JoinColumn(name = "id_author", referencedColumnName = "id_author",
                            nullable = false, updatable = false)})
    @JsonIgnoreProperties(value = "bookAuthors")
    private Set<Author> authors = new HashSet<>();

    @Column(name = "lang")
    private Language language;

    @Lob
    private String description;

    @ElementCollection(targetClass = Genre.class)
    @CollectionTable(name = "book_genre", joinColumns = @JoinColumn(name = "id_book"))
    @Enumerated(EnumType.STRING)
    @Column(name = "genre")
    private Set<Genre> genreSet;

    @OneToMany(mappedBy = "book", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<ImageData> imageDataList;

    // Mark user favorites
    @Transient
    private boolean favorite;

    public Book(Long idBook,
                String name,
                LocalDate publishDate,
                Set<Author> authors,
                Language language,
                String description,
                Set<Genre> genreSet,
                List<ImageData> imageDataList) {
        this.idBook = idBook;
        this.name = name;
        this.publishDate = publishDate;
        this.authors = authors;
        this.language = language;
        this.description = description;
        this.genreSet = genreSet;
        this.imageDataList = imageDataList;
    }

    public enum Language {
        ARABIC,
        CHINESE,
        ENGLISH,
        GERMAN,
        FRENCH,
        PORTUGUESE,
        RUSSIAN,
        SPANISH,
        UKRAINIAN,
        JAPANESE
    }
}
