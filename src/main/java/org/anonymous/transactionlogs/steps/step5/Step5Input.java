package org.anonymous.transactionlogs.steps.step5;

import java.util.Objects;

import org.anonymous.transactionlogs.steps.step4.Step4Result;

public class Step5Input {

	private final String analyzedEntitlement;

	private final Step4Result step4Result;

	// sliding window length in ms
	private long slidingWindowLength;

	public Step5Input(String analyzedEntitlement, Step4Result step4Result, long slidingWindowLength) {
		this.analyzedEntitlement = Objects.requireNonNull(analyzedEntitlement);
		this.step4Result = Objects.requireNonNull(step4Result);
		this.slidingWindowLength = slidingWindowLength;
	}

	public Step4Result getStep4Result() {
		return step4Result;
	}

	public long getSlidingWindowLength() {
		return slidingWindowLength;
	}

	public String getAnalyzedEntitlement() {
		return analyzedEntitlement;
	}

}
