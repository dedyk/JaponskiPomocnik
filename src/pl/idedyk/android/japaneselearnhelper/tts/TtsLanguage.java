package pl.idedyk.android.japaneselearnhelper.tts;

import java.util.Locale;

public enum TtsLanguage {

	JAPANESE(Locale.JAPAN), //, "com.svox.classic"),
	
	POLISH("PL"), //, "com.ivona.tts"),
	
	ENGLISH(Locale.ENGLISH); //, "com.svox.pico");
	
	private Locale locale;
	
	//private String engine;
	
	private TtsLanguage(Locale locale /*, String engine */) {
		this.locale = locale;
		
		//this.engine = engine;
	}
	
	private TtsLanguage(String localeString /*, String engine */) {
		
		Locale polishLocale = null;
		
		Locale[] availableLocales = Locale.getAvailableLocales();
		
		for (Locale currentLocale : availableLocales) {			
			if (currentLocale.getCountry().equals("PL") == true) { 
				polishLocale = currentLocale;
				
				break;
			}
		}
		
		if (polishLocale == null) {
			polishLocale = getLocale();
		}
		
		this.locale = polishLocale;
		
		//this.engine = engine;
	}

	public Locale getLocale() {
		return locale;
	}

	/*
	public String getEngine() {
		return engine;
	}
	*/
}
