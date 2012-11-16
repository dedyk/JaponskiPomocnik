package pl.idedyk.android.japaneselearnhelper.tts;

import java.lang.reflect.Constructor;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;

public class TtsConnector implements OnInitListener {

	private TextToSpeech textToSpeech = null;
	
	private TtsLanguage ttsLanguage;
	
	public TtsConnector(Context context, TtsLanguage ttsLanguage) {
		this.ttsLanguage = ttsLanguage;
		
		try {
			// android 4+
			Constructor<TextToSpeech> constructor = TextToSpeech.class.getConstructor(Context.class, TextToSpeech.OnInitListener.class, String.class);
			
			textToSpeech = constructor.newInstance(context, this, ttsLanguage.getEngine());
			
		} catch (Throwable e) {
			textToSpeech = new TextToSpeech(context, this);
		}
	}
	
	public void onInit(int status) {
		
		if (status == TextToSpeech.SUCCESS) {
			
			int result = textToSpeech.setLanguage(ttsLanguage.getLocale());
			
			if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
				
				if (textToSpeech != null) {
					textToSpeech.stop();
					textToSpeech.shutdown();
				}
				
				textToSpeech = null;
			} else {
				// success
			}
		} else {
			
			if (textToSpeech != null) {
				textToSpeech.stop();
				textToSpeech.shutdown();
			}
			
			textToSpeech = null;
		}
	}
	
	public void speak(String text) {		
		textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
	}
	
	public boolean isInitialized() {
		
		if (textToSpeech != null) {
			return true;
		} else {
			return false;
		}
	}
	
	public void stop() {
		
		if (textToSpeech != null) {
			textToSpeech.stop();
			textToSpeech.shutdown();
		}
	}
}
