package net.dprof.interview.auction.store;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertThat;

import java.util.Optional;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import net.dprof.interview.auction.domain.Bid;
import net.dprof.interview.auction.domain.Item;
import net.dprof.interview.auction.domain.User;

public class InMemoryThreadSafeBidStoreTest {

	private InMemoryThreadSafeBidStore<String> bidStore;
	
	private final User user1 = User.builder().userId(UUID.randomUUID()).build();
	private final User user2 = User.builder().userId(UUID.randomUUID()).build();
	
	private final Item item = Item.builder().itemId(UUID.randomUUID()).build();
	
	private final Bid bid1 = Bid.builder().bidId(UUID.randomUUID()).user(user1).item(item).amount(1.0).build();
	private final Bid bid2 = Bid.builder().bidId(UUID.randomUUID()).user(user1).item(item).amount(2.0).build();

	private final Bid bid3 = Bid.builder().bidId(UUID.randomUUID()).user(user2).item(item).amount(5.0).build();
	private final Bid bid4 = Bid.builder().bidId(UUID.randomUUID()).user(user2).item(item).amount(4.0).build();

	
	@Before
	public void setup() {
		bidStore = new InMemoryThreadSafeBidStore<>();
	}
	
	@Test
	public void emptyListReturnedByGetAll_givenNoBidsHaveBeenAddedInPutForAnyKey() {
		assertThat(bidStore.getAll("A"), containsInAnyOrder());
	}
	
	@Test
	public void emptyListReturnedByGetAll_givenNoBidsHaveBeenAddedInPutForSameKey() {
		bidStore.put("A", bid1);
		assertThat(bidStore.getAll("B"), containsInAnyOrder());
	}
	
	@Test
	public void singleBidIsRetrievableInGetAll_givenItHasBeenAddedInPutForSameKey() {
		bidStore.put("A", bid1);
		assertThat(bidStore.getAll("A"), containsInAnyOrder(bid1));
	}
	
	@Test
	public void multipleBidsForSameKeyAreRetrievableInGetAll_givenTheyHaveBeenAddedInPutForSameKey() {
		bidStore.put("A", bid1);
		bidStore.put("A", bid2);
		assertThat(bidStore.getAll("A"), containsInAnyOrder(bid1, bid2));
	}
	
	@Test
	public void multipleBidsForDifferentKeysAreRetrievableInDifferntGetAlls_givenTheyHaveBeenAddedInPutAgainstDifferentKeys() {
		bidStore.put("A", bid1);
		bidStore.put("A", bid2);
		bidStore.put("B", bid3);
		bidStore.put("B", bid4);
		assertThat(bidStore.getAll("A"), containsInAnyOrder(bid1, bid2));
		assertThat(bidStore.getAll("B"), containsInAnyOrder(bid3, bid4));
	}
	
	@Test
	public void emptyOptionalReturnedByHighestBid_givenNoBidsForAnyKeysExist() {
		assertThat(bidStore.getHighestBid("A"), is(Optional.empty()));
	}
	
	@Test
	public void emptyOptionalReturnedByHighestBid_givenNoBidsForGivenKeyExist() {
		bidStore.put("A", bid1);
		assertThat(bidStore.getHighestBid("B"), is(Optional.empty()));
	}

	@Test
	public void optionalContainingHighestBidReturnedByHighestBid_givenOneBidForKeyExists() {
		bidStore.put("A", bid1);
		assertThat(bidStore.getHighestBid("A"), is(Optional.of(bid1)));
	}
	
	@Test
	public void optionalContainingHighestBidReturnedByHighestBid_givenMultipleBidsAtDifferentAmountsForSameKeyExist() {
		bidStore.put("A", bid1);
		bidStore.put("A", bid2);
		assertThat(bidStore.getHighestBid("A"), is(Optional.of(bid2)));		
	}
	
	@Test
	public void optionalContainingHighestBidReturnedByHighestBid_givenMultipleBidsAtDifferentAmountsForMultipleKeysExist() {
		bidStore.put("A", bid1);
		bidStore.put("A", bid2);
		bidStore.put("B", bid3);
		bidStore.put("B", bid4);
		assertThat(bidStore.getHighestBid("A"), is(Optional.of(bid2)));	
		assertThat(bidStore.getHighestBid("B"), is(Optional.of(bid3)));
	}
}
