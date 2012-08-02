package pl.idedyk.android.japaneselearnhelper.example.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ExampleGroupTypeElements implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private ExampleGroupType exampleGroupType;
	
	private List<ExampleResult> exampleResults;

	public List<ExampleResult> getExampleResults() {
		
		if (exampleResults == null) {
			exampleResults = new ArrayList<ExampleResult>();
		}
		
		return exampleResults;
	}

	public ExampleGroupType getExampleGroupType() {
		return exampleGroupType;
	}

	public void setExampleGroupType(ExampleGroupType exampleGroupType) {
		this.exampleGroupType = exampleGroupType;
	}
}
