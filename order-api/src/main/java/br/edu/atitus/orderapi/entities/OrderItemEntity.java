package br.edu.atitus.orderapi.entities;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tb_order_item")
public class OrderItemEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "product_id", nullable = false)
    private Long productId;
    @Column(name = "product_title", nullable = false)
    private String title;
    @Column(name = "product_type", nullable = false)
    private String type;
    @Column(name = "price_at_purchase", nullable = false, precision = 12, scale = 2)
    private BigDecimal unitPrice;
    @Column(nullable = false)
    private Integer quantity;
    @Column(name = "currency_at_purchase", nullable = false, length = 3)
    private String currency = "BRL";
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private OrderEntity order;
    @ElementCollection
    @CollectionTable(name = "tb_order_item_drawn_book", joinColumns = @JoinColumn(name = "order_item_id"))
    private List<DrawnBookSnapshot> drawnBooks = new ArrayList<>();

    public Long getId() { return id; }
    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public BigDecimal getUnitPrice() { return unitPrice; }
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    public OrderEntity getOrder() { return order; }
    public void setOrder(OrderEntity order) { this.order = order; }
    public List<DrawnBookSnapshot> getDrawnBooks() { return drawnBooks; }
    public void setDrawnBooks(List<DrawnBookSnapshot> drawnBooks) { this.drawnBooks = drawnBooks; }
}
