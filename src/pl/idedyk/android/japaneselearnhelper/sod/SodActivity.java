package pl.idedyk.android.japaneselearnhelper.sod;

import java.util.ArrayList;
import java.util.List;

import pl.idedyk.android.japaneselearnhelper.R;
import pl.idedyk.android.japaneselearnhelper.sod.dto.StrokePathInfo;

import android.app.Activity;
import android.graphics.Matrix;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class SodActivity extends Activity implements OnClickListener {
	
	private static final String SAVE_STATE_STROKE_PATHS = "pl.idedyk.android.japaneselearnhelper.sod.SAVE_STATE_STROKE_PATHS";
	
    private static final float KANJIVG_SIZE = 109f;

    private Button drawButton;
    private Button clearButton;
    private Button animateButton;
    
    private StrokePathInfo strokePathsInfo;

    private StrokeOrderView strokeOrderView;

    private StrokedCharacter character;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.sod);

        findViews();

        drawButton.setOnClickListener(this);
        clearButton.setOnClickListener(this);
        animateButton.setOnClickListener(this);
        
        strokePathsInfo = (StrokePathInfo)getIntent().getSerializableExtra("strokePathsInfo");
        
        character = parseWsReply();
        
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
        	
            character = parseWsReply();
            
            if (character != null) {
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

        strokeOrderView.setCharacter(character);
        strokeOrderView.setAnnotateStrokes(true);
        strokeOrderView.invalidate();
    }

    void animate(StrokedCharacter character) {
        this.character = character;

        int animationDelay = 50;
        strokeOrderView.setAnimationDelayMillis(animationDelay);
        strokeOrderView.setCharacter(character);
        strokeOrderView.setAnnotateStrokes(true);
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

    private List<StrokePath> parseWsReplyStrokes() {

        List<StrokePath> result = new ArrayList<StrokePath>();
        
        List<List<String>> strokePaths = strokePathsInfo.getStrokePaths();
        
        for (int charStrokePathsIdx = 0; charStrokePathsIdx < strokePaths.size(); ++charStrokePathsIdx) {
        	
        	List<String> currentCharStrokePaths = strokePaths.get(charStrokePathsIdx);
        	
            for (int i = 0; i < currentCharStrokePaths.size(); i++) {
                String line = currentCharStrokePaths.get(i);
                
                if (line != null && !"".equals(line)) {
                    StrokePath strokePath = StrokePath.parsePath(line.trim());
                    
                    // move
                    Matrix matrix = new Matrix();
                    
                    // FIXME !!!!!!!!!!
                    matrix.setTranslate(charStrokePathsIdx * KANJIVG_SIZE, 0);
                    
                    strokePath.transformMoveTo(matrix);
                    
                    result.add(strokePath);
                }
            }	
		}

        return result;
    }

    private StrokedCharacter parseWsReply() {
        List<StrokePath> strokes = parseWsReplyStrokes();
        
        // FIXME !!!!!!!!!!
        StrokedCharacter result = new StrokedCharacter(strokes, KANJIVG_SIZE * strokePathsInfo.getStrokePaths().size(),
                KANJIVG_SIZE);

        return result;
    }
}
