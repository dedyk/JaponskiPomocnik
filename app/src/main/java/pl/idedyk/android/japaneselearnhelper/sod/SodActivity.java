package pl.idedyk.android.japaneselearnhelper.sod;

import pl.idedyk.android.japaneselearnhelper.JapaneseAndroidLearnHelperApplication;
import pl.idedyk.android.japaneselearnhelper.MenuShorterHelper;
import pl.idedyk.android.japaneselearnhelper.R;
import pl.idedyk.android.japaneselearnhelper.sod.dto.StrokePathInfo;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class SodActivity extends Activity implements OnClickListener {

	private static final String SAVE_STATE_STROKE_PATHS = "pl.idedyk.android.japaneselearnhelper.sod.SAVE_STATE_STROKE_PATHS";

	private Button drawButton;
	private Button clearButton;
	private Button animateButton;

	private StrokePathInfo strokePathsInfo;

	private StrokeOrderView strokeOrderView;

	private StrokedCharacter character;
	
	private boolean annotateStrokes;
	
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
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);

		JapaneseAndroidLearnHelperApplication.getInstance().setContentViewAndTheme(this, R.layout.sod);

		JapaneseAndroidLearnHelperApplication.getInstance().logScreen(this, getString(R.string.logs_sod));

		findViews();

		drawButton.setOnClickListener(this);
		clearButton.setOnClickListener(this);
		animateButton.setOnClickListener(this);

		strokePathsInfo = (StrokePathInfo)getIntent().getSerializableExtra("strokePathsInfo");
		
		annotateStrokes = getIntent().getBooleanExtra("annotateStrokes", true);
		
		character = SodStrokeParser.parseWsReply(strokePathsInfo);

		drawSod(character);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onStart() {
		super.onStart();
	}


	@Override
	protected void onResume() {
		super.onResume();

		drawSod(character);
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);

		if (savedInstanceState != null) {
			strokePathsInfo = (StrokePathInfo)savedInstanceState.getSerializable(SAVE_STATE_STROKE_PATHS);

			character = SodStrokeParser.parseWsReply(strokePathsInfo);

			if (character != null) {
				strokeOrderView.setAnnotateStrokes(annotateStrokes);
				strokeOrderView.setCharacter(character);
				strokeOrderView.invalidate();
			}
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		outState.putSerializable(SAVE_STATE_STROKE_PATHS, strokePathsInfo);
	}

	void drawSod(StrokedCharacter character) {
		this.character = character;

		strokeOrderView.setAnnotateStrokes(annotateStrokes);
		strokeOrderView.setCharacter(character);
		strokeOrderView.invalidate();
	}

	void animate(StrokedCharacter character) {
		this.character = character;

		int animationDelay = 50 + (15 * strokePathsInfo.getStrokePaths().size());
		strokeOrderView.setAnnotateStrokes(annotateStrokes);
		strokeOrderView.setAnimationDelayMillis(animationDelay);
		strokeOrderView.setCharacter(character);
		strokeOrderView.startAnimation();
	}

	private void findViews() {
		drawButton = (Button) findViewById(R.id.sod_draw_button);
		clearButton = (Button) findViewById(R.id.sod_clear_button);
		animateButton = (Button) findViewById(R.id.sod_animate_button);
		strokeOrderView = (StrokeOrderView) findViewById(R.id.sod_draw_view);
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.sod_draw_button:
			drawSod(character);
			break;
		case R.id.sod_animate_button:
			animate(character);
			break;
		case R.id.sod_clear_button:
			strokeOrderView.clear();
			strokeOrderView.invalidate();
			break;
		default:
			// do nothing
		}
	}
}
