package org.anonymous.transactionlogs.model;

public enum LogEntryType {

	ATTRIBUTE_VALUE_ADDED, ATTRIBUTE_VALUE_REMOVED, ENTITLEMENT_ADDED, ENTITLEMENT_REMOVED;

	public boolean isEntitlementType(){
		return this == ENTITLEMENT_ADDED || this == ENTITLEMENT_REMOVED;
	}

	public boolean isAttributeType(){
		return this == ATTRIBUTE_VALUE_ADDED || this == ATTRIBUTE_VALUE_REMOVED;
	}

}
