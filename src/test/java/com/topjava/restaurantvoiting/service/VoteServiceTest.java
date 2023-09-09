package com.topjava.restaurantvoiting.service;

import com.topjava.restaurantvoiting.model.Vote;
import com.topjava.restaurantvoiting.util.exception.NotFoundException;
import com.topjava.restaurantvoiting.util.exception.OutOfTimeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import static com.topjava.restaurantvoiting.data.RestaurantDataTest.RESTAURANT1_ID;
import static com.topjava.restaurantvoiting.data.UserDataTest.USER1_ID;
import static com.topjava.restaurantvoiting.data.VoteDataTest.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class VoteServiceTest extends AbstractServiceTest{
    private final LocalDateTime inputTimeTest = LocalDateTime.of(2023,8,8,9,0);
    private final LocalDateTime outputTimeTest = LocalDateTime.of(2023,8,8,10,45);
    private final ZoneId zoneId = ZoneId.systemDefault();
    private final Clock fClock = Clock.fixed(inputTimeTest.atZone(zoneId).toInstant(),zoneId);
    private final Clock fOutClock = Clock.fixed(outputTimeTest.atZone(zoneId).toInstant(),zoneId);

    @Autowired
    private VoteService voteService;

    @BeforeEach
    void setUp(){
        voteService.setClock(fClock);
    }
    @Test
    void create(){
        voteService.delete(VOTE1_ID);
        Vote created = voteService.create(USER1_ID,RESTAURANT1_ID);
        int newId = created.id();
        Vote newVote = getNew();
        newVote.setId(newId);
        VOTE_MATCHER.assertMatch(created,newVote);
        VOTE_MATCHER.assertMatch(voteService.get(newId),newVote);
    }
    @Test
    void createOutOfTime(){
        voteService.setClock(fOutClock);
        assertThrows(OutOfTimeException.class,
                () -> voteService.create(USER1_ID,RESTAURANT1_ID));
    }
    @Test
    void delete(){
        voteService.delete(VOTE1_ID);
        assertThrows(NotFoundException.class,
                () -> voteService.get(VOTE1_ID));
    }
    @Test
    void deleteNotFound(){
        assertThrows(NotFoundException.class,
                () -> voteService.delete(NOT_FOUND));
    }
    @Test
    void get(){
        Vote vote = voteService.get(VOTE1_ID);
        VOTE_MATCHER.assertMatch(vote,vote1);
    }
    @Test
    void getNotFound(){
        assertThrows(NotFoundException.class,
                () -> voteService.get(NOT_FOUND));
    }
    @Test
    void update(){
        voteService.update(VOTE1_ID,RESTAURANT1_ID);
        VOTE_MATCHER.assertMatch(voteService.get(VOTE1_ID),getUpdated());
    }
    @Test
    void updateOutOfTime(){
        voteService.setClock(fOutClock);
        assertThrows(OutOfTimeException.class,
                () -> voteService.update(USER1_ID,RESTAURANT1_ID));
    }
    @Test
    void getAll(){
        VOTE_MATCHER.assertMatch(voteService.getAll(),votes);
    }
    @Test
    void getAllByDate(){
        List<Vote> votesAllByDate = voteService.getAllByDate(LocalDate.of(2023,7,7));
        VOTE_MATCHER.assertMatch(votesAllByDate,vote5,vote6,vote7,vote8);
    }
    @Test
    void getByRestaurantAndDate(){
        List<Vote> votesByRestaurantAndDate = voteService.getByRestaurantAndDate(RESTAURANT1_ID,LocalDate.of(2023,7,7));
        VOTE_MATCHER.assertMatch(votesByRestaurantAndDate,vote5);
    }
    @Test
    void getByUserAndDate(){
        Vote voteUserByDate = voteService.getByUserAndDate(USER1_ID,LocalDate.of(2023,7,7));
        VOTE_MATCHER.assertMatch(voteUserByDate,vote5);

    }
}
