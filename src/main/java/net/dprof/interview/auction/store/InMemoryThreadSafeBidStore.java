package net.dprof.interview.auction.store;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;

import com.google.common.collect.ImmutableList;

import net.dprof.interview.auction.domain.Bid;
import net.dprof.interview.auction.logic.HighestBidRetriever;

/**
 * !!!!!!!!!!!!WARNING!!!!!!!!!!!
 * <ul>
 * <li>This Store is NOT Persistent. If the instance is lost, the bids will be lost.</li>
 * <li>Only ever instantiate ONE of this store, as 2 separate stores would not share their state</li>
 * <li>This store is NOT responsible for assuring exclusivity between bids for the same item. Nothing prevents there being 2 bids for the same item at the same price</li>
 * </ul>
 * 
 * This simple in memory store for Bids stores the bids using a combination of ComccurentHashMap and CopyOnWriteArrayList, to
 * achieve thread safety.
 * 
 * @param <T> The class used as the key for storing / retrieving Bids
 */
public class InMemoryThreadSafeBidStore<T> {

	private final ConcurrentMap<T, List<Bid>> bids;
	
	public InMemoryThreadSafeBidStore() {
		bids = new ConcurrentHashMap<>();
	}
	
	public void put(T key, Bid bid) {
		bids.compute(key, (k, v) -> {
			if(v == null) {
				v = new CopyOnWriteArrayList<>();
			}
			v.add(bid);
			return v;
		});
	}
	
	/**
	 * Retrieves all bids stored against the key, taken at the time of calling the method. If any bids
	 * are added whilst the method is running, they will be ignored.
	 * 
	 * @param key
	 * @return An immutable copy of the bids at the time of calling
	 */
	public List<Bid> getAll(T key){
		return ImmutableList.copyOf(bids.getOrDefault(key, new ArrayList<>()));
	}
	
	/**
	 * Retrieves the highest bid stored against the key by amount at the time of calling the method. If any bids
	 * are added whilst the method is running, they will be ignored. 
	 * 
	 * If there is more than one bid at the highest value, the either one added to the store is returned.
	 * 
	 * @param key
	 * @return An Optional<Bid>, that is empty if there is no bid at all for the key, and otherwise contains the bid against the key that has the highest amount value
	 */
	public Optional<Bid> getHighestBid(T key){
		return HighestBidRetriever.getHighestBid(getAll(key));
	}
}
