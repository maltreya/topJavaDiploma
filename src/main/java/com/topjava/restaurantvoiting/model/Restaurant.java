package com.topjava.restaurantvoiting.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.List;

@Entity
@Table(name = "restaurants",
        uniqueConstraints = {@UniqueConstraint(
                columnNames = "name", name = "restaurant_unique_idx")})
@NoArgsConstructor
@Getter
@Setter
public class Restaurant extends AbstractNamedEntity {
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "restaurant")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @BatchSize(size = 7)
    @JsonManagedReference
    private List<Food> foods;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "restaurant")
    @OrderBy("voteDate DESC")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @BatchSize(size = 200)
    private List<Vote> votes;

    public Restaurant(String name) {
        this(null, name);
    }

    public Restaurant(Integer id, String name) {
        super(id, name);
        this.name = name;
    }

    @Override
    public String toString() {
        return "Restaurant{" +
                "id=" + id +
                ", menu=" + foods +
                ", votes=" + votes +
                ", name=" + name +
                "}";
    }
}
