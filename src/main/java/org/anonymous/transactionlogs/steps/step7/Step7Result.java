package org.anonymous.transactionlogs.steps.step7;

import java.util.List;
import java.util.Objects;

import org.anonymous.transactionlogs.model.AssociationRuleWrapper;

import apriori4j.AnalysisResult;

public class Step7Result {

	private final Step7Input step7Input;

	private final AnalysisResult analysisResult;

	private final List<AssociationRuleWrapper> wrappedAssociationRules;

	public Step7Result(Step7Input step7Input, AnalysisResult analysisResult,
			List<AssociationRuleWrapper> wrappedAssociationRules) {
		this.step7Input = Objects.requireNonNull(step7Input);
		this.analysisResult = Objects.requireNonNull(analysisResult);
		this.wrappedAssociationRules = Objects.requireNonNull(wrappedAssociationRules);
	}

	public AnalysisResult getAnalysisResult() {
		return analysisResult;
	}

	public Step7Input getStep7Input() {
		return step7Input;
	}

	public List<AssociationRuleWrapper> getWrappedAssociationRules() {
		return wrappedAssociationRules;
	}

}
