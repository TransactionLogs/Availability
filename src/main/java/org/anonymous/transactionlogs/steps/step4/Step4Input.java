package org.anonymous.transactionlogs.steps.step4;

import java.util.Objects;

import org.anonymous.transactionlogs.steps.step3.Step3Result;

public class Step4Input {

	private final Step3Result step3Result;

	public Step4Input(Step3Result step3Result) {
		this.step3Result = Objects.requireNonNull(step3Result);
	}

	public Step3Result getStep3Result() {
		return step3Result;
	}

}
