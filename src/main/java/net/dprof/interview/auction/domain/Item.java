package net.dprof.interview.auction.domain;

import java.util.UUID;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Builder
@EqualsAndHashCode
public class Item {

	@NonNull
	public final UUID itemId;
	
}
