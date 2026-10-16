package be.ecam.biblioweb.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "livres")
public class Livre {

    @Id
    @GeneratedValue
    private UUID id;

    private String title;

    private String author;

    @Enumerated(EnumType.STRING)
    private Status status;

    protected Livre() {
    }

    public Livre(String title, String author) {
        this.title = title;
        this.author = author;
        this.status = Status.AVAILABLE;
    }

    public UUID getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Le livre est " + title + " de " + author;
    }
}
