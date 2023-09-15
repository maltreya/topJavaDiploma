package com.topjava.restaurantvoiting.service;

import com.topjava.restaurantvoiting.model.Vote;
import com.topjava.restaurantvoiting.repository.RestaurantRepository;
import com.topjava.restaurantvoiting.repository.UserRepository;
import com.topjava.restaurantvoiting.repository.VoteRepository;
import com.topjava.restaurantvoiting.util.exception.NotFoundException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static com.topjava.restaurantvoiting.util.ValidationUtil.*;

@Service
public class VoteService {
    private static final Sort SORT_DATE = Sort.by(Sort.Direction.DESC, "VoteDate", "id");
    private Clock clock = Clock.system(ZoneId.systemDefault());
    private final VoteRepository voteRepository;
    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;

    public VoteService(VoteRepository voteRepository, RestaurantRepository restaurantRepository, UserRepository userRepository) {
        this.voteRepository = voteRepository;
        this.restaurantRepository = restaurantRepository;
        this.userRepository = userRepository;
    }

    public void setClock(Clock clock) {
        this.clock = clock;
    }

    @Transactional
    public Vote create(int userId, int restaurantId) {
        votingTimeVerification(clock);
        Vote vote = new Vote(null, LocalDate.now(),
                userRepository.findById(userId).orElseThrow(
                        () -> new NotFoundException("User " + userId + " not found")),
                restaurantRepository.findById(restaurantId).orElseThrow(
                        () -> new NotFoundException("Restaurant " + restaurantId + " not found"))
        );
        Assert.notNull(vote, "vote should not be null");
        checkNew(vote);
        return voteRepository.save(vote);
    }

    public Vote get(int id) {
        return checkNotFoundWithId(voteRepository.findById(id).orElse(null), id);
    }

    public List<Vote> getAll() {
        return voteRepository.findAll(SORT_DATE);
    }

    public List<Vote> getAllByDate(LocalDate voteDate) {
        Assert.notNull(voteDate, "date should not be null");
        List<Vote> votes = voteRepository.getAllByDate(voteDate);
        return checkExisted(votes, voteDate);
    }

    public Vote getByUserAndDate(int userId, LocalDate voteDate) {
        Assert.notNull(voteDate, "date should not be null");
        return checkNotFoundWithId(voteRepository.getByUserAndDate(userId, voteDate), userId);
    }

    public List<Vote> getByRestaurantAndDate(int restaurantId, LocalDate voteDate) {
        Assert.notNull(voteDate, "date should not be null");
        List<Vote> votes = voteRepository.getByRestaurantAndDate(restaurantId, voteDate);
        return checkExisted(votes, voteDate, restaurantId);
    }

    @Transactional
    public void update(int id, int restaurantId) {
        votingTimeVerification(clock);
        Vote vote = voteRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Vote " + id + "not found"));
        Assert.notNull(vote, "vote should not be null");
        assureIdConsistent(vote, vote.id());
        vote.setRestaurant(restaurantRepository.findById(restaurantId).orElseThrow(
                () -> new NotFoundException("Restaurant " + restaurantId + " not found")
        ));
        checkNotFoundWithId(voteRepository.save(vote), vote.id());
    }

    public void delete(int id) {
        checkNotFoundWithId(voteRepository.delete(id) != 0, id);
    }
}
