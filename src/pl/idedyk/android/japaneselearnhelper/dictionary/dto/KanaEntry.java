package pl.idedyk.android.japaneselearnhelper.dictionary.dto;

public class KanaEntry {
	private String kanaJapanese;
	
	private String kana;

	public KanaEntry(String kanaJapanese, String kana) {
		this.kanaJapanese = kanaJapanese;
		this.kana = kana;
	}

	public String getKanaJapanese() {
		return kanaJapanese;
	}

	public String getKana() {
		return kana;
	}
}