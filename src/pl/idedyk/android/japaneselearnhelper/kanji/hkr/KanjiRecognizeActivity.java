package pl.idedyk.android.japaneselearnhelper.kanji.hkr;

import pl.idedyk.android.japaneselearnhelper.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class KanjiRecognizeActivity extends Activity {
	
	private KanjiDrawView drawView;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.kanji_recognizer);
        
        init();
    }
    
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);

		init();
	}
	
	private void init() {
		
		drawView = (KanjiDrawView)findViewById(R.id.kanji_recognizer_draw_view);		
		
		Button undoButton = (Button)findViewById(R.id.kanji_recognizer_undo_button);

		undoButton.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				drawView.removeLastStroke();
				
			}
		});
		
		Button clearButton = (Button)findViewById(R.id.kanji_recognizer_clear_button);
		
		clearButton.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				drawView.clear();
				
			}
		});
		
	}
}
