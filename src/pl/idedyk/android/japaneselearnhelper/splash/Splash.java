package pl.idedyk.android.japaneselearnhelper.splash;

import pl.idedyk.android.japaneselearnhelper.R;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;

public class Splash extends Activity {
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);

        setContentView(R.layout.splash);
        
        final ProgressBar progressBar = (ProgressBar)findViewById(R.id.splash_progressbar);
        
        class AsyncTaskTest extends AsyncTask<Integer, Integer, Long> {
        	
			@Override
			protected Long doInBackground(Integer... params) {
				
				for (int idx = 0; idx <= 100; ++idx) {
					publishProgress(idx);
					
					try {
						Thread.sleep(30);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
								
				return null;
			}
			
			@Override
			protected void onProgressUpdate(Integer... integer) {
				progressBar.setProgress(integer[0]);
			}
			
			@Override
			protected void onPostExecute(Long result) {
				finish();
			}
        }
        
        new AsyncTaskTest().execute(new Integer[] { 1 });
    }
}
