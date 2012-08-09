package pl.idedyk.android.japaneselearnhelper.dictionary.dto;

public class KanaEntry {
	private String kanaJapanese;
	
	private String kana;
	
	private KanaGroup kanaGroup;

	public KanaEntry(String kanaJapanese, String kana, KanaGroup kanaGroup) {
		this.kanaJapanese = kanaJapanese;
		this.kana = kana;
		this.kanaGroup = kanaGroup;
	}

	public String getKanaJapanese() {
		return kanaJapanese;
	}

	public String getKana() {
		return kana;
	}
	
	public KanaGroup getKanaGroup() {
		return kanaGroup;
	}

	public static enum KanaGroup {
		
		GOJUUON,
		
		DAKUTEN,
		
		YOOUN,
		
		OTHER;
	}
}