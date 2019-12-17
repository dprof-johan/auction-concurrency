package net.dprof.interview.auction.logic;


import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import org.junit.Test;

import net.dprof.interview.auction.domain.Bid;
import net.dprof.interview.auction.domain.Item;
import net.dprof.interview.auction.domain.User;

public class HighestBidRetrieverTest {

	private final Item item = Item.builder().itemId(UUID.randomUUID()).build();
	private final User user = User.builder().userId(UUID.randomUUID()).build();
	
	private final Bid bid1 = Bid.builder().bidId(UUID.randomUUID()).user(user).item(item).amount(1.0).build();
	private final Bid bid2 = Bid.builder().bidId(UUID.randomUUID()).user(user).item(item).amount(2.0).build();
	private final Bid bid3 = Bid.builder().bidId(UUID.randomUUID()).user(user).item(item).amount(2.0).build();
	
	@Test
	public void returnsEmptyOptional_givenNullBids() {
		assertThat(HighestBidRetriever.getHighestBid(null), is(Optional.empty()));
	}
	
	@Test
	public void returnsEmptyOptional_givenEmptyBids() {
		assertThat(HighestBidRetriever.getHighestBid(Arrays.asList()), is(Optional.empty()));
	}
	
	@Test
	public void returnsHighestBidByAmount_givenMultipleBidsAtDifferentAmounts() {
		assertThat(HighestBidRetriever.getHighestBid(Arrays.asList(bid1, bid2)), is(Optional.of(bid2)));
	}
	
	@Test
	public void returnsEitherOfHighestBids_givenMultipleBidsAtSameAmounts() {
		assertThat(HighestBidRetriever.getHighestBid(Arrays.asList(bid1, bid2, bid3)), anyOf(is(Optional.of(bid2)), is(Optional.of(bid3))));
	}

}
