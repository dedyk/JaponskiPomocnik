package pl.idedyk.android.japaneselearnhelper.splash;

import pl.idedyk.android.japaneselearnhelper.JapaneseAndroidLearnHelperApplication;
import pl.idedyk.android.japaneselearnhelper.JapaneseAndroidLearnHelperMainActivity;
import pl.idedyk.android.japaneselearnhelper.R;
import pl.idedyk.android.japaneselearnhelper.context.JapaneseAndroidLearnHelperContext;
import pl.idedyk.android.japaneselearnhelper.dictionary.DictionaryManager;
import pl.idedyk.android.japaneselearnhelper.dictionary.ILoadWithProgress;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

public class Splash extends Activity {
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);

        setContentView(R.layout.splash);
        
        final ProgressBar progressBar = (ProgressBar)findViewById(R.id.splash_progressbar);
        
        final TextView progressDesc = (TextView)findViewById(R.id.splash_desc_label);
        
        progressDesc.setText(getString(R.string.splash_load_words));
        
        // create dictionary manager
        final DictionaryManager dictionaryManager = new DictionaryManager();
        
        class InitJapaneseAndroidLearnHelperContextAsyncTask extends AsyncTask<Void, Integer, Void> {
        	
        	class LoadWithProgress implements ILoadWithProgress {

				public void setMaxValue(int maxValue) {
					progressBar.setMax(maxValue);
				}

				public void setCurrentPos(int currentPos) {
					progressBar.setProgress(currentPos);
				}

				public void setDescription(String desc) {
					progressDesc.setText(desc);
				}
        	}

			@Override
			protected Void doInBackground(Void... params) {
								
				LoadWithProgress loadWithProgress = new LoadWithProgress();
				
				dictionaryManager.init(loadWithProgress);
				
				try {
					Thread.sleep(1500);
				} catch (InterruptedException e) {
				}
				
				return null;
			}
			
			@Override
			protected void onPostExecute(Void result) {
								
				Intent intent = new Intent(getApplicationContext(), JapaneseAndroidLearnHelperMainActivity.class);
				
				startActivity(intent);
				
				finish();
			}
        }
        
        new InitJapaneseAndroidLearnHelperContextAsyncTask().execute();
        
        // create context
		JapaneseAndroidLearnHelperContext context = new JapaneseAndroidLearnHelperContext();
		
		JapaneseAndroidLearnHelperApplication.getInstance().setContext(context);
    }
}
