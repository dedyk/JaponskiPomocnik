package pl.idedyk.android.japaneselearnhelper.dictionary.dto;

public class KanaEntry {
	private String kanaJapanese;
	
	private String kana;
	
	private KanaType kanaType;
	
	private KanaGroup kanaGroup;

	public KanaEntry(String kanaJapanese, String kana, KanaType kanaType, KanaGroup kanaGroup) {
		this.kanaJapanese = kanaJapanese;
		this.kana = kana;
		this.kanaType = kanaType;
		this.kanaGroup = kanaGroup;
	}

	public String getKanaJapanese() {
		return kanaJapanese;
	}

	public String getKana() {
		return kana;
	}
	
	public KanaType getKanaType() {
		return kanaType;
	}

	public KanaGroup getKanaGroup() {
		return kanaGroup;
	}
	
	public static enum KanaType {
		HIRAGANA,
		
		KATAKANA;
	}

	public static enum KanaGroup {
		
		GOJUUON,
		
		DAKUTEN,
		
		YOOUN,
		
		OTHER;
	}
}