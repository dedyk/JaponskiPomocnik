package pl.idedyk.android.japaneselearnhelper.dictionary.dto;

import java.io.Serializable;
import java.util.List;

public class KanjiEntry implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private int id;
	
	private String kanji;
	
	private List<String> polishTranslates;
	
	private String info;
	
	private KanjiDic2Entry kanjiDic2Entry;
	
	private List<String> strokePaths;
	
	private boolean generated;
	
	private List<String> groups;

	public int getId() {
		return id;
	}

	public String getKanji() {
		return kanji;
	}

	public List<String> getPolishTranslates() {
		return polishTranslates;
	}

	public String getInfo() {
		return info;
	}

	public KanjiDic2Entry getKanjiDic2Entry() {
		return kanjiDic2Entry;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setKanji(String kanji) {
		this.kanji = kanji;
	}

	public void setPolishTranslates(List<String> polishTranslates) {
		this.polishTranslates = polishTranslates;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public void setKanjiDic2Entry(KanjiDic2Entry kanjiDic2Entry) {
		this.kanjiDic2Entry = kanjiDic2Entry;
	}

	public List<String> getStrokePaths() {
		return strokePaths;
	}

	public void setStrokePaths(List<String> strokePaths) {
		this.strokePaths = strokePaths;
	}

	public boolean isGenerated() {
		return generated;
	}

	public void setGenerated(boolean generated) {
		this.generated = generated;
	}

	public List<String> getGroups() {
		return groups;
	}

	public void setGroups(List<String> groups) {
		this.groups = groups;
	}
}
