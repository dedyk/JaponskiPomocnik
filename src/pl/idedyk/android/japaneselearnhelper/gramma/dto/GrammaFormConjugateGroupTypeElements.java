package pl.idedyk.android.japaneselearnhelper.gramma.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GrammaFormConjugateGroupTypeElements implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private GrammaFormConjugateGroupType grammaFormConjugateGroupType;
	
	private List<GrammaFormConjugateResult> grammaFormConjugateResults;

	public GrammaFormConjugateGroupType getGrammaFormConjugateGroupType() {
		return grammaFormConjugateGroupType;
	}

	public List<GrammaFormConjugateResult> getGrammaFormConjugateResults() {
		
		if (grammaFormConjugateResults == null) {
			grammaFormConjugateResults = new ArrayList<GrammaFormConjugateResult>();
		}
		
		return grammaFormConjugateResults;
	}

	public void setGrammaFormConjugateGroupType(GrammaFormConjugateGroupType grammaFormConjugateGroupType) {
		this.grammaFormConjugateGroupType = grammaFormConjugateGroupType;
	}
}
