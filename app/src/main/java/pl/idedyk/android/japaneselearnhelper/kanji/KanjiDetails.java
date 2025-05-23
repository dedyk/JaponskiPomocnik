package pl.idedyk.android.japaneselearnhelper.kanji;

import java.util.ArrayList;
import java.util.List;

import pl.idedyk.android.japaneselearnhelper.JapaneseAndroidLearnHelperApplication;
import pl.idedyk.android.japaneselearnhelper.MenuShorterHelper;
import pl.idedyk.android.japaneselearnhelper.R;
import pl.idedyk.android.japaneselearnhelper.data.DataManager;
import pl.idedyk.android.japaneselearnhelper.data.entity.UserGroupEntity;
import pl.idedyk.android.japaneselearnhelper.data.entity.UserGroupItemEntity;
import pl.idedyk.android.japaneselearnhelper.data.exception.DataManagerException;
import pl.idedyk.android.japaneselearnhelper.dictionary.DictionaryManagerCommon;
import pl.idedyk.android.japaneselearnhelper.dictionaryscreen.WordDictionaryTab;
import pl.idedyk.android.japaneselearnhelper.problem.ReportProblem;
import pl.idedyk.android.japaneselearnhelper.screen.IScreenItem;
import pl.idedyk.android.japaneselearnhelper.screen.Image;
import pl.idedyk.android.japaneselearnhelper.screen.StringValue;
import pl.idedyk.android.japaneselearnhelper.screen.TableLayout;
import pl.idedyk.android.japaneselearnhelper.screen.TableRow;
import pl.idedyk.android.japaneselearnhelper.screen.TitleItem;
import pl.idedyk.android.japaneselearnhelper.sod.SodActivity;
import pl.idedyk.android.japaneselearnhelper.sod.dto.StrokePathInfo;
import pl.idedyk.android.japaneselearnhelper.usergroup.UserGroupActivity;
import pl.idedyk.android.japaneselearnhelper.utils.WordKanjiDictionaryUtils;
import pl.idedyk.japanese.dictionary.api.dictionary.Utils;
import pl.idedyk.japanese.dictionary.api.dictionary.dto.FindWordRequest;
import pl.idedyk.japanese.dictionary.api.dictionary.dto.WordPlaceSearch;
import pl.idedyk.japanese.dictionary.api.dto.GroupEnum;
import pl.idedyk.japanese.dictionary.api.dto.KanjivgEntry;
import pl.idedyk.japanese.dictionary.api.exception.DictionaryException;
import pl.idedyk.japanese.dictionary2.kanjidic2.xsd.KanjiCharacterInfo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.ClipboardManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

@SuppressWarnings("deprecation")
public class KanjiDetails extends Activity {

	private List<IScreenItem> generatedDetails;

	private KanjiCharacterInfo kanjiEntry;

	//

	private final static int ADD_ITEM_ID_TO_USER_GROUP_ACTIVITY_REQUEST_CODE = 1;
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);

		MenuShorterHelper.onCreateOptionsMenu(menu);

		menu.add(Menu.NONE, R.id.kanji_details_menu_add_item_id_to_user_group, Menu.NONE, R.string.kanji_details_menu_add_item_id_to_user_group);

		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);

		if (item.getItemId() == R.id.kanji_details_menu_add_item_id_to_user_group) {

			Intent intent = new Intent(getApplicationContext(), UserGroupActivity.class);

			intent.putExtra("itemToAdd", kanjiEntry);

			startActivityForResult(intent, ADD_ITEM_ID_TO_USER_GROUP_ACTIVITY_REQUEST_CODE);

			return true;

		} else {
			return MenuShorterHelper.onOptionsItemSelected(item, getApplicationContext(), this);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == ADD_ITEM_ID_TO_USER_GROUP_ACTIVITY_REQUEST_CODE) {

			LinearLayout detailsMainLayout = (LinearLayout)findViewById(R.id.kanji_details_main_layout);

			generatedDetails = generateDetails(kanjiEntry);

			fillDetailsMainLayout(generatedDetails, detailsMainLayout);
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);

		JapaneseAndroidLearnHelperApplication.getInstance().setContentViewAndTheme(this, R.layout.kanji_details);
		
		JapaneseAndroidLearnHelperApplication.getInstance().logScreen(this, getString(R.string.logs_kanji_details));

		DictionaryManagerCommon dictionaryManager = JapaneseAndroidLearnHelperApplication.getInstance().getDictionaryManager(this);

		try {
			kanjiEntry = (KanjiCharacterInfo) dictionaryManager.getKanjiEntryById((Integer) getIntent().getSerializableExtra("id"));
		} catch (DictionaryException e) {
			Toast.makeText(this, getString(R.string.dictionary_exception_common_error_message, e.getMessage()), Toast.LENGTH_LONG).show();

			finish();
			return;
		}
		
		LinearLayout detailsMainLayout = (LinearLayout)findViewById(R.id.kanji_details_main_layout);
		
		generatedDetails = generateDetails(kanjiEntry);
		
		fillDetailsMainLayout(generatedDetails, detailsMainLayout);
		
		Button reportProblemButton = (Button)findViewById(R.id.kanji_details_report_problem_button);
		
		reportProblemButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View view) {
				
				StringBuffer detailsSb = new StringBuffer();
				
				for (IScreenItem currentGeneratedDetails : generatedDetails) {
					detailsSb.append(currentGeneratedDetails.toString()).append("\n\n");
				}
				
				String chooseEmailClientTitle = getString(R.string.choose_email_client);
				
				String mailSubject = getString(R.string.kanji_details_report_problem_email_subject);
				
				String mailBody = getString(R.string.kanji_details_report_problem_email_body,
						detailsSb.toString());
				
		        String versionName = "";
		        int versionCode = 0;
		        
		        try {
		        	PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
		        	
		            versionName = packageInfo.versionName;
		            versionCode = packageInfo.versionCode;

		        } catch (NameNotFoundException e) {        	
		        }
								
				Intent reportProblemIntent = ReportProblem.createReportProblemIntent(mailSubject, mailBody.toString(), versionName, versionCode); 
				
				startActivity(Intent.createChooser(reportProblemIntent, chooseEmailClientTitle));
			}
		});
	}
	
	private void fillDetailsMainLayout(List<IScreenItem> generatedDetails, LinearLayout detailsMainLayout) {

		detailsMainLayout.removeAllViews();
		
		for (IScreenItem currentDetailsReportItem : generatedDetails) {
			currentDetailsReportItem.generate(this, getResources(), detailsMainLayout);			
		}
	}

	private List<IScreenItem> generateDetails(final KanjiCharacterInfo kanjiEntry) {
		
		List<IScreenItem> report = new ArrayList<IScreenItem>();

		DictionaryManagerCommon dictionaryManager = JapaneseAndroidLearnHelperApplication.getInstance().getDictionaryManager(this);

		// Kanji		
		report.add(new TitleItem(getString(R.string.kanji_details_kanji_label), 0));
		
		StringValue kanjiStringValue = new StringValue(kanjiEntry.getKanji(), 35.0f, 0);
		
		report.add(kanjiStringValue);

		if (kanjiEntry.getMisc2().getStrokePaths().size() > 0) {
			report.add(new StringValue(getString(R.string.kanji_details_kanji_info), 12.0f, 0));
			
			kanjiStringValue.setOnClickListener(new OnClickListener() {
				
				public void onClick(View view) {

					StrokePathInfo strokePathInfo = new StrokePathInfo();

					List<KanjivgEntry> kanjivsEntryStrokePathsList = new ArrayList<KanjivgEntry>();
					kanjivsEntryStrokePathsList.add(WordKanjiDictionaryUtils.createKanjivgEntry(kanjiEntry));
					strokePathInfo.setStrokePaths(kanjivsEntryStrokePathsList);
					
					Intent intent = new Intent(getApplicationContext(), SodActivity.class);
										
					intent.putExtra("strokePathsInfo", strokePathInfo);
					
					startActivity(intent);
				}
			});
		}

		TableLayout actionButtons = new TableLayout(TableLayout.LayoutParam.WrapContent_WrapContent, true, null);
		TableRow actionTableRow = new TableRow();

		//

		// copy kanji
		Image clipboardKanji = new Image(getResources().getDrawable(R.drawable.clipboard_kanji), 0);
		clipboardKanji.setOnClickListener(new CopyToClipboard(kanjiEntry.getKanji()));
		actionTableRow.addScreenItem(clipboardKanji);

		//

		// add to favourite kanji list
		actionTableRow.addScreenItem(createFavouriteKanjiStar(dictionaryManager, kanjiEntry));

		actionButtons.addTableRow(actionTableRow);
		report.add(actionButtons);

		// Stroke count
		report.add(new TitleItem(getString(R.string.kanji_details_stroke_count_label), 0));

		Integer strokeNumber = WordKanjiDictionaryUtils.getStrokeNumber(kanjiEntry, null);

		if (strokeNumber != null) {
			report.add(new StringValue(String.valueOf(strokeNumber), 20.0f, 0));
		} else {
			report.add(new StringValue("-", 20.0f, 0));
		}
		
		// Radicals		
		Typeface babelStoneHanTypeface = JapaneseAndroidLearnHelperApplication.getInstance().getBabelStoneHanSubset(getAssets()); 
		
		report.add(new TitleItem(getString(R.string.kanji_details_radicals), 0));
		
		if (kanjiEntry.getMisc2() != null && kanjiEntry.getMisc2().getRadicals() != null && kanjiEntry.getMisc2().getRadicals().size() > 0) {
			List<String> radicals = kanjiEntry.getMisc2().getRadicals();
			
			for (String currentRadical : radicals) {
				StringValue currentRadicalStringValue = new StringValue(currentRadical, 20.0f, 0);
				
				currentRadicalStringValue.setTypeface(babelStoneHanTypeface);
				
				report.add(currentRadicalStringValue);
			}
		} else {
			report.add(new StringValue("-", 20.0f, 0));
		}
				
		// Kun reading
		report.add(new TitleItem(getString(R.string.kanji_details_kun_reading), 0));

		List<String> kunReading = Utils.getKunReading(kanjiEntry);
		
		if (kunReading != null && kunReading.size() > 0) {
			for (String currentKun : kunReading) {
				report.add(new StringValue(currentKun, 20.0f, 0));
			}
		} else {
			report.add(new StringValue("-", 20.0f, 0));
		}
		
		// On reading
		report.add(new TitleItem(getString(R.string.kanji_details_on_reading), 0));

		List<String> onReading = Utils.getOnReading(kanjiEntry);

		if (onReading != null && onReading.size() > 0) {
			for (String currentOn : onReading) {
				report.add(new StringValue(currentOn, 20.0f, 0));
			}
		} else {
			report.add(new StringValue("-", 20.0f, 0));
		}

		// nanori reading
		report.add(new TitleItem(getString(R.string.kanji_details_nanori_reading), 0));

		List<String> nanoriReading = Utils.getNanoriReading(kanjiEntry);

		if (nanoriReading != null && nanoriReading.size() > 0) {
			for (String currentNanori : nanoriReading) {
				report.add(new StringValue(currentNanori, 20.0f, 0));
			}
		} else {
			report.add(new StringValue("-", 20.0f, 0));
		}
			
		// Translate
		report.add(new TitleItem(getString(R.string.kanji_details_translate_label), 0));
		
		List<String> translates = Utils.getPolishTranslates(kanjiEntry);
		
		for (int idx = 0; idx < translates.size(); ++idx) {
			report.add(new StringValue(translates.get(idx), 20.0f, 0));
		}
		
		// Additional info
		report.add(new TitleItem(getString(R.string.kanji_details_additional_info_label), 0));
		
		String info = Utils.getPolishAdditionalInfo(kanjiEntry);
		
		if (info != null && info.length() > 0) {
			report.add(new StringValue(info, 20.0f, 0));
		} else {
			report.add(new StringValue("-", 20.0f, 0));
		}
		
		// kanji appearance
		List<GroupEnum> groups = kanjiEntry.getMisc2().getGroups();
		
		if (groups != null && groups.size() > 0) {
			report.add(new TitleItem(getString(R.string.kanji_details_kanji_appearance_label), 0));
			
			for (int idx = 0; idx < groups.size(); ++idx) {
				report.add(new StringValue(groups.get(idx).getValue(), 20.0f, 0));
			}			
		}

		// user groups
		report.add(new StringValue("", 15.0f, 2));
		report.add(new TitleItem(getString(R.string.kanji_details_user_groups), 0));

		final DataManager dataManager = dictionaryManager.getDataManager();

		List<UserGroupEntity> userGroupEntityListForItemId = dataManager.getUserGroupEntityListForItemId(UserGroupEntity.Type.USER_GROUP, UserGroupItemEntity.Type.KANJI_ENTRY, kanjiEntry.getId());

		for (UserGroupEntity currentUserGroupEntity : userGroupEntityListForItemId) {

			TableRow userGroupTableRow = new TableRow();

			OnClickListener	deleteItemIdFromUserGroupOnClickListener = createDeleteItemIdFromUserGroupOnClickListener(dataManager, kanjiEntry, currentUserGroupEntity, userGroupTableRow);

			StringValue	userGroupNameStringValue = new StringValue(currentUserGroupEntity.getName(), 15.0f, 0);
			Image userGroupNameDeleteImage = new Image(getResources().getDrawable(JapaneseAndroidLearnHelperApplication.getInstance().getThemeType().getDeleteIconId()), 0);

			userGroupNameStringValue.setOnClickListener(deleteItemIdFromUserGroupOnClickListener);
			userGroupNameDeleteImage.setOnClickListener(deleteItemIdFromUserGroupOnClickListener);

			userGroupTableRow.addScreenItem(userGroupNameStringValue);
			userGroupTableRow.addScreenItem(userGroupNameDeleteImage);

			report.add(userGroupTableRow);
		}

		report.add(new StringValue("", 15.0f, 2));

		// find kanji in words
		pl.idedyk.android.japaneselearnhelper.screen.Button findWordWithKanji = new pl.idedyk.android.japaneselearnhelper.screen.Button(
				getString(R.string.kanji_details_find_kanji_in_words));
		
		findWordWithKanji.setOnClickListener(new OnClickListener() {
			
			public void onClick(View view) {

				Intent intent = new Intent(getApplicationContext(), WordDictionaryTab.class);
				
				FindWordRequest findWordRequest = new FindWordRequest();
				
				findWordRequest.word = kanjiEntry.getKanji();
				findWordRequest.searchKanji = true;
				findWordRequest.searchKana = false;
				findWordRequest.searchRomaji = false;
				findWordRequest.searchTranslate = false;
				findWordRequest.searchInfo = false;
				findWordRequest.searchGrammaFormAndExamples = false;
				
				findWordRequest.wordPlaceSearch = WordPlaceSearch.ANY_PLACE;
				
				findWordRequest.dictionaryEntryTypeList = null;
				
				intent.putExtra("findWordRequest", findWordRequest);
				
				startActivity(intent);
			}
		});
		
		report.add(findWordWithKanji);
		
		return report;
	}

	private Image createFavouriteKanjiStar(DictionaryManagerCommon dictionaryManager, final KanjiCharacterInfo kanjiEntry) {

		final DataManager dataManager = dictionaryManager.getDataManager();

		UserGroupEntity startUserGroup = null;

		try {
			startUserGroup = dataManager.getStarUserGroup();

		} catch (DataManagerException e) {
			throw new RuntimeException(e);
		}

		final UserGroupEntity startUserGroup2 = startUserGroup;

		boolean kanjiEntryExistsInFavouriteList = dataManager.isItemIdExistsInUserGroup(startUserGroup, UserGroupItemEntity.Type.KANJI_ENTRY, kanjiEntry.getId());

		final int starBigOff = android.R.drawable.star_big_off;
		final int starBigOn = android.R.drawable.star_big_on;

		final Image starImage = new Image(getResources().getDrawable(kanjiEntryExistsInFavouriteList == false ? starBigOff : starBigOn), 0);

		starImage.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				boolean kanjiEntryExistsInFavouriteList = dataManager.isItemIdExistsInUserGroup(startUserGroup2, UserGroupItemEntity.Type.KANJI_ENTRY, kanjiEntry.getId());

				if (kanjiEntryExistsInFavouriteList == false) {
					dataManager.addItemIdToUserGroup(startUserGroup2, UserGroupItemEntity.Type.KANJI_ENTRY, kanjiEntry.getId());

					starImage.changeImage(getResources().getDrawable(starBigOn));

					Toast.makeText(KanjiDetails.this,
							getString(R.string.kanji_details_add_to_star_group), Toast.LENGTH_SHORT).show();


				} else {
					dataManager.deleteItemIdFromUserGroup(startUserGroup2, UserGroupItemEntity.Type.KANJI_ENTRY, kanjiEntry.getId());

					starImage.changeImage(getResources().getDrawable(starBigOff));

					Toast.makeText(KanjiDetails.this,
							getString(R.string.kanji_details_remove_from_star_group), Toast.LENGTH_SHORT).show();
				}
			}
		});

		return starImage;
	}

	private OnClickListener createDeleteItemIdFromUserGroupOnClickListener(final DataManager dataManager, final KanjiCharacterInfo kanjiEntry, final UserGroupEntity userGroupEntity, final TableRow userGroupTableRow) {

		return new OnClickListener() {
			@Override
			public void onClick(View v) {

				final AlertDialog alertDialog = new AlertDialog.Builder(KanjiDetails.this).create();

				alertDialog.setTitle(getString(R.string.kanji_details_delete_item_id_from_user_group_title));
				alertDialog.setMessage(getString(R.string.kanji_details_delete_item_id_from_user_group_message, userGroupEntity.getName()));

				alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.user_group_ok_button), new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

						// usuwamy z bazy danych
						dataManager.deleteItemIdFromUserGroup(userGroupEntity, UserGroupItemEntity.Type.KANJI_ENTRY, kanjiEntry.getId());

						// ukrywamy grupe
						userGroupTableRow.setVisibility(View.GONE);

						// komunikat
						Toast.makeText(KanjiDetails.this, getString(R.string.kanji_details_delete_item_id_from_user_group_toast, userGroupEntity.getName()), Toast.LENGTH_SHORT).show();
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

			Toast.makeText(KanjiDetails.this,
					getString(R.string. word_dictionary_details_clipboard_copy, text), Toast.LENGTH_SHORT).show();
		}
	}
}
