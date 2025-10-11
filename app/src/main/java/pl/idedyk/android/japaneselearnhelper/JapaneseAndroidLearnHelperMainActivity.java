package pl.idedyk.android.japaneselearnhelper;

import java.util.ArrayList;
import java.util.List;

import pl.idedyk.android.japaneselearnhelper.config.ConfigManager;
import pl.idedyk.android.japaneselearnhelper.counters.CountersActivity;
import pl.idedyk.android.japaneselearnhelper.dictionaryhear.DictionaryHearOptions;
import pl.idedyk.android.japaneselearnhelper.dictionaryscreen.WordDictionaryTab;
import pl.idedyk.android.japaneselearnhelper.info.InfoActivity;
import pl.idedyk.android.japaneselearnhelper.kana.Kana;
import pl.idedyk.android.japaneselearnhelper.kana.KanaTestOptions;
import pl.idedyk.android.japaneselearnhelper.kanji.KanjiSearch;
import pl.idedyk.android.japaneselearnhelper.kanji.hkr.KanjiRecognizeActivity;
import pl.idedyk.android.japaneselearnhelper.kanji.hkr.KanjiTestOptionsActivity;
import pl.idedyk.android.japaneselearnhelper.keigo.KeigoTable;
import pl.idedyk.android.japaneselearnhelper.problem.ReportProblem;
import pl.idedyk.android.japaneselearnhelper.serverclient.ServerClient;
import pl.idedyk.android.japaneselearnhelper.splash.Splash;
import pl.idedyk.android.japaneselearnhelper.test.WordTestOptions;
import pl.idedyk.android.japaneselearnhelper.testsm2.WordTestSM2Options;
import pl.idedyk.android.japaneselearnhelper.transitiveintransitive.TransitiveIntransitivePairsTable;
import pl.idedyk.android.japaneselearnhelper.usergroup.UserGroupActivity;
import pl.idedyk.japanese.dictionary.api.android.queue.event.StatEndAppEvent;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class JapaneseAndroidLearnHelperMainActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);

		JapaneseAndroidLearnHelperApplication.getInstance().setContentViewAndTheme(this, R.id.rootView, R.layout.main);
		
		JapaneseAndroidLearnHelperApplication.getInstance().logScreen(this, getString(R.string.logs_main_menu));

		// create menu
		createMenuList();

		// init menu actions
		initMenuActions();

		// check and show message for user
		checkAndShowMessageForUser();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onBackPressed() {

		super.onBackPressed();

		JapaneseAndroidLearnHelperApplication.getInstance().addQueueEvent(this, new StatEndAppEvent(
				JapaneseAndroidLearnHelperApplication.getInstance().getConfigManager(this).getCommonConfig().getOrGenerateUniqueUserId()
		));
	}

	private void createMenuList() {
		ListView mainMenuListView = (ListView)findViewById(R.id.mainMenuList);

		List<MainMenuItem> mainMenuListItems = new ArrayList<MainMenuItem>();

		mainMenuListItems.add(new MainMenuItem(
				getString(R.string.main_menu_kana_kanji),
				getString(R.string.main_menu_kana_text)));

		mainMenuListItems.add(new MainMenuItem(
				getString(R.string.main_menu_kana_test_kanji),
				getString(R.string.main_menu_kana_test_text)));    	

		mainMenuListItems.add(new MainMenuItem(
				getString(R.string.main_menu_counters_kanji),
				getString(R.string.main_menu_counters_text)));    	

		mainMenuListItems.add(new MainMenuItem(
				getString(R.string.main_menu_keigo_table_kanji),
				getString(R.string.main_menu_keigo_table_text)));

		mainMenuListItems.add(new MainMenuItem(
				getString(R.string.main_menu_transitive_intransitive_pairs_table_kanji),
				getString(R.string.main_menu_transitive_intransitive_pairs_table_text)));
		
		mainMenuListItems.add(new MainMenuItem(
				getString(R.string.main_menu_dictionary_kanji),
				getString(R.string.main_menu_dictionary_text)));

		if (android.os.Build.VERSION.SDK_INT >= 14) { // if Android 4+
			mainMenuListItems.add(new MainMenuItem(
					getString(R.string.main_menu_dictionary_hear_kanji),
					getString(R.string.main_menu_dictionary_hear_text)));
		}
		
    	mainMenuListItems.add(new MainMenuItem(
    			getString(R.string.main_menu_word_test_kanji),
    			getString(R.string.main_menu_word_test_text)));
    	
    	mainMenuListItems.add(new MainMenuItem(
    			getString(R.string.main_menu_word_test_sm2_kanji),
    			getString(R.string.main_menu_word_test_sm2_text)));

		mainMenuListItems.add(new MainMenuItem(
				getString(R.string.main_menu_kanji_kanji),
				getString(R.string.main_menu_kanji_text)));

		/*
		mainMenuListItems.add(new MainMenuItem(
				getString(R.string.main_menu_kanji_recognizer_kanji),
				getString(R.string.main_menu_kanji_recognizer_text)));
		*/

		mainMenuListItems.add(new MainMenuItem(
				getString(R.string.main_menu_kanji_test_kanji),
				getString(R.string.main_menu_kanji_test_text)));

		mainMenuListItems.add(new MainMenuItem(
				getString(R.string.main_menu_show_user_group_kana),  15.0f,
				getString(R.string.main_menu_show_user_group_text)));

		mainMenuListItems.add(new MainMenuItem(
				getString(R.string.main_menu_black_white_switcher_kanji),
				getString(R.string.main_menu_black_white_switcher_text)));

		mainMenuListItems.add(new MainMenuItem(
				getString(R.string.main_menu_suggestion_kanji),
				getString(R.string.main_menu_suggestion_text)));

		mainMenuListItems.add(new MainMenuItem(
				getString(R.string.main_menu_information_kanji),
				getString(R.string.main_menu_information_text)));

		MainMenuListItemAdapter mainMenuListItemsAdapter = new MainMenuListItemAdapter(this, R.layout.main_menu_simplerow, mainMenuListItems);

		mainMenuListView.setAdapter(mainMenuListItemsAdapter);
	}

	private void initMenuActions() {

		final ListView mainMenuListView = (ListView)findViewById(R.id.mainMenuList);

		mainMenuListView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				
				MainMenuItem mainMenuChosenItem = (MainMenuItem)mainMenuListView.getAdapter().getItem(position);
				
				String mainMenuChosenItemText = mainMenuChosenItem.getText();
				
				if (mainMenuChosenItemText.equals(getString(R.string.main_menu_kana_text)) == true) { // kana selected
					
					Intent intent = new Intent(getApplicationContext(), Kana.class);

					startActivity(intent);
				} else if (mainMenuChosenItemText.equals(getString(R.string.main_menu_kana_test_text)) == true) { // kana test selected
					
					Intent intent = new Intent(getApplicationContext(), KanaTestOptions.class);

					startActivity(intent);
					
				} else if (mainMenuChosenItemText.equals(getString(R.string.main_menu_counters_text)) == true) { // counter
					
					Intent intent = new Intent(getApplicationContext(), CountersActivity.class);

					startActivity(intent);
					
				} else if (mainMenuChosenItemText.equals(getString(R.string.main_menu_keigo_table_text)) == true) { // keigo table
					
					Intent intent = new Intent(getApplicationContext(), KeigoTable.class);

					startActivity(intent);
					
				} else if (mainMenuChosenItemText.equals(getString(R.string.main_menu_transitive_intransitive_pairs_table_text)) == true) { // transitive intransitive pairs table
					
					Intent intent = new Intent(getApplicationContext(), TransitiveIntransitivePairsTable.class);

					startActivity(intent);
					
				} else if (mainMenuChosenItemText.equals(getString(R.string.main_menu_dictionary_text)) == true) { // dictionary selected
					
					Intent intent = new Intent(getApplicationContext(), WordDictionaryTab.class);

					startActivity(intent);
					
				} else if (mainMenuChosenItemText.equals(getString(R.string.main_menu_dictionary_hear_text)) == true) { // dictionary hear

					Intent intent = new Intent(getApplicationContext(), DictionaryHearOptions.class);

					startActivity(intent);					
					
				} else if (mainMenuChosenItemText.equals(getString(R.string.main_menu_word_test_text)) == true) { // word test selected
					
					Intent intent = new Intent(getApplicationContext(), WordTestOptions.class);

					startActivity(intent);
					
				} else if (mainMenuChosenItemText.equals(getString(R.string.main_menu_word_test_sm2_text)) == true) { // word sm2 test selected

					Intent intent = new Intent(getApplicationContext(), WordTestSM2Options.class);

					startActivity(intent);					
					
				} else if (mainMenuChosenItemText.equals(getString(R.string.main_menu_kanji_text)) == true) { // kanji search
					
					Intent intent = new Intent(getApplicationContext(), KanjiSearch.class);

					startActivity(intent);
					
				/*} else if (mainMenuChosenItemText.equals(getString(R.string.main_menu_kanji_recognizer_text)) == true) { // kanji recognizer

					Intent intent = new Intent(getApplicationContext(), KanjiRecognizeActivity.class);

					startActivity(intent);					
				*/
				} else if (mainMenuChosenItemText.equals(getString(R.string.main_menu_kanji_test_text)) == true) { // kanji test

					Intent intent = new Intent(getApplicationContext(), KanjiTestOptionsActivity.class);

					startActivity(intent);					

				} else if (mainMenuChosenItemText.equals(getString(R.string.main_menu_show_user_group_text)) == true) { // grupy użytkownika

					Intent intent = new Intent(getApplicationContext(), UserGroupActivity.class);

					startActivity(intent);

				} else if (mainMenuChosenItemText.equals(getString(R.string.main_menu_suggestion_text)) == true) { // suggestion
					String chooseEmailClientTitle = getString(R.string.choose_email_client);

					String mailSubject = getString(R.string.main_menu_email_subject);

					String mailBody = getString(R.string.main_menu_email_body);

					String versionName = "";
					int versionCode = 0;

					try {
						PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);

						versionName = packageInfo.versionName;
						versionCode = packageInfo.versionCode;

					} catch (NameNotFoundException e) {        	
					}

					Intent reportSuggestionIntent = ReportProblem.createReportProblemIntent(mailSubject, mailBody, versionName, versionCode); 

					startActivity(Intent.createChooser(reportSuggestionIntent, chooseEmailClientTitle));

				} else if (mainMenuChosenItemText.equals(getString(R.string.main_menu_information_text)) == true) { // info
					
					Intent intent = new Intent(getApplicationContext(), InfoActivity.class);

					startActivity(intent);

				} else if (mainMenuChosenItemText.equals(getString(R.string.main_menu_black_white_switcher_text)) == true) { // przelaczenie aplikacji

					final ConfigManager.CommonConfig commonConfig = JapaneseAndroidLearnHelperApplication.getInstance()
							.getConfigManager(JapaneseAndroidLearnHelperMainActivity.this).getCommonConfig();

					JapaneseAndroidLearnHelperApplication.ThemeType themeType = commonConfig.getThemeType(JapaneseAndroidLearnHelperApplication.defaultThemeType);

					if (themeType == JapaneseAndroidLearnHelperApplication.ThemeType.BLACK) {
						themeType = JapaneseAndroidLearnHelperApplication.ThemeType.WHITE;

					} else {
						themeType = JapaneseAndroidLearnHelperApplication.ThemeType.BLACK;
					}

					commonConfig.setThemeType(themeType);

					// logowanie
					JapaneseAndroidLearnHelperApplication.getInstance().logEvent(JapaneseAndroidLearnHelperMainActivity.this, getString(R.string.logs_main_menu), getString(R.string.logs_main_menu_black_white_switcher_event), themeType.name());

					// wyswietlenie informacji uzytkownikowi o koniecznosci wylaczenia aplikacji
					AlertDialog.Builder builder = new AlertDialog.Builder(JapaneseAndroidLearnHelperMainActivity.this);

					builder.setCancelable(false);

					DialogInterface.OnClickListener positiveButtonOnClickListener = new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialogInterface, int i) {
							finish();
						}
					};

					builder.setMessage(getString(R.string.main_menu_black_white_switcher_alert_dialog))
							.setPositiveButton(getString(R.string.ok), positiveButtonOnClickListener).show();
				}
			}
		});
	}

	public void checkAndShowMessageForUser() {

		class GetMessageAsyncTask extends AsyncTask<Void, Void, ServerClient.GetMessageResult> {

			@Override
			protected ServerClient.GetMessageResult doInBackground(Void... params) {

				PackageInfo packageInfo = null;

				try {
					packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);

				} catch (NameNotFoundException e) {
				}

				// pobieramy komunikat
				ServerClient serverClient = new ServerClient();

				return serverClient.getMessage(packageInfo);
			}

			@Override
			protected void onPostExecute(final ServerClient.GetMessageResult getMessageResult) {

				// nic nie robimy
				if (getMessageResult == null) {
					return;
				}

				// sprawdzamy, czy nalezy wyswietlic komunikat
				final ConfigManager.CommonConfig commonConfig = JapaneseAndroidLearnHelperApplication.getInstance()
						.getConfigManager(JapaneseAndroidLearnHelperMainActivity.this).getCommonConfig();

				String messageLastTimestamp = commonConfig.getMessageLastTimestamp();

				// wyswietlamy, nowy komunikat
				if (messageLastTimestamp == null || getMessageResult.timestamp.equals(messageLastTimestamp) == false) {

					// wyswietlamy okienko
					AlertDialog alertDialog = new AlertDialog.Builder(JapaneseAndroidLearnHelperMainActivity.this).create();

					LayoutInflater layoutInflater = LayoutInflater.from(JapaneseAndroidLearnHelperMainActivity.this);

					View alertDialogView = layoutInflater.inflate(R.layout.main_menu_message_for_user, null);

					TextView alertDialogMessage = (TextView) alertDialogView.findViewById(R.id.main_menu_message_for_user_message);

					alertDialogMessage.setText(getMessageResult.message);

					alertDialog.setView(alertDialogView);
					alertDialog.setCancelable(false);

					alertDialog.setButton(getString(R.string.ok),
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog, int which) {
									commonConfig.setMessageLastTimestamp(getMessageResult.timestamp);
								}
							});

					if (isFinishing() == false) {
						alertDialog.show();
					}
				}
			}
		}

		new GetMessageAsyncTask().execute();
	}
}