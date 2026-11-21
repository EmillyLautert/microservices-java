package br.edu.atitus.orderapi.entities;

import jakarta.persistence.Embeddable;

@Embeddable
public class DrawnBookSnapshot {
    private String title;
    private String author;
    private String image;
    public DrawnBookSnapshot() { }
    public DrawnBookSnapshot(String title, String author, String image) { this.title = title; this.author = author; this.image = image; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getImage() { return image; }
}
