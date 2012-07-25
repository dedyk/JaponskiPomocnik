package pl.idedyk.android.japaneselearnhelper.splash;

import pl.idedyk.android.japaneselearnhelper.JapaneseAndroidLearnHelperApplication;
import pl.idedyk.android.japaneselearnhelper.JapaneseAndroidLearnHelperMainActivity;
import pl.idedyk.android.japaneselearnhelper.R;
import pl.idedyk.android.japaneselearnhelper.context.JapaneseAndroidLearnHelperContext;
import pl.idedyk.android.japaneselearnhelper.dictionary.DictionaryManager;
import pl.idedyk.android.japaneselearnhelper.dictionary.ILoadWithProgress;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
				
				AlertDialog alertDialog = new AlertDialog.Builder(Splash.this).create();
				
				alertDialog.setCancelable(false);
				
				alertDialog.setTitle(getString(R.string.splash_message_box_title));
				alertDialog.setMessage(getString(R.string.splash_message_box_info));
				
				alertDialog.setButton(getString(R.string.word_test_incorrect_ok), new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
													
						Intent intent = new Intent(getApplicationContext(), JapaneseAndroidLearnHelperMainActivity.class);
						
						startActivity(intent);
				        
				        
						finish();
					}
				});
				
				alertDialog.show();			
			}
        }
        
        InitJapaneseAndroidLearnHelperContextAsyncTask initJapaneseAndroidLearnHelperContextAsyncTask = 
        		new InitJapaneseAndroidLearnHelperContextAsyncTask();
        
        initJapaneseAndroidLearnHelperContextAsyncTask.execute();
		
        // create context
		JapaneseAndroidLearnHelperContext context = new JapaneseAndroidLearnHelperContext();
		
		JapaneseAndroidLearnHelperApplication.getInstance().setContext(context);
    }
}
