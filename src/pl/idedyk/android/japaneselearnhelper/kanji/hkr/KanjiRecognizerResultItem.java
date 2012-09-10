package pl.idedyk.android.japaneselearnhelper.kanji.hkr;

public class KanjiRecognizerResultItem {
	
	private String kanji;
	
	private float score;
	
	public KanjiRecognizerResultItem(String kanji, float score) {
		this.kanji = kanji;
		this.score = score;
	}

	public String getKanji() {
		return kanji;
	}

	public float getScore() {
		return score;
	}		
}
