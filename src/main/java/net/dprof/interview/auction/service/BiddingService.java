package net.dprof.interview.auction.service;

import java.util.List;
import java.util.Optional;

import net.dprof.interview.auction.domain.Bid;
import net.dprof.interview.auction.domain.Item;
import net.dprof.interview.auction.domain.User;

public interface BiddingService {

	public void bid(Item item, Bid bid);

	public Optional<Bid> getWinningBid(Item item);

	public List<Bid> getAllBids(Item item);

	public List<Item> getAllItems(User user);
	
}
