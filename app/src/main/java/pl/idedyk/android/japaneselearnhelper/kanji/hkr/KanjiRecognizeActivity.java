package pl.idedyk.android.japaneselearnhelper.kanji.hkr;

import java.util.ArrayList;
import java.util.List;

import pl.idedyk.android.japaneselearnhelper.JapaneseAndroidLearnHelperApplication;
import pl.idedyk.android.japaneselearnhelper.MenuShorterHelper;
import pl.idedyk.android.japaneselearnhelper.R;
import pl.idedyk.android.japaneselearnhelper.dictionary.DictionaryManagerCommon;
import pl.idedyk.android.japaneselearnhelper.dictionary.ZinniaManagerCommon;
import pl.idedyk.japanese.dictionary.api.dto.KanjiRecognizerResultItem;
import pl.idedyk.japanese.dictionary.api.exception.DictionaryException;
import pl.idedyk.japanese.dictionary2.kanjidic2.xsd.KanjiCharacterInfo;

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

		JapaneseAndroidLearnHelperApplication.getInstance().setContentViewAndTheme(this, R.layout.kanji_recognizer);
		
		JapaneseAndroidLearnHelperApplication.getInstance().logScreen(this, getString(R.string.logs_kanji_recognize));

		init();
	}

	public void onBackPressed() {
		super.onBackPressed();

		finish();
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

				class RecognizeAsyncTaskResult {

					private DictionaryException dictionaryException;

					private List<KanjiCharacterInfo> kanjiEntryList;

					public RecognizeAsyncTaskResult(DictionaryException dictionaryException) {
						this.dictionaryException = dictionaryException;
					}

					public RecognizeAsyncTaskResult(List<KanjiCharacterInfo> kanjiEntryList) {
						this.kanjiEntryList = kanjiEntryList;
					}
				}

				class RecognizeAsyncTask extends AsyncTask<Void, Void, RecognizeAsyncTaskResult> {

					@Override
					protected RecognizeAsyncTaskResult doInBackground(Void... arg0) {

						DictionaryManagerCommon dictionaryManager = JapaneseAndroidLearnHelperApplication.getInstance().getDictionaryManager(KanjiRecognizeActivity.this);
						
						ZinniaManagerCommon zinniaManager = dictionaryManager.getZinniaManager();
						
						// zinniaManager.open();

                        ZinniaManagerCommon.IZinnaManagerCharacter zinniaCharacter = zinniaManager.createNewCharacter();
						
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

						List<KanjiRecognizerResultItem> recognizeResult = null;

						try {
							recognizeResult = zinniaCharacter.recognize(100);

						} catch (DictionaryException e) {
							return new RecognizeAsyncTaskResult(e);

						} finally {
							zinniaCharacter.destroy();
						}

						List<String> findKanjiListRequest = new ArrayList<>();

						for (KanjiRecognizerResultItem currentRecognizeResult : recognizeResult) {
							findKanjiListRequest.add(currentRecognizeResult.getKanji());
						}

						List<KanjiCharacterInfo> kanjiEntries = null;

						try {
							kanjiEntries = dictionaryManager.findKanjiList(findKanjiListRequest);
						} catch (DictionaryException e) {
							return new RecognizeAsyncTaskResult(e);
						}

						return new RecognizeAsyncTaskResult(kanjiEntries);
					}
					
				    @Override
				    protected void onPostExecute(RecognizeAsyncTaskResult result) {

				        super.onPostExecute(result);

				        if (progressDialog != null && progressDialog.isShowing()) {
				        	progressDialog.dismiss();
				        }

						if (result.dictionaryException != null) {

							Toast.makeText(KanjiRecognizeActivity.this, getString(R.string.dictionary_exception_common_error_message, result.dictionaryException.getMessage()), Toast.LENGTH_LONG).show();

							return;
						}

						Intent intent = new Intent(getApplicationContext(), KanjiRecognizerResult.class);
						
						KanjiEntry[] kanjiEntriesAsArray = new KanjiEntry[result.kanjiEntryList.size()];

						result.kanjiEntryList.toArray(kanjiEntriesAsArray);
						
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
