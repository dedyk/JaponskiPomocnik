package pl.idedyk.android.japaneselearnhelper;

public class MainMenuItem {
	
	private String kanji;
	
	private String text;

	public MainMenuItem(String kanji, String text) {
		this.kanji = kanji;
		this.text = text;
	}

	public String getKanji() {
		return kanji;
	}

	public String getText() {
		return text;
	}
}
