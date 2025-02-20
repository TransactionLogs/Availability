package org.anonymous.transactionlogs.steps.ruleanalysis;

import java.util.List;

import org.anonymous.transactionlogs.model.AssociationRuleWrapper;
import org.anonymous.transactionlogs.model.LogEntryType;

import apriori4j.AssociationRule;
import apriori4j.ItemSet;

/**
 * This class sorts association rules. It also filters rules as done in the analysis of the real data set.
 */
public class AssociationRuleAnalyzer {

	public void sort(List<AssociationRuleWrapper> rules) {
		rules.sort(this::compareByPriority);
	}

	private int compareByPriority(AssociationRuleWrapper rule1Wrapper, AssociationRuleWrapper rule2Wrapper) {

		// first criterion: higher frequency before lower frequency
		if (rule1Wrapper.getFrequency() > rule2Wrapper.getFrequency()) {
			return -1;
		}

		if (rule1Wrapper.getFrequency() < rule2Wrapper.getFrequency()) {
			return 1;
		}

		AssociationRule rule1 = rule1Wrapper.getRule();
		AssociationRule rule2 = rule2Wrapper.getRule();

		// second criterion: higher confidence before lower confidence
		if (rule1.getConfidence() > rule2.getConfidence()) {
			return -1;
		}

		if (rule1.getConfidence() < rule2.getConfidence()) {
			return 1;
		}

		// third criterion: smaller rules before larger rules
		if (rule1.getLeftHandSide().size() < rule2.getLeftHandSide().size()) {
			return -1;
		}

		if (rule1.getLeftHandSide().size() > rule2.getLeftHandSide().size()) {
			return 1;
		}

		// fourth criterion: add before remove -> just sort alphabetically
		int addCountFirstRule = 0;
		for (String val : rule1.getLeftHandSide()) {
			if (val.contains("ADDED")) {
				addCountFirstRule++;
			}
		}
		int addCountSecondRule = 0;
		for (String val : rule2.getLeftHandSide()) {
			if (val.contains("ADDED")) {
				addCountSecondRule++;
			}
		}

		if (addCountFirstRule > addCountSecondRule) {
			return -1;
		}

		if (addCountFirstRule < addCountSecondRule) {
			return 1;
		}

		return 0;
	}

	public List<AssociationRuleWrapper> filterRulesInScope(List<AssociationRuleWrapper> rules) {
		return rules.stream().filter(this::isInScope).toList();
	}

	private boolean isInScope(AssociationRuleWrapper associationRuleWrapper) {

		AssociationRule associationRule = associationRuleWrapper.getRule();

		// left side: one entitlement ADD action
		ItemSet leftHandSide = associationRule.getLeftHandSide();
		if (leftHandSide.size() != 1) {
			return false;
		}
		String leftHandSiteItem = leftHandSide.iterator().next();
		if (!isEntitlementChangeEvent(leftHandSiteItem)) {
			return false;
		}

		if(!leftHandSiteItem.contains("ADDED")) {
			return false;
		}

		// right hand side: only attribute changes
		for (String rightHandSideItem : associationRule.getRightHandSide()) {
			if (!isAttributeChangeEvent(rightHandSideItem)) {
				return false;
			}

			if(!rightHandSideItem.contains("ADDED")){
				return false;
			}
		}

		return true;

	}

	private boolean isAttributeChangeEvent(String transactionValue) {
		return transactionValue.startsWith(LogEntryType.ATTRIBUTE_VALUE_ADDED.name()) || transactionValue.startsWith(
				LogEntryType.ATTRIBUTE_VALUE_REMOVED.name());
	}

	private boolean isEntitlementChangeEvent(String transactionValue) {
		return transactionValue.startsWith(LogEntryType.ENTITLEMENT_ADDED.name()) || transactionValue.startsWith(
				LogEntryType.ENTITLEMENT_REMOVED.name());
	}

}
