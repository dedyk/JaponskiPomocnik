package pl.idedyk.android.japaneselearnhelper.tts;

import java.util.Locale;

public enum TtsLanguage {

	JAPANESE(Locale.JAPAN, "com.svox.classic"),
	
	ENGLISH(Locale.ENGLISH, "com.svox.pico");
	
	private Locale locale;
	
	private String engine;
	
	private TtsLanguage(Locale locale, String engine) {
		this.locale = locale;
		
		this.engine = engine;
	}

	public Locale getLocale() {
		return locale;
	}

	public String getEngine() {
		return engine;
	}
}
