package org.anonymous.transactionlogs.steps.step3;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import org.anonymous.transactionlogs.steps.step2.Step2Result;

public class Step3Input {

	private Step2Result step2Result;

	private Set<String> relevantEntitlements;

	public Step3Input(Step2Result step2Result, Set<String> relevantEntitlements) {
		this.step2Result = Objects.requireNonNull(step2Result);
		this.relevantEntitlements = relevantEntitlements;
	}

	public Step2Result getStep2Result() {
		return step2Result;
	}

	public Optional<Set<String>> getRelevantEntitlements() {
		return Optional.ofNullable(relevantEntitlements);
	}

}
