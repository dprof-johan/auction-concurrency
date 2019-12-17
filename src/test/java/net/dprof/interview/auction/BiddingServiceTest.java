package net.dprof.interview.auction;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import net.dprof.interview.auction.domain.Bid;
import net.dprof.interview.auction.domain.Item;
import net.dprof.interview.auction.domain.User;
import net.dprof.interview.auction.service.ThreadSafeInMemoryStoredBiddingService;
import net.dprof.interview.auction.store.InMemoryThreadSafeBidStore;

@RunWith(MockitoJUnitRunner.class)
public class BiddingServiceTest {

	private ThreadSafeInMemoryStoredBiddingService biddingService;

	@Mock
	private InMemoryThreadSafeBidStore<Item> itemBidStore;
	@Mock
	private InMemoryThreadSafeBidStore<User> userBidStore;

	private final Item item = Item.builder().itemId(UUID.randomUUID()).build();
	private final Item item2 = Item.builder().itemId(UUID.randomUUID()).build();
	private final User user = User.builder().userId(UUID.randomUUID()).build();
	private final Bid bid = Bid.builder().bidId(UUID.randomUUID()).user(user).item(item).amount(1.0).build();
	private final Bid bid2 = Bid.builder().bidId(UUID.randomUUID()).user(user).item(item2).amount(1.0).build();
	private final Bid bid3 = Bid.builder().bidId(UUID.randomUUID()).user(user).item(item2).amount(1.0).build();

	@Before
	public void setup() {
		biddingService = new ThreadSafeInMemoryStoredBiddingService(itemBidStore, userBidStore);
	}

	@Test
	public void biddingForNewItem_addsToBothItemAndUserBidStores() {

		biddingService.bid(item, bid);

		verify(itemBidStore).put(item, bid);
		verify(userBidStore).put(user, bid);
	}

	@Test
	public void winningBid_getsHighestBidFromItemBidStore() {

		biddingService.getWinningBid(item);

		verify(itemBidStore).getHighestBid(item);
	}

	@Test
	public void allBids_getsAllBidsFromItemBidStore() {

		biddingService.getAllBids(item);

		verify(itemBidStore).getAll(item);

	}

	@Test
	public void allItems_getsAllItemsFromUserBidStore() {

		when(userBidStore.getAll(user))
			.thenReturn(Arrays.asList(bid, bid2, bid3));
		List<Item> allItems = biddingService.getAllItems(user);

		verify(userBidStore).getAll(user);
		
		assertThat(allItems, containsInAnyOrder(item, item2));
	}
}
