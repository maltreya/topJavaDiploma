package com.topjava.restaurantvoiting.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "votes", uniqueConstraints = {@UniqueConstraint(
        columnNames = {"user_id", "vote_date"}, name = "vote_unique_user_vote_date_idx")})
@NoArgsConstructor
@Getter
@Setter
public class Vote extends AbstractBaseEntity{
    @Column(name="vote_date", nullable = false)
    @NotNull
    private LocalDate voteDate;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="user_id")
    @NotNull
    @JsonBackReference
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="restaurant_id")
    @NotNull
    private Restaurant restaurant;

    public Vote(Integer id, LocalDate voteDate, User user, Restaurant restaurant){
        super(id);
        this.voteDate = voteDate;
        this.user = user;
        this.restaurant=restaurant;
    }
    @Override
    public String toString(){
        return "Vote{"+
                "id="+id+
                ", voteDate=" + voteDate +
                ", user="+user+
                ",restaurant="+restaurant+
                "}";
    }
}
