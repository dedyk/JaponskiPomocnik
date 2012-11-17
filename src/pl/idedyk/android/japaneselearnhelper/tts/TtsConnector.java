package pl.idedyk.android.japaneselearnhelper.tts;

import java.lang.reflect.Constructor;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;

public class TtsConnector implements OnInitListener {

	private TextToSpeech textToSpeech = null;
	
	private TtsLanguage ttsLanguage;
	
	private Boolean onInitResult = null;
	
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
		
		if (textToSpeech == null) {
			stop();
			
			textToSpeech = null;
			onInitResult = Boolean.FALSE;
			
			return;
		}
		
		if (status == TextToSpeech.SUCCESS) {
			
			int result = textToSpeech.setLanguage(ttsLanguage.getLocale());
			
			if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
				
				stop();
				
				textToSpeech = null;
				onInitResult = Boolean.FALSE;
			} else {
				// success
				
				onInitResult = Boolean.TRUE;
			}
		} else {
			
			stop();
			
			textToSpeech = null;
			onInitResult = Boolean.FALSE;
		}
	}
	
	public void speak(String text) {
		
		if (textToSpeech != null) {
			textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
		}
	}
	
	public void speakAndWait(String text) {
		speak(text);
		
		if (textToSpeech != null) {
			
			while (textToSpeech.isSpeaking()) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
				}
			}
			
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
			}
		}
	}
	
	public Boolean getOnInitResult() {
		return onInitResult;
	}

	public void stop() {
		
		if (textToSpeech != null) {
			textToSpeech.stop();
			textToSpeech.shutdown();
		}
	}
}
