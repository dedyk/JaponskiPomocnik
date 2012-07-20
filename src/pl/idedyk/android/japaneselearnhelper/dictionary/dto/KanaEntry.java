package pl.idedyk.android.japaneselearnhelper.dictionary.dto;

public class KanaEntry {
	private String kanaJapanese;
	
	private String kana;
	
	private String image;

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

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}
}