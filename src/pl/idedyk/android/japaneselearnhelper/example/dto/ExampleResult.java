package pl.idedyk.android.japaneselearnhelper.example.dto;

import java.io.Serializable;
import java.util.List;

public class ExampleResult implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String prefixKana;
	
	private String kanji;
	
	private List<String> kanaList;
	
	private String prefixRomaji;
	
	private List<String> romajiList;
		
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
	
	public String getPrefixKana() {
		return prefixKana;
	}

	public String getPrefixRomaji() {
		return prefixRomaji;
	}

	public void setPrefixKana(String prefixKana) {
		this.prefixKana = prefixKana;
	}

	public void setPrefixRomaji(String prefixRomaji) {
		this.prefixRomaji = prefixRomaji;
	}

	public boolean isKanjiExists() {
		if (kanji != null && kanji.equals("-") == false) {
			return true;
		} else {
			return false;
		}
	}
}
