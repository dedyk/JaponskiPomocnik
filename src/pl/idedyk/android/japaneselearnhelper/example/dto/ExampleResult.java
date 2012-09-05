package pl.idedyk.android.japaneselearnhelper.example.dto;

import java.io.Serializable;
import java.util.List;

public class ExampleResult implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String kanji;
	
	private List<String> kanaList;
	
	private List<String> romajiList;
	
	private boolean canAddPrefix = true;
	
	private ExampleResult alternative;

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

	public ExampleResult getAlternative() {
		return alternative;
	}

	public void setAlternative(ExampleResult alternative) {
		this.alternative = alternative;
	}

	public boolean isCanAddPrefix() {
		return canAddPrefix;
	}

	public void setCanAddPrefix(boolean canAddPrefix) {
		this.canAddPrefix = canAddPrefix;
	}
	
	public boolean isKanjiExists() {
		if (kanji != null && kanji.equals("-") == false) {
			return true;
		} else {
			return false;
		}
	}
}
