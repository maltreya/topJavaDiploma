package com.topjava.restaurantvoiting.util;

import com.topjava.restaurantvoiting.model.Vote;
import com.topjava.restaurantvoiting.to.VoteTo;
import lombok.experimental.UtilityClass;

import java.util.Collection;
import java.util.List;

@UtilityClass
public class VoteUtil {
    public static List<VoteTo> getTos(Collection<Vote> votes) {
        return votes.stream().map(VoteUtil::createTo).toList();
    }

    public static VoteTo createTo(Vote vote) {
        return new VoteTo(vote.getId(), vote.getVoteDate(), vote.getUser().getId(), vote.getRestaurant().getId());
    }
}
