package net.dprof.interview.auction.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import net.dprof.interview.auction.domain.Bid;
import net.dprof.interview.auction.domain.Item;
import net.dprof.interview.auction.domain.User;
import net.dprof.interview.auction.store.InMemoryThreadSafeBidStore;

/**
 * In Memory Bidding Service, keeping its own internal Bidding State. There is
 * nothing to keep 2 separate Bidding services from having conflicting bids for
 * the "same" item / users, and when the service instance is destroyed, there is no 
 * persisted store of the bids placed.
 * 
 * Due to the bid stores used being thread safe, this service is also thread safe.
 */
@RequiredArgsConstructor
public class ThreadSafeInMemoryStoredBiddingService implements BiddingService{

	private final InMemoryThreadSafeBidStore<Item> bidsForItems;
	private final InMemoryThreadSafeBidStore<User> bidsForUsers;

	@Override
	public void bid(Item item, Bid bid) {
		bidsForItems.put(item, bid);
		bidsForUsers.put(bid.user, bid);
	}

	@Override
	public Optional<Bid> getWinningBid(Item item) {
		return bidsForItems.getHighestBid(item);
	}

	@Override
	public List<Bid> getAllBids(Item item) {
		return bidsForItems.getAll(item);
	}

	@Override
	public List<Item> getAllItems(User user) {
		return bidsForUsers.getAll(user).stream()
				.map(bid -> bid.item)
				.distinct()
				.collect(Collectors.toList());
	}

}
