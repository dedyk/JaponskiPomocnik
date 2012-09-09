package pl.idedyk.android.japaneselearnhelper.kanji.hkr;

import java.util.List;

import pl.idedyk.android.japaneselearnhelper.R;
import pl.idedyk.android.japaneselearnhelper.dictionary.DictionaryManager;
import pl.idedyk.android.japaneselearnhelper.dictionary.ZinniaManager;
import pl.idedyk.android.japaneselearnhelper.dictionary.ZinniaManager.Character;
import pl.idedyk.android.japaneselearnhelper.dictionary.ZinniaManager.RecognizerResultItem;
import android.app.Activity;
import android.graphics.PointF;
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

		drawView = (KanjiDrawView) findViewById(R.id.kanji_recognizer_draw_view);

		Button undoButton = (Button) findViewById(R.id.kanji_recognizer_undo_button);

		undoButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				drawView.removeLastStroke();

			}
		});

		Button clearButton = (Button) findViewById(R.id.kanji_recognizer_clear_button);

		clearButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				drawView.clear();

			}
		});

		Button recognizeButton = (Button) findViewById(R.id.kanji_recognizer_recognize_button);

		recognizeButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				
				ZinniaManager zinniaManager = DictionaryManager.getInstance().getZinniaManager();
				
				zinniaManager.open();
				
				Character zinniaCharacter = zinniaManager.createNewCharacter();

				zinniaCharacter.clear();
				zinniaCharacter.setHeight(drawView.getHeight());
				zinniaCharacter.setWidth(drawView.getWidth());
				
				List<Stroke> strokes = drawView.getStrokes();
				
				for (int idx = 0; idx < strokes.size(); ++idx) {
					
					Stroke currentStroke = strokes.get(idx);
					
					List<PointF> currentStrokePoints = currentStroke.getPoints();
					
					for (PointF currentStrokeCurrentPoint : currentStrokePoints) {
						zinniaCharacter.add(idx, (int)currentStrokeCurrentPoint.x, (int)currentStrokeCurrentPoint.y);
					}
				}
				
				List<RecognizerResultItem> recognizeResult = zinniaCharacter.recognize();
				
				zinniaCharacter.destroy();
				
				
			}
		});
	}
}
