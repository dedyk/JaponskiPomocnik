package pl.idedyk.android.japaneselearnhelper.splash;

import pl.idedyk.android.japaneselearnhelper.JapaneseAndroidLearnHelperApplication;
import pl.idedyk.android.japaneselearnhelper.JapaneseAndroidLearnHelperMainActivity;
import pl.idedyk.android.japaneselearnhelper.R;
import pl.idedyk.android.japaneselearnhelper.config.ConfigManager;
import pl.idedyk.android.japaneselearnhelper.config.ConfigManager.SplashConfig;
import pl.idedyk.android.japaneselearnhelper.dictionary.DictionaryManager;
import pl.idedyk.android.japaneselearnhelper.dictionary.ILoadWithProgress;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class Splash extends Activity implements ActivityCompat.OnRequestPermissionsResultCallback {

	private static final int REQUEST_CODE_ASK_PERMISSIONS = 666;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		// init google analytics
		JapaneseAndroidLearnHelperApplication.getInstance().getTracker();

		requestWindowFeature(Window.FEATURE_NO_TITLE);

		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);

		setContentView(R.layout.splash);

		final TextView progressDesc = (TextView) findViewById(R.id.splash_desc_label);

		progressDesc.setText("");

		ConfigManager configManager = new ConfigManager(this);

		JapaneseAndroidLearnHelperApplication.getInstance().setConfigManager(configManager);

		// poproszenie o uprawnienie

		// sprawdzamy, czy mam nadane juz uprawnienie dostepu do pamieci urzadzenia
		int hasStoragePermission = ContextCompat.checkSelfPermission(this,
				Manifest.permission.WRITE_EXTERNAL_STORAGE);

		if (hasStoragePermission != PackageManager.PERMISSION_GRANTED) {

			// metoda shouldShowRequestPermissionRationale zwraca false, gdy uruchamiamy aplikacje pierwszy raz lub uzytkownik zablokowal pokazywanie prosb o uprawnienie
			if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == false) {

				// testy !!!!!!!!!!!!!!!!!
				showMessageOKCancel("TEST !!!! You need to allow access to Storage",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								ActivityCompat.requestPermissions(Splash.this,
										new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE},
										REQUEST_CODE_ASK_PERMISSIONS);
							}
						});
				return;
			}

			ActivityCompat.requestPermissions(this,
					new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE},
					REQUEST_CODE_ASK_PERMISSIONS);
			return;

		} else { // mamy juz uprawnienie

			int fixme = 1; // to jest ok !!!

			// uruchamiamy dalej aplikacje
			// doInit();
		}

	}

	private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {

		// testy !!!!!!!!!!!!

		new AlertDialog.Builder(this)
				.setMessage(message)
				.setPositiveButton("OK", okListener)
				.setNegativeButton("Cancel", null)
				.create()
				.show();
	}

	private void doInit() {

		final ProgressBar progressBar = (ProgressBar) findViewById(R.id.splash_progressbar);

		final TextView progressDesc = (TextView) findViewById(R.id.splash_desc_label);

		int versionCode = 0;

		try {
			PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);

			versionCode = packageInfo.versionCode;

		} catch (NameNotFoundException e) {
		}

		final int finalVersionCode = versionCode;

		final Resources resources = getResources();
		final AssetManager assets = getAssets();

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

					if (isFinishing() == false) {
						alertDialog.show();
					}

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

					if (isFinishing() == false) {
						alertDialog.show();
					}

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

	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

		switch (requestCode) {
			case REQUEST_CODE_ASK_PERMISSIONS:

				if (grantResults[0] == PackageManager.PERMISSION_GRANTED) { // dostalismy uprawnienie, inicjalizacja

					doInit();

				} else {

					// nie dostalismy uprawnienia

					// komunikat na ekranie

					Toast.makeText(Splash.this, "FM TEST !!!!!! WRITE_CONTACTS Denied", Toast.LENGTH_SHORT)
							.show();
				}
				break;
			default:
				super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		}
	}
}
