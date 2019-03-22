package de.fraunhofer.iosb.ilt.sta.model.Constraints;

import java.time.LocalDateTime;

public class AllowedTimeRange extends Constraint {

	private LocalDateTime minTime;
	private LocalDateTime maxTime;

	public AllowedTimeRange(LocalDateTime minTime, LocalDateTime maxTime) {
		this.minTime = minTime;
		this.maxTime = maxTime;
	}

	@Override
	public boolean isValid(Object input) {
		LocalDateTime[] times = (LocalDateTime[]) input;
		return times[0].isAfter(minTime) && times[1].isBefore(maxTime) && times[0].isBefore(times[1]);
	}
}
