package com.topjava.restaurantvoiting.to;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.beans.ConstructorProperties;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class VoteTo extends BaseTo implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private LocalDate voteDate;
    private Integer userId;
    private Integer restaurantId;

    @ConstructorProperties({"id","voteDate","userId"})
    public VoteTo (Integer id, LocalDate voteDate, Integer userId, Integer restaurantId){
        super(id);
        this.voteDate = voteDate;
        this.userId = userId;
        this.restaurantId = restaurantId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(voteDate,userId,restaurantId);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj ==null || getClass() !=obj.getClass()) return false;
        VoteTo voteTo = (VoteTo) obj;
        return voteDate.equals(voteTo.voteDate)
                && userId.equals(voteTo.userId)
                && restaurantId.equals(voteTo.restaurantId);
    }
}
