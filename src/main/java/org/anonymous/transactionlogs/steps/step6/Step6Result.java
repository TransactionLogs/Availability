package org.anonymous.transactionlogs.steps.step6;

import java.util.Objects;

public class Step6Result {

	private final Step6Input step6Input;

	public Step6Result(Step6Input step6Input) {
		this.step6Input = Objects.requireNonNull(step6Input);
	}

	public Step6Input getStep6Input() {
		return step6Input;
	}

}
