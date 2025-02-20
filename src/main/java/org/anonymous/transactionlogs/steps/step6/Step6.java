package org.anonymous.transactionlogs.steps.step6;

import org.anonymous.transactionlogs.steps.IDataProcessingStep;

/**
 * Step 6 mines frequent item sets used by the Apriori algorithm.
 * <p>
 * Due to the utilized Apriori library, we cannot separate the item set mining from the association rule generation. In this implementation example the frequent item set mining is also executed in step 7.
 * <p>
 * We still retain step 6 for conceptual clarity.
 */
public class Step6 implements IDataProcessingStep<Step6Input, Step6Result> {

	@Override
	public Step6Result execute(Step6Input step6Input) {

		// STUB - in this example implementation the frequent item set mining is executed in step7 due to dependency constraints (see class comment).

		// the apriori4j library does not allow separating item set and association rule generation, so we will do it combined in step 7.
		return new Step6Result(step6Input);

	}

}
