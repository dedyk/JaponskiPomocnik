package pl.idedyk.android.japaneselearnhelper.kanji.sod;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;

import pl.idedyk.android.japaneselearnhelper.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class SodActivity extends Activity implements OnClickListener {

    static class GetSodTask extends AsyncTask<String, Void, String> {

        private SodActivity sodActivity;
        private boolean animate;
        private ResponseHandler<String> responseHandler;

        GetSodTask(SodActivity sodActivity, boolean animate) {
            this.sodActivity = sodActivity;
            this.animate = animate;
        }

        @Override
        protected void onPreExecute() {
            if (sodActivity == null) {
                return;
            }

            String message = "MMMMMMMMM";
            sodActivity.showProgressDialog(message);
        }

        @Override
        protected String doInBackground(String... params) {
            String unicodeNumber = params[0];
            String lookupUrl = STROKE_PATH_LOOKUP_URL + unicodeNumber;
            HttpGet get = new HttpGet(lookupUrl);

            try {
                String responseStr = "猫 732b\n" +
                        "M36.05,19.5c0.07,0.61-0.17,1.57-0.63,2.21C30,29.25,23.75,36.25,11.25,45.29\n" +
                        "M19,19.25c28.25,23.5,17.71,84.53,6,71.5\n" +
                        "M31.33,46.75c0.05,0.63-0.01,1.58-0.36,2.26c-4.44,8.62-8.69,14.71-19.47,24.03\n" +
                        "M42.25,32.18c2.75,0.45,4.5,0.33,6.63,0.07c12.24-1.5,29.24-3.12,40.87-3.86c2.16-0.14,4.37-0.17,6.49,0.26\n" +
                        "M55.26,17.76c0.99,0.99,1.55,2.3,1.67,3.39C58.1,31.32,59.11,41.3,59.44,45\n" +
                        "M79.98,16.5c0.65,1,0.77,1.88,0.58,3.07c-1.66,10.51-2.17,14.72-4.17,24.58\n" +
                        "M47.32,52.24c0.97,0.97,1.36,2.43,1.5,3.5c1.06,8.64,2.3,20.37,3.03,31.5c0.13,1.92,0.24,3.8,0.35,5.59\n" +
                        "M49.78,54.28c12.83-1.32,25.34-2.9,36.47-3.46c3.89-0.19,6.42,0.53,6.01,4.81c-0.62,6.5-2.2,19.8-3.89,30.68c-0.35,2.25-0.7,4.4-1.05,6.36\n" +
                        "M67.8,53.84c0.87,0.87,1.08,2.01,1.08,3.16c0,8-0.01,25.49-0.01,31.62\n" +
                        "M51.47,70.33c5.4-0.58,35.24-2.33,38.19-2.39\n" +
                        "M53.28,90.49c9.97-0.62,22.66-1,33.57-1.4";
                Log.d(TAG, "got SOD response: " + responseStr);

                return responseStr;
            } catch (Exception e) {
                Log.e(TAG, e.getMessage(), e);

                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (sodActivity == null) {
                return;
            }

            sodActivity.dismissProgressDialog();
            if (result != null) {
                sodActivity.setStrokePathsStr(result);
                StrokedCharacter character = parseWsReply(result);
                if (character != null) {
                    if (animate) {
                        sodActivity.animate(character);
                    } else {
                        sodActivity.drawSod(character);
                    }
                } else {
                    /*
                	Toast t = Toast.makeText(sodActivity, String.format(
                            sodActivity.getString(R.string.no_sod_data),
                            sodActivity.getKanji()), Toast.LENGTH_SHORT);
                    t.show();
                    */
                	throw new RuntimeException("TTTT");
                }
            } else {
            	/*
                Toast t = Toast.makeText(sodActivity,
                        R.string.getting_sod_data_failed, Toast.LENGTH_SHORT);
                t.show();
                */
            	
            	throw new RuntimeException("TTTT2");
            }
        }

        void attach(SodActivity sodActivity) {
            this.sodActivity = sodActivity;
        }

        void detach() {
            sodActivity = null;
        }
    }

    private static final String TAG = SodActivity.class.getSimpleName();

    private static final String STROKE_PATH_LOOKUP_URL = "http://wwwjdic-android.appspot.com/kanji/";

    private static final String NOT_FOUND_STATUS = "not found";

    private static final String EXTRA_RIGHT_STROKE_PATHS_STRING = "org.nick.recognizer.quiz.RIGHT_STROKE_PATHS_STRING";

    private static final float KANJIVG_SIZE = 109f;

    private Button drawButton;
    private Button clearButton;
    private Button animateButton;

    private StrokeOrderView strokeOrderView;

    private String unicodeNumber;
    private String kanji;

    private StrokedCharacter character;
    private String strokePathsStr;

    private GetSodTask getSodTask;
    private boolean isRotating = false;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.kanji_details_sod);

        findViews();

        drawButton.setOnClickListener(this);
        clearButton.setOnClickListener(this);
        animateButton.setOnClickListener(this);

        unicodeNumber = "732b";
        kanji = "猫";

        String message = "GGGGGGG";
        setTitle(String.format(message, kanji));

        getSodTask = (GetSodTask) getLastNonConfigurationInstance();
        if (getSodTask != null) {
            getSodTask.attach(this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (getSodTask != null && !isRotating) {
            getSodTask.cancel(true);
            getSodTask = null;
        }
    }

    @Override
    public Object onRetainNonConfigurationInstance() {
        isRotating = true;

        if (getSodTask != null) {
            getSodTask.detach();
        }

        return getSodTask;
    }

    @Override
    protected void onStart() {
        super.onStart();

//        Analytics.startSession(this);
    }


    @Override
    protected void onResume() {
        super.onResume();

        drawSod();
    }

    @Override
    protected void onStop() {
        super.onStop();

        // Analytics.endSession(this);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState != null) {
            strokePathsStr = savedInstanceState
                    .getString(EXTRA_RIGHT_STROKE_PATHS_STRING);
            character = parseWsReply(strokePathsStr);
            if (character != null) {
                strokeOrderView.setCharacter(character);
                strokeOrderView.invalidate();
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(EXTRA_RIGHT_STROKE_PATHS_STRING, strokePathsStr);
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
        drawButton = (Button) findViewById(R.id.kanji_details_sod_draw_button);
        clearButton = (Button) findViewById(R.id.kanji_details_sod_clear_button);
        animateButton = (Button) findViewById(R.id.kanji_details_sod_animate_button);
        strokeOrderView = (StrokeOrderView) findViewById(R.id.sod_draw_view);
    }

    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.kanji_details_sod_draw_button:
            drawSod();
            break;
        case R.id.kanji_details_sod_animate_button:
            animate();
            break;
        case R.id.kanji_details_sod_clear_button:
            strokeOrderView.clear();
            strokeOrderView.invalidate();
            break;
        default:
            // do nothing
        }
    }

    private void drawSod() {
        // Analytics.event("drawSod", this);

        if (character == null) {
            getStrokes();
        } else {
            drawSod(character);
        }
    }

    private void getStrokes() {
        if (getSodTask != null) {
            getSodTask.cancel(true);
        }
        getSodTask = new GetSodTask(this, false);
        getSodTask.execute(unicodeNumber);
    }

    private void animate() {
        // Analytics.event("animateSod", this);

        if (character == null) {
            getStrokes();
        } else {
            animate(character);
        }
    }

    private static List<StrokePath> parseWsReplyStrokes(String reply) {
        if (reply == null || "".equals(reply)) {

            return null;
        }

        if (reply.startsWith(NOT_FOUND_STATUS)) {
            return null;
        }

        String[] lines = reply.split("\n");

        List<StrokePath> result = new ArrayList<StrokePath>();
        for (int i = 1; i < lines.length; i++) {
            String line = lines[i];
            if (line != null && !"".equals(line)) {
                StrokePath strokePath = StrokePath.parsePath(line.trim());
                result.add(strokePath);
            }
        }

        return result;
    }

    private static StrokedCharacter parseWsReply(String reply) {
        List<StrokePath> strokes = parseWsReplyStrokes(reply);
        if (strokes == null) {
            return null;
        }

        StrokedCharacter result = new StrokedCharacter(strokes, KANJIVG_SIZE,
                KANJIVG_SIZE);

        return result;
    }

    String getKanji() {
        return kanji;
    }

    String getStrokePathsStr() {
        return strokePathsStr;
    }

    void setStrokePathsStr(String strokePathsStr) {
        this.strokePathsStr = strokePathsStr;
    }

    void showProgressDialog(String message) {
        progressDialog = ProgressDialog.show(this, "", message, true);
    }

    void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()
                && !isFinishing()) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }
}
