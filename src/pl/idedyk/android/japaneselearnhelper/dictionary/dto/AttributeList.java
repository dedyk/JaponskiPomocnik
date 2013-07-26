package pl.idedyk.android.japaneselearnhelper.dictionary.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AttributeList implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private List<Attribute> attributeList = new ArrayList<Attribute>();

	public boolean contains(AttributeType attributeType) {
		
		for (Attribute currentAttribute : attributeList) {
			
			if (currentAttribute.getAttributeType() == attributeType) {
				return true;
			}
		}
		
		return false;
	}

	public void add(int i, AttributeType attributeType) {
		
		Attribute attribute = new Attribute();
		
		attribute.setAttributeType(attributeType);
		
		attributeList.add(i, attribute);
	}

	public void add(AttributeType attributeType) {
		
		Attribute attribute = new Attribute();
		
		attribute.setAttributeType(attributeType);
		
		attributeList.add(attribute);		
	}
	
	public void addAttributeValue(AttributeType attributeType, List<String> attributeValueList) {
		
		Attribute attribute = new Attribute();
		
		attribute.setAttributeType(attributeType);
		attribute.setAttributeValue(attributeValueList);
		
		attributeList.add(attribute);
	}
	
	public void addAttributeValue(AttributeType attributeType, String attributeValue) {
		
		Attribute attribute = new Attribute();
		
		attribute.setAttributeType(attributeType);
		attribute.setSingleAttributeValue(attributeValue);
		
		attributeList.add(attribute);		
	}

	public List<Attribute> getAttributeList() {
		return attributeList;
	}
	
	public List<String> convertAttributeListToListString() {
		
		List<String> result = new ArrayList<String>();
				
		for (int idx = 0; idx < attributeList.size(); ++idx) {
			
			Attribute currentAttribute = attributeList.get(idx);
			
			StringBuffer sb = new StringBuffer();
		
			sb.append(currentAttribute.getAttributeType().toString());
			
			List<String> attributeValue = currentAttribute.getAttributeValue();
		
			if (attributeValue != null && attributeValue.size() > 0) {
				
				for (String currentSingleAttributeValue : attributeValue) {
					sb.append(" ").append(currentSingleAttributeValue);
				}
			}
			
			result.add(sb.toString());
		}
		
		return result;
	}

	@Override
	public int hashCode() {
		
		final int prime = 31;
		
		int result = 1;
		
		result = prime * result + ((attributeList == null) ? 0 : attributeList.hashCode());
		
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		
		if (this == obj)
			return true;
		
		if (obj == null)
			return false;
		
		if (getClass() != obj.getClass())
			return false;
		
		AttributeList other = (AttributeList) obj;
		
		if (attributeList == null) {
			if (other.attributeList != null)
				return false;
			
		} else if (!attributeList.equals(other.attributeList))
			return false;
		
		return true;
	}
	
	public static AttributeList parseAttributesStringList(List<String> attributeListList) {
				
		AttributeList result = new AttributeList();
		
		for (String currentAttributeString : attributeListList) {
			
			String[] currentAttributeStringSplited = currentAttributeString.split(" ");
			
			if (currentAttributeStringSplited[0].equals("") == true) {
				continue;
			}
			
			AttributeType attributeType = AttributeType.valueOf(currentAttributeStringSplited[0]);
			
			List<String> attributeValueList = null;
			
			if (currentAttributeStringSplited.length > 1) {
				
				attributeValueList = new ArrayList<String>();
				
				for (int currentAttributeStringSplitedIdx = 1; currentAttributeStringSplitedIdx < currentAttributeStringSplited.length;
						currentAttributeStringSplitedIdx++) {
					
					attributeValueList.add(currentAttributeStringSplited[currentAttributeStringSplitedIdx]);
				}
			}
			
			result.addAttributeValue(attributeType, attributeValueList);
		}
		
		return result;
	}
}
