package com.groupon.mobile.model;

public enum FieldType {
	SHORT_TEXT("Text"), //
	LONG_TEXT("Long Text"), //
	INTEGER("Integer"), //
	FLOAT("Decimal Number"), //
	PHONE_NUMBER("Phone Number"), //
	DATE("Date"), //
	CHECKBOX("Checkboxes"), //
	RADIO("Multiple Choices"), //
	SELECT("Drop Down"), //
	PHOTO("Photo"), //
	LOCATION_MAP("Location");

	private String uiName;

	private FieldType(String uiName) {
		this.uiName = uiName;
	}

	public String getUIName() {
		return this.uiName;
	}

	public static String[] getFieldTypes() {
		String[] rv = new String[8];
		rv[0] = SHORT_TEXT.uiName;
		rv[1] = CHECKBOX.uiName;
		rv[2] = SELECT.uiName;
		rv[3] = RADIO.uiName;
		rv[4] = DATE.uiName;
		rv[5] = INTEGER.uiName;
		rv[6] = FLOAT.uiName;
		rv[7] = LONG_TEXT.uiName;
	
		return rv;

		/**
		 * TODO will be replaced soon FieldType[] values = FieldType.values();
		 * String[] rv = new String[values.length]; for (int i = 0; i <
		 * rv.length; i++) { rv[i] = values[i].uiName; } return rv;
		 **/
	}

	public static FieldType getFromUIName(String uiName) {
		for (FieldType f : values()) {
			if (f.uiName.equals(uiName)) {
				return f;
			}
		}
		return null;
	}
}
