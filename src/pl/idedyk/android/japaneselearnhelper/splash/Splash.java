package pl.idedyk.android.japaneselearnhelper.splash;

import pl.idedyk.android.japaneselearnhelper.JapaneseAndroidLearnHelperApplication;
import pl.idedyk.android.japaneselearnhelper.JapaneseAndroidLearnHelperMainActivity;
import pl.idedyk.android.japaneselearnhelper.R;
import pl.idedyk.android.japaneselearnhelper.config.ConfigManager;
import pl.idedyk.android.japaneselearnhelper.config.ConfigManager.SplashConfig;
import pl.idedyk.android.japaneselearnhelper.dictionary.DictionaryManager;
import pl.idedyk.android.japaneselearnhelper.dictionary.ILoadWithProgress;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
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

		final ProgressBar progressBar = (ProgressBar) findViewById(R.id.splash_progressbar);

		final TextView progressDesc = (TextView) findViewById(R.id.splash_desc_label);

		progressDesc.setText("");

		final Resources resources = getResources();
		final AssetManager assets = getAssets();

		int versionCode = 0;

		try {
			PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);

			versionCode = packageInfo.versionCode;

		} catch (NameNotFoundException e) {
		}

		final int finalVersionCode = versionCode;

		ConfigManager configManager = new ConfigManager(this);

		JapaneseAndroidLearnHelperApplication.getInstance().setConfigManager(configManager);

		// create dictionary manager
		final DictionaryManager dictionaryManager = new DictionaryManager();

		class ProgressInfo {
			Integer progressBarMaxValue;

			Integer progressBarValue;

			String description;

			String errorMessage;
		}

		class InitJapaneseAndroidLearnHelperContextAsyncTask extends AsyncTask<Void, ProgressInfo, Void> {

			class LoadWithProgress implements ILoadWithProgress {

				@Override
				public void setMaxValue(int maxValue) {
					ProgressInfo progressInfo = new ProgressInfo();

					progressInfo.progressBarMaxValue = maxValue;

					publishProgress(progressInfo);
				}

				@Override
				public void setCurrentPos(int currentPos) {
					ProgressInfo progressInfo = new ProgressInfo();

					progressInfo.progressBarValue = currentPos;

					publishProgress(progressInfo);
				}

				@Override
				public void setDescription(String desc) {
					ProgressInfo progressInfo = new ProgressInfo();

					progressInfo.description = desc;

					publishProgress(progressInfo);
				}

				@Override
				public void setError(String errorMessage) {

					ProgressInfo progressInfo = new ProgressInfo();

					progressInfo.errorMessage = errorMessage;

					publishProgress(progressInfo);
				}
			}

			private String errorMessage;

			@Override
			protected Void doInBackground(Void... params) {

				LoadWithProgress loadWithProgress = new LoadWithProgress();

				dictionaryManager.init(loadWithProgress, resources, assets, getPackageName(), finalVersionCode);

				JapaneseAndroidLearnHelperApplication.getInstance().setDictionaryManager(dictionaryManager);

				return null;
			}

			@Override
			protected void onProgressUpdate(ProgressInfo... values) {
				super.onProgressUpdate(values);

				ProgressInfo progressInfo = values[0];

				if (progressInfo.description != null) {
					progressDesc.setText(progressInfo.description);
				}

				if (progressInfo.progressBarMaxValue != null) {
					progressBar.setMax(progressInfo.progressBarMaxValue);
				}

				if (progressInfo.progressBarValue != null) {
					progressBar.setProgress(progressInfo.progressBarValue);
				}

				if (progressInfo.errorMessage != null) {
					this.errorMessage = progressInfo.errorMessage;
				}
			}

			@SuppressWarnings("deprecation")
			@Override
			protected void onPostExecute(Void result) {

				if (errorMessage != null) {

					AlertDialog alertDialog = new AlertDialog.Builder(Splash.this).create();

					alertDialog.setMessage(getString(R.string.init_dictionary_error, errorMessage));
					alertDialog.setCancelable(false);

					alertDialog.setButton(getString(R.string.ok), new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							finish();
						}
					});

					alertDialog.show();

					return;
				}

				final SplashConfig splashConfig = JapaneseAndroidLearnHelperApplication.getInstance()
						.getConfigManager(Splash.this).getSplashConfig();

				Boolean dialogBoxSkipResult = splashConfig.getDialogBoxSkip();

				if (dialogBoxSkipResult == null || dialogBoxSkipResult.booleanValue() == false) {

					AlertDialog alertDialog = new AlertDialog.Builder(Splash.this).create();

					LayoutInflater layoutInflater = LayoutInflater.from(Splash.this);

					View alertDialogView = layoutInflater.inflate(R.layout.splash_dialogbox, null);

					final TextView messageTextView = (TextView) alertDialogView
							.findViewById(R.id.splash_dialogbox_message);
					final CheckBox skipCheckBox = (CheckBox) alertDialogView
							.findViewById(R.id.splash_dialogbox_skipCheckBox);

					alertDialog.setView(alertDialogView);

					alertDialog.setCancelable(false);

					alertDialog.setTitle(getString(R.string.splash_message_box_title));
					messageTextView.setText(getString(R.string.splash_message_box_info));

					alertDialog.setButton(getString(R.string.word_test_incorrect_ok),
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog, int which) {

									splashConfig.setDialogBoxSkip(skipCheckBox.isChecked());

									Intent intent = new Intent(getApplicationContext(),
											JapaneseAndroidLearnHelperMainActivity.class);

									startActivity(intent);

									finish();
								}
							});

					alertDialog.show();

				} else {

					Intent intent = new Intent(getApplicationContext(), JapaneseAndroidLearnHelperMainActivity.class);

					startActivity(intent);

					finish();
				}
			}
		}

		InitJapaneseAndroidLearnHelperContextAsyncTask initJapaneseAndroidLearnHelperContextAsyncTask = new InitJapaneseAndroidLearnHelperContextAsyncTask();

		initJapaneseAndroidLearnHelperContextAsyncTask.execute();
	}
}
