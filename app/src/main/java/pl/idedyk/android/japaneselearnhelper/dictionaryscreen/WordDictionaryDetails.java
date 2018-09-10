package pl.idedyk.android.japaneselearnhelper.dictionaryscreen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Stack;

import pl.idedyk.android.japaneselearnhelper.JapaneseAndroidLearnHelperApplication;
import pl.idedyk.android.japaneselearnhelper.MenuShorterHelper;
import pl.idedyk.android.japaneselearnhelper.R;
import pl.idedyk.android.japaneselearnhelper.data.DataManager;
import pl.idedyk.android.japaneselearnhelper.data.entity.UserGroupEntity;
import pl.idedyk.android.japaneselearnhelper.data.entity.UserGroupItemEntity;
import pl.idedyk.android.japaneselearnhelper.data.exception.DataManagerException;
import pl.idedyk.android.japaneselearnhelper.dictionary.DictionaryManagerCommon;
import pl.idedyk.android.japaneselearnhelper.kanji.KanjiDetails;
import pl.idedyk.android.japaneselearnhelper.kanji.hkr.KanjiTestOptionsActivity;
import pl.idedyk.android.japaneselearnhelper.problem.ReportProblem;
import pl.idedyk.android.japaneselearnhelper.screen.IScreenItem;
import pl.idedyk.android.japaneselearnhelper.screen.Image;
import pl.idedyk.android.japaneselearnhelper.screen.StringValue;
import pl.idedyk.android.japaneselearnhelper.screen.TableLayout;
import pl.idedyk.android.japaneselearnhelper.screen.TableRow;
import pl.idedyk.android.japaneselearnhelper.screen.TitleItem;
import pl.idedyk.android.japaneselearnhelper.sod.SodActivity;
import pl.idedyk.android.japaneselearnhelper.sod.dto.StrokePathInfo;
import pl.idedyk.android.japaneselearnhelper.tts.TtsConnector;
import pl.idedyk.android.japaneselearnhelper.tts.TtsLanguage;
import pl.idedyk.android.japaneselearnhelper.usergroup.UserGroupActivity;
import pl.idedyk.japanese.dictionary.api.dto.Attribute;
import pl.idedyk.japanese.dictionary.api.dto.AttributeType;
import pl.idedyk.japanese.dictionary.api.dto.DictionaryEntry;
import pl.idedyk.japanese.dictionary.api.dto.DictionaryEntryType;
import pl.idedyk.japanese.dictionary.api.dto.FuriganaEntry;
import pl.idedyk.japanese.dictionary.api.dto.GroupEnum;
import pl.idedyk.japanese.dictionary.api.dto.GroupWithTatoebaSentenceList;
import pl.idedyk.japanese.dictionary.api.dto.KanjiEntry;
import pl.idedyk.japanese.dictionary.api.dto.KanjivgEntry;
import pl.idedyk.japanese.dictionary.api.dto.TatoebaSentence;
import pl.idedyk.japanese.dictionary.api.example.ExampleManager;
import pl.idedyk.japanese.dictionary.api.example.dto.ExampleGroupTypeElements;
import pl.idedyk.japanese.dictionary.api.example.dto.ExampleResult;
import pl.idedyk.japanese.dictionary.api.exception.DictionaryException;
import pl.idedyk.japanese.dictionary.api.gramma.GrammaConjugaterManager;
import pl.idedyk.japanese.dictionary.api.gramma.dto.GrammaFormConjugateGroupTypeElements;
import pl.idedyk.japanese.dictionary.api.gramma.dto.GrammaFormConjugateResult;
import pl.idedyk.japanese.dictionary.api.gramma.dto.GrammaFormConjugateResultType;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.ClipboardManager;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

@SuppressWarnings("deprecation")
public class WordDictionaryDetails extends Activity {

	private List<IScreenItem> generatedDetails;

	private TtsConnector ttsConnector;

	private final Stack<Integer> backScreenPositionStack = new Stack<Integer>();

	private List<IScreenItem> searchScreenItemList = null;

	private Integer searchScreenItemCurrentPos = null;

	private DictionaryEntry dictionaryEntry = null;
	private DictionaryEntryType forceDictionaryEntryType = null;

	//

	private final static int ADD_ITEM_ID_TO_USER_GROUP_ACTIVITY_REQUEST_CODE = 1;

	@Override
	protected void onDestroy() {

		if (ttsConnector != null) {
			ttsConnector.stop();
		}

		super.onDestroy();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onRestart() {
		super.onRestart();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);

		menu.add(Menu.NONE, R.id.word_dictionary_details_menu_search, Menu.NONE, R.string.word_dictionary_details_menu_search);
		menu.add(Menu.NONE, R.id.word_dictionary_details_menu_search_next, Menu.NONE,
				R.string.word_dictionary_details_menu_search_next);

		MenuShorterHelper.onCreateOptionsMenu(menu);

		if (dictionaryEntry.isName() == false) {
			menu.add(Menu.NONE, R.id.word_dictionary_details_menu_add_item_id_to_user_group, Menu.NONE, R.string.word_dictionary_details_menu_add_item_id_to_user_group);
		}

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);

		if (item.getItemId() == R.id.word_dictionary_details_menu_search) {

			AlertDialog.Builder alert = new AlertDialog.Builder(this);

			alert.setTitle(getString(R.string.word_dictionary_details_search_title));

			final EditText input = new EditText(this);

			input.setSingleLine(true);

			alert.setView(input);

			alert.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int whichButton) {

					String userInputSearchText = input.getText().toString().toLowerCase(Locale.getDefault());

					searchScreenItemList = new ArrayList<IScreenItem>();
					searchScreenItemCurrentPos = 0;

					TitleItem lastTitleItem = null;

					for (IScreenItem currentScreenItem : generatedDetails) {

						if (currentScreenItem instanceof TitleItem) {
							lastTitleItem = (TitleItem) currentScreenItem;
						}

						String currentScreenItemToString = currentScreenItem.toString();

						if (currentScreenItemToString.toLowerCase(Locale.getDefault()).indexOf(userInputSearchText) != -1) {

							if (lastTitleItem != null) {

								if (searchScreenItemList.contains(lastTitleItem) == false) {
									searchScreenItemList.add(lastTitleItem);
								}

							} else {

								if (searchScreenItemList.contains(currentScreenItem) == false) {
									searchScreenItemList.add(currentScreenItem);
								}
							}
						}
					}

					if (searchScreenItemList.size() == 0) {
						Toast.makeText(WordDictionaryDetails.this,
								getString(R.string.word_dictionary_details_search_no_result), Toast.LENGTH_SHORT)
								.show();

						return;
					}

					final ScrollView scrollMainLayout = (ScrollView) findViewById(R.id.word_dictionary_details_main_layout_scroll);

					backScreenPositionStack.push(scrollMainLayout.getScrollY());

					int counterPos = searchScreenItemList.get(searchScreenItemCurrentPos).getY();
					scrollMainLayout.scrollTo(0, counterPos - 3);
				}
			});

			alert.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int whichButton) {
					// noop
				}
			});

			alert.show();

			return true;

		} else if (item.getItemId() == R.id.word_dictionary_details_menu_search_next) {

			if (searchScreenItemList == null || searchScreenItemList.size() == 0) {
				Toast.makeText(WordDictionaryDetails.this,
						getString(R.string.word_dictionary_details_search_no_result), Toast.LENGTH_SHORT).show();

				return false;
			}

			searchScreenItemCurrentPos = searchScreenItemCurrentPos + 1;

			if (searchScreenItemCurrentPos >= searchScreenItemList.size()) {
				Toast.makeText(WordDictionaryDetails.this,
						getString(R.string.word_dictionary_details_search_no_more_result), Toast.LENGTH_SHORT).show();

				return false;
			}

			final ScrollView scrollMainLayout = (ScrollView) findViewById(R.id.word_dictionary_details_main_layout_scroll);

			backScreenPositionStack.push(scrollMainLayout.getScrollY());

			int counterPos = searchScreenItemList.get(searchScreenItemCurrentPos).getY();
			scrollMainLayout.scrollTo(0, counterPos - 3);

			return true;

		} else if (item.getItemId() == R.id.word_dictionary_details_menu_add_item_id_to_user_group) {

			Intent intent = new Intent(getApplicationContext(), UserGroupActivity.class);

			intent.putExtra("itemToAdd", dictionaryEntry);

			startActivityForResult(intent, ADD_ITEM_ID_TO_USER_GROUP_ACTIVITY_REQUEST_CODE);

			return true;

		} else {

			return MenuShorterHelper.onOptionsItemSelected(item, getApplicationContext(), this);
		}
	}

	@Override
	public void onBackPressed() {

		if (backScreenPositionStack.isEmpty() == true) {
			super.onBackPressed();

			return;
		}

		Integer backPostion = backScreenPositionStack.pop();

		final ScrollView scrollMainLayout = (ScrollView) findViewById(R.id.word_dictionary_details_main_layout_scroll);

		scrollMainLayout.scrollTo(0, backPostion);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		
		JapaneseAndroidLearnHelperApplication.getInstance().logScreen(getString(R.string.logs_word_dictionary_details));

		setContentView(R.layout.word_dictionary_details);

		dictionaryEntry = (DictionaryEntry) getIntent().getSerializableExtra("item");
		forceDictionaryEntryType = (DictionaryEntryType) getIntent().getSerializableExtra(
				"forceDictionaryEntryType");

		final ScrollView scrollMainLayout = (ScrollView) findViewById(R.id.word_dictionary_details_main_layout_scroll);
		final LinearLayout detailsMainLayout = (LinearLayout) findViewById(R.id.word_dictionary_details_main_layout);

		generatedDetails = generateDetails(dictionaryEntry, forceDictionaryEntryType, scrollMainLayout);

		fillDetailsMainLayout(generatedDetails, detailsMainLayout);

		Button reportProblemButton = (Button) findViewById(R.id.word_dictionary_details_report_problem_button);

		reportProblemButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				StringBuffer detailsSb = new StringBuffer();

				for (IScreenItem currentGeneratedDetails : generatedDetails) {
					detailsSb.append(currentGeneratedDetails.toString()).append("\n\n");
				}

				String chooseEmailClientTitle = getString(R.string.choose_email_client);

				String mailSubject = getString(R.string.word_dictionary_details_report_problem_email_subject);

				String mailBody = getString(R.string.word_dictionary_details_report_problem_email_body,
						detailsSb.toString());

				String versionName = "";
				int versionCode = 0;

				try {
					PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);

					versionName = packageInfo.versionName;
					versionCode = packageInfo.versionCode;

				} catch (NameNotFoundException e) {
				}

				Intent reportProblemIntent = ReportProblem.createReportProblemIntent(mailSubject, mailBody.toString(),
						versionName, versionCode);

				startActivity(Intent.createChooser(reportProblemIntent, chooseEmailClientTitle));
			}
		});

		if (ttsConnector != null) {
			ttsConnector.stop();
		}

		ttsConnector = new TtsConnector(this, TtsLanguage.JAPANESE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == ADD_ITEM_ID_TO_USER_GROUP_ACTIVITY_REQUEST_CODE) {

			final ScrollView scrollMainLayout = (ScrollView) findViewById(R.id.word_dictionary_details_main_layout_scroll);
			final LinearLayout detailsMainLayout = (LinearLayout) findViewById(R.id.word_dictionary_details_main_layout);

			generatedDetails = generateDetails(dictionaryEntry, forceDictionaryEntryType, scrollMainLayout);

			fillDetailsMainLayout(generatedDetails, detailsMainLayout);
		}
	}

	private List<IScreenItem> generateDetails(final DictionaryEntry dictionaryEntry,
											  DictionaryEntryType forceDictionaryEntryType, final ScrollView scrollMainLayout) {

		List<IScreenItem> report = new ArrayList<IScreenItem>();

		DictionaryManagerCommon dictionaryManager = JapaneseAndroidLearnHelperApplication.getInstance().getDictionaryManager(this);

		String prefixKana = dictionaryEntry.getPrefixKana();
		String prefixRomaji = dictionaryEntry.getPrefixRomaji();

		if (prefixKana != null && prefixKana.length() == 0) {
			prefixKana = null;
		}

		// info dla slow typu name
		if (dictionaryEntry.isName() == true) {

			StringValue dictionaryEntryInfoName = new StringValue(getString(R.string.word_dictionary_details_name_info), 12.0f, 0);

			report.add(dictionaryEntryInfoName);
			report.add(new StringValue("", 15.0f, 2));
		}

		// Kanji		
		report.add(new TitleItem(getString(R.string.word_dictionary_details_kanji_label), 0));

		final StringBuffer kanjiSb = new StringBuffer();

		boolean addKanjiWrite = false;

		if (dictionaryEntry.isKanjiExists() == true) {
			if (prefixKana != null) {
				kanjiSb.append("(").append(prefixKana).append(") ");
			}

			kanjiSb.append(dictionaryEntry.getKanji());

			addKanjiWrite = true;
		} else {
			kanjiSb.append("-");

			addKanjiWrite = false;
		}

		// kanji draw on click listener
		OnClickListener kanjiDrawOnClickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				List<KanjivgEntry> strokePathsForWord = JapaneseAndroidLearnHelperApplication.getInstance()
						.getDictionaryManager(WordDictionaryDetails.this).getStrokePathsForWord(kanjiSb.toString());

				StrokePathInfo strokePathInfo = new StrokePathInfo();

				strokePathInfo.setStrokePaths(strokePathsForWord);

				Intent intent = new Intent(getApplicationContext(), SodActivity.class);

				intent.putExtra("strokePathsInfo", strokePathInfo);
				intent.putExtra("annotateStrokes", false);

				startActivity(intent);
			}
		};

		List<String> kanaList = dictionaryEntry.getKanaList();

		boolean isAddFavouriteWordStar = false;

		// check furigana
		List<FuriganaEntry> furiganaEntries = null;

		try {
			furiganaEntries = dictionaryManager.getFurigana(dictionaryEntry);

		} catch (DictionaryException e) {
			Toast.makeText(this, getString(R.string.dictionary_exception_common_error_message, e.getMessage()), Toast.LENGTH_LONG).show();
		}

		if (furiganaEntries != null && furiganaEntries.size() > 0 && addKanjiWrite == true) {

			report.add(new StringValue(getString(R.string.word_dictionary_word_anim), 12.0f, 0));

			for (FuriganaEntry currentFuriganaEntry : furiganaEntries) {

				TableLayout furiganaTableLayout = new TableLayout(TableLayout.LayoutParam.WrapContent_WrapContent,
						true, null);

				final int maxPartsInLine = 7;

				List<String> furiganaKanaParts = currentFuriganaEntry.getKanaPart();
				List<String> furiganaKanjiParts = currentFuriganaEntry.getKanjiPart();

				int maxParts = furiganaKanaParts.size() / maxPartsInLine
						+ (furiganaKanaParts.size() % maxPartsInLine > 0 ? 1 : 0);

				for (int currentPart = 0; currentPart < maxParts; ++currentPart) {

					TableRow readingRow = new TableRow();

					StringValue spacer = new StringValue("", 15.0f, 0);
					spacer.setGravity(Gravity.CENTER);
					spacer.setNullMargins(true);

					readingRow.addScreenItem(spacer);

					for (int furiganaKanaPartsIdx = currentPart * maxPartsInLine; furiganaKanaPartsIdx < furiganaKanaParts
							.size() && furiganaKanaPartsIdx < (currentPart + 1) * maxPartsInLine; ++furiganaKanaPartsIdx) {

						StringValue currentKanaPartStringValue = new StringValue(
								furiganaKanaParts.get(furiganaKanaPartsIdx), 15.0f, 0);

						currentKanaPartStringValue.setGravity(Gravity.CENTER);
						currentKanaPartStringValue.setNullMargins(true);

						currentKanaPartStringValue.setOnClickListener(kanjiDrawOnClickListener);

						readingRow.addScreenItem(currentKanaPartStringValue);
					}

					furiganaTableLayout.addTableRow(readingRow);

					TableRow kanjiRow = new TableRow();

					StringValue spacer2 = new StringValue("  ", 25.0f, 0);
					spacer2.setGravity(Gravity.CENTER);
					spacer2.setNullMargins(true);

					kanjiRow.addScreenItem(spacer2);

					for (int furiganaKanjiPartsIdx = currentPart * maxPartsInLine; furiganaKanjiPartsIdx < furiganaKanjiParts
							.size() && furiganaKanjiPartsIdx < (currentPart + 1) * maxPartsInLine; ++furiganaKanjiPartsIdx) {
						StringValue currentKanjiPartStringValue = new StringValue(
								furiganaKanjiParts.get(furiganaKanjiPartsIdx), 35.0f, 0);

						currentKanjiPartStringValue.setGravity(Gravity.CENTER);
						currentKanjiPartStringValue.setNullMargins(true);

						currentKanjiPartStringValue.setOnClickListener(kanjiDrawOnClickListener);

						kanjiRow.addScreenItem(currentKanjiPartStringValue);
					}

					furiganaTableLayout.addTableRow(kanjiRow);
				}

				report.add(furiganaTableLayout);

				TableLayout actionButtons = new TableLayout(TableLayout.LayoutParam.WrapContent_WrapContent, true, null);
				TableRow actionTableRow = new TableRow();

				// speak image
				Image speakImage = new Image(getResources().getDrawable(android.R.drawable.ic_lock_silent_mode_off), 0);
				speakImage.setOnClickListener(new TTSJapaneseSpeak(null, currentFuriganaEntry.getKanaPartJoined()));
				actionTableRow.addScreenItem(speakImage);

				// copy kanji
				Image clipboardKanji = new Image(getResources().getDrawable(R.drawable.clipboard_kanji), 0);
				clipboardKanji.setOnClickListener(new CopyToClipboard(dictionaryEntry.getKanji()));
				actionTableRow.addScreenItem(clipboardKanji);

				// add to favourite word list
				if (isAddFavouriteWordStar == false && dictionaryEntry.isName() == false) {

					isAddFavouriteWordStar = true;
					actionTableRow.addScreenItem(createFavouriteWordStar(dictionaryManager, dictionaryEntry));
				}

				//

				actionButtons.addTableRow(actionTableRow);
				report.add(actionButtons);
			}
		} else {
			if (addKanjiWrite == true) {
				StringValue kanjiStringValue = new StringValue(kanjiSb.toString(), 35.0f, 0);

				report.add(new StringValue(getString(R.string.word_dictionary_word_anim), 12.0f, 0));

				kanjiStringValue.setOnClickListener(kanjiDrawOnClickListener);

				report.add(kanjiStringValue);

				TableLayout actionButtons = new TableLayout(TableLayout.LayoutParam.WrapContent_WrapContent, true, null);
				TableRow actionTableRow = new TableRow();

				Image speakImage = new Image(getResources().getDrawable(android.R.drawable.ic_lock_silent_mode_off), 0);

				if (kanaList != null && kanaList.size() > 0) {
					speakImage.setOnClickListener(new TTSJapaneseSpeak(null, kanaList.get(0)));
				} else {
					speakImage.setOnClickListener(new TTSJapaneseSpeak(null, dictionaryEntry.getKanji()));
				}

				actionTableRow.addScreenItem(speakImage);

				// clipboard kanji
				Image clipboardKanji = new Image(getResources().getDrawable(R.drawable.clipboard_kanji), 0);
				clipboardKanji.setOnClickListener(new CopyToClipboard(dictionaryEntry.getKanji()));
				actionTableRow.addScreenItem(clipboardKanji);

				// add to favourite word list
				if (isAddFavouriteWordStar == false && dictionaryEntry.isName() == false) {

					isAddFavouriteWordStar = true;
					actionTableRow.addScreenItem(createFavouriteWordStar(dictionaryManager, dictionaryEntry));
				}

				//

				actionButtons.addTableRow(actionTableRow);
				report.add(actionButtons);
			}
		}

		// Reading
		report.add(new TitleItem(getString(R.string.word_dictionary_details_reading_label), 0));
		report.add(new StringValue(getString(R.string.word_dictionary_word_anim), 12.0f, 0));

		List<String> romajiList = dictionaryEntry.getRomajiList();

		for (int idx = 0; idx < kanaList.size(); ++idx) {

			final StringBuffer sb = new StringBuffer();

			if (prefixKana != null) {
				sb.append("(").append(prefixKana).append(") ");
			}

			sb.append(kanaList.get(idx)).append(" - ");

			if (prefixRomaji != null) {
				sb.append("(").append(prefixRomaji).append(") ");
			}

			sb.append(romajiList.get(idx));

			StringValue readingStringValue = new StringValue(sb.toString(), 20.0f, 0);

			readingStringValue.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					List<KanjivgEntry> strokePathsForWord = JapaneseAndroidLearnHelperApplication.getInstance()
							.getDictionaryManager(WordDictionaryDetails.this).getStrokePathsForWord(sb.toString());

					StrokePathInfo strokePathInfo = new StrokePathInfo();

					strokePathInfo.setStrokePaths(strokePathsForWord);

					Intent intent = new Intent(getApplicationContext(), SodActivity.class);

					intent.putExtra("strokePathsInfo", strokePathInfo);
					intent.putExtra("annotateStrokes", false);

					startActivity(intent);
				}
			});

			report.add(readingStringValue);

			TableLayout actionButtons = new TableLayout(TableLayout.LayoutParam.WrapContent_WrapContent, true, null);
			TableRow actionTableRow = new TableRow();

			// speak image		
			Image speakImage = new Image(getResources().getDrawable(android.R.drawable.ic_lock_silent_mode_off), 0);
			speakImage.setOnClickListener(new TTSJapaneseSpeak(null, kanaList.get(idx)));
			actionTableRow.addScreenItem(speakImage);

			// clipboard kana
			Image clipboardKana = new Image(getResources().getDrawable(R.drawable.clipboard_kana), 0);
			clipboardKana.setOnClickListener(new CopyToClipboard(kanaList.get(idx)));
			actionTableRow.addScreenItem(clipboardKana);

			// clipboard romaji
			Image clipboardRomaji = new Image(getResources().getDrawable(R.drawable.clipboard_romaji), 0);
			clipboardRomaji.setOnClickListener(new CopyToClipboard(romajiList.get(idx)));
			actionTableRow.addScreenItem(clipboardRomaji);

			// add to favourite word list
			if (isAddFavouriteWordStar == false && dictionaryEntry.isName() == false) {

				isAddFavouriteWordStar = true;
				actionTableRow.addScreenItem(createFavouriteWordStar(dictionaryManager, dictionaryEntry));

			}

			actionButtons.addTableRow(actionTableRow);

			report.add(actionButtons);
		}

		// Translate
		report.add(new TitleItem(getString(R.string.word_dictionary_details_translate_label), 0));

		List<String> translates = dictionaryEntry.getTranslates();

		for (int idx = 0; idx < translates.size(); ++idx) {
			report.add(new StringValue(translates.get(idx), 20.0f, 0));
		}

		// Additional info
		report.add(new TitleItem(getString(R.string.word_dictionary_details_additional_info_label), 0));

		if (isSmTsukiNiKawatteOshiokiYo(kanjiSb.toString()) == true) {
			report.add(createSmTsukiNiKawatteOshiokiYo());
		} else {
			String info = dictionaryEntry.getInfo();

			if (info != null && info.length() > 0) {
				report.add(new StringValue(info, 20.0f, 0));
			} else {
				report.add(new StringValue("-", 20.0f, 0));
			}
		}

		// Word type
		int addableDictionaryEntryTypeInfoCounter = 0;

		List<DictionaryEntryType> dictionaryEntryTypeList = dictionaryEntry.getDictionaryEntryTypeList();

		if (dictionaryEntryTypeList != null) {
			for (DictionaryEntryType currentDictionaryEntryType : dictionaryEntryTypeList) {

				boolean addableDictionaryEntryTypeInfo = DictionaryEntryType
						.isAddableDictionaryEntryTypeInfo(currentDictionaryEntryType);

				if (addableDictionaryEntryTypeInfo == true) {
					addableDictionaryEntryTypeInfoCounter++;
				}
			}
		}

		if (addableDictionaryEntryTypeInfoCounter > 0) {
			report.add(new TitleItem(getString(R.string.word_dictionary_details_part_of_speech), 0));

			if (addableDictionaryEntryTypeInfoCounter > 1) {
				report.add(new StringValue(getString(R.string.word_dictionary_details_part_of_speech_press), 12.0f, 0));
			}

			for (final DictionaryEntryType currentDictionaryEntryType : dictionaryEntryTypeList) {

				StringValue currentDictionaryEntryTypeStringValue = new StringValue(
						currentDictionaryEntryType.getName(), 20.0f, 0);

				if (addableDictionaryEntryTypeInfoCounter > 1) {
					currentDictionaryEntryTypeStringValue.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							Intent intent = new Intent(getApplicationContext(), WordDictionaryDetails.class);

							intent.putExtra("item", dictionaryEntry);
							intent.putExtra("forceDictionaryEntryType", currentDictionaryEntryType);

							startActivity(intent);
						}
					});
				}

				report.add(currentDictionaryEntryTypeStringValue);
			}
		}

		List<Attribute> attributeList = dictionaryEntry.getAttributeList().getAttributeList();

		if (attributeList != null && attributeList.size() > 0) {
			report.add(new TitleItem(getString(R.string.word_dictionary_details_attributes), 0));

			for (Attribute currentAttribute : attributeList) {

				AttributeType attributeType = currentAttribute.getAttributeType();

				if (attributeType.isShow() == true) {
					report.add(new StringValue(attributeType.getName(), 15.0f, 0));
				}

				if (	attributeType == AttributeType.VERB_TRANSITIVITY_PAIR ||
						attributeType == AttributeType.VERB_INTRANSITIVITY_PAIR ||
						attributeType == AttributeType.ALTERNATIVE ||
						attributeType == AttributeType.RELATED) {

					Integer referenceWordId = Integer.parseInt(currentAttribute.getAttributeValue().get(0));

					final DictionaryEntry referenceDictionaryEntry = JapaneseAndroidLearnHelperApplication
							.getInstance().getDictionaryManager(WordDictionaryDetails.this)
							.getDictionaryEntryById(referenceWordId);

					if (referenceDictionaryEntry != null) {

						StringValue attributeTypeStringValue = new StringValue(attributeType.getName(), 15.0f, 0);

						OnClickListener goToReferenceDictionaryEntryDetails = new OnClickListener() {

							@Override
							public void onClick(View v) {

								Intent intent = new Intent(getApplicationContext(), WordDictionaryDetails.class);

								intent.putExtra("item", referenceDictionaryEntry);

								startActivity(intent);
							}
						};

						attributeTypeStringValue.setOnClickListener(goToReferenceDictionaryEntryDetails);

						report.add(attributeTypeStringValue);

						String kana = referenceDictionaryEntry.getKana();
						String romaji = referenceDictionaryEntry.getRomaji();
						
						StringBuffer referenceDictionaryEntrySb = new StringBuffer();

						if (referenceDictionaryEntry.isKanjiExists() == true) {
							referenceDictionaryEntrySb.append(referenceDictionaryEntry.getKanji()).append(", ");
						}

						referenceDictionaryEntrySb.append(kana).append(", ");
						referenceDictionaryEntrySb.append(romaji);

						StringValue referenceDictionaryEntryKanaRomajiStringValue = new StringValue(
								referenceDictionaryEntrySb.toString(), 15.0f, 1);

						referenceDictionaryEntryKanaRomajiStringValue
								.setOnClickListener(goToReferenceDictionaryEntryDetails);

						report.add(referenceDictionaryEntryKanaRomajiStringValue);
					}
					
				}
			}
		}

		// dictionary groups
		List<GroupEnum> groups = dictionaryEntry.getGroups();

		if (groups != null && groups.size() > 0) {
			
			report.add(new StringValue("", 15.0f, 2));
			report.add(new TitleItem(getString(R.string.word_dictionary_details_dictionary_groups), 0));

			for (int groupsIdx = 0; groupsIdx < groups.size(); ++groupsIdx) {
				report.add(new StringValue(String.valueOf(groups.get(groupsIdx).getValue()), 20.0f, 0));
			}
		}

		// user groups
		if (dictionaryEntry.isName() == false) { // tylko dla normalnych slowek

			report.add(new StringValue("", 15.0f, 2));
			report.add(new TitleItem(getString(R.string.word_dictionary_details_user_groups), 0));

			final DataManager dataManager = dictionaryManager.getDataManager();

			List<UserGroupEntity> userGroupEntityListForItemId = dataManager.getUserGroupEntityListForItemId(UserGroupEntity.Type.USER_GROUP, UserGroupItemEntity.Type.DICTIONARY_ENTRY, dictionaryEntry.getId());

			for (UserGroupEntity currentUserGroupEntity : userGroupEntityListForItemId) {

				TableRow userGroupTableRow = new TableRow();

				OnClickListener deleteItemIdFromUserGroupOnClickListener = createDeleteItemIdFromUserGroupOnClickListener(dataManager, dictionaryEntry, currentUserGroupEntity, userGroupTableRow);

				StringValue userGroupNameStringValue = new StringValue(currentUserGroupEntity.getName(), 15.0f, 0);
				Image userGroupNameDeleteImage = new Image(getResources().getDrawable(R.drawable.delete), 0);

				userGroupNameStringValue.setOnClickListener(deleteItemIdFromUserGroupOnClickListener);
				userGroupNameDeleteImage.setOnClickListener(deleteItemIdFromUserGroupOnClickListener);

				userGroupTableRow.addScreenItem(userGroupNameStringValue);
				userGroupTableRow.addScreenItem(userGroupNameDeleteImage);

				report.add(userGroupTableRow);
			}
		}

		/*
		// dictionary position
		report.add(new TitleItem(getString(R.string.word_dictionary_details_dictionary_position), 0));

		report.add(new StringValue(String.valueOf(dictionaryEntry.getId()), 20.0f, 0));
		*/

		// known kanji
		List<KanjiEntry> knownKanji = null;

		if (dictionaryEntry.isKanjiExists() == true) {

			try {
				knownKanji = JapaneseAndroidLearnHelperApplication.getInstance().getDictionaryManager(this)
						.findKnownKanji(dictionaryEntry.getKanji());

			} catch (DictionaryException e) {
				Toast.makeText(WordDictionaryDetails.this, getString(R.string.dictionary_exception_common_error_message, e.getMessage()), Toast.LENGTH_LONG).show();
			}
		}

		if (knownKanji != null && knownKanji.size() > 0) {

			report.add(new StringValue("", 15.0f, 2));
			report.add(new TitleItem(getString(R.string.word_dictionary_known_kanji), 0));
			report.add(new StringValue(getString(R.string.word_dictionary_known_kanji_info), 12.0f, 0));

			for (int knownKanjiIdx = 0; knownKanjiIdx < knownKanji.size(); ++knownKanjiIdx) {

				final KanjiEntry kanjiEntry = knownKanji.get(knownKanjiIdx);

				OnClickListener kanjiOnClickListener = new OnClickListener() {

					@Override
					public void onClick(View v) {

						// show kanji details

						Intent intent = new Intent(getApplicationContext(), KanjiDetails.class);

						intent.putExtra("item", kanjiEntry);

						startActivity(intent);
					}
				};

				StringValue knownKanjiStringValue = new StringValue(kanjiEntry.getKanji(), 16.0f, 1);
				StringValue polishTranslateStringValue = new StringValue(kanjiEntry.getPolishTranslates().toString(),
						16.0f, 1);

				knownKanjiStringValue.setOnClickListener(kanjiOnClickListener);
				polishTranslateStringValue.setOnClickListener(kanjiOnClickListener);

				report.add(knownKanjiStringValue);
				report.add(polishTranslateStringValue);

				if (knownKanjiIdx != knownKanji.size() - 1) {
					report.add(new StringValue("", 10.0f, 1));
				}

			}
		}

		// index
		int indexStartPos = report.size();

		Map<GrammaFormConjugateResultType, GrammaFormConjugateResult> grammaCache = new HashMap<GrammaFormConjugateResultType, GrammaFormConjugateResult>();

		// Conjugater
		List<GrammaFormConjugateGroupTypeElements> grammaFormConjugateGroupTypeElementsList = GrammaConjugaterManager
				.getGrammaConjufateResult(JapaneseAndroidLearnHelperApplication.getInstance()
						.getDictionaryManager(this).getKeigoHelper(), dictionaryEntry, grammaCache,
						forceDictionaryEntryType, false);

		if (grammaFormConjugateGroupTypeElementsList != null) {
			report.add(new StringValue("", 15.0f, 2));
			report.add(new TitleItem(getString(R.string.word_dictionary_details_conjugater_label), 0));

			for (GrammaFormConjugateGroupTypeElements currentGrammaFormConjugateGroupTypeElements : grammaFormConjugateGroupTypeElementsList) {

				report.add(new TitleItem(currentGrammaFormConjugateGroupTypeElements.getGrammaFormConjugateGroupType()
						.getName(), 1));

				List<GrammaFormConjugateResult> grammaFormConjugateResults = currentGrammaFormConjugateGroupTypeElements
						.getGrammaFormConjugateResults();

				for (GrammaFormConjugateResult currentGrammaFormConjugateResult : grammaFormConjugateResults) {

					if (currentGrammaFormConjugateResult.getResultType().isShow() == true) {
						report.add(new TitleItem(currentGrammaFormConjugateResult.getResultType().getName(), 2));
					}

					addGrammaFormConjugateResult(report, currentGrammaFormConjugateResult);
				}

				report.add(new StringValue("", 15.0f, 1));
			}
		}
		
		// Sentence example		
		List<String> exampleSentenceGroupIdsList = dictionaryEntry.getExampleSentenceGroupIdsList();
		
		List<GroupWithTatoebaSentenceList> tatoebaSentenceGroupList = null;
		
		if (exampleSentenceGroupIdsList != null && exampleSentenceGroupIdsList.size() != 0) {
			
			tatoebaSentenceGroupList = new ArrayList<GroupWithTatoebaSentenceList>();
			
	    	for (String currentExampleSentenceGroupId : exampleSentenceGroupIdsList) {

				GroupWithTatoebaSentenceList tatoebaSentenceGroup = null;
	    		try {
					tatoebaSentenceGroup = JapaneseAndroidLearnHelperApplication.getInstance()
							.getDictionaryManager(this).getTatoebaSentenceGroup(currentExampleSentenceGroupId);

				} catch (DictionaryException e) {
					Toast.makeText(WordDictionaryDetails.this, getString(R.string.dictionary_exception_common_error_message, e.getMessage()), Toast.LENGTH_LONG).show();

					break;
				}

	    		if (tatoebaSentenceGroup != null) {    			
	    			tatoebaSentenceGroupList.add(tatoebaSentenceGroup);
	    		}
			}
	    	
	    	if (tatoebaSentenceGroupList.size() > 0) {
	    		
				if (grammaFormConjugateGroupTypeElementsList == null) {
					report.add(new StringValue("", 15.0f, 2));
				}
	    		
	    		report.add(new TitleItem(getString(R.string.word_dictionary_details_sentence_example_label), 0));
	    		
	    		for (int tatoebaSentenceGroupListIdx = 0; tatoebaSentenceGroupListIdx < tatoebaSentenceGroupList.size(); ++tatoebaSentenceGroupListIdx) {
	    			
	    			GroupWithTatoebaSentenceList currentTatoebeSentenceGroup = tatoebaSentenceGroupList.get(tatoebaSentenceGroupListIdx);
	    			
	    			List<TatoebaSentence> tatoebaSentenceList = currentTatoebeSentenceGroup.getTatoebaSentenceList();
	    						
	    			List<TatoebaSentence> polishTatoebaSentenceList = new ArrayList<TatoebaSentence>();
	    			List<TatoebaSentence> japaneseTatoebaSentenceList = new ArrayList<TatoebaSentence>();
	    			
	    			for (TatoebaSentence currentTatoebaSentence : tatoebaSentenceList) {
	    				
	    				if (currentTatoebaSentence.getLang().equals("pol") == true) {
	    					polishTatoebaSentenceList.add(currentTatoebaSentence);
	    					
	    				} else if (currentTatoebaSentence.getLang().equals("jpn") == true) {
	    					japaneseTatoebaSentenceList.add(currentTatoebaSentence);
	    				}				
	    			}
	    			
	    			if (polishTatoebaSentenceList.size() > 0 && japaneseTatoebaSentenceList.size() > 0) {

	    				for (TatoebaSentence currentPolishTatoebaSentence : polishTatoebaSentenceList) {	    					
	    					report.add(new StringValue(currentPolishTatoebaSentence.getSentence(), 12.0f, 1));	    					
	    				}
	    				
	    				for (TatoebaSentence currentJapaneseTatoebaSentence : japaneseTatoebaSentenceList) {	    					
	    					report.add(new StringValue(currentJapaneseTatoebaSentence.getSentence(), 12.0f, 1));
	    				}
	    				
	    				if (tatoebaSentenceGroupListIdx != tatoebaSentenceGroupList.size() - 1) {	    					
	    					report.add(new StringValue("", 6.0f, 1));
	    				}
	    			}
	    		}
	    		
	    		report.add(new StringValue("", 15.0f, 1));
	    	}			
		}		

		// Example
		List<ExampleGroupTypeElements> exampleGroupTypeElementsList = ExampleManager.getExamples(
				JapaneseAndroidLearnHelperApplication.getInstance().getDictionaryManager(this).getKeigoHelper(),
				dictionaryEntry, grammaCache, forceDictionaryEntryType, false);

		if (exampleGroupTypeElementsList != null) {

			if (grammaFormConjugateGroupTypeElementsList == null && (tatoebaSentenceGroupList == null || tatoebaSentenceGroupList.size() == 0)) {
				report.add(new StringValue("", 15.0f, 2));
			}

			report.add(new TitleItem(getString(R.string.word_dictionary_details_example_label), 0));

			for (ExampleGroupTypeElements currentExampleGroupTypeElements : exampleGroupTypeElementsList) {

				report.add(new TitleItem(currentExampleGroupTypeElements.getExampleGroupType().getName(), 1));

				String exampleGroupInfo = currentExampleGroupTypeElements.getExampleGroupType().getInfo();

				if (exampleGroupInfo != null) {
					report.add(new StringValue(exampleGroupInfo, 12.0f, 1));
				}

				List<ExampleResult> exampleResults = currentExampleGroupTypeElements.getExampleResults();

				for (ExampleResult currentExampleResult : exampleResults) {
					addExampleResult(report, currentExampleResult);
				}

				report.add(new StringValue("", 15.0f, 1));
			}
		}

		// add index
		if (indexStartPos < report.size()) {

			int indexStopPos = report.size();

			List<IScreenItem> indexList = new ArrayList<IScreenItem>();

			indexList.add(new StringValue("", 15.0f, 2));
			indexList.add(new TitleItem(getString(R.string.word_dictionary_details_report_counters_index), 0));
			indexList.add(new StringValue(getString(R.string.word_dictionary_details_index_go), 12.0f, 1));

			for (int reportIdx = indexStartPos; reportIdx < indexStopPos; ++reportIdx) {

				IScreenItem currentReportScreenItem = report.get(reportIdx);

				if (currentReportScreenItem instanceof TitleItem == false) {
					continue;
				}

				final TitleItem currentReportScreenItemAsTitle = (TitleItem) currentReportScreenItem;

				final StringValue titleStringValue = new StringValue(currentReportScreenItemAsTitle.getTitle(), 15.0f,
						currentReportScreenItemAsTitle.getLevel() + 2);

				titleStringValue.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						backScreenPositionStack.push(scrollMainLayout.getScrollY());

						int counterPos = currentReportScreenItemAsTitle.getY();
						scrollMainLayout.scrollTo(0, counterPos - 3);
					}
				});

				indexList.add(titleStringValue);
			}

			for (int indexListIdx = 0, reportStartPos = indexStartPos; indexListIdx < indexList.size(); ++indexListIdx) {
				report.add(reportStartPos, indexList.get(indexListIdx));

				reportStartPos++;
			}
		}

		return report;
	}

	private void fillDetailsMainLayout(List<IScreenItem> generatedDetails, LinearLayout detailsMainLayout) {

		detailsMainLayout.removeAllViews();

		for (IScreenItem currentDetailsReportItem : generatedDetails) {
			currentDetailsReportItem.generate(this, getResources(), detailsMainLayout);
		}
	}

	private void addGrammaFormConjugateResult(List<IScreenItem> report,
			GrammaFormConjugateResult grammaFormConjugateResult) {

		TableLayout actionButtons = new TableLayout(TableLayout.LayoutParam.WrapContent_WrapContent, true, null);
		TableRow actionTableRow = new TableRow();

		String grammaFormKanji = grammaFormConjugateResult.getKanji();

		String prefixKana = grammaFormConjugateResult.getPrefixKana();
		String prefixRomaji = grammaFormConjugateResult.getPrefixRomaji();

		StringBuffer grammaFormKanjiSb = new StringBuffer();

		if (grammaFormKanji != null) {
			if (prefixKana != null && prefixKana.equals("") == false) {
				grammaFormKanjiSb.append("(").append(prefixKana).append(") ");
			}

			grammaFormKanjiSb.append(grammaFormKanji);

			report.add(new StringValue(grammaFormKanjiSb.toString(), 15.0f, 2));
		}

		List<String> grammaFormKanaList = grammaFormConjugateResult.getKanaList();
		List<String> grammaFormRomajiList = grammaFormConjugateResult.getRomajiList();

		for (int idx = 0; idx < grammaFormKanaList.size(); ++idx) {

			StringBuffer sb = new StringBuffer();

			if (prefixKana != null && prefixKana.equals("") == false) {
				sb.append("(").append(prefixKana).append(") ");
			}

			sb.append(grammaFormKanaList.get(idx));

			report.add(new StringValue(sb.toString(), 15.0f, 2));

			StringBuffer grammaFormRomajiSb = new StringBuffer();

			if (prefixRomaji != null && prefixRomaji.equals("") == false) {
				grammaFormRomajiSb.append("(").append(prefixRomaji).append(") ");
			}

			grammaFormRomajiSb.append(grammaFormRomajiList.get(idx));

			report.add(new StringValue(grammaFormRomajiSb.toString(), 15.0f, 2));

			// speak image
			Image speakImage = new Image(getResources().getDrawable(android.R.drawable.ic_lock_silent_mode_off), 2);
			speakImage.setOnClickListener(new TTSJapaneseSpeak(null, grammaFormKanaList.get(idx)));
			actionTableRow.addScreenItem(speakImage);

			// clipboard kanji
			if (grammaFormKanji != null) {
				Image clipboardKanji = new Image(getResources().getDrawable(R.drawable.clipboard_kanji), 0);
				clipboardKanji.setOnClickListener(new CopyToClipboard(grammaFormKanji));
				actionTableRow.addScreenItem(clipboardKanji);
			}

			// clipboard kana
			Image clipboardKana = new Image(getResources().getDrawable(R.drawable.clipboard_kana), 0);
			clipboardKana.setOnClickListener(new CopyToClipboard(grammaFormKanaList.get(idx)));
			actionTableRow.addScreenItem(clipboardKana);

			// clipboard romaji
			Image clipboardRomaji = new Image(getResources().getDrawable(R.drawable.clipboard_romaji), 0);
			clipboardRomaji.setOnClickListener(new CopyToClipboard(grammaFormRomajiList.get(idx)));
			actionTableRow.addScreenItem(clipboardRomaji);

			actionButtons.addTableRow(actionTableRow);

			report.add(actionButtons);
		}

		GrammaFormConjugateResult alternative = grammaFormConjugateResult.getAlternative();

		if (alternative != null) {
			report.add(new StringValue("", 5.0f, 1));

			addGrammaFormConjugateResult(report, alternative);
		}
	}

	private void addExampleResult(List<IScreenItem> report, ExampleResult exampleResult) {

		TableLayout actionButtons = new TableLayout(TableLayout.LayoutParam.WrapContent_WrapContent, true, null);
		TableRow actionTableRow = new TableRow();

		String exampleKanji = exampleResult.getKanji();
		String prefixKana = exampleResult.getPrefixKana();
		String prefixRomaji = exampleResult.getPrefixRomaji();

		StringBuffer exampleKanjiSb = new StringBuffer();

		if (exampleKanji != null) {
			if (prefixKana != null && prefixKana.equals("") == false) {
				exampleKanjiSb.append("(").append(prefixKana).append(") ");
			}

			exampleKanjiSb.append(exampleKanji);

			report.add(new StringValue(exampleKanjiSb.toString(), 15.0f, 2));
		}

		List<String> exampleKanaList = exampleResult.getKanaList();
		List<String> exampleRomajiList = exampleResult.getRomajiList();

		for (int idx = 0; idx < exampleKanaList.size(); ++idx) {

			StringBuffer sb = new StringBuffer();

			if (prefixKana != null && prefixKana.equals("") == false) {
				sb.append("(").append(prefixKana).append(") ");
			}

			sb.append(exampleKanaList.get(idx));

			report.add(new StringValue(sb.toString(), 15.0f, 2));

			StringBuffer exampleRomajiSb = new StringBuffer();

			if (prefixRomaji != null && prefixRomaji.equals("") == false) {
				exampleRomajiSb.append("(").append(prefixRomaji).append(") ");
			}

			exampleRomajiSb.append(exampleRomajiList.get(idx));

			report.add(new StringValue(exampleRomajiSb.toString(), 15.0f, 2));

			String exampleResultInfo = exampleResult.getInfo();

			if (exampleResultInfo != null) {
				report.add(new StringValue(exampleResultInfo, 12.0f, 2));
			}

			// speak image
			Image speakImage = new Image(getResources().getDrawable(android.R.drawable.ic_lock_silent_mode_off), 2);
			speakImage.setOnClickListener(new TTSJapaneseSpeak(null, exampleKanaList.get(idx)));
			actionTableRow.addScreenItem(speakImage);

			// clipboard kanji
			if (exampleKanji != null) {
				Image clipboardKanji = new Image(getResources().getDrawable(R.drawable.clipboard_kanji), 0);
				clipboardKanji.setOnClickListener(new CopyToClipboard(exampleKanji));
				actionTableRow.addScreenItem(clipboardKanji);
			}

			// clipboard kana
			Image clipboardKana = new Image(getResources().getDrawable(R.drawable.clipboard_kana), 0);
			clipboardKana.setOnClickListener(new CopyToClipboard(exampleKanaList.get(idx)));
			actionTableRow.addScreenItem(clipboardKana);

			// clipboard romaji
			Image clipboardRomaji = new Image(getResources().getDrawable(R.drawable.clipboard_romaji), 0);
			clipboardRomaji.setOnClickListener(new CopyToClipboard(exampleRomajiList.get(idx)));
			actionTableRow.addScreenItem(clipboardRomaji);

			actionButtons.addTableRow(actionTableRow);

			report.add(actionButtons);
		}

		ExampleResult alternative = exampleResult.getAlternative();

		if (alternative != null) {
			report.add(new StringValue("", 5.0f, 1));

			addExampleResult(report, alternative);
		}
	}

	// special
	private boolean isSmTsukiNiKawatteOshiokiYo(String value) {

		if (value == null) {
			return false;
		}

		if (value.equals("月に代わって、お仕置きよ!") == true) {
			return true;
		}

		return false;
	}

	private StringValue createSmTsukiNiKawatteOshiokiYo() {

		StringValue smStringValue = new StringValue(getString(R.string.sm_tsuki_ni_kawatte_oshioki_yo), 2.8f, 0);

		smStringValue.setTypeface(Typeface.MONOSPACE);
		smStringValue.setTextColor(Color.BLACK);
		smStringValue.setBackgroundColor(Color.WHITE);
		smStringValue.setGravity(Gravity.CENTER);

		return smStringValue;
	}

	private class TTSJapaneseSpeak implements OnClickListener {

		private final String prefix;

		private final String kanjiKana;

		public TTSJapaneseSpeak(String prefix, String kanjiKana) {
			this.prefix = prefix;
			this.kanjiKana = kanjiKana;
		}

		@Override
		public void onClick(View v) {

			StringBuffer text = new StringBuffer();

			if (prefix != null) {
				text.append(prefix);
			}

			if (kanjiKana != null) {
				text.append(kanjiKana);
			}

			if (ttsConnector != null && ttsConnector.getOnInitResult() != null
					&& ttsConnector.getOnInitResult().booleanValue() == true) {
				ttsConnector.speak(text.toString());
			} else {
				AlertDialog alertDialog = new AlertDialog.Builder(WordDictionaryDetails.this).create();

				alertDialog.setMessage(getString(R.string.tts_japanese_error));
				alertDialog.setCancelable(false);

				alertDialog.setButton(getString(R.string.tts_error_ok), new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// noop
					}
				});

				alertDialog.setButton2(getString(R.string.tts_google_play_go), new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

						Uri marketUri = Uri.parse(getString(R.string.tts_google_android_tts_url));

						Intent marketIntent = new Intent(Intent.ACTION_VIEW, marketUri);

						startActivity(marketIntent);
					}
				});

				alertDialog.show();
			}
		}
	}

	private Image createFavouriteWordStar(DictionaryManagerCommon dictionaryManager, final DictionaryEntry dictionaryEntry) {

		final DataManager dataManager = dictionaryManager.getDataManager();

		UserGroupEntity startUserGroup = null;

		try {
			startUserGroup = dataManager.getStarUserGroup();

		} catch (DataManagerException e) {
			throw new RuntimeException(e);
		}

		final UserGroupEntity startUserGroup2 = startUserGroup;

		boolean isItemIdExistsInStarGroup = dataManager.isItemIdExistsInUserGroup(startUserGroup, UserGroupItemEntity.Type.DICTIONARY_ENTRY, dictionaryEntry.getId());

		final int starBigOff = android.R.drawable.star_big_off;
		final int starBigOn = android.R.drawable.star_big_on;

		final Image starImage = new Image(getResources().getDrawable(isItemIdExistsInStarGroup == false ? starBigOff : starBigOn), 0);

		starImage.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				boolean isItemIdExistsInStarGroup = dataManager.isItemIdExistsInUserGroup(startUserGroup2, UserGroupItemEntity.Type.DICTIONARY_ENTRY, dictionaryEntry.getId());

				if (isItemIdExistsInStarGroup == false) {
					dataManager.addItemIdToUserGroup(startUserGroup2, UserGroupItemEntity.Type.DICTIONARY_ENTRY, dictionaryEntry.getId());

					starImage.changeImage(getResources().getDrawable(starBigOn));

					Toast.makeText(WordDictionaryDetails.this,
							getString(R.string.word_dictionary_details_add_to_star_group), Toast.LENGTH_SHORT).show();


				} else {
					dataManager.deleteItemIdFromUserGroup(startUserGroup2, UserGroupItemEntity.Type.DICTIONARY_ENTRY, dictionaryEntry.getId());

					starImage.changeImage(getResources().getDrawable(starBigOff));

					Toast.makeText(WordDictionaryDetails.this,
							getString(R.string.word_dictionary_details_remove_from_star_group), Toast.LENGTH_SHORT).show();
				}
			}
		});

		return starImage;
	}

	private OnClickListener createDeleteItemIdFromUserGroupOnClickListener(final DataManager dataManager, final DictionaryEntry dictionaryEntry, final UserGroupEntity userGroupEntity, final TableRow userGroupTableRow) {

		return new OnClickListener() {
			@Override
			public void onClick(View v) {

				final AlertDialog alertDialog = new AlertDialog.Builder(WordDictionaryDetails.this).create();

				alertDialog.setTitle(getString(R.string.word_dictionary_details_delete_item_id_from_user_group_title));
				alertDialog.setMessage(getString(R.string.word_dictionary_details_delete_item_id_from_user_group_message, userGroupEntity.getName()));

				alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.user_group_ok_button), new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

						// usuwamy z bazy danych
						dataManager.deleteItemIdFromUserGroup(userGroupEntity, UserGroupItemEntity.Type.DICTIONARY_ENTRY, dictionaryEntry.getId());

						// ukrywamy grupe
						userGroupTableRow.setVisibility(View.GONE);

						// komunikat
						Toast.makeText(WordDictionaryDetails.this,
								getString(R.string.word_dictionary_details_delete_item_id_from_user_group_toast, userGroupEntity.getName()), Toast.LENGTH_SHORT).show();
					}
				});

				alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.user_group_cancel_button), new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						alertDialog.dismiss();

					}
				});

				if (isFinishing() == false) {
					alertDialog.show();
				}
			}
		};
	}



	private class CopyToClipboard implements OnClickListener {

		private final String text;

		public CopyToClipboard(String text) {
			this.text = text;
		}

		@Override
		public void onClick(View v) {

			ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);

			clipboardManager.setText(text);

			Toast.makeText(WordDictionaryDetails.this,
					getString(R.string.word_dictionary_details_clipboard_copy, text), Toast.LENGTH_SHORT).show();
		}
	}
}
