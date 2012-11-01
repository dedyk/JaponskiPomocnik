package pl.idedyk.android.japaneselearnhelper.kanji.hkr;

import java.util.ArrayList;
import java.util.List;

import pl.idedyk.android.japaneselearnhelper.MenuShorterHelper;
import pl.idedyk.android.japaneselearnhelper.R;
import pl.idedyk.android.japaneselearnhelper.dictionary.DictionaryManager;
import pl.idedyk.android.japaneselearnhelper.dictionary.ZinniaManager;
import pl.idedyk.android.japaneselearnhelper.dictionary.ZinniaManager.Character;
import pl.idedyk.android.japaneselearnhelper.dictionary.dto.KanjiEntry;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.PointF;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class KanjiRecognizeActivity extends Activity {

	private KanjiDrawView drawView;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		
		MenuShorterHelper.onCreateOptionsMenu(menu);
		
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);

		return MenuShorterHelper.onOptionsItemSelected(item, getApplicationContext(), this);
	}
	
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
				
				final List<Stroke> strokes = drawView.getStrokes();
				
				if (strokes.size() == 0) {
					
					Toast toast = Toast.makeText(KanjiRecognizeActivity.this, getString(R.string.kanji_recognizer_please_draw), Toast.LENGTH_SHORT);
					
					toast.show();
					
					return;
				}
				
				final ProgressDialog progressDialog = ProgressDialog.show(KanjiRecognizeActivity.this, 
						getString(R.string.kanji_recognizer_recoginize1),
						getString(R.string.kanji_recognizer_recoginize2));
				
				final StringBuffer strokesStringBuffer = new StringBuffer();
				
				class RecognizeAsyncTask extends AsyncTask<Void, Void, List<KanjiEntry>> {

					@Override
					protected List<KanjiEntry> doInBackground(Void... arg0) {
						
						DictionaryManager dictionaryManager = DictionaryManager.getInstance();
						
						ZinniaManager zinniaManager = dictionaryManager.getZinniaManager();
						
						zinniaManager.open();
						
						Character zinniaCharacter = zinniaManager.createNewCharacter();
						
						strokesStringBuffer.append("Width: " + drawView.getWidth()).append("\n\n");
						strokesStringBuffer.append("Height: " + drawView.getHeight()).append("\n");
						
						zinniaCharacter.clear();
						zinniaCharacter.setWidth(drawView.getWidth());
						zinniaCharacter.setHeight(drawView.getHeight());
						
						for (int idx = 0; idx < strokes.size(); ++idx) {
							
							strokesStringBuffer.append(String.valueOf((idx + 1))).append(":");
							
							Stroke currentStroke = strokes.get(idx);
							
							List<PointF> currentStrokePoints = currentStroke.getPoints();
							
							for (PointF currentStrokeCurrentPoint : currentStrokePoints) {
								zinniaCharacter.add(idx, (int)currentStrokeCurrentPoint.x, (int)currentStrokeCurrentPoint.y);
								
								strokesStringBuffer.append(currentStrokeCurrentPoint.x).append(" ").append(currentStrokeCurrentPoint.y).append(";");
							}
							
							strokesStringBuffer.append("\n\n");
						}
						
						List<KanjiRecognizerResultItem> recognizeResult = zinniaCharacter.recognize(50);
						
						zinniaCharacter.destroy();
						
						List<KanjiEntry> kanjiEntries = new ArrayList<KanjiEntry>();
						
						for (KanjiRecognizerResultItem currentRecognizeResult : recognizeResult) {
							
							KanjiEntry kanjiEntry = dictionaryManager.findKanji(currentRecognizeResult.getKanji());
							
							if (kanjiEntry == null) {
								throw new RuntimeException("kanjiEntry == null");
							}
							
							kanjiEntries.add(kanjiEntry);							
						}						

						return kanjiEntries;
					}
					
				    @Override
				    protected void onPostExecute(List<KanjiEntry> kanjiEntries) {
				        super.onPostExecute(kanjiEntries);

				        progressDialog.dismiss();
				        
						Intent intent = new Intent(getApplicationContext(), KanjiRecognizerResult.class);
						
						KanjiEntry[] kanjiEntriesAsArray = new KanjiEntry[kanjiEntries.size()];
						
						kanjiEntries.toArray(kanjiEntriesAsArray);
						
						intent.putExtra("kanjiRecognizeResult", kanjiEntriesAsArray);
						intent.putExtra("kanjiRecognizeResultStrokes", strokesStringBuffer.toString());
						
						startActivity(intent);
				    }
				}
				
				new RecognizeAsyncTask().execute();
			}
		});
	}
}
