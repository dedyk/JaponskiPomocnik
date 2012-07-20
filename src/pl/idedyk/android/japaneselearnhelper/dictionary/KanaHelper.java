package pl.idedyk.android.japaneselearnhelper.dictionary;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import pl.idedyk.android.japaneselearnhelper.dictionary.dto.KanaEntry;
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
		
		hiraganaEntries.add(new KanaEntry("あ", "a"));
		hiraganaEntries.add(new KanaEntry("い", "i"));
		hiraganaEntries.add(new KanaEntry("う", "u"));
		hiraganaEntries.add(new KanaEntry("え", "e"));
		hiraganaEntries.add(new KanaEntry("お", "o"));
		
		hiraganaEntries.add(new KanaEntry("か", "ka"));
		hiraganaEntries.add(new KanaEntry("き", "ki"));
		hiraganaEntries.add(new KanaEntry("く", "ku"));
		hiraganaEntries.add(new KanaEntry("け", "ke"));
		hiraganaEntries.add(new KanaEntry("こ", "ko"));
		
		hiraganaEntries.add(new KanaEntry("さ", "sa"));
		hiraganaEntries.add(new KanaEntry("し", "shi"));
		hiraganaEntries.add(new KanaEntry("す", "su"));
		hiraganaEntries.add(new KanaEntry("せ", "se"));
		hiraganaEntries.add(new KanaEntry("そ", "so"));
		
		hiraganaEntries.add(new KanaEntry("た", "ta"));
		hiraganaEntries.add(new KanaEntry("ち", "chi"));
		hiraganaEntries.add(new KanaEntry("つ", "tsu"));
		hiraganaEntries.add(new KanaEntry("て", "te"));
		hiraganaEntries.add(new KanaEntry("と", "to"));
		
		hiraganaEntries.add(new KanaEntry("な", "na"));
		hiraganaEntries.add(new KanaEntry("に", "ni"));
		hiraganaEntries.add(new KanaEntry("ぬ", "nu"));
		hiraganaEntries.add(new KanaEntry("ね", "ne"));
		hiraganaEntries.add(new KanaEntry("の", "no"));
		
		hiraganaEntries.add(new KanaEntry("は", "ha"));
		hiraganaEntries.add(new KanaEntry("ひ", "hi"));
		hiraganaEntries.add(new KanaEntry("ふ", "fu"));
		hiraganaEntries.add(new KanaEntry("へ", "he"));
		hiraganaEntries.add(new KanaEntry("ほ", "ho"));
		
		hiraganaEntries.add(new KanaEntry("ま", "ma"));
		hiraganaEntries.add(new KanaEntry("み", "mi"));
		hiraganaEntries.add(new KanaEntry("む", "mu"));
		hiraganaEntries.add(new KanaEntry("め", "me"));
		hiraganaEntries.add(new KanaEntry("も", "mo"));
		
		hiraganaEntries.add(new KanaEntry("や", "ya"));
		hiraganaEntries.add(new KanaEntry("ゆ", "yu"));
		hiraganaEntries.add(new KanaEntry("よ", "yo"));
		
		hiraganaEntries.add(new KanaEntry("ら", "ra"));
		hiraganaEntries.add(new KanaEntry("り", "ri"));
		hiraganaEntries.add(new KanaEntry("る", "ru"));
		hiraganaEntries.add(new KanaEntry("れ", "re"));
		hiraganaEntries.add(new KanaEntry("ろ", "ro"));
		
		hiraganaEntries.add(new KanaEntry("わ", "wa"));
		hiraganaEntries.add(new KanaEntry("を", "wo"));
		
		hiraganaEntries.add(new KanaEntry("ん", "n"));
		
		hiraganaEntries.add(new KanaEntry("が", "ga"));
		hiraganaEntries.add(new KanaEntry("ぎ", "gi"));
		hiraganaEntries.add(new KanaEntry("ぐ", "gu"));
		hiraganaEntries.add(new KanaEntry("げ", "ge"));
		hiraganaEntries.add(new KanaEntry("ご", "go"));
		
		hiraganaEntries.add(new KanaEntry("ざ", "za"));
		hiraganaEntries.add(new KanaEntry("じ", "ji"));
		hiraganaEntries.add(new KanaEntry("ず", "zu"));
		hiraganaEntries.add(new KanaEntry("ぜ", "ze"));
		hiraganaEntries.add(new KanaEntry("ぞ", "zo"));
		
		hiraganaEntries.add(new KanaEntry("だ", "da"));
		hiraganaEntries.add(new KanaEntry("ぢ", "di"));
		hiraganaEntries.add(new KanaEntry("づ", "du"));
		hiraganaEntries.add(new KanaEntry("で", "de"));
		hiraganaEntries.add(new KanaEntry("ど", "do"));
		
		hiraganaEntries.add(new KanaEntry("ば", "ba"));
		hiraganaEntries.add(new KanaEntry("び", "bi"));
		hiraganaEntries.add(new KanaEntry("ぶ", "bu"));
		hiraganaEntries.add(new KanaEntry("べ", "be"));
		hiraganaEntries.add(new KanaEntry("ぼ", "bo"));
		
		hiraganaEntries.add(new KanaEntry("ぱ", "pa"));
		hiraganaEntries.add(new KanaEntry("ぴ", "pi"));
		hiraganaEntries.add(new KanaEntry("ぷ", "pu"));
		hiraganaEntries.add(new KanaEntry("ぺ", "pe"));
		hiraganaEntries.add(new KanaEntry("ぽ", "po"));
		
		hiraganaEntries.add(new KanaEntry("きゃ", "kya"));
		hiraganaEntries.add(new KanaEntry("きゅ", "kyu"));
		hiraganaEntries.add(new KanaEntry("きょ", "kyo"));
		
		hiraganaEntries.add(new KanaEntry("しゃ", "sha"));
		hiraganaEntries.add(new KanaEntry("しゅ", "shu"));		
		hiraganaEntries.add(new KanaEntry("しょ", "sho"));
		
		hiraganaEntries.add(new KanaEntry("ちゃ", "cha"));
		hiraganaEntries.add(new KanaEntry("ちゅ", "chu"));
		hiraganaEntries.add(new KanaEntry("ちょ", "cho"));
		
		hiraganaEntries.add(new KanaEntry("にゃ", "nya"));
		hiraganaEntries.add(new KanaEntry("にゅ", "nyu"));
		hiraganaEntries.add(new KanaEntry("にょ", "nyo"));
		
		hiraganaEntries.add(new KanaEntry("ひゃ", "hya"));
		hiraganaEntries.add(new KanaEntry("ひゅ", "hyu"));
		hiraganaEntries.add(new KanaEntry("ひょ", "hyo"));
		
		hiraganaEntries.add(new KanaEntry("みゃ", "mya"));
		hiraganaEntries.add(new KanaEntry("みゅ", "myu"));
		hiraganaEntries.add(new KanaEntry("みょ", "myo"));
		
		hiraganaEntries.add(new KanaEntry("りゃ", "rya"));
		hiraganaEntries.add(new KanaEntry("りゅ", "ryu"));
		hiraganaEntries.add(new KanaEntry("りょ", "ryo"));
		
		hiraganaEntries.add(new KanaEntry("ぎゃ", "gya"));
		hiraganaEntries.add(new KanaEntry("ぎゅ", "gyu"));
		hiraganaEntries.add(new KanaEntry("ぎょ", "gyo"));
		
		hiraganaEntries.add(new KanaEntry("じゃ", "ja"));		
		hiraganaEntries.add(new KanaEntry("じゅ", "ju"));
		hiraganaEntries.add(new KanaEntry("じょ", "jo"));
		
		hiraganaEntries.add(new KanaEntry("びゃ", "bya"));
		hiraganaEntries.add(new KanaEntry("びゅ", "byu"));
		hiraganaEntries.add(new KanaEntry("びょ", "byo"));
		
		hiraganaEntries.add(new KanaEntry("ぴゃ", "pya"));
		hiraganaEntries.add(new KanaEntry("ぴゅ", "pyu"));
		hiraganaEntries.add(new KanaEntry("ぴょ", "pyo"));	
		
		hiraganaEntries.add(new KanaEntry("っ", "ttsu"));
		
		return hiraganaEntries;		
	}
	
	public static List<KanaEntry> getAllKatakanaKanaEntries() {
		
		List<KanaEntry> katakanaEntries = new ArrayList<KanaEntry>();
		
		katakanaEntries.add(new KanaEntry("ア", "a"));
		katakanaEntries.add(new KanaEntry("イ", "i"));
		katakanaEntries.add(new KanaEntry("ウ", "u"));
		katakanaEntries.add(new KanaEntry("エ", "e"));
		katakanaEntries.add(new KanaEntry("オ", "o"));
		
		katakanaEntries.add(new KanaEntry("カ", "ka"));
		katakanaEntries.add(new KanaEntry("キ", "ki"));
		katakanaEntries.add(new KanaEntry("ク", "ku"));
		katakanaEntries.add(new KanaEntry("ケ", "ke"));
		katakanaEntries.add(new KanaEntry("コ", "ko"));
		
		katakanaEntries.add(new KanaEntry("サ", "sa"));
		katakanaEntries.add(new KanaEntry("シ", "shi"));
		katakanaEntries.add(new KanaEntry("ス", "su"));
		katakanaEntries.add(new KanaEntry("セ", "se"));
		katakanaEntries.add(new KanaEntry("ソ", "so"));
		
		katakanaEntries.add(new KanaEntry("タ", "ta"));
		katakanaEntries.add(new KanaEntry("チ", "chi"));
		katakanaEntries.add(new KanaEntry("ツ", "tsu"));
		katakanaEntries.add(new KanaEntry("テ", "te"));
		katakanaEntries.add(new KanaEntry("ト", "to"));
		
		katakanaEntries.add(new KanaEntry("ナ", "na"));
		katakanaEntries.add(new KanaEntry("ニ", "ni"));
		katakanaEntries.add(new KanaEntry("ヌ", "nu"));
		katakanaEntries.add(new KanaEntry("ネ", "ne"));
		katakanaEntries.add(new KanaEntry("ノ", "no"));
		
		katakanaEntries.add(new KanaEntry("ハ", "ha"));
		katakanaEntries.add(new KanaEntry("匕", "hi"));
		katakanaEntries.add(new KanaEntry("フ", "fu"));
		katakanaEntries.add(new KanaEntry("ヘ", "he"));
		katakanaEntries.add(new KanaEntry("ホ", "ho"));
		
		katakanaEntries.add(new KanaEntry("マ", "ma"));
		katakanaEntries.add(new KanaEntry("ミ", "mi"));
		katakanaEntries.add(new KanaEntry("厶", "mu"));
		katakanaEntries.add(new KanaEntry("メ", "me"));
		katakanaEntries.add(new KanaEntry("モ", "mo"));
		
		katakanaEntries.add(new KanaEntry("ヤ", "ya"));
		katakanaEntries.add(new KanaEntry("ユ", "yu"));
		katakanaEntries.add(new KanaEntry("ヨ", "yo"));
		
		katakanaEntries.add(new KanaEntry("ラ", "ra"));
		katakanaEntries.add(new KanaEntry("リ", "ri"));
		katakanaEntries.add(new KanaEntry("ル", "ru"));
		katakanaEntries.add(new KanaEntry("レ", "re"));
		katakanaEntries.add(new KanaEntry("ロ", "ro"));
		
		katakanaEntries.add(new KanaEntry("ワ", "wa"));
		katakanaEntries.add(new KanaEntry("ヲ", "wo"));
		
		katakanaEntries.add(new KanaEntry("ン", "n"));
		
		katakanaEntries.add(new KanaEntry("ガ", "ga"));
		katakanaEntries.add(new KanaEntry("ギ", "gi"));
		katakanaEntries.add(new KanaEntry("グ", "gu"));
		katakanaEntries.add(new KanaEntry("ゲ", "ge"));
		katakanaEntries.add(new KanaEntry("ゴ", "go"));
		
		katakanaEntries.add(new KanaEntry("ザ", "za"));
		katakanaEntries.add(new KanaEntry("ジ", "ji"));
		katakanaEntries.add(new KanaEntry("ズ", "zu"));
		katakanaEntries.add(new KanaEntry("ゼ", "ze"));
		katakanaEntries.add(new KanaEntry("ゾ", "zo"));
		
		katakanaEntries.add(new KanaEntry("ダ", "da"));
		katakanaEntries.add(new KanaEntry("ヂ", "di"));
		katakanaEntries.add(new KanaEntry("づ", "du"));
		katakanaEntries.add(new KanaEntry("デ", "de"));
		katakanaEntries.add(new KanaEntry("ド", "do"));
		
		katakanaEntries.add(new KanaEntry("バ", "ba"));
		katakanaEntries.add(new KanaEntry("ビ", "bi"));
		katakanaEntries.add(new KanaEntry("ブ", "bu"));
		katakanaEntries.add(new KanaEntry("ベ", "be"));
		katakanaEntries.add(new KanaEntry("ボ", "bo"));
		
		katakanaEntries.add(new KanaEntry("パ", "pa"));
		katakanaEntries.add(new KanaEntry("ピ", "pi"));
		katakanaEntries.add(new KanaEntry("プ", "pu"));
		katakanaEntries.add(new KanaEntry("ペ", "pe"));
		katakanaEntries.add(new KanaEntry("ポ", "po"));
		
		katakanaEntries.add(new KanaEntry("キャ", "kya"));
		katakanaEntries.add(new KanaEntry("キュ", "kyu"));
		katakanaEntries.add(new KanaEntry("キョ", "kyo"));
		
		katakanaEntries.add(new KanaEntry("シャ", "sha"));
		katakanaEntries.add(new KanaEntry("シュ", "shu"));		
		katakanaEntries.add(new KanaEntry("ショ", "sho"));
		
		katakanaEntries.add(new KanaEntry("チャ", "cha"));
		katakanaEntries.add(new KanaEntry("チュ", "chu"));
		katakanaEntries.add(new KanaEntry("チョ", "cho"));
		
		katakanaEntries.add(new KanaEntry("ニャ", "nya"));
		katakanaEntries.add(new KanaEntry("ニュ", "nyu"));
		katakanaEntries.add(new KanaEntry("ニョ", "nyo"));
		
		katakanaEntries.add(new KanaEntry("ヒャ", "hya"));
		katakanaEntries.add(new KanaEntry("ヒュ", "hyu"));
		katakanaEntries.add(new KanaEntry("ヒョ", "hyo"));
		
		katakanaEntries.add(new KanaEntry("ミャ", "mya"));
		katakanaEntries.add(new KanaEntry("ミュ", "myu"));
		katakanaEntries.add(new KanaEntry("ミョ", "myo"));
		
		katakanaEntries.add(new KanaEntry("リャ", "rya"));
		katakanaEntries.add(new KanaEntry("リュ", "ryu"));
		katakanaEntries.add(new KanaEntry("リョ", "ryo"));
		
		katakanaEntries.add(new KanaEntry("ギャ", "gya"));
		katakanaEntries.add(new KanaEntry("ギュ", "gyu"));
		katakanaEntries.add(new KanaEntry("ギョ", "gyo"));
		
		katakanaEntries.add(new KanaEntry("ジャ", "ja"));		
		katakanaEntries.add(new KanaEntry("ジュ", "ju"));
		katakanaEntries.add(new KanaEntry("ジョ", "jo"));
		
		katakanaEntries.add(new KanaEntry("ビャ", "bya"));
		katakanaEntries.add(new KanaEntry("ビュ", "byu"));
		katakanaEntries.add(new KanaEntry("ビョ", "byo"));
		
		katakanaEntries.add(new KanaEntry("ピャ", "pya"));
		katakanaEntries.add(new KanaEntry("ピュ", "pyu"));
		katakanaEntries.add(new KanaEntry("ピョ", "pyo"));	
		
		katakanaEntries.add(new KanaEntry("ウィ", "wi"));
		katakanaEntries.add(new KanaEntry("ウェ", "we"));
		
		katakanaEntries.add(new KanaEntry("シェ", "she"));
		katakanaEntries.add(new KanaEntry("ジェ", "je"));
		katakanaEntries.add(new KanaEntry("チェ", "che"));
		
		katakanaEntries.add(new KanaEntry("ファ", "fa"));
		katakanaEntries.add(new KanaEntry("フィ", "fi"));
		katakanaEntries.add(new KanaEntry("フェ", "fe"));
		katakanaEntries.add(new KanaEntry("フォ", "fo"));
		
		katakanaEntries.add(new KanaEntry("ティ", "ti"));
		katakanaEntries.add(new KanaEntry("ディ", "di"));
		katakanaEntries.add(new KanaEntry("ヂュ", "dyu"));
		
		katakanaEntries.add(new KanaEntry("ッ", "ttsu"));
		katakanaEntries.add(new KanaEntry("ー", "ttsu2"));
		
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
