package pl.idedyk.android.japaneselearnhelper.kana;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import pl.idedyk.android.japaneselearnhelper.JapaneseAndroidLearnHelperApplication;
import pl.idedyk.android.japaneselearnhelper.MenuShorterHelper;
import pl.idedyk.android.japaneselearnhelper.R;
import pl.idedyk.android.japaneselearnhelper.screen.IScreenItem;
import pl.idedyk.android.japaneselearnhelper.screen.StringValue;
import pl.idedyk.android.japaneselearnhelper.screen.TitleItem;
import pl.idedyk.android.japaneselearnhelper.sod.SodActivity;
import pl.idedyk.android.japaneselearnhelper.sod.dto.StrokePathInfo;
import pl.idedyk.japanese.dictionary.api.dto.KanaEntry;
import pl.idedyk.japanese.dictionary.api.dto.KanjivgEntry;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TableLayout;

public class Kana extends Activity {

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		super.onCreateOptionsMenu(menu);

		JapaneseAndroidLearnHelperApplication.getInstance().logScreen(this, getString(R.string.logs_kana));
		
		MenuShorterHelper.onCreateOptionsMenu(menu);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);

		return MenuShorterHelper.onOptionsItemSelected(item, getApplicationContext(), this);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		JapaneseAndroidLearnHelperApplication.getInstance().setContentViewAndTheme(this, R.layout.word_kana);

		TableLayout mainTableLayout = (TableLayout) findViewById(R.id.word_kana_main_layout);

		final List<IScreenItem> screenItems = new ArrayList<IScreenItem>();

		Map<String, KanaEntry> kanaCache = JapaneseAndroidLearnHelperApplication.getInstance()
				.getDictionaryManager(Kana.this).getKanaHelper().getKanaCache(true);

		Typeface radicalTypeface = JapaneseAndroidLearnHelperApplication.getInstance().getBabelStoneHanSubset(getAssets());
		
		generateHiraganaTable(screenItems, kanaCache, radicalTypeface);
		generateKatakanaTable(screenItems, kanaCache, radicalTypeface);
		generateKatakanaAdditionalTable(screenItems, kanaCache, radicalTypeface);

		fillMainLayout(screenItems, mainTableLayout);

		/*
		Button reportProblemButton = (Button) findViewById(R.id.word_kana_report_problem_button);

		reportProblemButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				StringBuffer detailsSb = new StringBuffer();

				for (IScreenItem currentscreenItem : screenItems) {
					detailsSb.append(currentscreenItem.toString()).append("\n\n");
				}

				String chooseEmailClientTitle = getString(R.string.choose_email_client);

				String mailSubject = getString(R.string.kana_report_problem_email_subject);

				String mailBody = getString(R.string.kana_report_problem_email_body, detailsSb.toString());

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
		*/
	}

	private void generateHiraganaTable(List<IScreenItem> screenItems, Map<String, KanaEntry> kanaCache, Typeface babelStoneHanTypeface) {

		screenItems.add(new TitleItem(getString(R.string.kana_hiragana_label), 0));
		screenItems.add(new StringValue(getString(R.string.kana_info), 12.0f, 0));
		screenItems.add(new StringValue(getString(R.string.kana_info2), 12.0f, 0));

		pl.idedyk.android.japaneselearnhelper.screen.TableRow a_tableRow = new pl.idedyk.android.japaneselearnhelper.screen.TableRow();

		addKanaItem(a_tableRow, kanaCache, "あ", babelStoneHanTypeface);
		addKanaItem(a_tableRow, kanaCache, "い", babelStoneHanTypeface);
		addKanaItem(a_tableRow, kanaCache, "う", babelStoneHanTypeface);
		addKanaItem(a_tableRow, kanaCache, "え", babelStoneHanTypeface);
		addKanaItem(a_tableRow, kanaCache, "お", babelStoneHanTypeface);

		screenItems.add(a_tableRow);

		pl.idedyk.android.japaneselearnhelper.screen.TableRow k_tableRow = new pl.idedyk.android.japaneselearnhelper.screen.TableRow();

		addKanaItem(k_tableRow, kanaCache, "か", babelStoneHanTypeface);
		addKanaItem(k_tableRow, kanaCache, "き", babelStoneHanTypeface);
		addKanaItem(k_tableRow, kanaCache, "く", babelStoneHanTypeface);
		addKanaItem(k_tableRow, kanaCache, "け", babelStoneHanTypeface);
		addKanaItem(k_tableRow, kanaCache, "こ", babelStoneHanTypeface);

		screenItems.add(k_tableRow);

		pl.idedyk.android.japaneselearnhelper.screen.TableRow s_tableRow = new pl.idedyk.android.japaneselearnhelper.screen.TableRow();

		addKanaItem(s_tableRow, kanaCache, "さ", babelStoneHanTypeface);
		addKanaItem(s_tableRow, kanaCache, "し", babelStoneHanTypeface);
		addKanaItem(s_tableRow, kanaCache, "す", babelStoneHanTypeface);
		addKanaItem(s_tableRow, kanaCache, "せ", babelStoneHanTypeface);
		addKanaItem(s_tableRow, kanaCache, "そ", babelStoneHanTypeface);

		screenItems.add(s_tableRow);

		pl.idedyk.android.japaneselearnhelper.screen.TableRow t_tableRow = new pl.idedyk.android.japaneselearnhelper.screen.TableRow();

		addKanaItem(t_tableRow, kanaCache, "た", babelStoneHanTypeface);
		addKanaItem(t_tableRow, kanaCache, "ち", babelStoneHanTypeface);
		addKanaItem(t_tableRow, kanaCache, "つ", babelStoneHanTypeface);
		addKanaItem(t_tableRow, kanaCache, "て", babelStoneHanTypeface);
		addKanaItem(t_tableRow, kanaCache, "と", babelStoneHanTypeface);

		screenItems.add(t_tableRow);

		pl.idedyk.android.japaneselearnhelper.screen.TableRow n_tableRow = new pl.idedyk.android.japaneselearnhelper.screen.TableRow();

		addKanaItem(n_tableRow, kanaCache, "な", babelStoneHanTypeface);
		addKanaItem(n_tableRow, kanaCache, "に", babelStoneHanTypeface);
		addKanaItem(n_tableRow, kanaCache, "ぬ", babelStoneHanTypeface);
		addKanaItem(n_tableRow, kanaCache, "ね", babelStoneHanTypeface);
		addKanaItem(n_tableRow, kanaCache, "の", babelStoneHanTypeface);

		screenItems.add(n_tableRow);

		pl.idedyk.android.japaneselearnhelper.screen.TableRow h_tableRow = new pl.idedyk.android.japaneselearnhelper.screen.TableRow();

		addKanaItem(h_tableRow, kanaCache, "は", babelStoneHanTypeface);
		addKanaItem(h_tableRow, kanaCache, "ひ", babelStoneHanTypeface);
		addKanaItem(h_tableRow, kanaCache, "ふ", babelStoneHanTypeface);
		addKanaItem(h_tableRow, kanaCache, "へ", babelStoneHanTypeface);
		addKanaItem(h_tableRow, kanaCache, "ほ", babelStoneHanTypeface);

		screenItems.add(h_tableRow);

		pl.idedyk.android.japaneselearnhelper.screen.TableRow m_tableRow = new pl.idedyk.android.japaneselearnhelper.screen.TableRow();

		addKanaItem(m_tableRow, kanaCache, "ま", babelStoneHanTypeface);
		addKanaItem(m_tableRow, kanaCache, "み", babelStoneHanTypeface);
		addKanaItem(m_tableRow, kanaCache, "む", babelStoneHanTypeface);
		addKanaItem(m_tableRow, kanaCache, "め", babelStoneHanTypeface);
		addKanaItem(m_tableRow, kanaCache, "も", babelStoneHanTypeface);

		screenItems.add(m_tableRow);

		pl.idedyk.android.japaneselearnhelper.screen.TableRow y_tableRow = new pl.idedyk.android.japaneselearnhelper.screen.TableRow();

		addKanaItem(y_tableRow, kanaCache, "や", babelStoneHanTypeface);
		addKanaItem(y_tableRow, kanaCache, null, babelStoneHanTypeface);
		addKanaItem(y_tableRow, kanaCache, "ゆ", babelStoneHanTypeface);
		addKanaItem(y_tableRow, kanaCache, null, babelStoneHanTypeface);
		addKanaItem(y_tableRow, kanaCache, "よ", babelStoneHanTypeface);

		screenItems.add(y_tableRow);

		pl.idedyk.android.japaneselearnhelper.screen.TableRow r_tableRow = new pl.idedyk.android.japaneselearnhelper.screen.TableRow();

		addKanaItem(r_tableRow, kanaCache, "ら", babelStoneHanTypeface);
		addKanaItem(r_tableRow, kanaCache, "り", babelStoneHanTypeface);
		addKanaItem(r_tableRow, kanaCache, "る", babelStoneHanTypeface);
		addKanaItem(r_tableRow, kanaCache, "れ", babelStoneHanTypeface);
		addKanaItem(r_tableRow, kanaCache, "ろ", babelStoneHanTypeface);

		screenItems.add(r_tableRow);

		pl.idedyk.android.japaneselearnhelper.screen.TableRow w_tableRow = new pl.idedyk.android.japaneselearnhelper.screen.TableRow();

		addKanaItem(w_tableRow, kanaCache, "わ", babelStoneHanTypeface);
		addKanaItem(w_tableRow, kanaCache, "ゐ", babelStoneHanTypeface);
		addKanaItem(w_tableRow, kanaCache, null, babelStoneHanTypeface);
		addKanaItem(w_tableRow, kanaCache, "ゑ", babelStoneHanTypeface);
		addKanaItem(w_tableRow, kanaCache, "を", babelStoneHanTypeface);

		screenItems.add(w_tableRow);
		
		pl.idedyk.android.japaneselearnhelper.screen.TableRow v_tableRow = new pl.idedyk.android.japaneselearnhelper.screen.TableRow();

		addKanaItem(v_tableRow, kanaCache, null, babelStoneHanTypeface);
		addKanaItem(v_tableRow, kanaCache, null, babelStoneHanTypeface);
		addKanaItem(v_tableRow, kanaCache, "ゔ", babelStoneHanTypeface);
		addKanaItem(v_tableRow, kanaCache, null, babelStoneHanTypeface);
		addKanaItem(v_tableRow, kanaCache, null, babelStoneHanTypeface);
		
		screenItems.add(v_tableRow);
		
		pl.idedyk.android.japaneselearnhelper.screen.TableRow n2_tableRow = new pl.idedyk.android.japaneselearnhelper.screen.TableRow();

		addKanaItem(n2_tableRow, kanaCache, "ん", babelStoneHanTypeface);
		addKanaItem(n2_tableRow, kanaCache, null, babelStoneHanTypeface);
		addKanaItem(n2_tableRow, kanaCache, null, babelStoneHanTypeface);
		addKanaItem(n2_tableRow, kanaCache, null, babelStoneHanTypeface);
		addKanaItem(n2_tableRow, kanaCache, null, babelStoneHanTypeface);

		screenItems.add(n2_tableRow);

		pl.idedyk.android.japaneselearnhelper.screen.TableRow g_tableRow = new pl.idedyk.android.japaneselearnhelper.screen.TableRow();

		addKanaItem(g_tableRow, kanaCache, "が", babelStoneHanTypeface);
		addKanaItem(g_tableRow, kanaCache, "ぎ", babelStoneHanTypeface);
		addKanaItem(g_tableRow, kanaCache, "ぐ", babelStoneHanTypeface);
		addKanaItem(g_tableRow, kanaCache, "げ", babelStoneHanTypeface);
		addKanaItem(g_tableRow, kanaCache, "ご", babelStoneHanTypeface);

		screenItems.add(g_tableRow);

		pl.idedyk.android.japaneselearnhelper.screen.TableRow z_tableRow = new pl.idedyk.android.japaneselearnhelper.screen.TableRow();

		addKanaItem(z_tableRow, kanaCache, "ざ", babelStoneHanTypeface);
		addKanaItem(z_tableRow, kanaCache, "じ", babelStoneHanTypeface);
		addKanaItem(z_tableRow, kanaCache, "ず", babelStoneHanTypeface);
		addKanaItem(z_tableRow, kanaCache, "ぜ", babelStoneHanTypeface);
		addKanaItem(z_tableRow, kanaCache, "ぞ", babelStoneHanTypeface);

		screenItems.add(z_tableRow);

		pl.idedyk.android.japaneselearnhelper.screen.TableRow d_tableRow = new pl.idedyk.android.japaneselearnhelper.screen.TableRow();

		addKanaItem(d_tableRow, kanaCache, "だ", babelStoneHanTypeface);
		addKanaItem(d_tableRow, kanaCache, "ぢ", babelStoneHanTypeface);
		addKanaItem(d_tableRow, kanaCache, "づ", babelStoneHanTypeface);
		addKanaItem(d_tableRow, kanaCache, "で", babelStoneHanTypeface);
		addKanaItem(d_tableRow, kanaCache, "ど", babelStoneHanTypeface);

		screenItems.add(d_tableRow);

		pl.idedyk.android.japaneselearnhelper.screen.TableRow b_tableRow = new pl.idedyk.android.japaneselearnhelper.screen.TableRow();

		addKanaItem(b_tableRow, kanaCache, "ば", babelStoneHanTypeface);
		addKanaItem(b_tableRow, kanaCache, "び", babelStoneHanTypeface);
		addKanaItem(b_tableRow, kanaCache, "ぶ", babelStoneHanTypeface);
		addKanaItem(b_tableRow, kanaCache, "べ", babelStoneHanTypeface);
		addKanaItem(b_tableRow, kanaCache, "ぼ", babelStoneHanTypeface);

		screenItems.add(b_tableRow);

		pl.idedyk.android.japaneselearnhelper.screen.TableRow p_tableRow = new pl.idedyk.android.japaneselearnhelper.screen.TableRow();

		addKanaItem(p_tableRow, kanaCache, "ぱ", babelStoneHanTypeface);
		addKanaItem(p_tableRow, kanaCache, "ぴ", babelStoneHanTypeface);
		addKanaItem(p_tableRow, kanaCache, "ぷ", babelStoneHanTypeface);
		addKanaItem(p_tableRow, kanaCache, "ぺ", babelStoneHanTypeface);
		addKanaItem(p_tableRow, kanaCache, "ぽ", babelStoneHanTypeface);

		screenItems.add(p_tableRow);

		pl.idedyk.android.japaneselearnhelper.screen.TableRow ky_tableRow = new pl.idedyk.android.japaneselearnhelper.screen.TableRow();

		addKanaItem(ky_tableRow, kanaCache, "きゃ", babelStoneHanTypeface);
		addKanaItem(ky_tableRow, kanaCache, null, babelStoneHanTypeface);
		addKanaItem(ky_tableRow, kanaCache, "きゅ", babelStoneHanTypeface);
		addKanaItem(ky_tableRow, kanaCache, null, babelStoneHanTypeface);
		addKanaItem(ky_tableRow, kanaCache, "きょ", babelStoneHanTypeface);

		screenItems.add(ky_tableRow);

		pl.idedyk.android.japaneselearnhelper.screen.TableRow shy_tableRow = new pl.idedyk.android.japaneselearnhelper.screen.TableRow();

		addKanaItem(shy_tableRow, kanaCache, "しゃ", babelStoneHanTypeface);
		addKanaItem(shy_tableRow, kanaCache, null, babelStoneHanTypeface);
		addKanaItem(shy_tableRow, kanaCache, "しゅ", babelStoneHanTypeface);
		addKanaItem(shy_tableRow, kanaCache, null, babelStoneHanTypeface);
		addKanaItem(shy_tableRow, kanaCache, "しょ", babelStoneHanTypeface);

		screenItems.add(shy_tableRow);

		pl.idedyk.android.japaneselearnhelper.screen.TableRow chy_tableRow = new pl.idedyk.android.japaneselearnhelper.screen.TableRow();

		addKanaItem(chy_tableRow, kanaCache, "ちゃ", babelStoneHanTypeface);
		addKanaItem(chy_tableRow, kanaCache, null, babelStoneHanTypeface);
		addKanaItem(chy_tableRow, kanaCache, "ちゅ", babelStoneHanTypeface);
		addKanaItem(chy_tableRow, kanaCache, null, babelStoneHanTypeface);
		addKanaItem(chy_tableRow, kanaCache, "ちょ", babelStoneHanTypeface);

		screenItems.add(chy_tableRow);

		pl.idedyk.android.japaneselearnhelper.screen.TableRow ny_tableRow = new pl.idedyk.android.japaneselearnhelper.screen.TableRow();

		addKanaItem(ny_tableRow, kanaCache, "にゃ", babelStoneHanTypeface);
		addKanaItem(ny_tableRow, kanaCache, null, babelStoneHanTypeface);
		addKanaItem(ny_tableRow, kanaCache, "にゅ", babelStoneHanTypeface);
		addKanaItem(ny_tableRow, kanaCache, null, babelStoneHanTypeface);
		addKanaItem(ny_tableRow, kanaCache, "にょ", babelStoneHanTypeface);

		screenItems.add(ny_tableRow);

		pl.idedyk.android.japaneselearnhelper.screen.TableRow hy_tableRow = new pl.idedyk.android.japaneselearnhelper.screen.TableRow();

		addKanaItem(hy_tableRow, kanaCache, "ひゃ", babelStoneHanTypeface);
		addKanaItem(hy_tableRow, kanaCache, null, babelStoneHanTypeface);
		addKanaItem(hy_tableRow, kanaCache, "ひゅ", babelStoneHanTypeface);
		addKanaItem(hy_tableRow, kanaCache, null, babelStoneHanTypeface);
		addKanaItem(hy_tableRow, kanaCache, "ひょ", babelStoneHanTypeface);

		screenItems.add(hy_tableRow);

		pl.idedyk.android.japaneselearnhelper.screen.TableRow my_tableRow = new pl.idedyk.android.japaneselearnhelper.screen.TableRow();

		addKanaItem(my_tableRow, kanaCache, "みゃ", babelStoneHanTypeface);
		addKanaItem(my_tableRow, kanaCache, null, babelStoneHanTypeface);
		addKanaItem(my_tableRow, kanaCache, "みゅ", babelStoneHanTypeface);
		addKanaItem(my_tableRow, kanaCache, null, babelStoneHanTypeface);
		addKanaItem(my_tableRow, kanaCache, "みょ", babelStoneHanTypeface);

		screenItems.add(my_tableRow);

		pl.idedyk.android.japaneselearnhelper.screen.TableRow ry_tableRow = new pl.idedyk.android.japaneselearnhelper.screen.TableRow();

		addKanaItem(ry_tableRow, kanaCache, "りゃ", babelStoneHanTypeface);
		addKanaItem(ry_tableRow, kanaCache, null, babelStoneHanTypeface);
		addKanaItem(ry_tableRow, kanaCache, "りゅ", babelStoneHanTypeface);
		addKanaItem(ry_tableRow, kanaCache, null, babelStoneHanTypeface);
		addKanaItem(ry_tableRow, kanaCache, "りょ", babelStoneHanTypeface);

		screenItems.add(ry_tableRow);

		pl.idedyk.android.japaneselearnhelper.screen.TableRow gy_tableRow = new pl.idedyk.android.japaneselearnhelper.screen.TableRow();

		addKanaItem(gy_tableRow, kanaCache, "ぎゃ", babelStoneHanTypeface);
		addKanaItem(gy_tableRow, kanaCache, null, babelStoneHanTypeface);
		addKanaItem(gy_tableRow, kanaCache, "ぎゅ", babelStoneHanTypeface);
		addKanaItem(gy_tableRow, kanaCache, null, babelStoneHanTypeface);
		addKanaItem(gy_tableRow, kanaCache, "ぎょ", babelStoneHanTypeface);

		screenItems.add(gy_tableRow);

		pl.idedyk.android.japaneselearnhelper.screen.TableRow jy_tableRow = new pl.idedyk.android.japaneselearnhelper.screen.TableRow();

		addKanaItem(jy_tableRow, kanaCache, "じゃ", babelStoneHanTypeface);
		addKanaItem(jy_tableRow, kanaCache, null, babelStoneHanTypeface);
		addKanaItem(jy_tableRow, kanaCache, "じゅ", babelStoneHanTypeface);
		addKanaItem(jy_tableRow, kanaCache, null, babelStoneHanTypeface);
		addKanaItem(jy_tableRow, kanaCache, "じょ", babelStoneHanTypeface);

		screenItems.add(jy_tableRow);

		pl.idedyk.android.japaneselearnhelper.screen.TableRow by_tableRow = new pl.idedyk.android.japaneselearnhelper.screen.TableRow();

		addKanaItem(by_tableRow, kanaCache, "びゃ", babelStoneHanTypeface);
		addKanaItem(by_tableRow, kanaCache, null, babelStoneHanTypeface);
		addKanaItem(by_tableRow, kanaCache, "びゅ", babelStoneHanTypeface);
		addKanaItem(by_tableRow, kanaCache, null, babelStoneHanTypeface);
		addKanaItem(by_tableRow, kanaCache, "びょ", babelStoneHanTypeface);

		screenItems.add(by_tableRow);

		pl.idedyk.android.japaneselearnhelper.screen.TableRow py_tableRow = new pl.idedyk.android.japaneselearnhelper.screen.TableRow();

		addKanaItem(py_tableRow, kanaCache, "ぴゃ", babelStoneHanTypeface);
		addKanaItem(py_tableRow, kanaCache, null, babelStoneHanTypeface);
		addKanaItem(py_tableRow, kanaCache, "ぴゅ", babelStoneHanTypeface);
		addKanaItem(py_tableRow, kanaCache, null, babelStoneHanTypeface);
		addKanaItem(py_tableRow, kanaCache, "ぴょ", babelStoneHanTypeface);

		screenItems.add(py_tableRow);

		pl.idedyk.android.japaneselearnhelper.screen.TableRow dy_tableRow = new pl.idedyk.android.japaneselearnhelper.screen.TableRow();

		addKanaItem(dy_tableRow, kanaCache, "ぢゃ", babelStoneHanTypeface);
		addKanaItem(dy_tableRow, kanaCache, null, babelStoneHanTypeface);
		addKanaItem(dy_tableRow, kanaCache, "ぢゅ", babelStoneHanTypeface);
		addKanaItem(dy_tableRow, kanaCache, null, babelStoneHanTypeface);
		addKanaItem(dy_tableRow, kanaCache, "ぢょ", babelStoneHanTypeface);

		screenItems.add(dy_tableRow);
	}

	private void generateKatakanaTable(List<IScreenItem> screenItems, Map<String, KanaEntry> kanaCache, Typeface radicalTypeface) {

		screenItems.add(new TitleItem(getString(R.string.kana_katakana_label), 0));
		screenItems.add(new StringValue(getString(R.string.kana_info), 12.0f, 0));
		screenItems.add(new StringValue(getString(R.string.kana_info2), 12.0f, 0));

		pl.idedyk.android.japaneselearnhelper.screen.TableRow a_tableRow = new pl.idedyk.android.japaneselearnhelper.screen.TableRow();

		addKanaItem(a_tableRow, kanaCache, "ア", radicalTypeface);
		addKanaItem(a_tableRow, kanaCache, "イ", radicalTypeface);
		addKanaItem(a_tableRow, kanaCache, "ウ", radicalTypeface);
		addKanaItem(a_tableRow, kanaCache, "エ", radicalTypeface);
		addKanaItem(a_tableRow, kanaCache, "オ", radicalTypeface);

		screenItems.add(a_tableRow);

		pl.idedyk.android.japaneselearnhelper.screen.TableRow k_tableRow = new pl.idedyk.android.japaneselearnhelper.screen.TableRow();

		addKanaItem(k_tableRow, kanaCache, "カ", radicalTypeface);
		addKanaItem(k_tableRow, kanaCache, "キ", radicalTypeface);
		addKanaItem(k_tableRow, kanaCache, "ク", radicalTypeface);
		addKanaItem(k_tableRow, kanaCache, "ケ", radicalTypeface);
		addKanaItem(k_tableRow, kanaCache, "コ", radicalTypeface);

		screenItems.add(k_tableRow);

		pl.idedyk.android.japaneselearnhelper.screen.TableRow s_tableRow = new pl.idedyk.android.japaneselearnhelper.screen.TableRow();

		addKanaItem(s_tableRow, kanaCache, "サ", radicalTypeface);
		addKanaItem(s_tableRow, kanaCache, "シ", radicalTypeface);
		addKanaItem(s_tableRow, kanaCache, "ス", radicalTypeface);
		addKanaItem(s_tableRow, kanaCache, "セ", radicalTypeface);
		addKanaItem(s_tableRow, kanaCache, "ソ", radicalTypeface);

		screenItems.add(s_tableRow);

		pl.idedyk.android.japaneselearnhelper.screen.TableRow t_tableRow = new pl.idedyk.android.japaneselearnhelper.screen.TableRow();

		addKanaItem(t_tableRow, kanaCache, "タ", radicalTypeface);
		addKanaItem(t_tableRow, kanaCache, "チ", radicalTypeface);
		addKanaItem(t_tableRow, kanaCache, "ツ", radicalTypeface);
		addKanaItem(t_tableRow, kanaCache, "テ", radicalTypeface);
		addKanaItem(t_tableRow, kanaCache, "ト", radicalTypeface);

		screenItems.add(t_tableRow);

		pl.idedyk.android.japaneselearnhelper.screen.TableRow n_tableRow = new pl.idedyk.android.japaneselearnhelper.screen.TableRow();

		addKanaItem(n_tableRow, kanaCache, "ナ", radicalTypeface);
		addKanaItem(n_tableRow, kanaCache, "ニ", radicalTypeface);
		addKanaItem(n_tableRow, kanaCache, "ヌ", radicalTypeface);
		addKanaItem(n_tableRow, kanaCache, "ネ", radicalTypeface);
		addKanaItem(n_tableRow, kanaCache, "ノ", radicalTypeface);

		screenItems.add(n_tableRow);

		pl.idedyk.android.japaneselearnhelper.screen.TableRow h_tableRow = new pl.idedyk.android.japaneselearnhelper.screen.TableRow();

		addKanaItem(h_tableRow, kanaCache, "ハ", radicalTypeface);
		addKanaItem(h_tableRow, kanaCache, "ヒ", radicalTypeface);
		addKanaItem(h_tableRow, kanaCache, "フ", radicalTypeface);
		addKanaItem(h_tableRow, kanaCache, "ヘ", radicalTypeface);
		addKanaItem(h_tableRow, kanaCache, "ホ", radicalTypeface);

		screenItems.add(h_tableRow);

		pl.idedyk.android.japaneselearnhelper.screen.TableRow m_tableRow = new pl.idedyk.android.japaneselearnhelper.screen.TableRow();

		addKanaItem(m_tableRow, kanaCache, "マ", radicalTypeface);
		addKanaItem(m_tableRow, kanaCache, "ミ", radicalTypeface);
		addKanaItem(m_tableRow, kanaCache, "ム", radicalTypeface);
		addKanaItem(m_tableRow, kanaCache, "メ", radicalTypeface);
		addKanaItem(m_tableRow, kanaCache, "モ", radicalTypeface);

		screenItems.add(m_tableRow);

		pl.idedyk.android.japaneselearnhelper.screen.TableRow y_tableRow = new pl.idedyk.android.japaneselearnhelper.screen.TableRow();

		addKanaItem(y_tableRow, kanaCache, "ヤ", radicalTypeface);
		addKanaItem(y_tableRow, kanaCache, null, radicalTypeface);
		addKanaItem(y_tableRow, kanaCache, "ユ", radicalTypeface);
		addKanaItem(y_tableRow, kanaCache, null, radicalTypeface);
		addKanaItem(y_tableRow, kanaCache, "ヨ", radicalTypeface);

		screenItems.add(y_tableRow);

		pl.idedyk.android.japaneselearnhelper.screen.TableRow r_tableRow = new pl.idedyk.android.japaneselearnhelper.screen.TableRow();

		addKanaItem(r_tableRow, kanaCache, "ラ", radicalTypeface);
		addKanaItem(r_tableRow, kanaCache, "リ", radicalTypeface);
		addKanaItem(r_tableRow, kanaCache, "ル", radicalTypeface);
		addKanaItem(r_tableRow, kanaCache, "レ", radicalTypeface);
		addKanaItem(r_tableRow, kanaCache, "ロ", radicalTypeface);

		screenItems.add(r_tableRow);

		pl.idedyk.android.japaneselearnhelper.screen.TableRow w_tableRow = new pl.idedyk.android.japaneselearnhelper.screen.TableRow();

		addKanaItem(w_tableRow, kanaCache, "ワ", radicalTypeface);
		addKanaItem(w_tableRow, kanaCache, "ヰ", radicalTypeface);
		addKanaItem(w_tableRow, kanaCache, null, radicalTypeface);
		addKanaItem(w_tableRow, kanaCache, "ヱ", radicalTypeface);
		addKanaItem(w_tableRow, kanaCache, "ヲ", radicalTypeface);

		screenItems.add(w_tableRow);

		pl.idedyk.android.japaneselearnhelper.screen.TableRow n2_tableRow = new pl.idedyk.android.japaneselearnhelper.screen.TableRow();

		addKanaItem(n2_tableRow, kanaCache, "ン", radicalTypeface);
		addKanaItem(n2_tableRow, kanaCache, null, radicalTypeface);
		addKanaItem(n2_tableRow, kanaCache, null, radicalTypeface);
		addKanaItem(n2_tableRow, kanaCache, null, radicalTypeface);
		addKanaItem(n2_tableRow, kanaCache, null, radicalTypeface);

		screenItems.add(n2_tableRow);

		pl.idedyk.android.japaneselearnhelper.screen.TableRow g_tableRow = new pl.idedyk.android.japaneselearnhelper.screen.TableRow();

		addKanaItem(g_tableRow, kanaCache, "ガ", radicalTypeface);
		addKanaItem(g_tableRow, kanaCache, "ギ", radicalTypeface);
		addKanaItem(g_tableRow, kanaCache, "グ", radicalTypeface);
		addKanaItem(g_tableRow, kanaCache, "ゲ", radicalTypeface);
		addKanaItem(g_tableRow, kanaCache, "ゴ", radicalTypeface);

		screenItems.add(g_tableRow);

		pl.idedyk.android.japaneselearnhelper.screen.TableRow z_tableRow = new pl.idedyk.android.japaneselearnhelper.screen.TableRow();

		addKanaItem(z_tableRow, kanaCache, "ザ", radicalTypeface);
		addKanaItem(z_tableRow, kanaCache, "ジ", radicalTypeface);
		addKanaItem(z_tableRow, kanaCache, "ズ", radicalTypeface);
		addKanaItem(z_tableRow, kanaCache, "ゼ", radicalTypeface);
		addKanaItem(z_tableRow, kanaCache, "ゾ", radicalTypeface);

		screenItems.add(z_tableRow);

		pl.idedyk.android.japaneselearnhelper.screen.TableRow d_tableRow = new pl.idedyk.android.japaneselearnhelper.screen.TableRow();

		addKanaItem(d_tableRow, kanaCache, "ダ", radicalTypeface);
		addKanaItem(d_tableRow, kanaCache, "ヂ", radicalTypeface);
		addKanaItem(d_tableRow, kanaCache, "ヅ", radicalTypeface);
		addKanaItem(d_tableRow, kanaCache, "デ", radicalTypeface);
		addKanaItem(d_tableRow, kanaCache, "ド", radicalTypeface);

		screenItems.add(d_tableRow);

		pl.idedyk.android.japaneselearnhelper.screen.TableRow b_tableRow = new pl.idedyk.android.japaneselearnhelper.screen.TableRow();

		addKanaItem(b_tableRow, kanaCache, "バ", radicalTypeface);
		addKanaItem(b_tableRow, kanaCache, "ビ", radicalTypeface);
		addKanaItem(b_tableRow, kanaCache, "ブ", radicalTypeface);
		addKanaItem(b_tableRow, kanaCache, "ベ", radicalTypeface);
		addKanaItem(b_tableRow, kanaCache, "ボ", radicalTypeface);

		screenItems.add(b_tableRow);

		pl.idedyk.android.japaneselearnhelper.screen.TableRow p_tableRow = new pl.idedyk.android.japaneselearnhelper.screen.TableRow();

		addKanaItem(p_tableRow, kanaCache, "パ", radicalTypeface);
		addKanaItem(p_tableRow, kanaCache, "ピ", radicalTypeface);
		addKanaItem(p_tableRow, kanaCache, "プ", radicalTypeface);
		addKanaItem(p_tableRow, kanaCache, "ペ", radicalTypeface);
		addKanaItem(p_tableRow, kanaCache, "ポ", radicalTypeface);

		screenItems.add(p_tableRow);

		pl.idedyk.android.japaneselearnhelper.screen.TableRow ky_tableRow = new pl.idedyk.android.japaneselearnhelper.screen.TableRow();

		addKanaItem(ky_tableRow, kanaCache, "キャ", radicalTypeface);
		addKanaItem(ky_tableRow, kanaCache, null, radicalTypeface);
		addKanaItem(ky_tableRow, kanaCache, "キュ", radicalTypeface);
		addKanaItem(ky_tableRow, kanaCache, null, radicalTypeface);
		addKanaItem(ky_tableRow, kanaCache, "キョ", radicalTypeface);

		screenItems.add(ky_tableRow);

		pl.idedyk.android.japaneselearnhelper.screen.TableRow shy_tableRow = new pl.idedyk.android.japaneselearnhelper.screen.TableRow();

		addKanaItem(shy_tableRow, kanaCache, "シャ", radicalTypeface);
		addKanaItem(shy_tableRow, kanaCache, null, radicalTypeface);
		addKanaItem(shy_tableRow, kanaCache, "シュ", radicalTypeface);
		addKanaItem(shy_tableRow, kanaCache, null, radicalTypeface);
		addKanaItem(shy_tableRow, kanaCache, "ショ", radicalTypeface);

		screenItems.add(shy_tableRow);

		pl.idedyk.android.japaneselearnhelper.screen.TableRow chy_tableRow = new pl.idedyk.android.japaneselearnhelper.screen.TableRow();

		addKanaItem(chy_tableRow, kanaCache, "チャ", radicalTypeface);
		addKanaItem(chy_tableRow, kanaCache, null, radicalTypeface);
		addKanaItem(chy_tableRow, kanaCache, "チュ", radicalTypeface);
		addKanaItem(chy_tableRow, kanaCache, null, radicalTypeface);
		addKanaItem(chy_tableRow, kanaCache, "チョ", radicalTypeface);

		screenItems.add(chy_tableRow);

		pl.idedyk.android.japaneselearnhelper.screen.TableRow ny_tableRow = new pl.idedyk.android.japaneselearnhelper.screen.TableRow();

		addKanaItem(ny_tableRow, kanaCache, "ニャ", radicalTypeface);
		addKanaItem(ny_tableRow, kanaCache, null, radicalTypeface);
		addKanaItem(ny_tableRow, kanaCache, "ニュ", radicalTypeface);
		addKanaItem(ny_tableRow, kanaCache, null, radicalTypeface);
		addKanaItem(ny_tableRow, kanaCache, "ニョ", radicalTypeface);

		screenItems.add(ny_tableRow);

		pl.idedyk.android.japaneselearnhelper.screen.TableRow hy_tableRow = new pl.idedyk.android.japaneselearnhelper.screen.TableRow();

		addKanaItem(hy_tableRow, kanaCache, "ヒャ", radicalTypeface);
		addKanaItem(hy_tableRow, kanaCache, null, radicalTypeface);
		addKanaItem(hy_tableRow, kanaCache, "ヒュ", radicalTypeface);
		addKanaItem(hy_tableRow, kanaCache, null, radicalTypeface);
		addKanaItem(hy_tableRow, kanaCache, "ヒョ", radicalTypeface);

		screenItems.add(hy_tableRow);

		pl.idedyk.android.japaneselearnhelper.screen.TableRow my_tableRow = new pl.idedyk.android.japaneselearnhelper.screen.TableRow();

		addKanaItem(my_tableRow, kanaCache, "ミャ", radicalTypeface);
		addKanaItem(my_tableRow, kanaCache, null, radicalTypeface);
		addKanaItem(my_tableRow, kanaCache, "ミュ", radicalTypeface);
		addKanaItem(my_tableRow, kanaCache, null, radicalTypeface);
		addKanaItem(my_tableRow, kanaCache, "ミョ", radicalTypeface);

		screenItems.add(my_tableRow);

		pl.idedyk.android.japaneselearnhelper.screen.TableRow ry_tableRow = new pl.idedyk.android.japaneselearnhelper.screen.TableRow();

		addKanaItem(ry_tableRow, kanaCache, "リャ", radicalTypeface);
		addKanaItem(ry_tableRow, kanaCache, null, radicalTypeface);
		addKanaItem(ry_tableRow, kanaCache, "リュ", radicalTypeface);
		addKanaItem(ry_tableRow, kanaCache, null, radicalTypeface);
		addKanaItem(ry_tableRow, kanaCache, "リョ", radicalTypeface);

		screenItems.add(ry_tableRow);

		pl.idedyk.android.japaneselearnhelper.screen.TableRow gy_tableRow = new pl.idedyk.android.japaneselearnhelper.screen.TableRow();

		addKanaItem(gy_tableRow, kanaCache, "ギャ", radicalTypeface);
		addKanaItem(gy_tableRow, kanaCache, null, radicalTypeface);
		addKanaItem(gy_tableRow, kanaCache, "ギュ", radicalTypeface);
		addKanaItem(gy_tableRow, kanaCache, null, radicalTypeface);
		addKanaItem(gy_tableRow, kanaCache, "ギョ", radicalTypeface);

		screenItems.add(gy_tableRow);

		pl.idedyk.android.japaneselearnhelper.screen.TableRow jy_tableRow = new pl.idedyk.android.japaneselearnhelper.screen.TableRow();

		addKanaItem(jy_tableRow, kanaCache, "ジャ", radicalTypeface);
		addKanaItem(jy_tableRow, kanaCache, null, radicalTypeface);
		addKanaItem(jy_tableRow, kanaCache, "ジュ", radicalTypeface);
		addKanaItem(jy_tableRow, kanaCache, null, radicalTypeface);
		addKanaItem(jy_tableRow, kanaCache, "ジョ", radicalTypeface);

		screenItems.add(jy_tableRow);

		pl.idedyk.android.japaneselearnhelper.screen.TableRow by_tableRow = new pl.idedyk.android.japaneselearnhelper.screen.TableRow();

		addKanaItem(by_tableRow, kanaCache, "ビャ", radicalTypeface);
		addKanaItem(by_tableRow, kanaCache, null, radicalTypeface);
		addKanaItem(by_tableRow, kanaCache, "ビュ", radicalTypeface);
		addKanaItem(by_tableRow, kanaCache, null, radicalTypeface);
		addKanaItem(by_tableRow, kanaCache, "ビョ", radicalTypeface);

		screenItems.add(by_tableRow);

		pl.idedyk.android.japaneselearnhelper.screen.TableRow py_tableRow = new pl.idedyk.android.japaneselearnhelper.screen.TableRow();

		addKanaItem(py_tableRow, kanaCache, "ピャ", radicalTypeface);
		addKanaItem(py_tableRow, kanaCache, null, radicalTypeface);
		addKanaItem(py_tableRow, kanaCache, "ピュ", radicalTypeface);
		addKanaItem(py_tableRow, kanaCache, null, radicalTypeface);
		addKanaItem(py_tableRow, kanaCache, "ピョ", radicalTypeface);

		screenItems.add(py_tableRow);

		pl.idedyk.android.japaneselearnhelper.screen.TableRow dy_tableRow = new pl.idedyk.android.japaneselearnhelper.screen.TableRow();

		addKanaItem(dy_tableRow, kanaCache, "ヂャ", radicalTypeface);
		addKanaItem(dy_tableRow, kanaCache, null, radicalTypeface);
		addKanaItem(dy_tableRow, kanaCache, "ヂュ", radicalTypeface);
		addKanaItem(dy_tableRow, kanaCache, null, radicalTypeface);
		addKanaItem(dy_tableRow, kanaCache, "ヂョ", radicalTypeface);

		screenItems.add(dy_tableRow);
	}

	private void generateKatakanaAdditionalTable(List<IScreenItem> screenItems, Map<String, KanaEntry> kanaCache, Typeface radicalTypeface) {

		screenItems.add(new TitleItem(getString(R.string.kana_katakana_additional_label), 0));
		screenItems.add(new StringValue(getString(R.string.kana_info), 12.0f, 0));
		screenItems.add(new StringValue(getString(R.string.kana_info2), 12.0f, 0));
		//screenItems.add(new StringValue(getString(R.string.kana_info3), 12.0f, 0));
		
		pl.idedyk.android.japaneselearnhelper.screen.TableRow ye_tableRow = new pl.idedyk.android.japaneselearnhelper.screen.TableRow();

		addKanaItem(ye_tableRow, kanaCache, null, radicalTypeface);
		addKanaItem(ye_tableRow, kanaCache, null, radicalTypeface);
		addKanaItem(ye_tableRow, kanaCache, null, radicalTypeface);
		addKanaItem(ye_tableRow, kanaCache, "イェ", radicalTypeface);
		addKanaItem(ye_tableRow, kanaCache, null, radicalTypeface);

		screenItems.add(ye_tableRow);

		pl.idedyk.android.japaneselearnhelper.screen.TableRow u_tableRow = new pl.idedyk.android.japaneselearnhelper.screen.TableRow();

		addKanaItem(u_tableRow, kanaCache, null, radicalTypeface);
		addKanaItem(u_tableRow, kanaCache, "ウィ", radicalTypeface);
		addKanaItem(u_tableRow, kanaCache, null, radicalTypeface);
		addKanaItem(u_tableRow, kanaCache, "ウェ", radicalTypeface);
		addKanaItem(u_tableRow, kanaCache, "ウォ", radicalTypeface);

		screenItems.add(u_tableRow);

		pl.idedyk.android.japaneselearnhelper.screen.TableRow v1_tableRow = new pl.idedyk.android.japaneselearnhelper.screen.TableRow();

		addKanaItem(v1_tableRow, kanaCache, "ヷ", radicalTypeface);
		addKanaItem(v1_tableRow, kanaCache, "ヸ", radicalTypeface);
		addKanaItem(v1_tableRow, kanaCache, null, radicalTypeface);
		addKanaItem(v1_tableRow, kanaCache, "ヹ", radicalTypeface);
		addKanaItem(v1_tableRow, kanaCache, "ヺ", radicalTypeface);

		screenItems.add(v1_tableRow);
		
		pl.idedyk.android.japaneselearnhelper.screen.TableRow v2_tableRow = new pl.idedyk.android.japaneselearnhelper.screen.TableRow();

		addKanaItem(v2_tableRow, kanaCache, "ヴァ", radicalTypeface);
		addKanaItem(v2_tableRow, kanaCache, "ヴィ", radicalTypeface);
		addKanaItem(v2_tableRow, kanaCache, "ヴ", radicalTypeface);
		addKanaItem(v2_tableRow, kanaCache, "ヴェ", radicalTypeface);
		addKanaItem(v2_tableRow, kanaCache, "ヴォ", radicalTypeface);

		screenItems.add(v2_tableRow);

		pl.idedyk.android.japaneselearnhelper.screen.TableRow s_tableRow = new pl.idedyk.android.japaneselearnhelper.screen.TableRow();

		addKanaItem(s_tableRow, kanaCache, null, radicalTypeface);
		addKanaItem(s_tableRow, kanaCache, null, radicalTypeface);
		addKanaItem(s_tableRow, kanaCache, null, radicalTypeface);
		addKanaItem(s_tableRow, kanaCache, "シェ", radicalTypeface);
		addKanaItem(s_tableRow, kanaCache, null, radicalTypeface);

		screenItems.add(s_tableRow);
		
		pl.idedyk.android.japaneselearnhelper.screen.TableRow j_tableRow = new pl.idedyk.android.japaneselearnhelper.screen.TableRow();

		addKanaItem(j_tableRow, kanaCache, null, radicalTypeface);
		addKanaItem(j_tableRow, kanaCache, null, radicalTypeface);
		addKanaItem(j_tableRow, kanaCache, null, radicalTypeface);
		addKanaItem(j_tableRow, kanaCache, "ジェ", radicalTypeface);
		addKanaItem(j_tableRow, kanaCache, null, radicalTypeface);

		screenItems.add(j_tableRow);
		
		pl.idedyk.android.japaneselearnhelper.screen.TableRow c_tableRow = new pl.idedyk.android.japaneselearnhelper.screen.TableRow();

		addKanaItem(c_tableRow, kanaCache, null, radicalTypeface);
		addKanaItem(c_tableRow, kanaCache, null, radicalTypeface);
		addKanaItem(c_tableRow, kanaCache, null, radicalTypeface);
		addKanaItem(c_tableRow, kanaCache, "チェ", radicalTypeface);
		addKanaItem(c_tableRow, kanaCache, null, radicalTypeface);

		screenItems.add(c_tableRow);
		
		pl.idedyk.android.japaneselearnhelper.screen.TableRow t_tableRow = new pl.idedyk.android.japaneselearnhelper.screen.TableRow();

		addKanaItem(t_tableRow, kanaCache, null, radicalTypeface);
		addKanaItem(t_tableRow, kanaCache, "ティ", radicalTypeface);
		addKanaItem(t_tableRow, kanaCache, "トゥ", radicalTypeface);
		addKanaItem(t_tableRow, kanaCache, null, radicalTypeface);
		addKanaItem(t_tableRow, kanaCache, null, radicalTypeface);

		screenItems.add(t_tableRow);

		pl.idedyk.android.japaneselearnhelper.screen.TableRow tyu_tableRow = new pl.idedyk.android.japaneselearnhelper.screen.TableRow();

		addKanaItem(tyu_tableRow, kanaCache, null, radicalTypeface);
		addKanaItem(tyu_tableRow, kanaCache, null, radicalTypeface);
		addKanaItem(tyu_tableRow, kanaCache, "テュ", radicalTypeface);
		addKanaItem(tyu_tableRow, kanaCache, null, radicalTypeface);
		addKanaItem(tyu_tableRow, kanaCache, null, radicalTypeface);

		screenItems.add(tyu_tableRow);
		
		pl.idedyk.android.japaneselearnhelper.screen.TableRow d_tableRow = new pl.idedyk.android.japaneselearnhelper.screen.TableRow();

		addKanaItem(d_tableRow, kanaCache, null, radicalTypeface);
		addKanaItem(d_tableRow, kanaCache, "ディ", radicalTypeface);
		addKanaItem(d_tableRow, kanaCache, "ドゥ", radicalTypeface);
		addKanaItem(d_tableRow, kanaCache, null, radicalTypeface);
		addKanaItem(d_tableRow, kanaCache, null, radicalTypeface);

		screenItems.add(d_tableRow);

		pl.idedyk.android.japaneselearnhelper.screen.TableRow dyu_tableRow = new pl.idedyk.android.japaneselearnhelper.screen.TableRow();

		addKanaItem(dyu_tableRow, kanaCache, null, radicalTypeface);
		addKanaItem(dyu_tableRow, kanaCache, null, radicalTypeface);
		addKanaItem(dyu_tableRow, kanaCache, "デュ", radicalTypeface);
		addKanaItem(dyu_tableRow, kanaCache, null, radicalTypeface);
		addKanaItem(dyu_tableRow, kanaCache, null, radicalTypeface);

		screenItems.add(dyu_tableRow);
		
		pl.idedyk.android.japaneselearnhelper.screen.TableRow ts_tableRow = new pl.idedyk.android.japaneselearnhelper.screen.TableRow();

		addKanaItem(ts_tableRow, kanaCache, "ツァ", radicalTypeface);
		addKanaItem(ts_tableRow, kanaCache, "ツィ", radicalTypeface);
		addKanaItem(ts_tableRow, kanaCache, null, radicalTypeface);
		addKanaItem(ts_tableRow, kanaCache, "ツェ", radicalTypeface);
		addKanaItem(ts_tableRow, kanaCache, "ツォ", radicalTypeface);

		screenItems.add(ts_tableRow);
		
		pl.idedyk.android.japaneselearnhelper.screen.TableRow f_tableRow = new pl.idedyk.android.japaneselearnhelper.screen.TableRow();

		addKanaItem(f_tableRow, kanaCache, "ファ", radicalTypeface);
		addKanaItem(f_tableRow, kanaCache, "フィ", radicalTypeface);
		addKanaItem(f_tableRow, kanaCache, null, radicalTypeface);
		addKanaItem(f_tableRow, kanaCache, "フェ", radicalTypeface);
		addKanaItem(f_tableRow, kanaCache, "フォ", radicalTypeface);

		screenItems.add(f_tableRow);		

		pl.idedyk.android.japaneselearnhelper.screen.TableRow fyu_tableRow = new pl.idedyk.android.japaneselearnhelper.screen.TableRow();

		addKanaItem(fyu_tableRow, kanaCache, null, radicalTypeface);
		addKanaItem(fyu_tableRow, kanaCache, null, radicalTypeface);
		addKanaItem(fyu_tableRow, kanaCache, "フュ", radicalTypeface);
		addKanaItem(fyu_tableRow, kanaCache, null, radicalTypeface);
		addKanaItem(fyu_tableRow, kanaCache, null, radicalTypeface);

		screenItems.add(fyu_tableRow);
	}

	private void addKanaItem(pl.idedyk.android.japaneselearnhelper.screen.TableRow tableRow,
			Map<String, KanaEntry> kanaCache, String kana, Typeface radicalTypeface) {

		addKanaItem(tableRow, kanaCache, kana, null, radicalTypeface);
	}
	
	private void addKanaItem(pl.idedyk.android.japaneselearnhelper.screen.TableRow tableRow,
			Map<String, KanaEntry> kanaCache, String kana, String forceKanaDisplay, Typeface radicalTypeface) {

		StringValue stringValue = null;

		if (kana != null) {
			final KanaEntry kanaEntry = kanaCache.get(kana);

			if (kanaEntry == null) {
				throw new RuntimeException(kana);
			}
			
			boolean use = kanaEntry.isUse();

			Spanned spanned = Html.fromHtml("<b>" + kanaEntry.getKanaDisplay() + 
					(use == true ? "" : " *") +
					"</b><br/>"
					+ (forceKanaDisplay == null ? kanaEntry.getKanaJapanese() : forceKanaDisplay) + "<br/>");

			stringValue = new StringValue(spanned, 20, 0);

			stringValue.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					List<KanjivgEntry> allStrokePaths = kanaEntry.getStrokePaths();
					List<List<String>> allStrokePathsResult = new ArrayList<>();

					for (KanjivgEntry kanjivgEntry : allStrokePaths) {
						allStrokePathsResult.add(kanjivgEntry.getStrokePaths());
					}

					StrokePathInfo strokePathInfo = new StrokePathInfo();
					strokePathInfo.setStrokePaths(allStrokePathsResult);

					Intent intent = new Intent(getApplicationContext(), SodActivity.class);

					intent.putExtra("strokePathsInfo", strokePathInfo);

					startActivity(intent);
				}
			});

		} else {
			stringValue = new StringValue("", 20, 0);
		}

		stringValue.setTypeface(radicalTypeface);
		stringValue.setGravity(Gravity.CENTER);
		stringValue.setNullMargins(true);

		tableRow.addScreenItem(stringValue);
	}

	private void fillMainLayout(List<IScreenItem> generatedDetails, TableLayout mainTableLayout) {

		for (IScreenItem currentDetailsReportItem : generatedDetails) {
			currentDetailsReportItem.generate(this, getResources(), mainTableLayout);
		}
	}
}
