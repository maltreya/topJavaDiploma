package com.topjava.restaurantvoiting.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.validator.constraints.Range;


import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "foods",
        uniqueConstraints = {@UniqueConstraint
                (columnNames =
                        {"restaurant_id", "prep_date", "description"},
                        name = "food_unique_restaurant_prep_date_description_idx")})
@NoArgsConstructor
@Getter
@Setter
public class Food extends AbstractBaseEntity {
    @Column(name = "prep_date", nullable = false)
    @NotNull
    private LocalDate prepDate;

    @Column(name = "description", nullable = false)
    @NotBlank
    @Size(min = 2, max = 160)
    private String description;

    @Column(name = "price", nullable = false)
    @Range(min = 0, max = 1000)
    private BigDecimal price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    @JsonBackReference
    private Restaurant restaurant;

    public Food(LocalDate prepDate, String description, BigDecimal price, Restaurant restaurant) {
        this(null, prepDate, description, price, restaurant);
    }

    public Food(Integer id, LocalDate prepDate, String description, BigDecimal price, Restaurant restaurant) {
        super(id);
        this.prepDate = prepDate;
        this.description = description;
        this.price = price;
        this.restaurant = restaurant;
    }

    @Override
    public String toString() {
        return "Food{" +
                "id=" + id +
                ", voteDate=" + prepDate +
                ", description=" + description +
                ", price=" + price +
                ", restaurant=" + restaurant+
                "}";
    }


}
