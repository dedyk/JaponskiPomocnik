package pl.idedyk.android.japaneselearnhelper.dictionary.dto;

import java.util.ArrayList;
import java.util.List;

public enum AttributeType {
	
	VERB_TRANSITIVITY("czasownik przechodni"),
	
	VERB_INTRANSITIVITY("czasownik nieprzechodni"),
	
	VERB_KEIGO_HIGH("czasownik honoryfikatywny (wywyższający)"),
	
	SURU_VERB("suru czasownik");
	
	private String name;
	
	AttributeType(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
	// static
	
	public static List<AttributeType> convertToListAttributeType(List<String> values) {

		List<AttributeType> attributeTypeList = new ArrayList<AttributeType>();

		for (String currentValue : values) {
			
			if (currentValue.equals("") == true) {
				continue;
			}
			
			AttributeType attributeType = AttributeType.valueOf(currentValue);
			
			attributeTypeList.add(attributeType);
		}

		return attributeTypeList;
	}
	
	public static List<String> convertToValues(List<AttributeType> groups) {

		List<String> values = new ArrayList<String>();

		for (AttributeType currentAttributeType : groups) {
			values.add(currentAttributeType.toString());
		}

		return values;		
	}
}
