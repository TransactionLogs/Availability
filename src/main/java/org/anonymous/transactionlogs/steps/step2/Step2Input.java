package org.anonymous.transactionlogs.steps.step2;

import java.util.Objects;
import java.util.Set;

import org.anonymous.transactionlogs.steps.step1.Step1Result;

public class Step2Input {

	private final Step1Result step1Result;

	private final Set<String> semanticallyMeaningfulAttributes;

	public Step2Input(Step1Result step1Result, Set<String> semanticallyMeaningfulAttributes) {
		this.step1Result = Objects.requireNonNull(step1Result);
		this.semanticallyMeaningfulAttributes = Objects.requireNonNull(semanticallyMeaningfulAttributes);
	}

	public Step1Result getStep1Result() {
		return step1Result;
	}

	public Set<String> getSemanticallyMeaningfulAttributes() {
		return semanticallyMeaningfulAttributes;
	}
}
