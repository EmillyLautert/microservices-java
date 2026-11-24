package br.edu.atitus.productapi.entities;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "tb_product")
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String author;
    @Column(columnDefinition = "TEXT")
    private String description;
    @Column(length = 255)
    private String category;

    @Enumerated(EnumType.STRING)
    private ProductType type;

    @Column(name = "book_count")
    private Integer bookCount;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal price;

    @Column(nullable = false, length = 3)
    private String currency = "BRL";

    private String image;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public ProductType getType() { return type; }
    public void setType(ProductType type) { this.type = type; }
    public Integer getBookCount() { return bookCount; }
    public void setBookCount(Integer bookCount) { this.bookCount = bookCount; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }
}
