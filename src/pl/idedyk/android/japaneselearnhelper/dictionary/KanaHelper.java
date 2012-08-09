package pl.idedyk.android.japaneselearnhelper.dictionary;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import pl.idedyk.android.japaneselearnhelper.dictionary.dto.KanaEntry;
import pl.idedyk.android.japaneselearnhelper.dictionary.dto.KanaEntry.KanaGroup;
import pl.idedyk.android.japaneselearnhelper.dictionary.exception.DictionaryException;

public class KanaHelper {
	
	public static String createKanaString(KanaWord kanaWord) {
		
		StringBuffer sb = new StringBuffer();
		
		for (KanaEntry kanaEntry : kanaWord.kanaEntries) {
			sb.append(kanaEntry.getKanaJapanese());
		}
		
		return sb.toString();
	}
	
	public static String createRomajiString(KanaWord kanaWord) {

		StringBuffer sb = new StringBuffer();

		boolean repeat = false;
		boolean lastN = false;

		for (KanaEntry kanaEntry : kanaWord.kanaEntries) {
			String kana = kanaEntry.getKana();

			if (lastN == true && (kana.startsWith("y") == true || kana.equals("i") == true)) {				
				sb.append("'");

				lastN = true;	
			} else {
				lastN = false;
			}

			if (kana.equals("ttsu") == true) {
				repeat = true;

				continue;
			}

			if (kana.equals("ttsu2") == true) {
				sb.append(sb.charAt(sb.length() - 1));

				continue;				
			}

			if (repeat == true) {
				repeat = false;

				sb.append(kana.charAt(0));
			}

			sb.append(kana);

			if (kana.equals("n") == true) {
				lastN = true; 
			}
		}

		return sb.toString();
	}
	
	public static List<KanaEntry> getAllHiraganaKanaEntries() {
		
		List<KanaEntry> hiraganaEntries = new ArrayList<KanaEntry>();
		
		hiraganaEntries.add(new KanaEntry("あ", "a", KanaGroup.GOJUUON));
		hiraganaEntries.add(new KanaEntry("い", "i", KanaGroup.GOJUUON));
		hiraganaEntries.add(new KanaEntry("う", "u", KanaGroup.GOJUUON));
		hiraganaEntries.add(new KanaEntry("え", "e", KanaGroup.GOJUUON));
		hiraganaEntries.add(new KanaEntry("お", "o", KanaGroup.GOJUUON));
		
		hiraganaEntries.add(new KanaEntry("か", "ka", KanaGroup.GOJUUON));
		hiraganaEntries.add(new KanaEntry("き", "ki", KanaGroup.GOJUUON));
		hiraganaEntries.add(new KanaEntry("く", "ku", KanaGroup.GOJUUON));
		hiraganaEntries.add(new KanaEntry("け", "ke", KanaGroup.GOJUUON));
		hiraganaEntries.add(new KanaEntry("こ", "ko", KanaGroup.GOJUUON));
		
		hiraganaEntries.add(new KanaEntry("さ", "sa", KanaGroup.GOJUUON));
		hiraganaEntries.add(new KanaEntry("し", "shi", KanaGroup.GOJUUON));
		hiraganaEntries.add(new KanaEntry("す", "su", KanaGroup.GOJUUON));
		hiraganaEntries.add(new KanaEntry("せ", "se", KanaGroup.GOJUUON));
		hiraganaEntries.add(new KanaEntry("そ", "so", KanaGroup.GOJUUON));
		
		hiraganaEntries.add(new KanaEntry("た", "ta", KanaGroup.GOJUUON));
		hiraganaEntries.add(new KanaEntry("ち", "chi", KanaGroup.GOJUUON));
		hiraganaEntries.add(new KanaEntry("つ", "tsu", KanaGroup.GOJUUON));
		hiraganaEntries.add(new KanaEntry("て", "te", KanaGroup.GOJUUON));
		hiraganaEntries.add(new KanaEntry("と", "to", KanaGroup.GOJUUON));
		
		hiraganaEntries.add(new KanaEntry("な", "na", KanaGroup.GOJUUON));
		hiraganaEntries.add(new KanaEntry("に", "ni", KanaGroup.GOJUUON));
		hiraganaEntries.add(new KanaEntry("ぬ", "nu", KanaGroup.GOJUUON));
		hiraganaEntries.add(new KanaEntry("ね", "ne", KanaGroup.GOJUUON));
		hiraganaEntries.add(new KanaEntry("の", "no", KanaGroup.GOJUUON));
		
		hiraganaEntries.add(new KanaEntry("は", "ha", KanaGroup.GOJUUON));
		hiraganaEntries.add(new KanaEntry("ひ", "hi", KanaGroup.GOJUUON));
		hiraganaEntries.add(new KanaEntry("ふ", "fu", KanaGroup.GOJUUON));
		hiraganaEntries.add(new KanaEntry("へ", "he", KanaGroup.GOJUUON));
		hiraganaEntries.add(new KanaEntry("ほ", "ho", KanaGroup.GOJUUON));
		
		hiraganaEntries.add(new KanaEntry("ま", "ma", KanaGroup.GOJUUON));
		hiraganaEntries.add(new KanaEntry("み", "mi", KanaGroup.GOJUUON));
		hiraganaEntries.add(new KanaEntry("む", "mu", KanaGroup.GOJUUON));
		hiraganaEntries.add(new KanaEntry("め", "me", KanaGroup.GOJUUON));
		hiraganaEntries.add(new KanaEntry("も", "mo", KanaGroup.GOJUUON));
		
		hiraganaEntries.add(new KanaEntry("や", "ya", KanaGroup.GOJUUON));
		hiraganaEntries.add(new KanaEntry("ゆ", "yu", KanaGroup.GOJUUON));
		hiraganaEntries.add(new KanaEntry("よ", "yo", KanaGroup.GOJUUON));
		
		hiraganaEntries.add(new KanaEntry("ら", "ra", KanaGroup.GOJUUON));
		hiraganaEntries.add(new KanaEntry("り", "ri", KanaGroup.GOJUUON));
		hiraganaEntries.add(new KanaEntry("る", "ru", KanaGroup.GOJUUON));
		hiraganaEntries.add(new KanaEntry("れ", "re", KanaGroup.GOJUUON));
		hiraganaEntries.add(new KanaEntry("ろ", "ro", KanaGroup.GOJUUON));
		
		hiraganaEntries.add(new KanaEntry("わ", "wa", KanaGroup.GOJUUON));
		hiraganaEntries.add(new KanaEntry("を", "wo", KanaGroup.GOJUUON));
		
		hiraganaEntries.add(new KanaEntry("ん", "n", KanaGroup.GOJUUON));
		
		hiraganaEntries.add(new KanaEntry("が", "ga", KanaGroup.DAKUTEN));
		hiraganaEntries.add(new KanaEntry("ぎ", "gi", KanaGroup.DAKUTEN));
		hiraganaEntries.add(new KanaEntry("ぐ", "gu", KanaGroup.DAKUTEN));
		hiraganaEntries.add(new KanaEntry("げ", "ge", KanaGroup.DAKUTEN));
		hiraganaEntries.add(new KanaEntry("ご", "go", KanaGroup.DAKUTEN));
		
		hiraganaEntries.add(new KanaEntry("ざ", "za", KanaGroup.DAKUTEN));
		hiraganaEntries.add(new KanaEntry("じ", "ji", KanaGroup.DAKUTEN));
		hiraganaEntries.add(new KanaEntry("ず", "zu", KanaGroup.DAKUTEN));
		hiraganaEntries.add(new KanaEntry("ぜ", "ze", KanaGroup.DAKUTEN));
		hiraganaEntries.add(new KanaEntry("ぞ", "zo", KanaGroup.DAKUTEN));
		
		hiraganaEntries.add(new KanaEntry("だ", "da", KanaGroup.DAKUTEN));
		hiraganaEntries.add(new KanaEntry("ぢ", "di", KanaGroup.DAKUTEN));
		hiraganaEntries.add(new KanaEntry("づ", "du", KanaGroup.DAKUTEN));
		hiraganaEntries.add(new KanaEntry("で", "de", KanaGroup.DAKUTEN));
		hiraganaEntries.add(new KanaEntry("ど", "do", KanaGroup.DAKUTEN));
		
		hiraganaEntries.add(new KanaEntry("ば", "ba", KanaGroup.DAKUTEN));
		hiraganaEntries.add(new KanaEntry("び", "bi", KanaGroup.DAKUTEN));
		hiraganaEntries.add(new KanaEntry("ぶ", "bu", KanaGroup.DAKUTEN));
		hiraganaEntries.add(new KanaEntry("べ", "be", KanaGroup.DAKUTEN));
		hiraganaEntries.add(new KanaEntry("ぼ", "bo", KanaGroup.DAKUTEN));
		
		hiraganaEntries.add(new KanaEntry("ぱ", "pa", KanaGroup.DAKUTEN));
		hiraganaEntries.add(new KanaEntry("ぴ", "pi", KanaGroup.DAKUTEN));
		hiraganaEntries.add(new KanaEntry("ぷ", "pu", KanaGroup.DAKUTEN));
		hiraganaEntries.add(new KanaEntry("ぺ", "pe", KanaGroup.DAKUTEN));
		hiraganaEntries.add(new KanaEntry("ぽ", "po", KanaGroup.DAKUTEN));
		
		hiraganaEntries.add(new KanaEntry("きゃ", "kya", KanaGroup.YOOUN));
		hiraganaEntries.add(new KanaEntry("きゅ", "kyu", KanaGroup.YOOUN));
		hiraganaEntries.add(new KanaEntry("きょ", "kyo", KanaGroup.YOOUN));
		
		hiraganaEntries.add(new KanaEntry("しゃ", "sha", KanaGroup.YOOUN));
		hiraganaEntries.add(new KanaEntry("しゅ", "shu", KanaGroup.YOOUN));
		hiraganaEntries.add(new KanaEntry("しょ", "sho", KanaGroup.YOOUN));
		
		hiraganaEntries.add(new KanaEntry("ちゃ", "cha", KanaGroup.YOOUN));
		hiraganaEntries.add(new KanaEntry("ちゅ", "chu", KanaGroup.YOOUN));
		hiraganaEntries.add(new KanaEntry("ちょ", "cho", KanaGroup.YOOUN));
		
		hiraganaEntries.add(new KanaEntry("にゃ", "nya", KanaGroup.YOOUN));
		hiraganaEntries.add(new KanaEntry("にゅ", "nyu", KanaGroup.YOOUN));
		hiraganaEntries.add(new KanaEntry("にょ", "nyo", KanaGroup.YOOUN));
		
		hiraganaEntries.add(new KanaEntry("ひゃ", "hya", KanaGroup.YOOUN));
		hiraganaEntries.add(new KanaEntry("ひゅ", "hyu", KanaGroup.YOOUN));
		hiraganaEntries.add(new KanaEntry("ひょ", "hyo", KanaGroup.YOOUN));
		
		hiraganaEntries.add(new KanaEntry("みゃ", "mya", KanaGroup.YOOUN));
		hiraganaEntries.add(new KanaEntry("みゅ", "myu", KanaGroup.YOOUN));
		hiraganaEntries.add(new KanaEntry("みょ", "myo", KanaGroup.YOOUN));
		
		hiraganaEntries.add(new KanaEntry("りゃ", "rya", KanaGroup.YOOUN));
		hiraganaEntries.add(new KanaEntry("りゅ", "ryu", KanaGroup.YOOUN));
		hiraganaEntries.add(new KanaEntry("りょ", "ryo", KanaGroup.YOOUN));
		
		hiraganaEntries.add(new KanaEntry("ぎゃ", "gya", KanaGroup.YOOUN));
		hiraganaEntries.add(new KanaEntry("ぎゅ", "gyu", KanaGroup.YOOUN));
		hiraganaEntries.add(new KanaEntry("ぎょ", "gyo", KanaGroup.YOOUN));
		
		hiraganaEntries.add(new KanaEntry("じゃ", "ja", KanaGroup.YOOUN));
		hiraganaEntries.add(new KanaEntry("じゅ", "ju", KanaGroup.YOOUN));
		hiraganaEntries.add(new KanaEntry("じょ", "jo", KanaGroup.YOOUN));
		
		hiraganaEntries.add(new KanaEntry("びゃ", "bya", KanaGroup.YOOUN));
		hiraganaEntries.add(new KanaEntry("びゅ", "byu", KanaGroup.YOOUN));
		hiraganaEntries.add(new KanaEntry("びょ", "byo", KanaGroup.YOOUN));
		
		hiraganaEntries.add(new KanaEntry("ぴゃ", "pya", KanaGroup.YOOUN));
		hiraganaEntries.add(new KanaEntry("ぴゅ", "pyu", KanaGroup.YOOUN));
		hiraganaEntries.add(new KanaEntry("ぴょ", "pyo", KanaGroup.YOOUN));
		
		hiraganaEntries.add(new KanaEntry("っ", "ttsu", KanaGroup.OTHER));
		
		return hiraganaEntries;		
	}
	
	public static List<KanaEntry> getAllKatakanaKanaEntries() {
		
		List<KanaEntry> katakanaEntries = new ArrayList<KanaEntry>();
		
		katakanaEntries.add(new KanaEntry("ア", "a", KanaGroup.GOJUUON));
		katakanaEntries.add(new KanaEntry("イ", "i", KanaGroup.GOJUUON));
		katakanaEntries.add(new KanaEntry("ウ", "u", KanaGroup.GOJUUON));
		katakanaEntries.add(new KanaEntry("エ", "e", KanaGroup.GOJUUON));
		katakanaEntries.add(new KanaEntry("オ", "o", KanaGroup.GOJUUON));
		
		katakanaEntries.add(new KanaEntry("カ", "ka", KanaGroup.GOJUUON));
		katakanaEntries.add(new KanaEntry("キ", "ki", KanaGroup.GOJUUON));
		katakanaEntries.add(new KanaEntry("ク", "ku", KanaGroup.GOJUUON));
		katakanaEntries.add(new KanaEntry("ケ", "ke", KanaGroup.GOJUUON));
		katakanaEntries.add(new KanaEntry("コ", "ko", KanaGroup.GOJUUON));
		
		katakanaEntries.add(new KanaEntry("サ", "sa", KanaGroup.GOJUUON));
		katakanaEntries.add(new KanaEntry("シ", "shi", KanaGroup.GOJUUON));
		katakanaEntries.add(new KanaEntry("ス", "su", KanaGroup.GOJUUON));
		katakanaEntries.add(new KanaEntry("セ", "se", KanaGroup.GOJUUON));
		katakanaEntries.add(new KanaEntry("ソ", "so", KanaGroup.GOJUUON));
		
		katakanaEntries.add(new KanaEntry("タ", "ta", KanaGroup.GOJUUON));
		katakanaEntries.add(new KanaEntry("チ", "chi", KanaGroup.GOJUUON));
		katakanaEntries.add(new KanaEntry("ツ", "tsu", KanaGroup.GOJUUON));
		katakanaEntries.add(new KanaEntry("テ", "te", KanaGroup.GOJUUON));
		katakanaEntries.add(new KanaEntry("ト", "to", KanaGroup.GOJUUON));
		
		katakanaEntries.add(new KanaEntry("ナ", "na", KanaGroup.GOJUUON));
		katakanaEntries.add(new KanaEntry("ニ", "ni", KanaGroup.GOJUUON));
		katakanaEntries.add(new KanaEntry("ヌ", "nu", KanaGroup.GOJUUON));
		katakanaEntries.add(new KanaEntry("ネ", "ne", KanaGroup.GOJUUON));
		katakanaEntries.add(new KanaEntry("ノ", "no", KanaGroup.GOJUUON));
		
		katakanaEntries.add(new KanaEntry("ハ", "ha", KanaGroup.GOJUUON));
		katakanaEntries.add(new KanaEntry("匕", "hi", KanaGroup.GOJUUON));
		katakanaEntries.add(new KanaEntry("フ", "fu", KanaGroup.GOJUUON));
		katakanaEntries.add(new KanaEntry("ヘ", "he", KanaGroup.GOJUUON));
		katakanaEntries.add(new KanaEntry("ホ", "ho", KanaGroup.GOJUUON));
		
		katakanaEntries.add(new KanaEntry("マ", "ma", KanaGroup.GOJUUON));
		katakanaEntries.add(new KanaEntry("ミ", "mi", KanaGroup.GOJUUON));
		katakanaEntries.add(new KanaEntry("厶", "mu", KanaGroup.GOJUUON));
		katakanaEntries.add(new KanaEntry("ム", "mu", KanaGroup.GOJUUON));
		katakanaEntries.add(new KanaEntry("メ", "me", KanaGroup.GOJUUON));
		katakanaEntries.add(new KanaEntry("モ", "mo", KanaGroup.GOJUUON));
		
		katakanaEntries.add(new KanaEntry("ヤ", "ya", KanaGroup.GOJUUON));
		katakanaEntries.add(new KanaEntry("ユ", "yu", KanaGroup.GOJUUON));
		katakanaEntries.add(new KanaEntry("ヨ", "yo", KanaGroup.GOJUUON));
		
		katakanaEntries.add(new KanaEntry("ラ", "ra", KanaGroup.GOJUUON));
		katakanaEntries.add(new KanaEntry("リ", "ri", KanaGroup.GOJUUON));
		katakanaEntries.add(new KanaEntry("ル", "ru", KanaGroup.GOJUUON));
		katakanaEntries.add(new KanaEntry("レ", "re", KanaGroup.GOJUUON));
		katakanaEntries.add(new KanaEntry("ロ", "ro", KanaGroup.GOJUUON));
		
		katakanaEntries.add(new KanaEntry("ワ", "wa", KanaGroup.GOJUUON));
		katakanaEntries.add(new KanaEntry("ヲ", "wo", KanaGroup.GOJUUON));
		
		katakanaEntries.add(new KanaEntry("ン", "n", KanaGroup.GOJUUON));
		
		katakanaEntries.add(new KanaEntry("ガ", "ga", KanaGroup.DAKUTEN));
		katakanaEntries.add(new KanaEntry("ギ", "gi", KanaGroup.DAKUTEN));
		katakanaEntries.add(new KanaEntry("グ", "gu", KanaGroup.DAKUTEN));
		katakanaEntries.add(new KanaEntry("ゲ", "ge", KanaGroup.DAKUTEN));
		katakanaEntries.add(new KanaEntry("ゴ", "go", KanaGroup.DAKUTEN));
		
		katakanaEntries.add(new KanaEntry("ザ", "za", KanaGroup.DAKUTEN));
		katakanaEntries.add(new KanaEntry("ジ", "ji", KanaGroup.DAKUTEN));
		katakanaEntries.add(new KanaEntry("ズ", "zu", KanaGroup.DAKUTEN));
		katakanaEntries.add(new KanaEntry("ゼ", "ze", KanaGroup.DAKUTEN));
		katakanaEntries.add(new KanaEntry("ゾ", "zo", KanaGroup.DAKUTEN));
		
		katakanaEntries.add(new KanaEntry("ダ", "da", KanaGroup.DAKUTEN));
		katakanaEntries.add(new KanaEntry("ヂ", "di", KanaGroup.DAKUTEN));
		katakanaEntries.add(new KanaEntry("づ", "du", KanaGroup.DAKUTEN));
		katakanaEntries.add(new KanaEntry("デ", "de", KanaGroup.DAKUTEN));
		katakanaEntries.add(new KanaEntry("ド", "do", KanaGroup.DAKUTEN));
		
		katakanaEntries.add(new KanaEntry("バ", "ba", KanaGroup.DAKUTEN));
		katakanaEntries.add(new KanaEntry("ビ", "bi", KanaGroup.DAKUTEN));
		katakanaEntries.add(new KanaEntry("ブ", "bu", KanaGroup.DAKUTEN));
		katakanaEntries.add(new KanaEntry("ベ", "be", KanaGroup.DAKUTEN));
		katakanaEntries.add(new KanaEntry("ボ", "bo", KanaGroup.DAKUTEN));
		
		katakanaEntries.add(new KanaEntry("パ", "pa", KanaGroup.DAKUTEN));
		katakanaEntries.add(new KanaEntry("ピ", "pi", KanaGroup.DAKUTEN));
		katakanaEntries.add(new KanaEntry("プ", "pu", KanaGroup.DAKUTEN));
		katakanaEntries.add(new KanaEntry("ペ", "pe", KanaGroup.DAKUTEN));
		katakanaEntries.add(new KanaEntry("ポ", "po", KanaGroup.DAKUTEN));
		
		katakanaEntries.add(new KanaEntry("キャ", "kya", KanaGroup.YOOUN));
		katakanaEntries.add(new KanaEntry("キュ", "kyu", KanaGroup.YOOUN));
		katakanaEntries.add(new KanaEntry("キョ", "kyo", KanaGroup.YOOUN));
		
		katakanaEntries.add(new KanaEntry("シャ", "sha", KanaGroup.YOOUN));
		katakanaEntries.add(new KanaEntry("シュ", "shu", KanaGroup.YOOUN));
		katakanaEntries.add(new KanaEntry("ショ", "sho", KanaGroup.YOOUN));
		
		katakanaEntries.add(new KanaEntry("チャ", "cha", KanaGroup.YOOUN));
		katakanaEntries.add(new KanaEntry("チュ", "chu", KanaGroup.YOOUN));
		katakanaEntries.add(new KanaEntry("チョ", "cho", KanaGroup.YOOUN));
		
		katakanaEntries.add(new KanaEntry("ニャ", "nya", KanaGroup.YOOUN));
		katakanaEntries.add(new KanaEntry("ニュ", "nyu", KanaGroup.YOOUN));
		katakanaEntries.add(new KanaEntry("ニョ", "nyo", KanaGroup.YOOUN));
		
		katakanaEntries.add(new KanaEntry("ヒャ", "hya", KanaGroup.YOOUN));
		katakanaEntries.add(new KanaEntry("ヒュ", "hyu", KanaGroup.YOOUN));
		katakanaEntries.add(new KanaEntry("ヒョ", "hyo", KanaGroup.YOOUN));
		
		katakanaEntries.add(new KanaEntry("ミャ", "mya", KanaGroup.YOOUN));
		katakanaEntries.add(new KanaEntry("ミュ", "myu", KanaGroup.YOOUN));
		katakanaEntries.add(new KanaEntry("ミョ", "myo", KanaGroup.YOOUN));
		
		katakanaEntries.add(new KanaEntry("リャ", "rya", KanaGroup.YOOUN));
		katakanaEntries.add(new KanaEntry("リュ", "ryu", KanaGroup.YOOUN));
		katakanaEntries.add(new KanaEntry("リョ", "ryo", KanaGroup.YOOUN));
		
		katakanaEntries.add(new KanaEntry("ギャ", "gya", KanaGroup.YOOUN));
		katakanaEntries.add(new KanaEntry("ギュ", "gyu", KanaGroup.YOOUN));
		katakanaEntries.add(new KanaEntry("ギョ", "gyo", KanaGroup.YOOUN));
		
		katakanaEntries.add(new KanaEntry("ジャ", "ja", KanaGroup.YOOUN));
		katakanaEntries.add(new KanaEntry("ジュ", "ju", KanaGroup.YOOUN));
		katakanaEntries.add(new KanaEntry("ジョ", "jo", KanaGroup.YOOUN));
		
		katakanaEntries.add(new KanaEntry("ビャ", "bya", KanaGroup.YOOUN));
		katakanaEntries.add(new KanaEntry("ビュ", "byu", KanaGroup.YOOUN));
		katakanaEntries.add(new KanaEntry("ビョ", "byo", KanaGroup.YOOUN));
		
		katakanaEntries.add(new KanaEntry("ピャ", "pya", KanaGroup.YOOUN));
		katakanaEntries.add(new KanaEntry("ピュ", "pyu", KanaGroup.YOOUN));
		katakanaEntries.add(new KanaEntry("ピョ", "pyo", KanaGroup.YOOUN));	
		
		katakanaEntries.add(new KanaEntry("ウィ", "wi", KanaGroup.YOOUN));
		katakanaEntries.add(new KanaEntry("ウェ", "we", KanaGroup.YOOUN));
		
		katakanaEntries.add(new KanaEntry("シェ", "she", KanaGroup.YOOUN));
		katakanaEntries.add(new KanaEntry("ジェ", "je", KanaGroup.YOOUN));
		katakanaEntries.add(new KanaEntry("チェ", "che", KanaGroup.YOOUN));
		
		katakanaEntries.add(new KanaEntry("ファ", "fa", KanaGroup.YOOUN));
		katakanaEntries.add(new KanaEntry("フィ", "fi", KanaGroup.YOOUN));
		katakanaEntries.add(new KanaEntry("フェ", "fe", KanaGroup.YOOUN));
		katakanaEntries.add(new KanaEntry("フォ", "fo", KanaGroup.YOOUN));
		
		katakanaEntries.add(new KanaEntry("ティ", "ti", KanaGroup.YOOUN));
		katakanaEntries.add(new KanaEntry("ディ", "di", KanaGroup.YOOUN));
		katakanaEntries.add(new KanaEntry("ヂュ", "dyu", KanaGroup.YOOUN));
		
		katakanaEntries.add(new KanaEntry("ッ", "ttsu", KanaGroup.OTHER));
		katakanaEntries.add(new KanaEntry("ー", "ttsu2", KanaGroup.OTHER));
		
		return katakanaEntries;
	}
	
	public static Map<String, KanaEntry> getKanaCache() {
		
		List<KanaEntry> hiraganaEntries = getAllHiraganaKanaEntries();
		List<KanaEntry> allKatakanaKanaEntries = getAllKatakanaKanaEntries();
		
		Map<String, KanaEntry> kanaCache = new HashMap<String, KanaEntry>();

		for (KanaEntry kanaEntry : hiraganaEntries) {
			kanaCache.put(kanaEntry.getKanaJapanese(), kanaEntry);
		}

		for (KanaEntry kanaEntry : allKatakanaKanaEntries) {
			kanaCache.put(kanaEntry.getKanaJapanese(), kanaEntry);
		}
		
		return kanaCache;
	}
	
	public static KanaWord convertRomajiIntoHiraganaWord(Map<String, KanaEntry> hiraganaCache, String word) throws DictionaryException {
		
		word = word.toLowerCase();
		
		List<KanaEntry> kanaEntries = new ArrayList<KanaEntry>();
		
		String remaingRestChars = "";
		
		String currentRestChars = "";
		
		for (int idx = 0; idx < word.length(); ++idx) {
			String currentChar = String.valueOf(word.charAt(idx));
			
			if (currentChar.equals(" ") == true) {
				continue;
			}
			
			currentRestChars += currentChar.toLowerCase();
			
			if (currentRestChars.length() == 2 && currentRestChars.charAt(0) == currentRestChars.charAt(1) &&
					currentRestChars.charAt(0) != 'n') {
				
				KanaEntry kanaEntry = hiraganaCache.get("ttsu");
				
				if (kanaEntry == null) {
					throw new DictionaryException("Can't find kanaEntry!");
				}
				
				kanaEntries.add(kanaEntry);
				
				currentRestChars = "" + currentRestChars.charAt(1);
				
				continue;				
			}
			
			if (currentRestChars.equals("a") == true ||
					currentRestChars.equals("i") == true ||
					currentRestChars.equals("u") == true ||
					currentRestChars.equals("e") == true ||
					currentRestChars.equals("o") == true) {
				
				KanaEntry kanaEntry = hiraganaCache.get(currentRestChars);
				
				if (kanaEntry == null) {
					throw new DictionaryException("Can't find kanaEntry!");
				}
				
				kanaEntries.add(kanaEntry);
				
				currentRestChars = "";
			} else if (currentRestChars.equals("ka") == true ||
					currentRestChars.equals("ki") == true ||
					currentRestChars.equals("ku") == true ||
					currentRestChars.equals("ke") == true ||
					currentRestChars.equals("ko") == true ||
					currentRestChars.equals("kya") == true ||
					currentRestChars.equals("kyu") == true ||
					currentRestChars.equals("kyo") == true) {
				
				KanaEntry kanaEntry = hiraganaCache.get(currentRestChars);
				
				if (kanaEntry == null) {
					throw new DictionaryException("Can't find kanaEntry!");
				}
				
				kanaEntries.add(kanaEntry);
				
				currentRestChars = "";					
			} else if (currentRestChars.equals("sa") == true ||
					currentRestChars.equals("shi") == true ||
					currentRestChars.equals("sha") == true ||
					currentRestChars.equals("shu") == true ||
					currentRestChars.equals("sho") == true ||
					currentRestChars.equals("su") == true ||
					currentRestChars.equals("se") == true ||
					currentRestChars.equals("so") == true) {
				
				KanaEntry kanaEntry = hiraganaCache.get(currentRestChars);
				
				if (kanaEntry == null) {
					throw new DictionaryException("Can't find kanaEntry!");
				}
				
				kanaEntries.add(kanaEntry);
				
				currentRestChars = "";					
			} else if (currentRestChars.equals("ta") == true ||
					currentRestChars.equals("tsu") == true ||
					currentRestChars.equals("te") == true ||
					currentRestChars.equals("to") == true) {
				
				KanaEntry kanaEntry = hiraganaCache.get(currentRestChars);
				
				if (kanaEntry == null) {
					throw new DictionaryException("Can't find kanaEntry!");
				}
				
				kanaEntries.add(kanaEntry);
				
				currentRestChars = "";					
			} else if (currentRestChars.equals("chi") == true ||
					currentRestChars.equals("cha") == true ||
					currentRestChars.equals("chu") == true ||
					currentRestChars.equals("cho") == true) {
				
				KanaEntry kanaEntry = hiraganaCache.get(currentRestChars);
				
				if (kanaEntry == null) {
					throw new DictionaryException("Can't find kanaEntry!");
				}
				
				kanaEntries.add(kanaEntry);
				
				currentRestChars = "";					
			} else if (currentRestChars.startsWith("n") == true || currentRestChars.equals("n'") == true) {
				
				boolean nProcessed = false;
				
				if (currentRestChars.equals("n'") == true) {
					KanaEntry kanaEntry = hiraganaCache.get("n");
					
					if (kanaEntry == null) {
						throw new DictionaryException("Can't find kanaEntry!");
					}
					
					kanaEntries.add(kanaEntry);
					
					currentRestChars = "";
					
					nProcessed = true;					
				}
				
				if (nProcessed == false && (currentRestChars.equals("na") == true ||
						currentRestChars.equals("ni") == true ||
						currentRestChars.equals("nu") == true ||
						currentRestChars.equals("ne") == true ||
						currentRestChars.equals("no") == true ||
						currentRestChars.equals("nya") == true ||
						currentRestChars.equals("nyu") == true ||
						currentRestChars.equals("nyo") == true)) {

					KanaEntry kanaEntry = hiraganaCache.get(currentRestChars);
					
					if (kanaEntry == null) {
						throw new DictionaryException("Can't find kanaEntry!");
					}
					
					kanaEntries.add(kanaEntry);
					
					currentRestChars = "";
					
					nProcessed = true;
				} else if (nProcessed == false && currentRestChars.length() > 1) {
					
					if (currentRestChars.startsWith("ny") == false) {
						KanaEntry kanaEntry = hiraganaCache.get("n");
						
						if (kanaEntry == null) {
							throw new DictionaryException("Can't find kanaEntry!");
						}
						
						kanaEntries.add(kanaEntry);
						
						currentRestChars = currentRestChars.substring(1);
						
						nProcessed = true;
					}						
				}
				
				if (nProcessed == false && currentRestChars.length() == 1 && idx == word.length() - 1) {
					KanaEntry kanaEntry = hiraganaCache.get("n");
					
					if (kanaEntry == null) {
						throw new DictionaryException("Can't find kanaEntry!");
					}
					
					kanaEntries.add(kanaEntry);
					
					currentRestChars = "";
					
					nProcessed = true;
				}
			} else if (currentRestChars.equals("ha") == true ||
					currentRestChars.equals("hi") == true ||
					currentRestChars.equals("he") == true ||
					currentRestChars.equals("ho") == true ||
					currentRestChars.equals("hya") == true ||
					currentRestChars.equals("hyu") == true ||
					currentRestChars.equals("hyo") == true) {
				
				KanaEntry kanaEntry = hiraganaCache.get(currentRestChars);
				
				if (kanaEntry == null) {
					throw new DictionaryException("Can't find kanaEntry!");
				}
				
				kanaEntries.add(kanaEntry);
				
				currentRestChars = "";					
			} else if (currentRestChars.equals("fu") == true) {
				
				KanaEntry kanaEntry = hiraganaCache.get(currentRestChars);
				
				if (kanaEntry == null) {
					throw new DictionaryException("Can't find kanaEntry!");
				}
				
				kanaEntries.add(kanaEntry);
				
				currentRestChars = "";					
			} else if (currentRestChars.equals("ma") == true ||
					currentRestChars.equals("mi") == true ||
					currentRestChars.equals("mu") == true ||
					currentRestChars.equals("me") == true ||
					currentRestChars.equals("mo") == true ||
					currentRestChars.equals("mya") == true ||
					currentRestChars.equals("myu") == true ||
					currentRestChars.equals("myo") == true) {
				
				KanaEntry kanaEntry = hiraganaCache.get(currentRestChars);
				
				if (kanaEntry == null) {
					throw new DictionaryException("Can't find kanaEntry!");
				}
				
				kanaEntries.add(kanaEntry);
				
				currentRestChars = "";					
			} else if (currentRestChars.equals("ya") == true ||
					currentRestChars.equals("yu") == true ||
					currentRestChars.equals("yo") == true) {
				
				KanaEntry kanaEntry = hiraganaCache.get(currentRestChars);
				
				if (kanaEntry == null) {
					throw new DictionaryException("Can't find kanaEntry!");
				}
				
				kanaEntries.add(kanaEntry);
				
				currentRestChars = "";					
			} else if (currentRestChars.equals("ra") == true ||
					currentRestChars.equals("ri") == true ||
					currentRestChars.equals("ru") == true ||
					currentRestChars.equals("re") == true ||
					currentRestChars.equals("ro") == true ||
					currentRestChars.equals("rya") == true ||
					currentRestChars.equals("ryu") == true ||
					currentRestChars.equals("ryo") == true) {
				
				KanaEntry kanaEntry = hiraganaCache.get(currentRestChars);
				
				if (kanaEntry == null) {
					throw new DictionaryException("Can't find kanaEntry!");
				}
				
				kanaEntries.add(kanaEntry);
				
				currentRestChars = "";					
			} else if (currentRestChars.equals("wa") == true ||
					currentRestChars.equals("wo") == true) {
				
				KanaEntry kanaEntry = hiraganaCache.get(currentRestChars);
				
				if (kanaEntry == null) {
					throw new DictionaryException("Can't find kanaEntry!");
				}
				
				kanaEntries.add(kanaEntry);
				
				currentRestChars = "";					
			} else if (currentRestChars.equals("ga") == true ||
					currentRestChars.equals("gi") == true ||
					currentRestChars.equals("gu") == true ||
					currentRestChars.equals("ge") == true ||
					currentRestChars.equals("go") == true ||
					currentRestChars.equals("gya") == true ||
					currentRestChars.equals("gyu") == true ||
					currentRestChars.equals("gyo") == true) {
				
				KanaEntry kanaEntry = hiraganaCache.get(currentRestChars);
				
				if (kanaEntry == null) {
					throw new DictionaryException("Can't find kanaEntry!");
				}
				
				kanaEntries.add(kanaEntry);
				
				currentRestChars = "";					
			} else if (currentRestChars.equals("za") == true ||
					currentRestChars.equals("zu") == true ||
					currentRestChars.equals("ze") == true ||
					currentRestChars.equals("zo") == true) {
				
				KanaEntry kanaEntry = hiraganaCache.get(currentRestChars);
				
				if (kanaEntry == null) {
					throw new DictionaryException("Can't find kanaEntry!");
				}
				
				kanaEntries.add(kanaEntry);
				
				currentRestChars = "";					
			} else if (currentRestChars.equals("ji") == true ||
					currentRestChars.equals("ja") == true ||
					currentRestChars.equals("ju") == true ||
					currentRestChars.equals("jo") == true) {
				
				KanaEntry kanaEntry = hiraganaCache.get(currentRestChars);
				
				if (kanaEntry == null) {
					throw new DictionaryException("Can't find kanaEntry!");
				}
				
				kanaEntries.add(kanaEntry);
				
				currentRestChars = "";					
			} else if (currentRestChars.equals("da") == true ||
					currentRestChars.equals("di") == true ||
					currentRestChars.equals("du") == true ||
					currentRestChars.equals("de") == true ||
					currentRestChars.equals("do") == true) {
				
				KanaEntry kanaEntry = hiraganaCache.get(currentRestChars);
				
				if (kanaEntry == null) {
					throw new DictionaryException("Can't find kanaEntry!");
				}
				
				kanaEntries.add(kanaEntry);
				
				currentRestChars = "";					
			} else if (currentRestChars.equals("ba") == true ||
					currentRestChars.equals("bi") == true ||
					currentRestChars.equals("bu") == true ||
					currentRestChars.equals("be") == true ||
					currentRestChars.equals("bo") == true ||
					currentRestChars.equals("bya") == true ||
					currentRestChars.equals("byu") == true ||
					currentRestChars.equals("byo") == true) {
				
				KanaEntry kanaEntry = hiraganaCache.get(currentRestChars);
				
				if (kanaEntry == null) {
					throw new DictionaryException("Can't find kanaEntry!");
				}
				
				kanaEntries.add(kanaEntry);
				
				currentRestChars = "";					
			} else if (currentRestChars.equals("pa") == true ||
					currentRestChars.equals("pi") == true ||
					currentRestChars.equals("pu") == true ||
					currentRestChars.equals("pe") == true ||
					currentRestChars.equals("po") == true ||
					currentRestChars.equals("pya") == true ||
					currentRestChars.equals("pyu") == true ||
					currentRestChars.equals("pyo") == true) {
				
				KanaEntry kanaEntry = hiraganaCache.get(currentRestChars);
				
				if (kanaEntry == null) {
					throw new DictionaryException("Can't find kanaEntry!");
				}
				
				kanaEntries.add(kanaEntry);
				
				currentRestChars = "";					
			}
			
			remaingRestChars = currentRestChars;
		}
		
		KanaWord result = new KanaWord();
		
		result.kanaEntries = kanaEntries;
		result.remaingRestChars = remaingRestChars;
		
		return result;	
	}

	public static KanaWord convertRomajiIntoKatakanaWord(Map<String, KanaEntry> kitakanaCache, String word) throws DictionaryException {
		
		word = word.toLowerCase();
		
		List<KanaEntry> kanaEntries = new ArrayList<KanaEntry>();
		
		String remaingRestChars = "";
		
		String currentRestChars = "";
		
		for (int idx = 0; idx < word.length(); ++idx) {
			String currentChar = String.valueOf(word.charAt(idx));
			
			if (currentChar.equals(" ") == true) {
				continue;
			}
			
			currentRestChars += currentChar.toLowerCase();
			
			if (idx > 0) {
				char previousChar = word.charAt(idx - 1);
				char currentCharChar = word.charAt(idx);
				
				if (previousChar == currentCharChar && isVowel(previousChar) == true && isVowel(currentCharChar) == true) {
					KanaEntry kanaEntry = kitakanaCache.get("ttsu2");
					
					if (kanaEntry == null) {
						throw new DictionaryException("Can't find kanaEntry!");
					}
					
					kanaEntries.add(kanaEntry);

					currentRestChars = "";
					
					continue;
				}
			}
			
			if (currentRestChars.length() == 2 && currentRestChars.charAt(0) == currentRestChars.charAt(1) &&
					currentRestChars.charAt(0) != 'n') {
				
				KanaEntry kanaEntry = kitakanaCache.get("ttsu");
				
				if (kanaEntry == null) {
					throw new DictionaryException("Can't find kanaEntry!");
				}
				
				kanaEntries.add(kanaEntry);
				
				currentRestChars = "" + currentRestChars.charAt(1);
				
				continue;				
			}
			
			if (currentRestChars.equals("a") == true ||
					currentRestChars.equals("i") == true ||
					currentRestChars.equals("u") == true ||
					currentRestChars.equals("e") == true ||
					currentRestChars.equals("o") == true) {
				
				KanaEntry kanaEntry = kitakanaCache.get(currentRestChars);
				
				if (kanaEntry == null) {
					throw new DictionaryException("Can't find kanaEntry!");
				}
				
				kanaEntries.add(kanaEntry);
				
				currentRestChars = "";
			} else if (currentRestChars.equals("ka") == true ||
					currentRestChars.equals("ki") == true ||
					currentRestChars.equals("ku") == true ||
					currentRestChars.equals("ke") == true ||
					currentRestChars.equals("ko") == true ||
					currentRestChars.equals("kya") == true ||
					currentRestChars.equals("kyu") == true ||
					currentRestChars.equals("kyo") == true) {
				
				KanaEntry kanaEntry = kitakanaCache.get(currentRestChars);
				
				if (kanaEntry == null) {
					throw new DictionaryException("Can't find kanaEntry!");
				}
				
				kanaEntries.add(kanaEntry);
				
				currentRestChars = "";					
			} else if (currentRestChars.equals("sa") == true ||
					currentRestChars.equals("shi") == true ||
					currentRestChars.equals("sha") == true ||
					currentRestChars.equals("shu") == true ||
					currentRestChars.equals("sho") == true ||
					currentRestChars.equals("she") == true ||
					currentRestChars.equals("su") == true ||
					currentRestChars.equals("se") == true ||
					currentRestChars.equals("so") == true) {
				
				KanaEntry kanaEntry = kitakanaCache.get(currentRestChars);
				
				if (kanaEntry == null) {
					throw new DictionaryException("Can't find kanaEntry!");
				}
				
				kanaEntries.add(kanaEntry);
				
				currentRestChars = "";					
			} else if (currentRestChars.equals("ta") == true ||
					currentRestChars.equals("tsu") == true ||
					currentRestChars.equals("te") == true ||
					currentRestChars.equals("to") == true ||
					currentRestChars.equals("ti") == true) {
				
				KanaEntry kanaEntry = kitakanaCache.get(currentRestChars);
				
				if (kanaEntry == null) {
					throw new DictionaryException("Can't find kanaEntry!");
				}
				
				kanaEntries.add(kanaEntry);
				
				currentRestChars = "";					
			} else if (currentRestChars.equals("chi") == true ||
					currentRestChars.equals("cha") == true ||
					currentRestChars.equals("chu") == true ||
					currentRestChars.equals("cho") == true ||
					currentRestChars.equals("che") == true) {
				
				KanaEntry kanaEntry = kitakanaCache.get(currentRestChars);
				
				if (kanaEntry == null) {
					throw new DictionaryException("Can't find kanaEntry!");
				}
				
				kanaEntries.add(kanaEntry);
				
				currentRestChars = "";					
			} else if (currentRestChars.startsWith("n") == true || currentRestChars.equals("n'") == true) {
				
				boolean nProcessed = false;
				
				if (currentRestChars.equals("n'") == true) {
					KanaEntry kanaEntry = kitakanaCache.get("n");
					
					if (kanaEntry == null) {
						throw new DictionaryException("Can't find kanaEntry!");
					}
					
					kanaEntries.add(kanaEntry);
					
					currentRestChars = "";
					
					nProcessed = true;					
				}
				
				if (nProcessed == false && (currentRestChars.equals("na") == true ||
						currentRestChars.equals("ni") == true ||
						currentRestChars.equals("nu") == true ||
						currentRestChars.equals("ne") == true ||
						currentRestChars.equals("no") == true ||
						currentRestChars.equals("nya") == true ||
						currentRestChars.equals("nyu") == true ||
						currentRestChars.equals("nyo") == true)) {

					KanaEntry kanaEntry = kitakanaCache.get(currentRestChars);
					
					if (kanaEntry == null) {
						throw new DictionaryException("Can't find kanaEntry!");
					}
					
					kanaEntries.add(kanaEntry);
					
					currentRestChars = "";
					
					nProcessed = true;
				} else if (nProcessed == false && currentRestChars.length() > 1) {
					
					if (currentRestChars.startsWith("ny") == false) {
						KanaEntry kanaEntry = kitakanaCache.get("n");
						
						if (kanaEntry == null) {
							throw new DictionaryException("Can't find kanaEntry!");
						}
						
						kanaEntries.add(kanaEntry);
						
						currentRestChars = currentRestChars.substring(1);
						
						nProcessed = true;
					}						
				}
				
				if (nProcessed == false && currentRestChars.length() == 1 && idx == word.length() - 1) {
					KanaEntry kanaEntry = kitakanaCache.get("n");
					
					if (kanaEntry == null) {
						throw new DictionaryException("Can't find kanaEntry!");
					}
					
					kanaEntries.add(kanaEntry);
					
					currentRestChars = "";
					
					nProcessed = true;
				}
			} else if (currentRestChars.equals("ha") == true ||
					currentRestChars.equals("hi") == true ||
					currentRestChars.equals("he") == true ||
					currentRestChars.equals("ho") == true ||
					currentRestChars.equals("hya") == true ||
					currentRestChars.equals("hyu") == true ||
					currentRestChars.equals("hyo") == true) {
				
				KanaEntry kanaEntry = kitakanaCache.get(currentRestChars);
				
				if (kanaEntry == null) {
					throw new DictionaryException("Can't find kanaEntry!");
				}
				
				kanaEntries.add(kanaEntry);
				
				currentRestChars = "";					
			} else if (currentRestChars.equals("fu") == true || 
					currentRestChars.equals("fa") == true ||
					currentRestChars.equals("fi") == true ||
					currentRestChars.equals("fe") == true ||
					currentRestChars.equals("fo") == true) {
				
				KanaEntry kanaEntry = kitakanaCache.get(currentRestChars);
				
				if (kanaEntry == null) {
					throw new DictionaryException("Can't find kanaEntry!");
				}
				
				kanaEntries.add(kanaEntry);
				
				currentRestChars = "";					
			} else if (currentRestChars.equals("ma") == true ||
					currentRestChars.equals("mi") == true ||
					currentRestChars.equals("mu") == true ||
					currentRestChars.equals("me") == true ||
					currentRestChars.equals("mo") == true ||
					currentRestChars.equals("mya") == true ||
					currentRestChars.equals("myu") == true ||
					currentRestChars.equals("myo") == true) {
				
				KanaEntry kanaEntry = kitakanaCache.get(currentRestChars);
				
				if (kanaEntry == null) {
					throw new DictionaryException("Can't find kanaEntry!");
				}
				
				kanaEntries.add(kanaEntry);
				
				currentRestChars = "";					
			} else if (currentRestChars.equals("ya") == true ||
					currentRestChars.equals("yu") == true ||
					currentRestChars.equals("yo") == true) {
				
				KanaEntry kanaEntry = kitakanaCache.get(currentRestChars);
				
				if (kanaEntry == null) {
					throw new DictionaryException("Can't find kanaEntry!");
				}
				
				kanaEntries.add(kanaEntry);
				
				currentRestChars = "";					
			} else if (currentRestChars.equals("ra") == true ||
					currentRestChars.equals("ri") == true ||
					currentRestChars.equals("ru") == true ||
					currentRestChars.equals("re") == true ||
					currentRestChars.equals("ro") == true ||
					currentRestChars.equals("rya") == true ||
					currentRestChars.equals("ryu") == true ||
					currentRestChars.equals("ryo") == true) {
				
				KanaEntry kanaEntry = kitakanaCache.get(currentRestChars);
				
				if (kanaEntry == null) {
					throw new DictionaryException("Can't find kanaEntry!");
				}
				
				kanaEntries.add(kanaEntry);
				
				currentRestChars = "";					
			} else if (currentRestChars.equals("wa") == true ||
					currentRestChars.equals("wo") == true || 
					currentRestChars.equals("wi") == true ||
					currentRestChars.equals("we") == true) {
				
				KanaEntry kanaEntry = kitakanaCache.get(currentRestChars);
				
				if (kanaEntry == null) {
					throw new DictionaryException("Can't find kanaEntry!");
				}
				
				kanaEntries.add(kanaEntry);
				
				currentRestChars = "";					
			} else if (currentRestChars.equals("ga") == true ||
					currentRestChars.equals("gi") == true ||
					currentRestChars.equals("gu") == true ||
					currentRestChars.equals("ge") == true ||
					currentRestChars.equals("go") == true ||
					currentRestChars.equals("gya") == true ||
					currentRestChars.equals("gyu") == true ||
					currentRestChars.equals("gyo") == true) {
				
				KanaEntry kanaEntry = kitakanaCache.get(currentRestChars);
				
				if (kanaEntry == null) {
					throw new DictionaryException("Can't find kanaEntry!");
				}
				
				kanaEntries.add(kanaEntry);
				
				currentRestChars = "";					
			} else if (currentRestChars.equals("za") == true ||
					currentRestChars.equals("zu") == true ||
					currentRestChars.equals("ze") == true ||
					currentRestChars.equals("zo") == true) {
				
				KanaEntry kanaEntry = kitakanaCache.get(currentRestChars);
				
				if (kanaEntry == null) {
					throw new DictionaryException("Can't find kanaEntry!");
				}
				
				kanaEntries.add(kanaEntry);
				
				currentRestChars = "";					
			} else if (currentRestChars.equals("ji") == true ||
					currentRestChars.equals("ja") == true ||
					currentRestChars.equals("ju") == true ||
					currentRestChars.equals("jo") == true ||
					currentRestChars.equals("je") == true) {
				
				KanaEntry kanaEntry = kitakanaCache.get(currentRestChars);
				
				if (kanaEntry == null) {
					throw new DictionaryException("Can't find kanaEntry!");
				}
				
				kanaEntries.add(kanaEntry);
				
				currentRestChars = "";					
			} else if (currentRestChars.equals("da") == true ||
					currentRestChars.equals("di") == true ||
					currentRestChars.equals("du") == true ||
					currentRestChars.equals("de") == true ||
					currentRestChars.equals("do") == true ||
					currentRestChars.equals("di") == true ||
					currentRestChars.equals("dyu") == true) {
				
				KanaEntry kanaEntry = kitakanaCache.get(currentRestChars);
				
				if (kanaEntry == null) {
					throw new DictionaryException("Can't find kanaEntry!");
				}
				
				kanaEntries.add(kanaEntry);
				
				currentRestChars = "";					
			} else if (currentRestChars.equals("ba") == true ||
					currentRestChars.equals("bi") == true ||
					currentRestChars.equals("bu") == true ||
					currentRestChars.equals("be") == true ||
					currentRestChars.equals("bo") == true ||
					currentRestChars.equals("bya") == true ||
					currentRestChars.equals("byu") == true ||
					currentRestChars.equals("byo") == true) {
				
				KanaEntry kanaEntry = kitakanaCache.get(currentRestChars);
				
				if (kanaEntry == null) {
					throw new DictionaryException("Can't find kanaEntry!");
				}
				
				kanaEntries.add(kanaEntry);
				
				currentRestChars = "";					
			} else if (currentRestChars.equals("pa") == true ||
					currentRestChars.equals("pi") == true ||
					currentRestChars.equals("pu") == true ||
					currentRestChars.equals("pe") == true ||
					currentRestChars.equals("po") == true ||
					currentRestChars.equals("pya") == true ||
					currentRestChars.equals("pyu") == true ||
					currentRestChars.equals("pyo") == true) {
				
				KanaEntry kanaEntry = kitakanaCache.get(currentRestChars);
				
				if (kanaEntry == null) {
					throw new DictionaryException("Can't find kanaEntry!");
				}
				
				kanaEntries.add(kanaEntry);
				
				currentRestChars = "";					
			}
			
			remaingRestChars = currentRestChars;
		}
		
		KanaWord result = new KanaWord();
		
		result.kanaEntries = kanaEntries;
		result.remaingRestChars = remaingRestChars;
		
		return result;		
	}
	
	public static class KanaWord {
		public List<KanaEntry> kanaEntries;
		
		public String remaingRestChars;
	}

	private static boolean isVowel(char char_) {
		if (char_ == 'e' || char_ == 'u' || char_ == 'i' || char_ == 'o' || char_ == 'a') {
			return true;
		} else {
			return false;
		}
	}
	
	public static KanaWord convertKanaStringIntoKanaWord(String kana, Map<String, KanaEntry> kanaCache) {

		List<KanaEntry> kanaResultEntries = new ArrayList<KanaEntry>();

		int pos = 0;

		while(true) {
			if (pos >= kana.length()) {
				break;
			}

			String nextDoubleKanaPart = getNextDoubleKanaPart(kana, pos);

			if (nextDoubleKanaPart != null) {
				KanaEntry nextDoubleKanaPartKanaEntry = kanaCache.get(nextDoubleKanaPart);

				if (nextDoubleKanaPartKanaEntry != null) {
					kanaResultEntries.add(nextDoubleKanaPartKanaEntry);

					pos += 2;

					continue;
				}
			}

			String nextSingleKanaPart = getNextSingleKanaPart(kana, pos);

			if (nextSingleKanaPart != null) {
				KanaEntry nextSingleKanaPartKanaEntry = kanaCache.get(nextSingleKanaPart);

				if (nextSingleKanaPartKanaEntry != null) {
					kanaResultEntries.add(nextSingleKanaPartKanaEntry);

					pos += 1;

					continue;
				} else {
					throw new RuntimeException(kana);
				}
			}
		}

		KanaWord result = new KanaWord();

		result.kanaEntries = kanaResultEntries;

		return result;
	}

	private static String getNextDoubleKanaPart(String kana, int startPos) {
		if (kana.length() < startPos + 2) {
			return null;
		}

		return kana.substring(startPos, startPos + 2);
	}

	private static String getNextSingleKanaPart(String kana, int startPos) {
		if (kana.length() < startPos) {
			return null;
		}

		return kana.substring(startPos, startPos + 1);
	}	
}
