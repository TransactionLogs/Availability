package org.anonymous.transactionlogs.steps;

public interface IDataProcessingStep<INPUT, RESULT> {

	RESULT execute(INPUT input);

}
