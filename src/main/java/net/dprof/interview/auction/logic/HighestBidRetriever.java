package net.dprof.interview.auction.logic;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import lombok.experimental.UtilityClass;
import net.dprof.interview.auction.domain.Bid;

@UtilityClass
public class HighestBidRetriever {

	private static final Comparator<Bid> BY_AMOUNT = Comparator.comparing(bid -> bid.amount);
	
	public Optional<Bid> getHighestBid(List<Bid> bids) {
		if (null == bids) {
			return Optional.empty();
		} else {
			return bids.stream().max(BY_AMOUNT);
		}
	}

}
