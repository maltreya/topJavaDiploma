package com.topjava.restaurantvoiting.data;

import com.topjava.restaurantvoiting.MatchFactory;
import com.topjava.restaurantvoiting.model.Vote;
import com.topjava.restaurantvoiting.to.VoteTo;

import java.time.LocalDate;
import java.util.List;

import static com.topjava.restaurantvoiting.data.RestaurantDataTest.*;

import static com.topjava.restaurantvoiting.data.UserDataTest.*;
import static com.topjava.restaurantvoiting.model.AbstractBaseEntity.START_SEQ;

public class VoteDataTest {
    public static final MatchFactory.Matcher<Vote> VOTE_MATCHER = MatchFactory.usingIgnoringFieldsComparator(Vote.class, "user", "restaurant");
    public static final MatchFactory.Matcher<VoteTo> VOTE_TO_MATCHER = MatchFactory.usingEqualsComparator(VoteTo.class);

    public static final int NOT_FOUND = 11;
    public static final int VOTE1_ID = START_SEQ + 29;
    public static final int VOTE2_ID = START_SEQ + 30;
    public static final int VOTE3_ID = START_SEQ + 31;
    public static final int VOTE4_ID = START_SEQ + 32;
    public static final int VOTE5_ID = START_SEQ + 33;
    public static final int VOTE6_ID = START_SEQ + 34;
    public static final int VOTE7_ID = START_SEQ + 35;
    public static final int VOTE8_ID = START_SEQ + 36;
    public static final Vote vote1 = new Vote(VOTE1_ID, LocalDate.now(), user1, restaurant1);
    public static final Vote vote2 = new Vote(VOTE2_ID, LocalDate.now(), user2, restaurant2);
    public static final Vote vote3 = new Vote(VOTE3_ID, LocalDate.now(), user3, restaurant3);
    public static final Vote vote4 = new Vote(VOTE4_ID, LocalDate.now(), admin, restaurant4);
    public static final Vote vote5 = new Vote(VOTE5_ID, LocalDate.of(2023, 7, 7), user1, restaurant4);
    public static final Vote vote6 = new Vote(VOTE6_ID, LocalDate.of(2023, 7, 7), user2, restaurant3);
    public static final Vote vote7 = new Vote(VOTE7_ID, LocalDate.of(2023, 7, 7), user3, restaurant2);
    public static final Vote vote8 = new Vote(VOTE8_ID, LocalDate.of(2023, 7, 7), admin, restaurant1);

    public static final List<Vote> votes = List.of(vote1, vote2, vote3, vote4,
            vote5, vote6, vote7, vote8);

    public static Vote getNew() {
        return new Vote(null, LocalDate.now(), user1, restaurant1);
    }

    public static Vote getUpdated() {
        return new Vote(VOTE1_ID, LocalDate.now(), user1, RestaurantDataTest.getUpdated());
    }
}
