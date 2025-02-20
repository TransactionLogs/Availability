package org.anonymous.transactionlogs.model;

import java.util.Iterator;
import java.util.Objects;

import apriori4j.AssociationRule;

public class AssociationRuleWrapper {

	private final AssociationRule rule;

	// Additional info: The frequency determines how many observed items were responsible for the rule generation
	private final int frequency;

	public AssociationRuleWrapper(AssociationRule rule, int frequency) {
		this.rule = Objects.requireNonNull(rule);
		this.frequency = frequency;
	}

	public AssociationRule getRule() {
		return rule;
	}

	public int getFrequency() {
		return frequency;
	}

	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		sb.append(frequency);
		sb.append("]\t");
		sb.append("[");
		sb.append(rule.getConfidence());
		sb.append("]\t");
		sb.append("(");

		// left hand side items (condition)
		Iterator<String> lhs = rule.getLeftHandSide().iterator();
		while(lhs.hasNext()){
			sb.append(lhs.next());
			if(lhs.hasNext()){
				sb.append(",");
			}
		}

		sb.append(") -> (");

		// right hand side items (implication)
		Iterator<String> rhs = rule.getRightHandSide().iterator();
		while(rhs.hasNext()){
			sb.append(rhs.next());
			if(rhs.hasNext()){
				sb.append(",");
			}
		}

		sb.append(")");

		return sb.toString();

	}

}
