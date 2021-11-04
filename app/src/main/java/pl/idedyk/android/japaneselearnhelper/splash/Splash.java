package pl.idedyk.android.japaneselearnhelper.splash;

import pl.idedyk.android.japaneselearnhelper.JapaneseAndroidLearnHelperApplication;
import pl.idedyk.android.japaneselearnhelper.JapaneseAndroidLearnHelperMainActivity;
import pl.idedyk.android.japaneselearnhelper.R;
import pl.idedyk.android.japaneselearnhelper.config.ConfigManager;
import pl.idedyk.android.japaneselearnhelper.config.ConfigManager.SplashConfig;
import pl.idedyk.android.japaneselearnhelper.dictionary.DictionaryManagerCommon;
import pl.idedyk.android.japaneselearnhelper.dictionary.ILoadWithProgress;
import pl.idedyk.japanese.dictionary.api.android.queue.event.StatStartAppEvent;

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
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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
		// JapaneseAndroidLearnHelperApplication.getInstance().getTracker();

		requestWindowFeature(Window.FEATURE_NO_TITLE);

		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);

		JapaneseAndroidLearnHelperApplication.getInstance().setContentViewAndTheme(this, R.layout.splash);

		final TextView progressDesc = (TextView) findViewById(R.id.splash_desc_label);

		progressDesc.setText("");

		ConfigManager configManager = new ConfigManager(this);

		JapaneseAndroidLearnHelperApplication.getInstance().setConfigManager(configManager);

		// poproszenie o uprawnienie

		progressDesc.setText(getString(R.string.splash_check_permission));

		// sprawdzamy, czy mam nadane juz uprawnienie dostepu do pamieci urzadzenia
		int hasStoragePermission = ContextCompat.checkSelfPermission(this,
				Manifest.permission.WRITE_EXTERNAL_STORAGE);

		if (hasStoragePermission != PackageManager.PERMISSION_GRANTED) {

			// metoda shouldShowRequestPermissionRationale zwraca false, gdy uruchamiamy aplikacje pierwszy raz lub uzytkownik zablokowal pokazywanie prosb o uprawnienie
			//if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == false) {

			AlertDialog alertDialog = new AlertDialog.Builder(Splash.this).create();

			alertDialog.setCancelable(false);

			alertDialog.setTitle(getString(R.string.splash_permission_request_message_box_title));
			alertDialog.setMessage(getString(R.string.splash_permission_request_message_box_message));

			alertDialog.setButton(getString(R.string.splash_ok),
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {

							ActivityCompat.requestPermissions(Splash.this,
									new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE},
									REQUEST_CODE_ASK_PERMISSIONS);
						}
					});

			if (isFinishing() == false) {
				alertDialog.show();
			}

			return;

		} else { // mamy juz uprawnienie
			doInit();
		}
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
		final DictionaryManagerCommon dictionaryManager = DictionaryManagerCommon.getDictionaryManager();

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

				dictionaryManager.init(Splash.this, loadWithProgress, resources, assets, getPackageName(), finalVersionCode);

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

					alertDialog.setButton(getString(R.string.splash_ok),
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog, int which) {

									splashConfig.setDialogBoxSkip(skipCheckBox.isChecked());

									Intent intent = new Intent(getApplicationContext(),
											JapaneseAndroidLearnHelperMainActivity.class);

									startActivity(intent);

									finish();

									// start queue thread
									JapaneseAndroidLearnHelperApplication.getInstance().startQueueThread(Splash.this);

									// send start event
									JapaneseAndroidLearnHelperApplication.getInstance().addQueueEvent(Splash.this, new StatStartAppEvent(
                                            JapaneseAndroidLearnHelperApplication.getInstance().getConfigManager(Splash.this).getCommonConfig().getOrGenerateUniqueUserId()));
								}
							});

					if (isFinishing() == false) {
						alertDialog.show();
					}

				} else {

					Intent intent = new Intent(getApplicationContext(), JapaneseAndroidLearnHelperMainActivity.class);

					startActivity(intent);

					finish();

					// start queue thread
					JapaneseAndroidLearnHelperApplication.getInstance().startQueueThread(Splash.this);

					// send start event
					JapaneseAndroidLearnHelperApplication.getInstance().addQueueEvent(Splash.this, new StatStartAppEvent(
                            JapaneseAndroidLearnHelperApplication.getInstance().getConfigManager(Splash.this).getCommonConfig().getOrGenerateUniqueUserId()));
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

                    AlertDialog alertDialog = new AlertDialog.Builder(Splash.this).create();

                    alertDialog.setCancelable(false);

                    alertDialog.setMessage(getString(R.string.splash_permission_request_denied_message_box_message));

                    alertDialog.setButton(getString(R.string.splash_ok),
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            });

                    if (isFinishing() == false) {
                        alertDialog.show();
                    }
				}
				break;
			default:
				super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		}
	}
}
