package net.dprof.interview.auction.domain;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Builder
@EqualsAndHashCode
public class Bid {

	@NonNull
	public final UUID bidId;
	
	public final LocalDateTime bidTime;
	
	@NonNull
	public final User user;
	@NonNull
	public final Item item;
	@NonNull
	public final Double amount;
	
}
