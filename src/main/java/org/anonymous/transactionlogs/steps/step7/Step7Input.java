package org.anonymous.transactionlogs.steps.step7;

import java.util.Objects;

import org.anonymous.transactionlogs.steps.step6.Step6Result;

public class Step7Input {

	private final String analyzedEntitlement;

	private final Step6Result step6Result;

	private final double minconf;

	public Step7Input(String analyzedEntitlement, Step6Result step6Result, double minconf) {
		this.analyzedEntitlement = Objects.requireNonNull(analyzedEntitlement);
		this.step6Result = Objects.requireNonNull(step6Result);
		this.minconf = minconf;
	}

	public Step6Result getStep6Result() {
		return step6Result;
	}

	public double getMinconf() {
		return minconf;
	}

	public String getAnalyzedEntitlement() {
		return analyzedEntitlement;
	}

}
