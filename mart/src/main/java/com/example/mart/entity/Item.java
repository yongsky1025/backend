package com.example.mart.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
// @ToString(exclude = { "orderItems", "categories" })
@ToString(exclude = { "orderItems", "categoryItems" })
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter

@Table(name = "mart_item")
public class Item {

    // id, name, price, quantity

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "item_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private int quantity;

    @Builder.Default
    @OneToMany(mappedBy = "item")
    private List<OrderItem> orderItems = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "item")
    private List<CategoryItem> categoryItems = new ArrayList<>();

    // // 양방향
    // @Builder.Default
    // @ManyToMany(mappedBy = "items")
    // private List<Category> categories = new ArrayList<>();

    public void changeQuantity(int quantity) {
        this.quantity = quantity;
    }
}
