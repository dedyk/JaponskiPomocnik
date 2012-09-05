package pl.idedyk.android.japaneselearnhelper.gramma.dto;

import java.io.Serializable;
import java.util.List;

public class GrammaFormConjugateResult implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private GrammaFormConjugateResultType resultType;
	
	private String kanji;
	
	private List<String> kanaList;
	
	private List<String> romajiList;
	
	private GrammaFormConjugateResult alternative;

	public String getKanji() {
		return kanji;
	}

	public List<String> getKanaList() {
		return kanaList;
	}

	public List<String> getRomajiList() {
		return romajiList;
	}

	public void setKanji(String kanji) {
		this.kanji = kanji;
	}

	public void setKanaList(List<String> kanaList) {
		this.kanaList = kanaList;
	}

	public void setRomajiList(List<String> romajiList) {
		this.romajiList = romajiList;
	}

	public GrammaFormConjugateResultType getResultType() {
		return resultType;
	}

	public void setResultType(GrammaFormConjugateResultType resultType) {
		this.resultType = resultType;
	}

	public GrammaFormConjugateResult getAlternative() {
		return alternative;
	}

	public void setAlternative(GrammaFormConjugateResult alternative) {
		this.alternative = alternative;
	}
	
	public boolean isKanjiExists() {
		if (kanji != null && kanji.equals("-") == false) {
			return true;
		} else {
			return false;
		}
	}
}
