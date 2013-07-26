package pl.idedyk.android.japaneselearnhelper.dictionary.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Attribute implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private AttributeType attributeType;
	
	private List<String> attributeValueList;

	public AttributeType getAttributeType() {
		return attributeType;
	}

	public void setAttributeType(AttributeType attributeType) {
		this.attributeType = attributeType;
	}

	public List<String> getAttributeValue() {
		return attributeValueList;
	}

	public void setAttributeValue(List<String> attributeValueList) {
		this.attributeValueList = attributeValueList;
	}

	public void setSingleAttributeValue(String attributeValue) {
		
		attributeValueList = new ArrayList<String>();
		
		attributeValueList.add(attributeValue);
	}	
}
