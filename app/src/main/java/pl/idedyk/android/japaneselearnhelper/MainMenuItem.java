package pl.idedyk.android.japaneselearnhelper;

public class MainMenuItem {
	
	private String kanji;
	
	private String text;

	private Float kanjiSize = null;

	public MainMenuItem(String kanji, String text) {
		this.kanji = kanji;
		this.text = text;
	}

	public MainMenuItem(String kanji, Float kanjiSize, String text) {
		this.kanji = kanji;
		this.kanjiSize = kanjiSize;

		this.text = text;
	}

	public String getKanji() {
		return kanji;
	}

	public String getText() {
		return text;
	}

	public Float getKanjiSize() {
		return kanjiSize;
	}
}
