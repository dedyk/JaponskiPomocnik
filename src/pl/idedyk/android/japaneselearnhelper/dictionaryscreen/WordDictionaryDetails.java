package pl.idedyk.android.japaneselearnhelper.dictionaryscreen;

import java.util.ArrayList;
import java.util.List;

import pl.idedyk.android.japaneselearnhelper.R;
import pl.idedyk.android.japaneselearnhelper.dictionary.DictionaryManager;
import pl.idedyk.android.japaneselearnhelper.dictionary.dto.DictionaryEntry;
import pl.idedyk.android.japaneselearnhelper.dictionary.dto.DictionaryEntryType;
import pl.idedyk.android.japaneselearnhelper.dictionary.dto.KanjiEntry;
import pl.idedyk.android.japaneselearnhelper.example.ExampleManager;
import pl.idedyk.android.japaneselearnhelper.example.dto.ExampleGroupTypeElements;
import pl.idedyk.android.japaneselearnhelper.example.dto.ExampleResult;
import pl.idedyk.android.japaneselearnhelper.gramma.GrammaConjugaterManager;
import pl.idedyk.android.japaneselearnhelper.gramma.dto.GrammaFormConjugateGroupTypeElements;
import pl.idedyk.android.japaneselearnhelper.gramma.dto.GrammaFormConjugateResult;
import pl.idedyk.android.japaneselearnhelper.kanji.KanjiDetails;
import pl.idedyk.android.japaneselearnhelper.problem.ReportProblem;
import pl.idedyk.android.japaneselearnhelper.screen.IScreenItem;
import pl.idedyk.android.japaneselearnhelper.screen.StringValue;
import pl.idedyk.android.japaneselearnhelper.screen.TitleItem;
import pl.idedyk.android.japaneselearnhelper.sod.SodActivity;
import pl.idedyk.android.japaneselearnhelper.sod.dto.StrokePathInfo;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

public class WordDictionaryDetails extends Activity {

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

	@Override
	protected void onDestroy() {
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
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.word_dictionary_details);
		
		DictionaryEntry dictionaryEntry = (DictionaryEntry)getIntent().getSerializableExtra("item");
				
		LinearLayout detailsMainLayout = (LinearLayout)findViewById(R.id.word_dictionary_details_main_layout);
		
		final List<IScreenItem> generatedDetails = generateDetails(dictionaryEntry);
		
		fillDetailsMainLayout(generatedDetails, detailsMainLayout);
		
		Button reportProblemButton = (Button)findViewById(R.id.word_dictionary_details_report_problem_button);
		
		reportProblemButton.setOnClickListener(new OnClickListener() {
			
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
				
				Intent reportProblemIntent = ReportProblem.createReportProblemIntent(mailSubject, mailBody.toString(), versionName, versionCode); 
				
				startActivity(Intent.createChooser(reportProblemIntent, chooseEmailClientTitle));
			}
		});
	}

	private List<IScreenItem> generateDetails(DictionaryEntry dictionaryEntry) {
		
		List<IScreenItem> report = new ArrayList<IScreenItem>();
		
		String prefixKana = dictionaryEntry.getPrefixKana();
		String prefixRomaji = dictionaryEntry.getPrefixRomaji();
		
		if (prefixKana != null && prefixKana.length() == 0) {
			prefixKana = null;
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
		
		StringValue kanjiStringValue = new StringValue(kanjiSb.toString(), 35.0f, 0);
		
		if (addKanjiWrite == true) {
			report.add(new StringValue(getString(R.string.word_dictionary_word_anim), 12.0f, 0));
			
			kanjiStringValue.setOnClickListener(new OnClickListener() {
				
				public void onClick(View v) {
					List<List<String>> strokePathsForWord = DictionaryManager.getInstance().getStrokePathsForWord(kanjiSb.toString());
					
					StrokePathInfo strokePathInfo = new StrokePathInfo();
					
					strokePathInfo.setStrokePaths(strokePathsForWord);
					
					Intent intent = new Intent(getApplicationContext(), SodActivity.class);
										
					intent.putExtra("strokePathsInfo", strokePathInfo);
					intent.putExtra("annotateStrokes", false);
					
					startActivity(intent);
				}
			});			
		}
				
		report.add(kanjiStringValue);
		
		// Reading
		report.add(new TitleItem(getString(R.string.word_dictionary_details_reading_label), 0));
		report.add(new StringValue(getString(R.string.word_dictionary_word_anim), 12.0f, 0));
		
		List<String> kanaList = dictionaryEntry.getKanaList();
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
				
				public void onClick(View v) {
					List<List<String>> strokePathsForWord = DictionaryManager.getInstance().getStrokePathsForWord(sb.toString());
					
					StrokePathInfo strokePathInfo = new StrokePathInfo();
					
					strokePathInfo.setStrokePaths(strokePathsForWord);
					
					Intent intent = new Intent(getApplicationContext(), SodActivity.class);
										
					intent.putExtra("strokePathsInfo", strokePathInfo);
					intent.putExtra("annotateStrokes", false);
					
					startActivity(intent);					
				}
			});
			
			report.add(readingStringValue);
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
		boolean addableDictionaryEntryTypeInfo = DictionaryEntryType.isAddableDictionaryEntryTypeInfo(dictionaryEntry.getDictionaryEntryType());
		
		if (addableDictionaryEntryTypeInfo == true) {
			report.add(new TitleItem(getString(R.string.word_dictionary_details_part_of_speech), 0));
			
			report.add(new StringValue(dictionaryEntry.getDictionaryEntryType().getName(), 20.0f, 0));			
		}
		
		List<KanjiEntry> knownKanji = null;
		
		if (dictionaryEntry.isKanjiExists() == true) {
			knownKanji = DictionaryManager.getInstance().findKnownKanji(dictionaryEntry.getKanji());
		}
		
		if (knownKanji != null && knownKanji.size() > 0) {
			
			report.add(new StringValue("", 15.0f, 2));
			report.add(new TitleItem(getString(R.string.word_dictionary_known_kanji), 0));
			report.add(new StringValue(getString(R.string.word_dictionary_known_kanji_info), 12.0f, 0));
			
			for (int knownKanjiIdx = 0; knownKanjiIdx < knownKanji.size(); ++knownKanjiIdx) {
				
				final KanjiEntry kanjiEntry = knownKanji.get(knownKanjiIdx);				
				
				OnClickListener kanjiOnClickListener = new OnClickListener() {
					
					public void onClick(View v) {
						
						// show kanji details
						
						Intent intent = new Intent(getApplicationContext(), KanjiDetails.class);
						
						intent.putExtra("item", kanjiEntry);
						
						startActivity(intent);
					}
				};
				
				StringValue knownKanjiStringValue = new StringValue(kanjiEntry.getKanji(), 16.0f, 1);
				StringValue polishTranslateStringValue = new StringValue(kanjiEntry.getPolishTranslates().toString(), 16.0f, 1);
				
				knownKanjiStringValue.setOnClickListener(kanjiOnClickListener);
				polishTranslateStringValue.setOnClickListener(kanjiOnClickListener);
											
				report.add(knownKanjiStringValue);
				report.add(polishTranslateStringValue);
				
				if (knownKanjiIdx != knownKanji.size() - 1) {
					report.add(new StringValue("", 10.0f, 1));
				}

			}
		}
				
		// Conjugater
		List<GrammaFormConjugateGroupTypeElements> grammaFormConjugateGroupTypeElementsList = GrammaConjugaterManager.getGrammaConjufateResult(dictionaryEntry);
		
		if (grammaFormConjugateGroupTypeElementsList != null) {
			report.add(new StringValue("", 15.0f, 2));
			report.add(new TitleItem(getString(R.string.word_dictionary_details_conjugater_label), 0));
			
			for (GrammaFormConjugateGroupTypeElements currentGrammaFormConjugateGroupTypeElements : 
				grammaFormConjugateGroupTypeElementsList) {
				
				report.add(new TitleItem(currentGrammaFormConjugateGroupTypeElements.getGrammaFormConjugateGroupType().getName(), 1));
				
				List<GrammaFormConjugateResult> grammaFormConjugateResults = currentGrammaFormConjugateGroupTypeElements.getGrammaFormConjugateResults();
				
				for (GrammaFormConjugateResult currentGrammaFormConjugateResult : grammaFormConjugateResults) {
					
					if (currentGrammaFormConjugateResult.getResultType().isShow() == true) {
						report.add(new TitleItem(currentGrammaFormConjugateResult.getResultType().getName(), 2));
					}
					
					addGrammaFormConjugateResult(report, prefixKana, prefixRomaji, currentGrammaFormConjugateResult);
				}
				
				report.add(new StringValue("", 15.0f, 1));
			}
		}	
		
		// Exampler
		List<ExampleGroupTypeElements> exampleGroupTypeElementsList = ExampleManager.getExamples(dictionaryEntry);
		
		if (exampleGroupTypeElementsList != null) {
			
			if (grammaFormConjugateGroupTypeElementsList == null) {
				report.add(new StringValue("", 15.0f, 2));	
			}
			
			report.add(new TitleItem(getString(R.string.word_dictionary_details_exampler_label), 0));
			
			for (ExampleGroupTypeElements currentExampleGroupTypeElements : exampleGroupTypeElementsList) {
				
				report.add(new TitleItem(currentExampleGroupTypeElements.getExampleGroupType().getName(), 1));
				
				String exampleGroupInfo = currentExampleGroupTypeElements.getExampleGroupType().getInfo(); 
				
				if (exampleGroupInfo != null) {
					report.add(new StringValue(exampleGroupInfo, 15.0f, 1));
				}
				
				List<ExampleResult> exampleResults = currentExampleGroupTypeElements.getExampleResults();
				
				for (ExampleResult currentExampleResult : exampleResults) {					
					addExampleResult(report, prefixKana, prefixRomaji, currentExampleResult);
				}
				
				report.add(new StringValue("", 15.0f, 1));
			}
		}
		
		return report;
	}
	
	private void fillDetailsMainLayout(List<IScreenItem> generatedDetails, LinearLayout detailsMainLayout) {
		
		for (IScreenItem currentDetailsReportItem : generatedDetails) {
			currentDetailsReportItem.generate(this, getResources(), detailsMainLayout);			
		}
	}
	
	private void addGrammaFormConjugateResult(List<IScreenItem> report, String prefixKana, String prefixRomaji, GrammaFormConjugateResult grammaFormConjugateResult) {
		
		String grammaFormKanji = grammaFormConjugateResult.getKanji();
		
		StringBuffer grammaFormKanjiSb = new StringBuffer();
		
		if (grammaFormKanji != null) {
			if (prefixKana != null) {
				grammaFormKanjiSb.append("(").append(prefixKana).append(") ");
			}
			
			grammaFormKanjiSb.append(grammaFormKanji);
			
			report.add(new StringValue(grammaFormKanjiSb.toString(), 15.0f, 2));
		}
		
		List<String> grammaFormKanaList = grammaFormConjugateResult.getKanaList();
		List<String> grammaFormRomajiList = grammaFormConjugateResult.getRomajiList();
		
		for (int idx = 0; idx < grammaFormKanaList.size(); ++idx) {
			
			StringBuffer sb = new StringBuffer();
			
			if (prefixKana != null) {
				sb.append("(").append(prefixKana).append(") ");
			}
			
			sb.append(grammaFormKanaList.get(idx));
						
			report.add(new StringValue(sb.toString(), 15.0f, 2));
			
			StringBuffer grammaFormRomajiSb = new StringBuffer();
			
			if (prefixRomaji != null) {
				grammaFormRomajiSb.append("(").append(prefixRomaji).append(") ");
			}
			
			grammaFormRomajiSb.append(grammaFormRomajiList.get(idx));
			
			report.add(new StringValue(grammaFormRomajiSb.toString(), 15.0f, 2));
		}
		
		GrammaFormConjugateResult alternative = grammaFormConjugateResult.getAlternative();
		
		if (alternative != null) {
			report.add(new StringValue("", 5.0f, 1));
			
			addGrammaFormConjugateResult(report, prefixKana, prefixRomaji, alternative);	
		}		
	}
	
	private void addExampleResult(List<IScreenItem> report, String prefixKana, String prefixRomaji, ExampleResult exampleResult) {
		
		String exampleKanji = exampleResult.getKanji();
		
		StringBuffer exampleKanjiSb = new StringBuffer();
		
		if (exampleKanji != null) {
			if (prefixKana != null && exampleResult.isCanAddPrefix() == true) {
				exampleKanjiSb.append("(").append(prefixKana).append(") ");
			}
			
			exampleKanjiSb.append(exampleKanji);
			
			report.add(new StringValue(exampleKanjiSb.toString(), 15.0f, 2));
		}
		
		List<String> exampleKanaList = exampleResult.getKanaList();
		List<String> exampleRomajiList = exampleResult.getRomajiList();
		
		for (int idx = 0; idx < exampleKanaList.size(); ++idx) {
			
			StringBuffer sb = new StringBuffer();
			
			if (prefixKana != null && exampleResult.isCanAddPrefix() == true) {
				sb.append("(").append(prefixKana).append(") ");
			}
			
			sb.append(exampleKanaList.get(idx));
						
			report.add(new StringValue(sb.toString(), 15.0f, 2));
			
			StringBuffer exampleRomajiSb = new StringBuffer();

			if (prefixRomaji != null && exampleResult.isCanAddPrefix() == true) {
				exampleRomajiSb.append("(").append(prefixRomaji).append(") ");
			}
			
			exampleRomajiSb.append(exampleRomajiList.get(idx));
			
			report.add(new StringValue(exampleRomajiSb.toString(), 15.0f, 2));
		}
		
		ExampleResult alternative = exampleResult.getAlternative();
		
		if (alternative != null) {
			report.add(new StringValue("", 5.0f, 1));
			
			addExampleResult(report, prefixKana, prefixRomaji, alternative);	
		}		
	}
	
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
		
		StringBuffer sm = new StringBuffer();
		
		sm.append("$$$$$$$$$$$$$$$$$Z$$$$$$$$$$$$$$$Z$$$$$$$$$$$$$$$$$$$Z$$ZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZ$~ZZZZZZZZZZZZZZZZZZZZZ$ZZ$$$$$$$$Z$$$$$$Z$\n");
		sm.append("$$$$$$$$$$$$$Z$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$Z$$$$$$ZZ$ZZZZZZZZZZZZZZZZZZZZZZZ$ZZZZZZZZZZZZ8I++++IOZZZZZZZZZZ$ZZZZZZZZZZZZZZ$$$$$$$$Z$$$$$$$$\n");
		sm.append("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$ZZZZ$ZZZ$$$$ZZZZZZZZZZZ$ZZZZZZZZZZZZZZZ$ZZZZZZZZZZ7?Z??+??I?+OZZZZZZZZZZZZZZZZZZ$$Z$$$Z$$$$$$Z$$$$$$$7\n");
		sm.append("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$ZZZZZZZZZZZZZZZZZZZZ$ZZZZZZZZZZZZZZZZZNZ?+??78D$Z???O=ONO=???$MZZZZZZZZZZZZ$$Z$I$$$$$Z$$$$$$$$$$$$$$$7\n");
		sm.append("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$ZZZZZZZZZZZZZZZZZZZZ+?IZ?+OZZZZZO8Z78?????$I?++I$NN?Z8O:::Z+$77?D.Z=$Z$$$$$Z$$$$$$7$$$$Z+$$$$$$$$$$$$$7\n");
		sm.append("$$$$$$$$$$$$$$$$$$$7$$$$$$$$$$$$$$$$Z$ZZZZZZZZZZZZZZZZ8+??+?+7???N+????????ZI+??????????+DOO:8::8D777$$ZZZZ$$$$$$Z$$$$Z$$$$$$$$$$$$$$$$$$$$7\n");
		sm.append("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$ZZZZZZZZ$ZZZZZZ$??8ZOZZ8OI?I?????I7$????????III????????OZZO7787Z$$$.Z$$$$$$$$$$$$$$$$$$$$$$$$+,$$$$$$7\n");
		sm.append("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$ZZZZZZZZZZZZZZZ8??78::~D7+????????I7?8OI??I777777777I??II+DI7787NZ$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$7I\n");
		sm.append("$$$$$$$$$$$$,$$$$$$$$$$$$$7$$$$$$$$$$$ZZZZZZZZZZZ?OZO7O8$:=D????????$OO7I7+??????I7777777777???$I777O8$$$$$$$$$Z$$$Z$$$$$$$$$$$$$$$$$$$7777I\n");
		sm.append("7$$$$$$$$$$$$$$$$$$$$7?$$$$$$$$$$$$$$$$$ZZZZZZZZZZZZ8I$IZO8???????I77777$?????????I7777777777???+777I7$$$$$$$$Z$$$$$$$Z?$$$$$$$$$$$$$$$$$77I\n");
		sm.append("777$$$$$$$$$$$7$7$$$$$$$$$$$$$$$$$$$$$$$$$$$ZZ$ZZZZZZO77?8????????????I77?+I+??????I7777777777IZI7$777O$$$$?$$$$$$$$$?Z$$$$$$$$$$$$$$$$$777I\n");
		sm.append("7777$$$$$$$$$$7777777$$$$$$$$$$$$$$$$$$$$Z$$$$$Z$$$$$DO+??+????????77I7I78O~~:+?????7777777777777$77777$$$$$$$$$$$ZZO+$$$$$$$$$$$$$$$$$7777I\n");
		sm.append("777777$$$$$$$$$77I77777$$$$$$$$$$$$$$$$$$$$$$$$$$$7$$??????$?????I77Z:7$~=~~~~~????O77777777777777D777?8$$$$$$$$$$$$$$$$$$$$$$$$$$$$$77777I?\n");
		sm.append("7777777$$$$$$$$7$777777$$$$$$$$$$$$$$$$$$$$$$$$$$$$$8?+??????????777~~~~~~~~~78???IO7?I777777777I7II77II$Z$$$$$$$$$$$$$$$$$$$$$$$$$7777I7II?\n");
		sm.append("777777777777$$$$77$7777$$$$$$$$$$$$$$$$$$$$$$$$$$$$Z???????I?????77I:~~~~~~DD8????7?8~I778777777II7777I?O$$$$ZZ$$$$$$$$$$$$$$$$$$$777777III+\n");
		sm.append("77777777777777$$$$$7$7$$$$$$$$$$$$$$$$$$$$$$?$$$$$$?D???7I77??++7777DD7Z~:8IIZ++O7$D~DII7N77777777II777?I$$$$$$$.$$$$$$$$$$$$$777777777I7II+\n");
		sm.append("7777777777777777$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$8M??I7777I??+8?O777IO$?Z8ZIIII=~~?DI7I$I77777II7I7777??8$$$$$$$$$$$$$$I$$$777777777IIIIII+\n");
		sm.append("7777777777777777777$$$$$$$$$$$$$$$$$$$$$$$?$$$$$$$Z??D77I77I??I8+D88$ZOIZ:OZ?II~~~I77+:DI7IO7777Z777777I??7$$=$$$$$$=7777777777777IIIIIIII?+\n");
		sm.append("7777777777777777777$:$$$$$$$$$$$$I$$$$$$$$$$$$$$$$$?87777777N???8~~~8$7?I7OZDO~~+N+,,~8~8+DI7777I$777777??877777777777777I777777IIIIIIIII??+\n");
		sm.append("I777777777777777777$7$$?$$$$$$$$$$$$$$$$$$$$$$$$$$$877877777+:$++=+~~~~~7ZII~~+::=OZON:,~~8ODI77$$877777I?I7777777777II77I77IIIIIIIIIII????=\n");
		sm.append("IIIII7777777777777777$$$$$$$$$$7$7$$$$$7$$7$$$$7$$Z?7II777777?~~~~~+=DNZ$=~~~~~~:87:8OD:+~~~N=8I7$$I7777I??7777I7I7II77I7IIIIIIIIIIIIIIIII?+\n");
		sm.append("????IIIIII777777777777$$$$$$$$77$7$777777777$777$$7?8II777I777I+?+=N=DD.~~~~~~~~~8D:?:,::~~~+:~O777NI7777??D7777IIII77?IIIIIIIIIIIIIIIIII??+\n");
		sm.append("??????IIIIIIII77777777777777$$$$$$?777777777777777+?IDO7III7I:~~~D7~O:DO8:~~~~~~~~=OOND::~~~::~?777$D7777???II7I7I7I7IIIIIII7IIIIIIIII?IIII+\n");
		sm.append("???????IIIIIIIIIIII7777777777777777$7777777777777O??I7II7ZD?77?8=D:=O::N+=~~~~~~~:=OOO8:~~~=~$=$7777??777I??7IIIII7I7IIIIIIIIIIIIIIIIIII???=\n");
		sm.append("?????IIIIIIIIIIIIIIII7I7777777777777$777777777777I??I77I?I7?I?~=~~::N,~N:~~~~~~~~~::,,+~~~~~~~O777778Z7777???ZIII7II77II7IIIIIIIII?IIII????=\n");
		sm.append("??????II??IIIIIIIIIIIII777I777777777777777777777$???7777ZD8Z+++~Z~=::8OOO?~~O=~~~~~~~~~~~~~:=$777II7$II777????I7II7IIIIIIIIIIIIII???++++++++\n");
		sm.append("????I???I??????IIIIIIIIIIIIII77ZO77777,777777777Z???7777N7I7I77~7:~~::NO8:~~D~~~~~~~~~~~~~~ZN77IIIIII$D777I???87IIIIIIIIIIIIIII???+++++++++=\n");
		sm.append("??????I??????+???IIIIIIIIIIIII$:?77777777777II77????777I777777I::~~~~~~$:~~~~=~=~~~~~~~~~~~Z77IIIIIII8II77I????7?I????I??????I???+++++++++++\n");
		sm.append("??++++?????++++??+?+????IIIIII7:,OII7IIIIIIIIIIZ???I7778I77777778~$~~~~~+~~~~8III8~~~~~~~~8IIII7IIIIII7O777????$???????I?I???+++++=+++++=++~\n");
		sm.append("?I?+????+??+++++I?++I??????III$,:,8IIIIIIIIIIIII???I77ID777777777IDN~~~~~~~~~8IIII8~~~~~~$IIIII=IIIII777Z77?????+??????????+++++?+=+======+~\n");
		sm.append("++??+I+++?+??+++?+++I?+?I????I??:::OIIIIIII???Z????77787I77777777ZZZD~~~~~~~~~7II78~~~~:$IIIIIIIIIIII?O7II7I????7????II??+?I?++++======+===:\n");
		sm.append("++++?++?+??+??++++++++?+?+II????8:::$I????????I????77DOIIII7III77:D877?~~~~~~~=7I$~~~~8DI8IIIIIIIIIII??7787I?????$????????+++++===+========:\n");
		sm.append("+++?+I++++?+++++?+?I++I+++?++??IO?:::,=??????+?????77I$IIIIIIIIIIIII7IIIO7:~~~~~~~~~D~~=III????????????Z77O7????II+???++++++++===========~=:\n");
		sm.append("?+???+++++++++=I7$$Z~,::::::::::::::::,I??+++Z????II$ZIIIIIIIIIIIIIIIIIIIII?NO$+~+N7~~~=7I??++++??++++++$7I7I?????8?++++++++++===+=====~==~,\n");
		sm.append("+++++?+?+++++++::::::::::=:::::::::::::,=+++++????7D7Z???????O:DI???IIIIIIIII7~~~~~~~~Z$$ZO$ZDOOON++++++$7787???????++++++++++=========~~~=:\n");
		sm.append("+?++++++++++++++==+==+=I8Z+,~I7I::::::::D+++O????II7$????????$::8???IIIIIIIIIZ~$~~Z$$$$D88M?,,:::,,7O=+++Z7III?????I+====+++++========~===~:\n");
		sm.append("?????++++++++++++?+++++?N:::::::::::::::,O+=+????7$7Z????????=::8???????IIIII7$?OZZI8D$N,:::::::::::::N++?I777??????~=====++=+=~=======~~~~,\n");
		sm.append("====???+++++++++++?++++O:::::::::::::~,:::N$?????O7$++++????+,::Z???????III+ZZO87MDI$Z:::::::::::::::::~==D778I?????7===========+=====~~=~~:\n");
		sm.append("~~~~~~++++++++++++++++?=77:::::::::=:::::::Z????877Z+++++??+N:::?++?D$Z?$$$$N++$Z$Z8,:::::::::::::::::::8+=777O?????I$+===========+~~~~~~~~:\n");
		sm.append("~~~~~~~~~~=++++++=++++++=$::::::::~,,:::::::7??O77I++O,:,8++,::$+::$8O78$$$$~=8O88?,::::=ZZ?~~+ZI,::::::,?+Z77I???????=========~===~~=~~~~=:\n");
		sm.append("~~~~~~~~~~~~~=+=========+:::::::::~::::::::::~87778++$::::?$:::8::,Z=Z:8$$?~7NO8:~:::+I::::::::::::I:::::8==I77$??????Z=~=~~~~~~~=~~~~~~===:\n");
		sm.append("~~~~~~~~~~~~~~=+======~==O:::::,.,7I?:::::::::$I7I=+++7?+MI:,78:,.=$IZ$D$+~8DD,::I:I,:::::::::::::::~,:::7~=D7777???III$==~=~~~=~=~~=======:\n");
		sm.append("=?~~~~~~~~~~~~~=+====~=~Z:::=$:~=~~=?::::::::::=I8=====~Z8Z,::::::.$8N$Z$?O8,:::,,=::::::::::::::::::,:::O7==777I?I??II?~~~~~~~=~~==~~=====:\n");
		sm.append("~~?=~~~~~~=~~~~~~====~~:::,$~~~~~~~~~D::::::::::.:====+I:::::::::=IDO??DND,:::::,:D8?:~~78$:::::::::::=::8D==$7777??I??I$~~~~~~~~=~========:\n");
		sm.append("~~~~~~~~~~~~~~~~~~~~==O::I~==~~~~~~==~~:::::::::::8==~O:::::,N,::::~8?N$,I$$$O:==~~~~~~~~~~~~:$:::::::$:8OO~~~N777I?I?????~~~=~~~~=======~~:\n");
		sm.append("~~~~~~~~~~~~~~~~~~~~:Z:+~~~~==~~~~~~~~O?Z::::::::::I:~:+~::,::::::$,:7~8$$$$$+Z~~~~~~~~~~~~~~~~~::::::ZOOOO?~~:7777I????I?~~==~~~~====~~~~~:\n");
		sm.append("~~~~~~~~~+=~~=~~~~~:I:8==~~~~~~~~~~~~:???D::::::::::,I~::I::::::::::=8Z88$$$Z~~~~~~~~~~~~~~~~~~~=?::::OOOOOD~~~I7777Z+????$=~~~~~~==~~~~~~~:\n");
		sm.append("~~~~~~~~~~~~~~~~~~~~~~~====~~~~~~~~~~$I?II+:::::::::::Z::?:::::N::::ZI$$$$+~~~~~~~~~~~~~~~~~~~~~N,:Z::OOOOOO+~~~?7777$?I???O~~~~===~~~~~~=~:\n");
		sm.append("~~~~~~~~+~~~~~=~=~~~~~~====~~~~~~~~~:??Z?II=:::::::::::,,:::$:::::DD7Z$8~~~~~~~~~~~~~~~~~~~~~:N,::::::OOOOOO8~~~777777?+I???=~+====~~~~~~~~:\n");
		sm.append("~~~~~~~~+++~~~~:~~~~~~~~====~~~~~~~=8?8I?777:::::::::::::I:::$::~?OIID~~~~~~~~~~~~~~~~~~~~==7,:::::::~OOOOD8D=~~=O77777??????=====~~~~~~~~=:\n");
		sm.append("~~~~~~~~~~=+~~~~+~==~~~~~~+========+?8??$ZD,8:::::::::::::~:,:$8ZID~=~~~~~~~~~~~~~~~~~~~~=?::::::::::DD,:~~~~~~=~~I77777+????8====~~~~~~~?~=\n");
		sm.append("+~~?=~~+~=~~~~~~====++====~========7$?I8$~::~O:::::::::,8$$$8Z8$D~==~~=~~~~~~~~~~~~~~~~=8:::::::::::7=~~~~~~~~=~~=D777777I+???===~~~~~~~~~~,\n");
		sm.append("==~~~~~~~~~~~~~~~~~~~+?~=~~~==~=~~II+IO$7:::::::::::::O$$$Z$$O$?=~==~~=~~~~~~~~~~~~~=~Z:::::::::::::~~~~~=~=~~~~~~~O77777II???I~~~~~~~~~~~=,\n");
		sm.append("~~~~~~~~~~~=======~=~~+==~~~=====~I?II$$::::::8:::::,Z$$Z$$$$$8~~~~~~~~=~~~~~~~~~=~~D::::::::::::::======~~~~=~~~~~=777777I++?IO~~~~+~~~~:~,\n");
		sm.append("~=~~~~~~~=======~=~+~~~=~=~======8O?I8$Z::::::,=::::Z$$$$$$8$$+==~~=~~~=~~~~~=~~=~D:::::::::::::::=========~~~~~~=~~$777777??+???~~~~~=~~~~:\n");
		sm.append("==?===+=~=+++==+~I~=+==+====+=+==8IIIO$$:::::::::::D$$$8$$$$$$~====~=~======~~=??::::::::::::::,8==~=~==========~~~~+I777777?+???~~=~~~~~~~:\n");
		sm.append("~~=?+?==+===+==+~+=+====+====?==D?I?$8$$D:::::::,~:$$$$$$$O$$$~===~====~=====ZI?,::::::::::::$I========~===+=======~=D777777I?$?IO~~~=~~=~~:\n");
		sm.append("~~~=~~~+===============+~~======II?77D8$$O,::::::DN$$$Z$$$O$$$~=======~===:8::::::::~I??=::~,=====+=+======+==========7777777II$??~=~~==~~~:\n");
		sm.append("==~~~==~=~==~==?==+=+=+========N?II777D$O$$$$ZOOZ~~$$$O$$$8$$$7~===~=====Z:::::::::::::::::?======I===~~~~============87777777??I?O=====~==:\n");
		sm.append("=I~?=======+~=+++====?========O+II77777Z78OZODI~===O$$Z$$$$$$$O~======O:::::::::::::::::::Z=?+==?=OO$$$$$$Z====~======~Z777777II?7?7=+=~=~=:\n");
		sm.append("~~=?=?========+===============7II777777========++??=O$$O$$$O$$$8===~D::::::::::::::::::::D===OO$$$$$$$$$$$O============?I777777IIID?~======~\n");
		sm.append("++===========++=====+?===+===NIII77777N===========++?$Z$$$$$8$$$$8,:::::::::::::::::::::O+8$$$$$$$$$$$$$$$~=?===========O777777IIIIOD======~\n");
		sm.append("==========+?+?====+?========D?II77777I~========+==+=+=?~~O$$$O=~:::::::::::::::::::::::7$$$$$$$$$$$$$$$$$I==~===========+777777$?II?++=====:\n");
		sm.append("~+=========++===?=========+=?III77777N===============+++~=??Z::::::::::::::::::::::::::8$$$$$$$$$$$$$$$$O================77777777IIII7=====~\n");
		sm.append("=========++===+============ZIII777777====~=?=?===+=~?==+=+==+::::::::::::::::::::::::::$$O$$$$$Z$$$$$$$$+================O7777777III?D$====~\n");
		sm.append("========+=================~?III777777===+=========~==+~~=~==I:::::::::::::::::::::::::=8?=IO$Z$$$$$$$$$O===+==============$7777777III?I====:\n");
		sm.append("=============+++=+==+=====DIIII7777$8=======~===~=+~===~~~~:$::::::::::::::::::::::$:::::::::$$$$$$$$$$8Z+==+===+=========77777777IIII$+===:");
		
		StringValue smStringValue = new StringValue(sm.toString(), 2.8f, 0);
		
		smStringValue.setTypeface(Typeface.MONOSPACE);
		smStringValue.setTextColor(Color.BLACK);
		smStringValue.setBackgroundColor(Color.WHITE);
		smStringValue.setGravity(Gravity.CENTER);
		
		return smStringValue;
	}
}
