package pl.idedyk.android.japaneselearnhelper.kana;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import pl.idedyk.android.japaneselearnhelper.R;
import pl.idedyk.android.japaneselearnhelper.dictionary.KanaHelper;
import pl.idedyk.android.japaneselearnhelper.dictionary.dto.KanaEntry;
import pl.idedyk.android.japaneselearnhelper.problem.ReportProblem;
import pl.idedyk.android.japaneselearnhelper.screen.IScreenItem;
import pl.idedyk.android.japaneselearnhelper.screen.StringValue;
import pl.idedyk.android.japaneselearnhelper.screen.TitleItem;
import pl.idedyk.android.japaneselearnhelper.sod.SodActivity;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TableLayout;

public class Kana extends Activity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {	
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.word_kana);
		
		TableLayout mainTableLayout = (TableLayout)findViewById(R.id.word_kana_main_layout);
		
		final List<IScreenItem> screenItems = new ArrayList<IScreenItem>();
		
		Map<String, KanaEntry> kanaCache = KanaHelper.getInstance().getKanaCache();
		
		generateHiraganaTable(screenItems, kanaCache);
		generateKatakanaTable(screenItems, kanaCache);
		generateKatakanaAdditionalTable(screenItems, kanaCache);
		
		fillMainLayout(screenItems, mainTableLayout);
		
		Button reportProblemButton = (Button)findViewById(R.id.word_kana_report_problem_button);
		
		reportProblemButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View view) {
				
				StringBuffer detailsSb = new StringBuffer();
				
				for (IScreenItem currentscreenItem : screenItems) {
					detailsSb.append(currentscreenItem.toString()).append("\n\n");
				}
				
				String chooseEmailClientTitle = getString(R.string.choose_email_client);
				
				String mailSubject = getString(R.string.kana_report_problem_email_subject);
				
				String mailBody = getString(R.string.kana_report_problem_email_body,
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
	
	private void generateHiraganaTable(List<IScreenItem> screenItems, Map<String, KanaEntry> kanaCache) {
		
		screenItems.add(new TitleItem(getString(R.string.kana_hiragana_label), 0));
		screenItems.add(new StringValue(getString(R.string.kana_info), 12.0f, 0));
		
		pl.idedyk.android.japaneselearnhelper.screen.TableRow a_tableRow = new pl.idedyk.android.japaneselearnhelper.screen.TableRow();
		
		addKanaItem(a_tableRow, kanaCache, "あ");
		addKanaItem(a_tableRow, kanaCache, "い");
		addKanaItem(a_tableRow, kanaCache, "う");
		addKanaItem(a_tableRow, kanaCache, "え");
		addKanaItem(a_tableRow, kanaCache, "お");
		
		screenItems.add(a_tableRow);
		
		pl.idedyk.android.japaneselearnhelper.screen.TableRow k_tableRow = new pl.idedyk.android.japaneselearnhelper.screen.TableRow();
		
		addKanaItem(k_tableRow, kanaCache, "か");
		addKanaItem(k_tableRow, kanaCache, "き");
		addKanaItem(k_tableRow, kanaCache, "く");
		addKanaItem(k_tableRow, kanaCache, "け");
		addKanaItem(k_tableRow, kanaCache, "こ");
				
		screenItems.add(k_tableRow);

		pl.idedyk.android.japaneselearnhelper.screen.TableRow s_tableRow = new pl.idedyk.android.japaneselearnhelper.screen.TableRow();
		
		addKanaItem(s_tableRow, kanaCache, "さ");
		addKanaItem(s_tableRow, kanaCache, "し");
		addKanaItem(s_tableRow, kanaCache, "す");
		addKanaItem(s_tableRow, kanaCache, "せ");
		addKanaItem(s_tableRow, kanaCache, "そ");
						
		screenItems.add(s_tableRow);
		
		pl.idedyk.android.japaneselearnhelper.screen.TableRow t_tableRow = new pl.idedyk.android.japaneselearnhelper.screen.TableRow();
		
		addKanaItem(t_tableRow, kanaCache, "た");
		addKanaItem(t_tableRow, kanaCache, "ち");
		addKanaItem(t_tableRow, kanaCache, "つ");
		addKanaItem(t_tableRow, kanaCache, "て");
		addKanaItem(t_tableRow, kanaCache, "と");
		
		screenItems.add(t_tableRow);
		
		pl.idedyk.android.japaneselearnhelper.screen.TableRow n_tableRow = new pl.idedyk.android.japaneselearnhelper.screen.TableRow();

		addKanaItem(n_tableRow, kanaCache, "な");
		addKanaItem(n_tableRow, kanaCache, "に");
		addKanaItem(n_tableRow, kanaCache, "ぬ");
		addKanaItem(n_tableRow, kanaCache, "ね");
		addKanaItem(n_tableRow, kanaCache, "の");
		
		screenItems.add(n_tableRow);
		
		pl.idedyk.android.japaneselearnhelper.screen.TableRow h_tableRow = new pl.idedyk.android.japaneselearnhelper.screen.TableRow();

		addKanaItem(h_tableRow, kanaCache, "は");
		addKanaItem(h_tableRow, kanaCache, "ひ");
		addKanaItem(h_tableRow, kanaCache, "ふ");
		addKanaItem(h_tableRow, kanaCache, "へ");
		addKanaItem(h_tableRow, kanaCache, "ほ");
		
		screenItems.add(h_tableRow);
		
		pl.idedyk.android.japaneselearnhelper.screen.TableRow m_tableRow = new pl.idedyk.android.japaneselearnhelper.screen.TableRow();

		addKanaItem(m_tableRow, kanaCache, "ま");
		addKanaItem(m_tableRow, kanaCache, "み");
		addKanaItem(m_tableRow, kanaCache, "む");
		addKanaItem(m_tableRow, kanaCache, "め");
		addKanaItem(m_tableRow, kanaCache, "も");
		
		screenItems.add(m_tableRow);
		
		pl.idedyk.android.japaneselearnhelper.screen.TableRow y_tableRow = new pl.idedyk.android.japaneselearnhelper.screen.TableRow();

		addKanaItem(y_tableRow, kanaCache, "や");
		addKanaItem(y_tableRow, kanaCache, null);
		addKanaItem(y_tableRow, kanaCache, "ゆ");
		addKanaItem(y_tableRow, kanaCache, null);
		addKanaItem(y_tableRow, kanaCache, "よ");
		
		screenItems.add(y_tableRow);
		
		pl.idedyk.android.japaneselearnhelper.screen.TableRow r_tableRow = new pl.idedyk.android.japaneselearnhelper.screen.TableRow();

		addKanaItem(r_tableRow, kanaCache, "ら");
		addKanaItem(r_tableRow, kanaCache, "り");
		addKanaItem(r_tableRow, kanaCache, "る");
		addKanaItem(r_tableRow, kanaCache, "れ");
		addKanaItem(r_tableRow, kanaCache, "ろ");
		
		screenItems.add(r_tableRow);
		
		pl.idedyk.android.japaneselearnhelper.screen.TableRow w_tableRow = new pl.idedyk.android.japaneselearnhelper.screen.TableRow();
		
		addKanaItem(w_tableRow, kanaCache, "わ");
		addKanaItem(w_tableRow, kanaCache, null);
		addKanaItem(w_tableRow, kanaCache, null);
		addKanaItem(w_tableRow, kanaCache, null);
		addKanaItem(w_tableRow, kanaCache, "を");
		
		screenItems.add(w_tableRow);
		
		pl.idedyk.android.japaneselearnhelper.screen.TableRow n2_tableRow = new pl.idedyk.android.japaneselearnhelper.screen.TableRow();

		addKanaItem(n2_tableRow, kanaCache, "ん");
		addKanaItem(n2_tableRow, kanaCache, null);
		addKanaItem(n2_tableRow, kanaCache, null);
		addKanaItem(n2_tableRow, kanaCache, null);
		addKanaItem(n2_tableRow, kanaCache, null);
		
		screenItems.add(n2_tableRow);

		pl.idedyk.android.japaneselearnhelper.screen.TableRow g_tableRow = new pl.idedyk.android.japaneselearnhelper.screen.TableRow();

		addKanaItem(g_tableRow, kanaCache, "が");
		addKanaItem(g_tableRow, kanaCache, "ぎ");
		addKanaItem(g_tableRow, kanaCache, "ぐ");
		addKanaItem(g_tableRow, kanaCache, "げ");
		addKanaItem(g_tableRow, kanaCache, "ご");
		
		screenItems.add(g_tableRow);
		
		pl.idedyk.android.japaneselearnhelper.screen.TableRow z_tableRow = new pl.idedyk.android.japaneselearnhelper.screen.TableRow();

		addKanaItem(z_tableRow, kanaCache, "ざ");
		addKanaItem(z_tableRow, kanaCache, "じ");
		addKanaItem(z_tableRow, kanaCache, "ず");
		addKanaItem(z_tableRow, kanaCache, "ぜ");
		addKanaItem(z_tableRow, kanaCache, "ぞ");
		
		screenItems.add(z_tableRow);
		
		pl.idedyk.android.japaneselearnhelper.screen.TableRow d_tableRow = new pl.idedyk.android.japaneselearnhelper.screen.TableRow();

		addKanaItem(d_tableRow, kanaCache, "だ");
		addKanaItem(d_tableRow, kanaCache, "ぢ");
		addKanaItem(d_tableRow, kanaCache, "づ");
		addKanaItem(d_tableRow, kanaCache, "で");
		addKanaItem(d_tableRow, kanaCache, "ど");
		
		screenItems.add(d_tableRow);
		
		pl.idedyk.android.japaneselearnhelper.screen.TableRow b_tableRow = new pl.idedyk.android.japaneselearnhelper.screen.TableRow();

		addKanaItem(b_tableRow, kanaCache, "ば");
		addKanaItem(b_tableRow, kanaCache, "び");
		addKanaItem(b_tableRow, kanaCache, "ぶ");
		addKanaItem(b_tableRow, kanaCache, "べ");
		addKanaItem(b_tableRow, kanaCache, "ぼ");
		
		screenItems.add(b_tableRow);
		
		pl.idedyk.android.japaneselearnhelper.screen.TableRow p_tableRow = new pl.idedyk.android.japaneselearnhelper.screen.TableRow();

		addKanaItem(p_tableRow, kanaCache, "ぱ");
		addKanaItem(p_tableRow, kanaCache, "ぴ");
		addKanaItem(p_tableRow, kanaCache, "ぷ");
		addKanaItem(p_tableRow, kanaCache, "ぺ");
		addKanaItem(p_tableRow, kanaCache, "ぽ");
		
		screenItems.add(p_tableRow);
		
		pl.idedyk.android.japaneselearnhelper.screen.TableRow ky_tableRow = new pl.idedyk.android.japaneselearnhelper.screen.TableRow();

		addKanaItem(ky_tableRow, kanaCache, "きゃ");
		addKanaItem(ky_tableRow, kanaCache, null);
		addKanaItem(ky_tableRow, kanaCache, "きゅ");
		addKanaItem(ky_tableRow, kanaCache, null);
		addKanaItem(ky_tableRow, kanaCache, "きょ");
		
		screenItems.add(ky_tableRow);
		
		pl.idedyk.android.japaneselearnhelper.screen.TableRow shy_tableRow = new pl.idedyk.android.japaneselearnhelper.screen.TableRow();

		addKanaItem(shy_tableRow, kanaCache, "しゃ");
		addKanaItem(shy_tableRow, kanaCache, null);
		addKanaItem(shy_tableRow, kanaCache, "しゅ");
		addKanaItem(shy_tableRow, kanaCache, null);
		addKanaItem(shy_tableRow, kanaCache, "しょ");
		
		screenItems.add(shy_tableRow);
		
		pl.idedyk.android.japaneselearnhelper.screen.TableRow chy_tableRow = new pl.idedyk.android.japaneselearnhelper.screen.TableRow();

		addKanaItem(chy_tableRow, kanaCache, "ちゃ");
		addKanaItem(chy_tableRow, kanaCache, null);
		addKanaItem(chy_tableRow, kanaCache, "ちゅ");
		addKanaItem(chy_tableRow, kanaCache, null);
		addKanaItem(chy_tableRow, kanaCache, "ちょ");
		
		screenItems.add(chy_tableRow);
		
		pl.idedyk.android.japaneselearnhelper.screen.TableRow ny_tableRow = new pl.idedyk.android.japaneselearnhelper.screen.TableRow();

		addKanaItem(ny_tableRow, kanaCache, "にゃ");
		addKanaItem(ny_tableRow, kanaCache, null);
		addKanaItem(ny_tableRow, kanaCache, "にゅ");
		addKanaItem(ny_tableRow, kanaCache, null);
		addKanaItem(ny_tableRow, kanaCache, "にょ");
		
		screenItems.add(ny_tableRow);
		
		pl.idedyk.android.japaneselearnhelper.screen.TableRow hy_tableRow = new pl.idedyk.android.japaneselearnhelper.screen.TableRow();

		addKanaItem(hy_tableRow, kanaCache, "ひゃ");
		addKanaItem(hy_tableRow, kanaCache, null);
		addKanaItem(hy_tableRow, kanaCache, "ひゅ");
		addKanaItem(hy_tableRow, kanaCache, null);
		addKanaItem(hy_tableRow, kanaCache, "ひょ");
		
		screenItems.add(hy_tableRow);
		
		pl.idedyk.android.japaneselearnhelper.screen.TableRow my_tableRow = new pl.idedyk.android.japaneselearnhelper.screen.TableRow();

		addKanaItem(my_tableRow, kanaCache, "みゃ");
		addKanaItem(my_tableRow, kanaCache, null);
		addKanaItem(my_tableRow, kanaCache, "みゅ");
		addKanaItem(my_tableRow, kanaCache, null);
		addKanaItem(my_tableRow, kanaCache, "みょ");
		
		screenItems.add(my_tableRow);
		
		pl.idedyk.android.japaneselearnhelper.screen.TableRow ry_tableRow = new pl.idedyk.android.japaneselearnhelper.screen.TableRow();

		addKanaItem(ry_tableRow, kanaCache, "りゃ");
		addKanaItem(ry_tableRow, kanaCache, null);
		addKanaItem(ry_tableRow, kanaCache, "りゅ");
		addKanaItem(ry_tableRow, kanaCache, null);
		addKanaItem(ry_tableRow, kanaCache, "りょ");
		
		screenItems.add(ry_tableRow);
		
		pl.idedyk.android.japaneselearnhelper.screen.TableRow gy_tableRow = new pl.idedyk.android.japaneselearnhelper.screen.TableRow();

		addKanaItem(gy_tableRow, kanaCache, "ぎゃ");
		addKanaItem(gy_tableRow, kanaCache, null);
		addKanaItem(gy_tableRow, kanaCache, "ぎゅ");
		addKanaItem(gy_tableRow, kanaCache, null);
		addKanaItem(gy_tableRow, kanaCache, "ぎょ");
		
		screenItems.add(gy_tableRow);
		
		pl.idedyk.android.japaneselearnhelper.screen.TableRow jy_tableRow = new pl.idedyk.android.japaneselearnhelper.screen.TableRow();

		addKanaItem(jy_tableRow, kanaCache, "じゃ");
		addKanaItem(jy_tableRow, kanaCache, null);
		addKanaItem(jy_tableRow, kanaCache, "じゅ");
		addKanaItem(jy_tableRow, kanaCache, null);
		addKanaItem(jy_tableRow, kanaCache, "じょ");
		
		screenItems.add(jy_tableRow);
		
		pl.idedyk.android.japaneselearnhelper.screen.TableRow by_tableRow = new pl.idedyk.android.japaneselearnhelper.screen.TableRow();

		addKanaItem(by_tableRow, kanaCache, "びゃ");
		addKanaItem(by_tableRow, kanaCache, null);
		addKanaItem(by_tableRow, kanaCache, "びゅ");
		addKanaItem(by_tableRow, kanaCache, null);
		addKanaItem(by_tableRow, kanaCache, "びょ");
		
		screenItems.add(by_tableRow);
		
		pl.idedyk.android.japaneselearnhelper.screen.TableRow py_tableRow = new pl.idedyk.android.japaneselearnhelper.screen.TableRow();

		addKanaItem(py_tableRow, kanaCache, "ぴゃ");
		addKanaItem(py_tableRow, kanaCache, null);
		addKanaItem(py_tableRow, kanaCache, "ぴゅ");
		addKanaItem(py_tableRow, kanaCache, null);
		addKanaItem(py_tableRow, kanaCache, "ぴょ");
		
		screenItems.add(py_tableRow);
	}

	private void generateKatakanaTable(List<IScreenItem> screenItems, Map<String, KanaEntry> kanaCache) {
		
		screenItems.add(new TitleItem(getString(R.string.kana_katakana_label), 0));
		screenItems.add(new StringValue(getString(R.string.kana_info), 12.0f, 0));
		
		pl.idedyk.android.japaneselearnhelper.screen.TableRow a_tableRow = new pl.idedyk.android.japaneselearnhelper.screen.TableRow();
		
		addKanaItem(a_tableRow, kanaCache, "ア");
		addKanaItem(a_tableRow, kanaCache, "イ");
		addKanaItem(a_tableRow, kanaCache, "ウ");
		addKanaItem(a_tableRow, kanaCache, "エ");
		addKanaItem(a_tableRow, kanaCache, "オ");
		
		screenItems.add(a_tableRow);
		
		pl.idedyk.android.japaneselearnhelper.screen.TableRow k_tableRow = new pl.idedyk.android.japaneselearnhelper.screen.TableRow();
		
		addKanaItem(k_tableRow, kanaCache, "カ");
		addKanaItem(k_tableRow, kanaCache, "キ");
		addKanaItem(k_tableRow, kanaCache, "ク");
		addKanaItem(k_tableRow, kanaCache, "ケ");
		addKanaItem(k_tableRow, kanaCache, "コ");
				
		screenItems.add(k_tableRow);

		pl.idedyk.android.japaneselearnhelper.screen.TableRow s_tableRow = new pl.idedyk.android.japaneselearnhelper.screen.TableRow();
		
		addKanaItem(s_tableRow, kanaCache, "サ");
		addKanaItem(s_tableRow, kanaCache, "シ");
		addKanaItem(s_tableRow, kanaCache, "ス");
		addKanaItem(s_tableRow, kanaCache, "セ");
		addKanaItem(s_tableRow, kanaCache, "ソ");
						
		screenItems.add(s_tableRow);
		
		pl.idedyk.android.japaneselearnhelper.screen.TableRow t_tableRow = new pl.idedyk.android.japaneselearnhelper.screen.TableRow();
		
		addKanaItem(t_tableRow, kanaCache, "タ");
		addKanaItem(t_tableRow, kanaCache, "チ");
		addKanaItem(t_tableRow, kanaCache, "ツ");
		addKanaItem(t_tableRow, kanaCache, "テ");
		addKanaItem(t_tableRow, kanaCache, "ト");
		
		screenItems.add(t_tableRow);
		
		pl.idedyk.android.japaneselearnhelper.screen.TableRow n_tableRow = new pl.idedyk.android.japaneselearnhelper.screen.TableRow();

		addKanaItem(n_tableRow, kanaCache, "ナ");
		addKanaItem(n_tableRow, kanaCache, "ニ");
		addKanaItem(n_tableRow, kanaCache, "ヌ");
		addKanaItem(n_tableRow, kanaCache, "ネ");
		addKanaItem(n_tableRow, kanaCache, "ノ");
		
		screenItems.add(n_tableRow);
		
		pl.idedyk.android.japaneselearnhelper.screen.TableRow h_tableRow = new pl.idedyk.android.japaneselearnhelper.screen.TableRow();

		addKanaItem(h_tableRow, kanaCache, "ハ");
		addKanaItem(h_tableRow, kanaCache, "ヒ");
		addKanaItem(h_tableRow, kanaCache, "フ");
		addKanaItem(h_tableRow, kanaCache, "ヘ");
		addKanaItem(h_tableRow, kanaCache, "ホ");
		
		screenItems.add(h_tableRow);
		
		pl.idedyk.android.japaneselearnhelper.screen.TableRow m_tableRow = new pl.idedyk.android.japaneselearnhelper.screen.TableRow();

		addKanaItem(m_tableRow, kanaCache, "マ");
		addKanaItem(m_tableRow, kanaCache, "ミ");
		addKanaItem(m_tableRow, kanaCache, "ム");
		addKanaItem(m_tableRow, kanaCache, "メ");
		addKanaItem(m_tableRow, kanaCache, "モ");
		
		screenItems.add(m_tableRow);
		
		pl.idedyk.android.japaneselearnhelper.screen.TableRow y_tableRow = new pl.idedyk.android.japaneselearnhelper.screen.TableRow();
		
		addKanaItem(y_tableRow, kanaCache, "ヤ");
		addKanaItem(y_tableRow, kanaCache, null);
		addKanaItem(y_tableRow, kanaCache, "ユ");
		addKanaItem(y_tableRow, kanaCache, null);
		addKanaItem(y_tableRow, kanaCache, "ヨ");
		
		screenItems.add(y_tableRow);
		
		pl.idedyk.android.japaneselearnhelper.screen.TableRow r_tableRow = new pl.idedyk.android.japaneselearnhelper.screen.TableRow();

		addKanaItem(r_tableRow, kanaCache, "ラ");
		addKanaItem(r_tableRow, kanaCache, "リ");
		addKanaItem(r_tableRow, kanaCache, "ル");
		addKanaItem(r_tableRow, kanaCache, "レ");
		addKanaItem(r_tableRow, kanaCache, "ロ");
		
		screenItems.add(r_tableRow);
		
		pl.idedyk.android.japaneselearnhelper.screen.TableRow w_tableRow = new pl.idedyk.android.japaneselearnhelper.screen.TableRow();
		
		addKanaItem(w_tableRow, kanaCache, "ワ");
		addKanaItem(w_tableRow, kanaCache, null);
		addKanaItem(w_tableRow, kanaCache, null);
		addKanaItem(w_tableRow, kanaCache, null);
		addKanaItem(w_tableRow, kanaCache, "ヲ");
		
		screenItems.add(w_tableRow);
		
		pl.idedyk.android.japaneselearnhelper.screen.TableRow n2_tableRow = new pl.idedyk.android.japaneselearnhelper.screen.TableRow();

		addKanaItem(n2_tableRow, kanaCache, "ン");
		addKanaItem(n2_tableRow, kanaCache, null);
		addKanaItem(n2_tableRow, kanaCache, null);
		addKanaItem(n2_tableRow, kanaCache, null);
		addKanaItem(n2_tableRow, kanaCache, null);
		
		screenItems.add(n2_tableRow);

		pl.idedyk.android.japaneselearnhelper.screen.TableRow g_tableRow = new pl.idedyk.android.japaneselearnhelper.screen.TableRow();

		addKanaItem(g_tableRow, kanaCache, "ガ");
		addKanaItem(g_tableRow, kanaCache, "ギ");
		addKanaItem(g_tableRow, kanaCache, "グ");
		addKanaItem(g_tableRow, kanaCache, "ゲ");
		addKanaItem(g_tableRow, kanaCache, "ゴ");

		screenItems.add(g_tableRow);
		
		pl.idedyk.android.japaneselearnhelper.screen.TableRow z_tableRow = new pl.idedyk.android.japaneselearnhelper.screen.TableRow();

		addKanaItem(z_tableRow, kanaCache, "ザ");
		addKanaItem(z_tableRow, kanaCache, "ジ");
		addKanaItem(z_tableRow, kanaCache, "ズ");
		addKanaItem(z_tableRow, kanaCache, "ゼ");
		addKanaItem(z_tableRow, kanaCache, "ゾ");
		
		screenItems.add(z_tableRow);
		
		pl.idedyk.android.japaneselearnhelper.screen.TableRow d_tableRow = new pl.idedyk.android.japaneselearnhelper.screen.TableRow();

		addKanaItem(d_tableRow, kanaCache, "ダ");
		addKanaItem(d_tableRow, kanaCache, "ヂ");
		addKanaItem(d_tableRow, kanaCache, "づ");
		addKanaItem(d_tableRow, kanaCache, "デ");
		addKanaItem(d_tableRow, kanaCache, "ド");
		
		screenItems.add(d_tableRow);
		
		pl.idedyk.android.japaneselearnhelper.screen.TableRow b_tableRow = new pl.idedyk.android.japaneselearnhelper.screen.TableRow();

		addKanaItem(b_tableRow, kanaCache, "バ");
		addKanaItem(b_tableRow, kanaCache, "ビ");
		addKanaItem(b_tableRow, kanaCache, "ブ");
		addKanaItem(b_tableRow, kanaCache, "ベ");
		addKanaItem(b_tableRow, kanaCache, "ボ");
		
		screenItems.add(b_tableRow);
		
		pl.idedyk.android.japaneselearnhelper.screen.TableRow p_tableRow = new pl.idedyk.android.japaneselearnhelper.screen.TableRow();

		addKanaItem(p_tableRow, kanaCache, "パ");
		addKanaItem(p_tableRow, kanaCache, "ピ");
		addKanaItem(p_tableRow, kanaCache, "プ");
		addKanaItem(p_tableRow, kanaCache, "ペ");
		addKanaItem(p_tableRow, kanaCache, "ポ");
		
		screenItems.add(p_tableRow);
		
		pl.idedyk.android.japaneselearnhelper.screen.TableRow ky_tableRow = new pl.idedyk.android.japaneselearnhelper.screen.TableRow();

		addKanaItem(ky_tableRow, kanaCache, "キャ");
		addKanaItem(ky_tableRow, kanaCache, null);
		addKanaItem(ky_tableRow, kanaCache, "キュ");
		addKanaItem(ky_tableRow, kanaCache, null);
		addKanaItem(ky_tableRow, kanaCache, "キョ");
		
		screenItems.add(ky_tableRow);
		
		pl.idedyk.android.japaneselearnhelper.screen.TableRow shy_tableRow = new pl.idedyk.android.japaneselearnhelper.screen.TableRow();

		addKanaItem(shy_tableRow, kanaCache, "シャ");
		addKanaItem(shy_tableRow, kanaCache, null);
		addKanaItem(shy_tableRow, kanaCache, "シュ");
		addKanaItem(shy_tableRow, kanaCache, null);
		addKanaItem(shy_tableRow, kanaCache, "ショ");
		
		screenItems.add(shy_tableRow);
		
		pl.idedyk.android.japaneselearnhelper.screen.TableRow chy_tableRow = new pl.idedyk.android.japaneselearnhelper.screen.TableRow();

		addKanaItem(chy_tableRow, kanaCache, "チャ");
		addKanaItem(chy_tableRow, kanaCache, null);
		addKanaItem(chy_tableRow, kanaCache, "チュ");
		addKanaItem(chy_tableRow, kanaCache, null);
		addKanaItem(chy_tableRow, kanaCache, "チョ");
		
		screenItems.add(chy_tableRow);
		
		pl.idedyk.android.japaneselearnhelper.screen.TableRow ny_tableRow = new pl.idedyk.android.japaneselearnhelper.screen.TableRow();

		addKanaItem(ny_tableRow, kanaCache, "ニャ");
		addKanaItem(ny_tableRow, kanaCache, null);
		addKanaItem(ny_tableRow, kanaCache, "ニュ");
		addKanaItem(ny_tableRow, kanaCache, null);
		addKanaItem(ny_tableRow, kanaCache, "ニョ");
		
		screenItems.add(ny_tableRow);
		
		pl.idedyk.android.japaneselearnhelper.screen.TableRow hy_tableRow = new pl.idedyk.android.japaneselearnhelper.screen.TableRow();

		addKanaItem(hy_tableRow, kanaCache, "ヒャ");
		addKanaItem(hy_tableRow, kanaCache, null);
		addKanaItem(hy_tableRow, kanaCache, "ヒュ");
		addKanaItem(hy_tableRow, kanaCache, null);
		addKanaItem(hy_tableRow, kanaCache, "ヒョ");
		
		screenItems.add(hy_tableRow);
		
		pl.idedyk.android.japaneselearnhelper.screen.TableRow my_tableRow = new pl.idedyk.android.japaneselearnhelper.screen.TableRow();

		addKanaItem(my_tableRow, kanaCache, "ミャ");
		addKanaItem(my_tableRow, kanaCache, null);
		addKanaItem(my_tableRow, kanaCache, "ミュ");
		addKanaItem(my_tableRow, kanaCache, null);
		addKanaItem(my_tableRow, kanaCache, "ミョ");
		
		screenItems.add(my_tableRow);
		
		pl.idedyk.android.japaneselearnhelper.screen.TableRow ry_tableRow = new pl.idedyk.android.japaneselearnhelper.screen.TableRow();

		addKanaItem(ry_tableRow, kanaCache, "リャ");
		addKanaItem(ry_tableRow, kanaCache, null);
		addKanaItem(ry_tableRow, kanaCache, "リュ");
		addKanaItem(ry_tableRow, kanaCache, null);
		addKanaItem(ry_tableRow, kanaCache, "リョ");
		
		screenItems.add(ry_tableRow);
		
		pl.idedyk.android.japaneselearnhelper.screen.TableRow gy_tableRow = new pl.idedyk.android.japaneselearnhelper.screen.TableRow();

		addKanaItem(gy_tableRow, kanaCache, "ギャ");
		addKanaItem(gy_tableRow, kanaCache, null);
		addKanaItem(gy_tableRow, kanaCache, "ギュ");
		addKanaItem(gy_tableRow, kanaCache, null);
		addKanaItem(gy_tableRow, kanaCache, "ギョ");
		
		screenItems.add(gy_tableRow);
		
		pl.idedyk.android.japaneselearnhelper.screen.TableRow jy_tableRow = new pl.idedyk.android.japaneselearnhelper.screen.TableRow();

		addKanaItem(jy_tableRow, kanaCache, "ジャ");
		addKanaItem(jy_tableRow, kanaCache, null);
		addKanaItem(jy_tableRow, kanaCache, "ジュ");
		addKanaItem(jy_tableRow, kanaCache, null);
		addKanaItem(jy_tableRow, kanaCache, "ジョ");
		
		screenItems.add(jy_tableRow);
		
		pl.idedyk.android.japaneselearnhelper.screen.TableRow by_tableRow = new pl.idedyk.android.japaneselearnhelper.screen.TableRow();

		addKanaItem(by_tableRow, kanaCache, "ビャ");
		addKanaItem(by_tableRow, kanaCache, null);
		addKanaItem(by_tableRow, kanaCache, "ビュ");
		addKanaItem(by_tableRow, kanaCache, null);
		addKanaItem(by_tableRow, kanaCache, "ビョ");
		
		screenItems.add(by_tableRow);
		
		pl.idedyk.android.japaneselearnhelper.screen.TableRow py_tableRow = new pl.idedyk.android.japaneselearnhelper.screen.TableRow();

		addKanaItem(py_tableRow, kanaCache, "ピャ");
		addKanaItem(py_tableRow, kanaCache, null);
		addKanaItem(py_tableRow, kanaCache, "ピュ");
		addKanaItem(py_tableRow, kanaCache, null);
		addKanaItem(py_tableRow, kanaCache, "ピョ");
		
		screenItems.add(py_tableRow);
	}
	
	private void generateKatakanaAdditionalTable(List<IScreenItem> screenItems, Map<String, KanaEntry> kanaCache) {
		
		screenItems.add(new TitleItem(getString(R.string.kana_katakana_additional_label), 0));
		screenItems.add(new StringValue(getString(R.string.kana_info), 12.0f, 0));

		pl.idedyk.android.japaneselearnhelper.screen.TableRow u_tableRow = new pl.idedyk.android.japaneselearnhelper.screen.TableRow();

		addKanaItem(u_tableRow, kanaCache, null);
		addKanaItem(u_tableRow, kanaCache, "ウィ");
		addKanaItem(u_tableRow, kanaCache, null);
		addKanaItem(u_tableRow, kanaCache, "ウェ");
		addKanaItem(u_tableRow, kanaCache, null);
		
		screenItems.add(u_tableRow);
		
		pl.idedyk.android.japaneselearnhelper.screen.TableRow s_tableRow = new pl.idedyk.android.japaneselearnhelper.screen.TableRow();

		addKanaItem(s_tableRow, kanaCache, null);
		addKanaItem(s_tableRow, kanaCache, null);
		addKanaItem(s_tableRow, kanaCache, null);
		addKanaItem(s_tableRow, kanaCache, "シェ");
		addKanaItem(s_tableRow, kanaCache, null);
		
		screenItems.add(s_tableRow);
		
		pl.idedyk.android.japaneselearnhelper.screen.TableRow j_tableRow = new pl.idedyk.android.japaneselearnhelper.screen.TableRow();

		addKanaItem(j_tableRow, kanaCache, null);
		addKanaItem(j_tableRow, kanaCache, null);
		addKanaItem(j_tableRow, kanaCache, null);
		addKanaItem(j_tableRow, kanaCache, "ジェ");
		addKanaItem(j_tableRow, kanaCache, null);
		
		screenItems.add(j_tableRow);
		
		pl.idedyk.android.japaneselearnhelper.screen.TableRow c_tableRow = new pl.idedyk.android.japaneselearnhelper.screen.TableRow();

		addKanaItem(c_tableRow, kanaCache, null);
		addKanaItem(c_tableRow, kanaCache, null);
		addKanaItem(c_tableRow, kanaCache, null);
		addKanaItem(c_tableRow, kanaCache, "チェ");
		addKanaItem(c_tableRow, kanaCache, null);
		
		screenItems.add(c_tableRow);
		
		pl.idedyk.android.japaneselearnhelper.screen.TableRow f_tableRow = new pl.idedyk.android.japaneselearnhelper.screen.TableRow();

		addKanaItem(f_tableRow, kanaCache, "ファ");
		addKanaItem(f_tableRow, kanaCache, "フィ");
		addKanaItem(f_tableRow, kanaCache, null);
		addKanaItem(f_tableRow, kanaCache, "フェ");
		addKanaItem(f_tableRow, kanaCache, "フォ");
		
		screenItems.add(f_tableRow);
		
		pl.idedyk.android.japaneselearnhelper.screen.TableRow t_tableRow = new pl.idedyk.android.japaneselearnhelper.screen.TableRow();

		addKanaItem(t_tableRow, kanaCache, null);
		addKanaItem(t_tableRow, kanaCache, "ティ");
		addKanaItem(t_tableRow, kanaCache, null);
		addKanaItem(t_tableRow, kanaCache, null);
		addKanaItem(t_tableRow, kanaCache, null);
		
		pl.idedyk.android.japaneselearnhelper.screen.TableRow c2_tableRow = new pl.idedyk.android.japaneselearnhelper.screen.TableRow();
		
		addKanaItem(c2_tableRow, kanaCache, null);
		addKanaItem(c2_tableRow, kanaCache, "ディ");
		addKanaItem(c2_tableRow, kanaCache, null);
		addKanaItem(c2_tableRow, kanaCache, null);
		addKanaItem(c2_tableRow, kanaCache, null);

		screenItems.add(c2_tableRow);
		
		pl.idedyk.android.japaneselearnhelper.screen.TableRow d_tableRow = new pl.idedyk.android.japaneselearnhelper.screen.TableRow();

		addKanaItem(d_tableRow, kanaCache, null);
		addKanaItem(d_tableRow, kanaCache, null);
		addKanaItem(d_tableRow, kanaCache, "ヂュ");
		addKanaItem(d_tableRow, kanaCache, null);
		addKanaItem(d_tableRow, kanaCache, null);
		
		screenItems.add(d_tableRow);		
	}
	
	private void addKanaItem(pl.idedyk.android.japaneselearnhelper.screen.TableRow tableRow, Map<String, KanaEntry> kanaCache, String kana) {
		
		StringValue stringValue = null;
		
		if (kana != null) {
			final KanaEntry kanaEntry = kanaCache.get(kana);
			
			if (kanaEntry == null) {
				throw new RuntimeException();
			}
			
			Spanned spanned = Html.fromHtml("<b>" + kanaEntry.getKana() + "</b><br/>" + kanaEntry.getKanaJapanese() + "<br/>");
			
			stringValue = new StringValue(spanned, 20, 0);	
			
			stringValue.setOnClickListener(new OnClickListener() {
				
				public void onClick(View v) {
					
					List<List<String>> allStrokePaths = kanaEntry.getStrokePaths();
					
					List<String> strokePaths = new ArrayList<String>();
					
					// FIXME !!!!
					
					for (List<String> currentStrokePaths : allStrokePaths) {
						strokePaths.addAll(currentStrokePaths);
					}
					
					Intent intent = new Intent(getApplicationContext(), SodActivity.class);
					
					intent.putStringArrayListExtra("strokePaths", (ArrayList<String>)strokePaths);
					
					startActivity(intent);					
				}
			});
			
		} else {
			stringValue = new StringValue("", 20, 0);
		}
		
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
